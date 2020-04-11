package com.gdut.generator;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Parent exercisesRoot;
    public static Scene exercisesScene;
    public static Parent checkRoot;
    public static Scene checkScene;
    public static Stage primaryStage;
    private int width= 862;
    private int height = 767;

    @Override
    public void start(Stage primaryStage) throws Exception {
        exercisesRoot = FXMLLoader.load(getClass().getResource("./view/sample.fxml"));
        exercisesScene = new Scene(exercisesRoot, width, height);

        Main.primaryStage = primaryStage;
        //导入css
        exercisesScene.getStylesheets().add(getClass().getResource("./view/css/style.css").toString());
        primaryStage.setTitle("generator-一个四则运算题目生成器");
        primaryStage.setScene(exercisesScene);
        primaryStage.setResizable(true);
        primaryStage.show();

        checkRoot = FXMLLoader.load(getClass().getResource("./view/check.fxml"));
        checkScene = new Scene(checkRoot, width, height);

    }


    @FXML
    public void change() {

    }


    public static void main(String[] args) {
        launch(args);
    }
}
