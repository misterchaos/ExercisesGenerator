package com.gdut.generator.util;

import java.io.File;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 用于处理JavaBean
 * @date 2020-03-19 18:54
 */
public class BeanUtil {


    /**
     * 返回文件的信息(路径+大小)
     *
     * @param file
     */
    public static String getFileInfo(File file) {
        return file.getAbsolutePath() + "  " + getFileLength(file);
    }

    /**
     * 返回文件的大小
     *
     * @param file
     */
    public static String getFileLength(File file) {
        long length = file.length();
        if (length < 1024) {
            return length + "B";
        } else if (length < (1024 * 1024)) {
            return length / 1024 + "KB";
        } else if (length < (1024 * 1024 * 1024)) {
            return length / (1024 * 1024) + "MB";
        } else {
            return length / (1024 * 1024 * 1024) + "GB";
        }
    }

    /**
     * 返回运行耗时信息
     *
     * @param begin 开始执行的时间（时间戳毫秒数）
     * @return
     */
    public static String getRunTime(long begin) {
        long time = System.currentTimeMillis() - begin;
        if (time < 1000) {
            return time + "ms";
        } else {
            return time / 1000 + "s";
        }
    }
}
