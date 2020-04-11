package com.gdut.generator.controller;

import com.gdut.generator.Main;
import com.gdut.generator.model.Exercises;
import com.gdut.generator.service.GenerateService;
import com.gdut.generator.service.impl.GenerateServiceImpl;
import com.gdut.generator.util.BeanUtil;
import com.gdut.generator.util.JavaFxUtil;
import com.gdut.generator.util.ThreadPoolUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description
 * @date 2020-03-27 15:42
 */
public class GenerateController {
    private static final GenerateService generateService = new GenerateServiceImpl();


    //题目表格
    @FXML
    private TableView<Exercises> exercisesTableView;
    //题目数量
    @FXML
    private TextField exercisesNumberTextField;
    //数值范围
    @FXML
    private TextField numberRangeTextField;
    //题目
    @FXML
    private TableColumn<Exercises, String> question;
    @FXML
    private TableColumn<Exercises, String> answer;
    @FXML
    private Label runTimeLabel;

    @FXML
    public static ObservableList<Exercises> EXERCISES_OBSERVABLE_LIST = FXCollections.observableArrayList();
    //记录总耗时
    private static long start;

    private CountDownLatch countDownLatch;


    //题目数量
    private int exercisesNumber;
    //数值范围
    private int numberRange;


    /**
     * 执行生成题目功能
     */
    @FXML
    public void generate() {

        try {
            if (exercisesNumber == 0 || numberRange == 0) {
                JavaFxUtil.alert("请先输入题目数量和数值范围！");
                return;
            }
            //最小范围是2
            if (numberRange < 2) {
                throw new RuntimeException("最小的范围是2以内的自然数");
            }
            //清空表格
            EXERCISES_OBSERVABLE_LIST.clear();
            start = System.currentTimeMillis();
            //剩余工作量
            int remainWorkload = exercisesNumber;
            //每个线程的工作量
            int workload = 5000;
            //记录线程编号
            int count = 1;
            //启动生成题目线程组
            int threadCount = exercisesNumber % workload == 0 ? (exercisesNumber / workload) : (exercisesNumber / workload + 1);
            //生成线程+一个输出线程
            countDownLatch = new CountDownLatch(threadCount + 1);
            while (true) {
                if (remainWorkload > workload) {
                    ThreadPoolUtil.execute(new GenerateWorker(workload, numberRange, count++, countDownLatch));
                    remainWorkload -= workload;
                } else {
                    ThreadPoolUtil.execute(new GenerateWorker(remainWorkload, numberRange, count, countDownLatch));
                    break;
                }
            }
            //启动输出到文件线程
            ThreadPoolUtil.execute(new OuputWorker(exercisesNumber, countDownLatch));
            exercisesNumber = 0;
            exercisesNumberTextField.setText("");
            numberRange = 0;
            numberRangeTextField.setText("");
            countDownLatch.await();
        } catch (Exception e) {
            JavaFxUtil.alert(e.getMessage());
            e.printStackTrace();
        }
        setRunTimeLabel(BeanUtil.getRunTime(start));
    }

    private void setRunTimeLabel(String runTime) {
        runTimeLabel.setText("运行耗时：" + runTime);
    }


    /**
     * 切换模式
     */
    @FXML
    public void changeMode() {
        Main.primaryStage.setScene(Main.checkScene);
    }


    @FXML
    public void initialize() {
        //添加输入框监听器
        exercisesNumberTextField.textProperty().addListener(new ExercisesNumberChange<>());
        numberRangeTextField.textProperty().addListener(new NumberRangeChange<>());
        //绑定表格属性
        exercisesTableView.setItems(EXERCISES_OBSERVABLE_LIST);
        question.setCellValueFactory(param -> param.getValue().formatQuestionProperty());
        answer.setCellValueFactory(param -> param.getValue().answerProperty());
    }


    private class ExercisesNumberChange<T> implements ChangeListener<T> {
        @Override
        public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
            String number = exercisesNumberTextField.getText();
            try {
                if (number != null && !number.trim().isEmpty()) {
                    exercisesNumber = Integer.parseInt(number);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JavaFxUtil.alert("您输入的数字格式不正确或者超出范围！");
            }
        }
    }

    private class NumberRangeChange<T> implements ChangeListener<T> {
        @Override
        public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
            String number = numberRangeTextField.getText();
            try {
                if (number != null && !number.trim().isEmpty()) {
                    numberRange = Integer.parseInt(number);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JavaFxUtil.alert("您输入的数字格式不正确或者超出范围！");
            }
        }
    }


    /**
     * 用于执行生成题目的任务
     */
    private static class GenerateWorker implements Runnable {

        private int exerciseNum;
        private int numRange;
        //线程编号
        private int workerNumber;

        private CountDownLatch countDownLatch;

        public GenerateWorker(int exerciseNum, int numRange, int workerNumber, CountDownLatch countDownLatch) {
            this.exerciseNum = exerciseNum;
            this.numRange = numRange;
            this.workerNumber = workerNumber;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                long start = System.currentTimeMillis();
                generateService.generateExercises(exerciseNum, numRange);
                countDownLatch.countDown();
                System.out.println("线程" + workerNumber + "生成题目数：" + exerciseNum + " 耗时：" + BeanUtil.getRunTime(start));
            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }

    /**
     * 用于执行输出题目到文件的任务
     */
    private static class OuputWorker implements Runnable {
        private int exerciseNum;

        private CountDownLatch countDownLatch;

        public OuputWorker(int exerciseNum, CountDownLatch countDownLatch) {
            this.exerciseNum = exerciseNum;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                generateService.writeToFile(exerciseNum);
                countDownLatch.countDown();
                System.out.println("生成题目总数：" + exerciseNum + "  总耗时：" + BeanUtil.getRunTime(start));
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }


}
