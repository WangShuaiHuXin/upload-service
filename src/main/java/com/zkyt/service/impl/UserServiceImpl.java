package com.zkyt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zkyt.dto.UserLoginDto;
import com.zkyt.entity.User;
import com.zkyt.exception.ServiceException;
import com.zkyt.mapper.UserMapper;
import com.zkyt.service.UserService;
import com.zkyt.util.HttpServletUtil;
import com.zkyt.util.RSAUtils;
import com.zkyt.util.TokenUtil;
import org.springframework.stereotype.Service;


/**
 * @author licheng
 * @description 针对表【t_user】的数据库操作Service实现
 * @createDate 2022-07-19 11:56:04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public Object login(UserLoginDto userLoginDto) throws Exception {
        //校验参数
        if (StringUtils.isEmpty(userLoginDto.getUsername()) || StringUtils.isEmpty(userLoginDto.getPassword())){
            throw new ServiceException("账号或密码错误！");
        }

        //进入数据库查找
        LambdaQueryWrapper<User> lambda = new LambdaQueryWrapper<>();
        lambda.eq(User::getUsername, userLoginDto.getUsername());
        User one = getOne(lambda);

        //进行了RSA解密过程
        String password = userLoginDto.getPassword();
        password = RSAUtils.decrypt(password);

        if (one == null || !one.getPassword().equals(password)) {
            throw new ServiceException("账号或密码错误！");
        }
        return TokenUtil.getToken(one.getId());
    }

    @Override
    public String getPath() {
        User user = getUser();
        return user.getPath();
    }

    /**
     * 在web环境下使用
     *
     * @since 2022/7/19
     */
    public User getUser() {
        try {
            String token = HttpServletUtil.getHeader(TokenUtil.HeaderKey);
            String userId = TokenUtil.getUserId(token);
            LambdaQueryWrapper<User> lambda = new LambdaQueryWrapper<>();
            lambda.eq(User::getId, userId);
            return getOne(lambda);
        } catch (Exception e) {
            throw new ServiceException("没有获取Token或Token中的用户不存在");
        }
    }
}




