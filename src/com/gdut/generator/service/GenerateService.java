package com.gdut.generator.service;

import com.gdut.generator.model.Exercises;
import com.gdut.generator.model.Result;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 用于提供生成题目的服务
 * @date 2020-03-27 16:07
 */
public interface GenerateService {
    //生成题目
    void generateExercises(int exercisesNum, int numRange) throws IOException;
    //输出到文件
    void writeToFile(int exercisesNum) throws IOException;

    //生成答案
    void generateAnswer(Exercises e);

    //批卷
    Result checkAnswer(List<Exercises> exercises) throws IOException;

    //读取题目和答案文件
    List<Exercises> readFile(File exercisesFile, File answerFile) throws Exception;

}
