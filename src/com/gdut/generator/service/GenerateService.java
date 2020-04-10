package com.gdut.generator.service;

import com.gdut.generator.model.Exercises;

import java.util.List;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 用于提供生成题目的服务
 * @date 2020-03-27 16:07
 */
public interface GenerateService {
    //生成题目
    List<Exercises> generateExercises(int exercisesNum,int numRange);
    //生成答案
    void generateAnswer(Exercises e);



}
