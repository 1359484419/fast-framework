package com.bix.bixApi.controller;

import com.alibaba.fastjson.JSONObject;
import com.bix.bixApi.entity.User;
import com.bix.bixApi.service.UserService;
import com.bix.bixApi.utils.RedisClient;
import com.bix.config.auth.interceptor.UnAnth;
import com.bix.config.jwt.JwtUtil;
import com.bix.config.web.RequestJson;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
@Autowired
JwtUtil jwtUtil;
    @Autowired
    private RedisClient redisUtil;

    @UnAnth
    @PostMapping("login")
    public ResponseEntity login(@RequestBody User user, HttpServletRequest httpServletRequest){
       return userService.login(user,httpServletRequest);
    }

    @PostMapping("test")
    public ResponseEntity test(HttpServletRequest request){
        Long userId = jwtUtil.getUserId(request);
        System.out.println(userId);
        return ResponseEntity.ok("goodjob");
    }

    @UnAnth
    @PostMapping("register")
    public ResponseEntity register(@RequestBody @Valid User user){
        return userService.register(user);
    }

//
//    @UnAnth
//    @RequestMapping("getUser")
//    public ResponseEntity getUser(@RequestJson String name) {
//        log.info("name" + name);
////        throw new BixApiException("222");
////        log.info("33333333333");
//        User user = userService.selectUserById(5L);
//
////        User usertemp = JSONObject.parseObject(redisUtil.get("user"), User.class);
////        System.out.println(usertemp);
//        System.out.println(Thread.currentThread().getName());
//        System.out.println("------------");
//        redisUtil.set("user", user);
//        return ResponseEntity.ok(user);
//    }
//
//    @UnAnth
//    @RequestMapping("/insertUser")
//    public ResponseEntity insertUser() {
//        User user = new User();
//        user.setName("3333");
//        userService.insert(user);
//        return ResponseEntity.ok(user);
//    }
//
//    @UnAnth
//    @RequestMapping("/getByName")
//    public ResponseEntity selectUserByName(@RequestBody @Valid User user, Errors errors) {
//        List<ObjectError> errorList = errors.getAllErrors();
//        Map<String, String> map = Maps.newHashMap();
//        if (errorList != null && !errorList.isEmpty()) {
//            for (ObjectError error : errorList) {
//                map.put(error.getObjectName(), error.getDefaultMessage());
//            }
//            return ResponseEntity.ok(map);
//        }
//        return ResponseEntity.ok(userService.selectUserById(user.getId()));
//    }


}
