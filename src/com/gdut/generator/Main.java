package com.gdut.generator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("./view/sample.fxml"));

        Scene scene = new Scene(root, 1460, 900);
        //导入css
        scene.getStylesheets().add(getClass().getResource("./view/css/style.css").toString());

        primaryStage.setTitle("generator-一个神奇的计算器");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
