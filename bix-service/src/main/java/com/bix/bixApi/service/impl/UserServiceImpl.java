package com.bix.bixApi.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bix.bixApi.ErrorCode;
import com.bix.bixApi.common.exception.BixApiException;
import com.bix.bixApi.constant.RedisConstant;
import com.bix.bixApi.entity.User;
import com.bix.bixApi.mapper.UserMapper;
import com.bix.bixApi.service.UserService;
import com.bix.bixApi.utils.RedisClient;
import com.bix.config.jwt.JwtPropertie;
import com.bix.config.jwt.JwtUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.bix.bixApi.ErrorCode.USERNAME_PASSWORD_NOT_MATCH;
import static org.apache.commons.jexl2.parser.ParserConstants.req;

@Service
@DS("basic")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private RedisClient redisUtil;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JwtPropertie jwtPropertie;
    @Autowired
    private RedisClient redisClient;


    @Override
    @Cacheable(value = "redisCache", key = "'redis_user_' + #id", unless = "#result == null ")
    public User selectUserById(Long id) {
        User user = baseMapper.selectUserById(id);
        return user;
    }

    @Override
    public ResponseEntity login(User user, HttpServletRequest request) {
        // 校验用户密码
        LambdaQueryWrapper<User> eq = new LambdaQueryWrapper<User>().eq(User::getName, user.getName()).eq(User::getPassword, user.getPassword());
        User one = getOne(eq);
        if (one == null){
            throw new BixApiException(USERNAME_PASSWORD_NOT_MATCH);
        }
        // 生成token
        String deviceNumber = request.getHeader("User-Agent");
        String token = jwtPropertie.getTokenPrefix() + jwtUtil.createToken(one.getId(), deviceNumber, System.currentTimeMillis()+"");
        String md5Token = DigestUtils.md5Hex(token.substring("Bearer ".length()));
        //限制一个账号只能登录一个设备 start
        String ssoLoginUidKey = "SSO_WALLET_ADMIN_".concat(one.getId().toString());
        String ssoMd5Token = DigestUtils.md5Hex(token.substring("Bearer ".length()).concat(deviceNumber));
        long pastDueTime = 60 * 60L;
        redisClient.set(ssoLoginUidKey, ssoMd5Token, pastDueTime);
        //限制一个账号只能登录一个设备 end
        String loginKey = RedisConstant.REDIS_KEY_SESSION_USERID + one.getId();

        redisClient.set(token.substring("Bearer ".length()),"123",111);
        return ResponseEntity.ok(token);
    }

    @Override
    public ResponseEntity register(User user) {
        boolean save = save(user);
        return ResponseEntity.ok(user);
    }
}
