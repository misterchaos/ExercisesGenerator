package com.gdut.generator.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * 字符串匹配
     * @param REGEX 需要查找字符串
     * @param INPUT 被查找的字符串
     * @return num 查找到的数量
     */
    public static int searchStr(String REGEX, String INPUT){
        int num = 0;
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(INPUT);
        while(m.find()){
            num++;
        }
        return num;
    }

    /**
     * 返回查找的字符串（第一个）
     * @param REGEX 需要查找字符串
     * @param INPUT 被查找的字符串
     */
    public static String getStr(String REGEX, String INPUT){
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(INPUT);
        m.find();
        return m.group();
    }
}
