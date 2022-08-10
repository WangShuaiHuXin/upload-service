package com.zkyt.exception;

import com.zkyt.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 异常处理
 *
 * @author lc
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理 ServiceException 异常
     *
     * @since 2021/6/23
     */
    @ExceptionHandler(ServiceException.class)
    public Object doHandleServiceException(ServiceException e) {
        e.printStackTrace();
        return R.fail(e.getMessage());
    }

    /**
     * 处理 Exception 异常
     *
     * @since 2021/6/23
     */
    @ExceptionHandler(Exception.class)
    public Object doHandleServiceException(Exception e) {
        e.printStackTrace();
        return R.fail("系统繁忙");
    }

    /**
     * 处理 BindException 异常
     *
     * @since 2021/6/23
     */
    @ExceptionHandler(BindException.class)
    public Object doHandleServiceException(BindException e) {
        e.printStackTrace();
        List<ObjectError> list = e.getBindingResult().getAllErrors();
        StringBuilder sb = new StringBuilder();
        list.forEach(t->{
            sb.append(t.getDefaultMessage()).append("或");
        });
        sb.replace(sb.length()-1, sb.length(), "");

        return R.fail(sb.toString());
    }
}