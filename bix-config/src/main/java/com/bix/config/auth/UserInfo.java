package com.bix.config.auth;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo implements Serializable {

    @JSONField(name = "Account")
    private String account;
    @JSONField(name = "Name")
    private String name;
    @JSONField(name = "ID")
    private Long id;

}
