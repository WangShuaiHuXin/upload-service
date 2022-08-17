package com.zkyt.dto;

import lombok.Data;
import javax.validation.constraints.NotEmpty;


/**
 * @author lc
 * @since 2022/7/29
 */
@Data
public class UserLoginDto {
    /**
     * 用户名
     */
    @NotEmpty(message = "用户名为空")
    private String username;

    /**
     * 用户密码
     */
    @NotEmpty(message = "用户密码为空")
    private String password;
}
