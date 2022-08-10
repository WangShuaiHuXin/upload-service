package com.zkyt.service;

import com.zkyt.dto.UserLoginDto;
import com.zkyt.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author licheng
* @description 针对表【t_user】的数据库操作Service
* @createDate 2022-07-19 11:56:04
*/
public interface UserService extends IService<User> {

    Object login(UserLoginDto userLoginDto) throws Exception;

    String getPath();
}
