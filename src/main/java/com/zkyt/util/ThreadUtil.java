package com.zkyt.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lc
 * @since 7/13/22
 */
public class ThreadUtil {
    public final static ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(10, 100, 10
            , TimeUnit.SECONDS,new ArrayBlockingQueue<>(100));
}
