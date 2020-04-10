package com.gdut.generator.model;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 用于保存题目信息
 * @date 2020-03-27 16:11
 */

public class Exercises {
    //field
    private ArrayList<String> valueList = new ArrayList<String>(); //存储运算式链表，实际上采用的是树的结构，用下标来获取
    //运算符个数
    private int operatorNumber;
    //答案
    private String answer;
    //题目
    private String question;
    //题号
    private int number;

    //methods
    public ArrayList<String> getValueList() {
        return valueList;
    }

    public void addValue(String value) {
        this.valueList.add(value);
    }
    public void addValue(int index, String value){
        this.valueList.add(index, value);
    }

    public int getOperatorNumber() {
        return operatorNumber;
    }

    public void setOperatorNumber(int operatorNumber) {
        this.operatorNumber = operatorNumber;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercises exercises = (Exercises) o;

        return Objects.equals(answer, exercises.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question);
    }
}
