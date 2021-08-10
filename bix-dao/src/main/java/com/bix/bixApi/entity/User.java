package com.bix.bixApi.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bix.config.mybatisplus.annotations.CreateTime;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@TableName("user")
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 2359504409842364928L;

    @TableId("id")
    private Long id;

    @NotBlank(message = "姓名不能为空")
    private String name;

    @TableField(value = "created_time",fill =
            FieldFill.INSERT)
    @CreateTime
    private Date createdTime;
    @NotBlank(message = "密码不得为空")
    private String password;
}
