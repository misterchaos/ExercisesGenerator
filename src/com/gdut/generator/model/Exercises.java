package com.gdut.generator.model;

import java.util.ArrayList;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 用于保存题目信息
 * @date 2020-03-27 16:11
 */
public class Exercises {
    //field
    private ArrayList<String> valueList = new ArrayList<String>(); //存储运算式链表，实际上采用的是树的结构，用下标来获取
    private int signalNum; //运算符个数
    private String answer; //答案
    //constructor
    public Exercises(){};

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

    public int getSignalNum() {
        return signalNum;
    }

    public void setSignalNum(int signalNum) {
        this.signalNum = signalNum;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
