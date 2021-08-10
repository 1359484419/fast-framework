package com.bix.bixApi.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.bix.bixApi.entity.User;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface UserService extends IService<User> {
    User selectUserById(Long id);

    ResponseEntity login(User user, HttpServletRequest httpServletRequest);

    ResponseEntity register(User user);
}
