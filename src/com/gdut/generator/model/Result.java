package com.gdut.generator.model;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 批卷结果
 * @date 2020-04-10 19:58
 */

public class Result {
    private List<Integer> correctList= new LinkedList<>();
    private List<Integer> wrongList = new LinkedList<>();

    public List<Integer> getCorrectList() {
        return correctList;
    }

    public void setCorrectList(List<Integer> correctList) {
        this.correctList = correctList;
    }

    public List<Integer> getWrongList() {
        return wrongList;
    }

    public void setWrongList(List<Integer> wrongList) {
        this.wrongList = wrongList;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Correct:(");
        for (int i = 0; i < correctList.size(); i++) {
            if (i == correctList.size() - 1) {
                stringBuilder.append(correctList.get(i));
            } else {
                stringBuilder.append(correctList.get(i)).append(",");
            }
        }
        stringBuilder.append(")").append("\n").append("Wrong:(");
        for (int i = 0; i < wrongList.size(); i++) {
            if (i == wrongList.size() - 1) {
                stringBuilder.append(wrongList.get(i));
            } else {
                stringBuilder.append(wrongList.get(i)).append(",");
            }
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
