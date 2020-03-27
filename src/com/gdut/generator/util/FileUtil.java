package com.gdut.generator.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 用于文件处理
 * @date 2020-03-19 11:37
 */
public class FileUtil {


    /**
     * 将一个文件路径转为BufferedReader对象
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static BufferedReader getBufferedReader(File file) throws Exception {
        if (null == file) {
            throw new Exception("文件名不能为空");
        } else if (file.isDirectory()) {
            throw new Exception("不支持读取文件夹，请输入文件路径");
        } else if (!file.exists()) {
            throw new Exception("该文件不存在");
        } else {
            return new BufferedReader(new FileReader(file));
        }
    }


    /**
     * 从当前目录下获取满足指定正则的所有文件
     *
     * @param regex 文件名（包含通配符）
     * @return
     */
    public static List<File> listFileByRegex(String regex) throws Exception {
        if (null == regex) {
            throw new Exception("缺少必要的参数");
        }

        File baseDir = new File(System.getProperty("user.dir"));
        List<File> fileList = listFiles(baseDir);
        List<File> resultList = new LinkedList<>();
        if (null == fileList || fileList.size() == 0) {
            throw new Exception("当前目录(" + baseDir.getAbsolutePath() + ")下没有任何文件");
        }

        for (File f : fileList) {
            if (f.getName().matches(regex)) {
                resultList.add(f);
            }
        }

        if (resultList.size() == 0) {
            throw new Exception("当前目录(" + baseDir.getAbsolutePath() + ")下没有符合条件(" + regex + ")的文件");
        }
        return resultList;
    }


    /**
     * 返回指定目录及其子目录下的所有文件
     *
     * @param file
     * @return
     */
    public static List<File> listFiles(File file) throws Exception {
        List<File> fileList = new LinkedList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (null == files) {
                throw new Exception("该目录下没有任何文件");
            }
            for (File child : files) {
                if (child.isDirectory()) {
                    fileList.addAll(listFiles(child));
                } else {
                    fileList.add(child);
                }
            }
        } else {
            fileList.add(file);
        }
        return fileList;
    }
}
