/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * Copyright (c) 2012-2019. haiyi Inc.
 * entity-api All rights reserved.
 */

package com.bix.bixApi.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bix.bixApi.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper<User> {
    User selectUserById(@Param(value = "id") Long id);

}
