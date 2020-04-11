package com.gdut.generator.controller;

import com.gdut.generator.Main;
import com.gdut.generator.model.Exercises;
import com.gdut.generator.model.Result;
import com.gdut.generator.service.GenerateService;
import com.gdut.generator.service.impl.GenerateServiceImpl;
import com.gdut.generator.util.BeanUtil;
import com.gdut.generator.util.JavaFxUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.File;
import java.util.List;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 批卷模式
 * @date 2020-04-11 22:54
 */
public class CheckController {
    private static final GenerateService generateService = new GenerateServiceImpl();

    //题目表格
    @FXML
    private TableView<Exercises> exercisesTableView;
    //题目
    @FXML
    private TableColumn<Exercises, String> question;

    @FXML
    private TableColumn<Exercises, String> answer;

    @FXML
    private TableColumn<Exercises, String> studentAnswer;

    @FXML
    private Button selectExercisesFile;

    @FXML
    private Button selectAnswerFile;

    @FXML
    private Label runTimeLabel;

    @FXML
    private Label correctLabel;

    @FXML
    private Label wrongLabel;

    @FXML
    public static ObservableList<Exercises> CHECK_EXERCISES_OBSERVABLE_LIST = FXCollections.observableArrayList();

    //题目文件
    private File exercisesFile;
    //答案文件
    private File answerFile;

    @FXML
    public void initialize() {
        //绑定表格属性
        exercisesTableView.setItems(CHECK_EXERCISES_OBSERVABLE_LIST);
        question.setCellValueFactory(param -> param.getValue().formatQuestionProperty());
        answer.setCellValueFactory(param -> param.getValue().answerProperty());
        studentAnswer.setCellValueFactory(param -> param.getValue().studentAnswerProperty());
    }

    @FXML
    private void selectExercisesFile() {
        exercisesFile = JavaFxUtil.selectFile("请选择题目文件");
        if (exercisesFile == null) {
            selectExercisesFile.setText("请选择题目文件");
        } else {
            selectExercisesFile.setText("已选择题目文件");
        }
    }

    @FXML
    private void selectAnswerFile() {
        answerFile = JavaFxUtil.selectFile("请选择答案文件");
        if (answerFile == null) {
            selectAnswerFile.setText("请选择答案文件");
        } else {
            selectAnswerFile.setText("已选择答案文件");
        }
    }

    private void setRunTimeLabel(String runTime) {
        runTimeLabel.setText("运行耗时：" + runTime);
    }

    private void setCorrectAndWrongLabel(Result result) {
        correctLabel.setText("正确题数：" + result.getCorrectList().size());
        wrongLabel.setText("错误题数：" + result.getWrongList().size());
    }

    /**
     * 用于执行批卷功能
     */
    @FXML
    public void checkPaper() {
        long start = System.currentTimeMillis();
        try {
            if (exercisesFile == null) {
                JavaFxUtil.alert("请选择题目文件!");
            } else if (answerFile == null) {
                JavaFxUtil.alert("请选择答案文件！");
            }
            Result result = generateService.checkAnswer(generateService.readFile(exercisesFile, answerFile));
            System.out.println("批卷耗时：" +BeanUtil.getRunTime(start));
            setRunTimeLabel(BeanUtil.getRunTime(start));
            setCorrectAndWrongLabel(result);
            exercisesFile = null;
            answerFile = null;
            selectAnswerFile.setText("请选择答案文件");
            selectExercisesFile.setText("请选择题目文件");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换模式
     */
    @FXML
    public void changeMode() {
        Main.primaryStage.setScene(Main.exercisesScene);
    }


}
