package com.gdut.generator.util;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 线程池
 * @date 2020-04-11 01:31
 */
public class ThreadPoolUtil {

    /**
     * 核心线程数（默认线程数）
     */
    private static final int CORE_POOL_SIZE = 4;
    /**
     * 最大线程数
     */
    private static final int MAX_POOL_SIZE = 4;
    /**
     * 允许线程空闲时间（单位：默认为秒）
     */
    private static final int KEEP_ALIVE_TIME = 10;
    /**
     * 缓冲队列大小
     */
    private static final int QUEUE_CAPACITY = 20000;


    private static ThreadPoolExecutor executor =
            new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingDeque(QUEUE_CAPACITY));


    public static void execute(Runnable runnable){
        executor.execute(runnable);
    }
    private ThreadPoolUtil() {
    }
}
