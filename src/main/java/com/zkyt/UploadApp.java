package com.zkyt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lc
 * @since 7/11/22
 */
@SpringBootApplication
public class UploadApp {
    public static void main(String[] args) {
        SpringApplication.run(UploadApp.class,args);
        System.out.println("<h1>Hello World</h1>");
    }
    
}
