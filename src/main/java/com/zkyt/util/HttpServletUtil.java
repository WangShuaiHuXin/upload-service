package com.zkyt.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;


/**
 * 必须在web环境使用,否则会出现空指针异常
 * @author lc
 * @since 2022/7/11
 */
public final class HttpServletUtil {
    private HttpServletUtil() {
    }

    /**
     * @return 当前请求的路径
     */
    public static String getPath() {
        return getHttpServletRequest().getRequestURI();
    }


    /**
     * 获取HttpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * 获取请求参数
     */
    public static Map<String, String> getParamMap() {
        return ServletUtil.getParamMap(getHttpServletRequest());
    }

    /**
     * web环境使用
     */
    public static String getHeader(String name) {
        return getHttpServletRequest().getHeader(name);
    }


    /**
     * 获取请求类型GET,POST
     */
    public static String getMethod() {
        return getHttpServletRequest().getMethod();
    }

    /**
     * 路径+参数等于唯一key
     */
    public static String getKey(String str) {
        return getPath() + str;
    }

    /**
     * web环境使用
     */
    public static void isParamMap(String... str) {
        Map<String, String> map = getParamMap();
        for (String s : str) {
            if (StrUtil.isBlank(map.get(s))) {
                throw new RuntimeException("参数错误,请检查!");
            }
        }
    }


}
