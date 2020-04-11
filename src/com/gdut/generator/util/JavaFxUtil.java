package com.gdut.generator.util;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description
 * @date 2020-04-11 23:15
 */
public class JavaFxUtil {
    /**
     * 向用户发出提示
     *
     * @param message
     */
    public static void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示信息");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * 选取文件
     * @param title
     */
    public static File selectFile(String title) {
        try {
            //显示一个文件选择器
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(title);
            return fileChooser.showOpenDialog(stage);
        } catch (Exception e) {
            JavaFxUtil.alert(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
