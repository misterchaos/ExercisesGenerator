package com.gdut.generator.controller;

import com.gdut.generator.model.Exercises;
import com.gdut.generator.model.Result;
import com.gdut.generator.service.GenerateService;
import com.gdut.generator.service.impl.GenerateServiceImpl;
import com.gdut.generator.util.BeanUtil;
import com.gdut.generator.util.ThreadPoolUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description
 * @date 2020-03-27 15:42
 */
public class Controller {
    private static final GenerateService generateService = new GenerateServiceImpl();

    @FXML
    public static ObservableList<Exercises> EXERCISES_OBSERVABLE_LIST = FXCollections.observableArrayList();
    //记录总耗时
    private static long start;


    public static void main(String args[]) {
        try {
            int exerciseNumber = 10000;
            int range = 2;
            generate(exerciseNumber,range);

            File file1 = new File(System.getProperty("user.dir") + "/Exercises.txt");
            File file2 = new File(System.getProperty("user.dir") + "/Answer.txt");
            checkPaper(file1, file2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    /**
     * 执行生成题目功能
     *
     * @param exerciseNumber
     * @param numberRange
     */
    private static void generate(int exerciseNumber, int numberRange) {
        start = System.currentTimeMillis();
        //剩余工作量
        int remainWorkload = exerciseNumber;
        //每个线程的工作量
        int workload = 5000;
        //记录线程编号
        int count = 1;
        //启动生成题目线程组
        while (true) {
            if (remainWorkload > workload) {
                ThreadPoolUtil.execute(new GenerateWorker(workload, numberRange, count++));
                remainWorkload -= workload;
            } else {
                ThreadPoolUtil.execute(new GenerateWorker(remainWorkload, numberRange, count));
                break;
            }
        }
        //启动输出到文件线程
        ThreadPoolUtil.execute(new OuputWorker(exerciseNumber));
    }


    /**
     * 用于执行批卷功能
     *
     * @param exercisesFile
     * @param answerFile
     */
    private static void checkPaper(File exercisesFile, File answerFile) {
        ThreadPoolUtil.execute(new CheckWorker(exercisesFile, answerFile));
    }


    /**
     * 用于执行生成题目的任务
     */
    private static class GenerateWorker implements Runnable {

        private int exerciseNum;
        private int numRange;
        //线程编号
        private int workerNumber;

        public GenerateWorker(int exerciseNum, int numRange, int workerNumber) {
            this.exerciseNum = exerciseNum;
            this.numRange = numRange;
            this.workerNumber = workerNumber;
        }

        @Override
        public void run() {
            try {
                long start = System.currentTimeMillis();
                generateService.generateExercises(exerciseNum, numRange);
                System.out.println("线程" + workerNumber + "生成题目数：" + exerciseNum + " 耗时：" + BeanUtil.getRunTime(start));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 用于执行输出题目到文件的任务
     */
    private static class OuputWorker implements Runnable {
        private int exerciseNum;

        public OuputWorker(int exerciseNum) {
            this.exerciseNum = exerciseNum;
        }

        @Override
        public void run() {
            try {
                generateService.writeToFile(exerciseNum);
                System.out.println("生成题目总数："+exerciseNum+"  总耗时："+BeanUtil.getRunTime(start));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 用于执行批卷的任务
     */
    private static class CheckWorker implements Runnable {
        private File exercisesFile;
        private File answerFile;

        public CheckWorker(File exercisesFile, File answerFile) {
            this.exercisesFile = exercisesFile;
            this.answerFile = answerFile;
        }

        @Override
        public void run() {
            try {
                long start = System.currentTimeMillis();

                List<Exercises> list = generateService.readFile(exercisesFile, answerFile);
                Result result = generateService.checkAnswer(list);
                System.out.println("批卷耗时：" + BeanUtil.getRunTime(start));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
