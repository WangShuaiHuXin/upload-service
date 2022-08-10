package com.zkyt.exception;

/**
 * @author lc
 * @since 7/11/22
 */
public class ServiceException extends RuntimeException{
    public ServiceException(String s) {
        super(s);
    }
}
