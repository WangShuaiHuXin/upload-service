package com.zkyt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenTest {
    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("id",1);
        map.put("mobile","13123134564");
        String compact = Jwts.builder().signWith(SignatureAlgorithm.HS256, "123456")
                .setClaims(map)
                .setExpiration(new Date(System.currentTimeMillis() + 600000000))
                .compact();
        //eyJhbGciOiJIUzI1NiJ9.eyJtb2JpbGUiOiIxMzEyMzEzNDU2NCIsImlkIjoxLCJleHAiOjE2NjA3MDgyNjd9.fbAjY36tiiQm-Ps-nkr4rns7cFdYVIl0ccwYaj8Cz8E
        System.out.println(new Date(System.currentTimeMillis() + 600000000));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd ss:mm:HH");
        String format = simpleDateFormat.format(new Date(System.currentTimeMillis() + 600000000));
        System.out.println(format);
        System.out.println(compact);
    }
}