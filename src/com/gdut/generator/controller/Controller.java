package com.gdut.generator.controller;

import com.gdut.generator.model.Exercises;
import com.gdut.generator.model.Result;
import com.gdut.generator.service.impl.GenerateServiceImpl;

import java.io.File;
import java.util.List;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description
 * @date 2020-03-27 15:42
 */
public class Controller {

    public static void main(String args[]) {
        GenerateServiceImpl giml = new GenerateServiceImpl();
        try {
            List<Exercises> exercisesList = giml.generateExercises(10000, 2);
            for (int i = 0; i < exercisesList.size(); i++) {
                System.out.println(exercisesList.get(i).getValueList() + "   ======   " + exercisesList.get(i).getFormatQuestion() + exercisesList.get(i).getAnswer());
            }
            File file1 = new File(System.getProperty("user.dir") + "/Exercises.txt");
            File file2 = new File(System.getProperty("user.dir") + "/Answer.txt");
            exercisesList = giml.readFile(file1, file2);
            Result result = giml.checkAnswer(exercisesList);
            System.out.println("================================================");
            for (int i = 0; i < exercisesList.size(); i++) {
                System.out.println(exercisesList.get(i).getValueList() + "   ======   " + exercisesList.get(i).getSimplestFormatQuestion()
                         + " 学生答案：" + exercisesList.get(i).getStudentAnswer());
            }
            System.out.printf("result:" + result.toString());
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }


}
