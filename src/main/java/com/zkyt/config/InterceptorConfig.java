package com.zkyt.config;

import com.zkyt.annotations.PassToken;
import com.zkyt.entity.User;
import com.zkyt.exception.ServiceException;
import com.zkyt.service.UserService;
import com.zkyt.util.HttpServletUtil;
import com.zkyt.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lc
 */
@Configuration
@Slf4j
public class InterceptorConfig implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //获取目标方法 - 后续获取目标方法上的注解 以作其他用途
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        PassToken token = handlerMethod.getMethodAnnotation(PassToken.class);
        if (token != null) {
            return true;
        }

        try {
            String header = HttpServletUtil.getHeader(TokenUtil.HeaderKey);
            String userId = TokenUtil.getUserId(header);
            User user = userService.getById(userId);
            if (user == null) {
                throw new ServiceException("用户不存在");
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("登入失效");
        }
        return true;
    }
}