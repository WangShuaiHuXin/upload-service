package com.zkyt.controller;

import com.zkyt.annotations.PassToken;
import com.zkyt.dto.UserLoginDto;
import com.zkyt.service.UserService;
import com.zkyt.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author lc
 * @since 2022/7/19
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    @PassToken
    public Object login(@RequestBody UserLoginDto userLoginDto) throws Exception {
        return R.okData(userService.login(userLoginDto));
    }


}
