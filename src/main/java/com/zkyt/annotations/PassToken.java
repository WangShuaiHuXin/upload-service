package com.zkyt.annotations;

import java.lang.annotation.*;

/**
 * 排除需要登入的接口
 * @author lc
 * @since 8/1/22
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PassToken {
}
