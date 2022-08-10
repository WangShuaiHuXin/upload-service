package com.zkyt.util;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.zkyt.exception.ServiceException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT的封装用于更易用
 * @author lc
 * @since 7/11/22
 */
public class TokenUtil  {
    /** 密钥串 **/
    private static final String SECRET_KEY = "izVguZPRsBQ5Rqw6dhMvcIwy8_9lQnrO3vpxGwPxfAxDs";
    private static final  String USER_ID_KEY = "userId";
    private static final  String EXPIRE_TIME_KEY = "expireTime";
    public static String HeaderKey = "Authorization";


    /**
     * 获取Token
     * @since 7/11/22
     */
    public static String getToken(Serializable userId){
        Map<String, Object> map = new HashMap<>();
        map.put(USER_ID_KEY, userId);
        map.put(EXPIRE_TIME_KEY, System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 3);
        return JWTUtil.createToken(map, SECRET_KEY.getBytes());
    }

    /**
     * 获取用户id
     * @since 7/11/22
     */
    public static String getUserId(String token){
        JWT jwt = JWTUtil.parseToken(token);
        if (!JWTUtil.verify(token, SECRET_KEY.getBytes())) {
            throw new ServiceException("无效Token!");
        }
        Object payload = jwt.getPayload(EXPIRE_TIME_KEY);
        if (payload == null || System.currentTimeMillis() > (long)payload) {
            throw new ServiceException("登入失效!");
        }
        return jwt.getPayload(USER_ID_KEY).toString();
    }


}