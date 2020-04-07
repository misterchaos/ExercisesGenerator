package com.gdut.generator.service;

import com.gdut.generator.model.Exercises;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 用于提供生成题目的服务
 * @date 2020-03-27 16:07
 */
public interface GenerateService {
    public Exercises generateExercises(Exercises e, int numRange); //生成运算式
    public String generateNum(int numRange); //生成数字
    public String generateOperator(); //生成运算符
    public Exercises generateAnswer(Exercises e); //生成答案

}
