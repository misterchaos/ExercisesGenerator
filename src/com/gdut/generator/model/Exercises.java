package com.gdut.generator.model;

import com.gdut.generator.service.impl.OperatorEnum;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

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
    private SimpleStringProperty answer = new SimpleStringProperty();
    //学生答案
    private SimpleStringProperty studentAnswer = new SimpleStringProperty();
    //题号
    private SimpleIntegerProperty number = new SimpleIntegerProperty();

    //规范题目格式
    private SimpleStringProperty formatQuestion = new SimpleStringProperty();
    //最简式（最后一层表达式且不含括号）
    private String simplestFormatQuestion;

    //methods
    public ArrayList<String> getValueList() {
        return valueList;
    }

    public void addValue(String value) {
        this.valueList.add(value);
    }

    public void addValue(int index, String value) {
        this.valueList.add(index, value);
    }

    public int getOperatorNumber() {
        return operatorNumber;
    }

    public void setOperatorNumber(int operatorNumber) {
        this.operatorNumber = operatorNumber;
    }

    public String getAnswer() {
        return answer.get();
    }

    public SimpleStringProperty answerProperty() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer.set(answer);
    }

    public int getNumber() {
        return number.get();
    }

    public SimpleIntegerProperty numberProperty() {
        return number;
    }

    public void setNumber(int number) {
        this.number.set(number);
    }

    public String getStudentAnswer() {
        return studentAnswer.get();
    }

    public SimpleStringProperty studentAnswerProperty() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer.set(studentAnswer);
    }



    /**
     * 返回符合格式的题目
     *
     * @return
     */
    public String getFormatQuestion() {
        String bracket = "(e)";
        Queue<String> queue = new LinkedList<>();
        //将所有运算数进队列
        for (int i = 2 * operatorNumber; i > operatorNumber - 1; i--) {
            queue.add(valueList.get(i));
        }

        //取出每个运算符,再从队列取出两个数字进行运算，结果再放入队尾中，直到取完所有运算符，此时队列中的数字为最终答案
        for (int i = operatorNumber - 1; i >= 0; i--) {
            String operator = valueList.get(i);
            //从队列取出两个数字
            String e1 = queue.remove();
            String e2 = queue.remove();
            //添加一层括号
            String expression = bracket.replace("e", e1 + operator + e2);
            queue.add(expression);
        }

        StringBuilder stringBuilder = new StringBuilder("").append(number.get()).append(".").append(queue.remove()).append("=");
        formatQuestion.set(stringBuilder.toString());
        return formatQuestion.get();
    }


    /**
     * 返回最简格式的题目
     *
     * @return
     */
    public String getSimplestFormatQuestion() {
        if (null == simplestFormatQuestion) {
            Queue<String> queue = new LinkedList<>();

            //将所有运算数进队列
            for (int i = 2 * operatorNumber; i > operatorNumber - 1; i--) {
                queue.add(valueList.get(i));
            }

            //取出每个运算符,再从队列取出两个数字进行运算，结果再放入队尾中，直到取完所有运算符，此时队列中的数字为最终答案
            for (int i = operatorNumber - 1; i >= 0; i--) {
                String opSymbol = valueList.get(i);

                //从队列取出两个数字
                String num1 = queue.remove();
                String num2 = queue.remove();
                //计算两数运算后结果
                String answer = OperatorEnum.getEnumByOpSymbol(opSymbol).op(num1, num2);
                //如果是最后一层则生成最简式
                if (i == 0) {
                    simplestFormatQuestion = num1 + opSymbol + num2;
                }
                queue.add(answer);
            }
        }
        return simplestFormatQuestion;
    }

    public SimpleStringProperty formatQuestionProperty() {
        if(null==formatQuestion.get()||formatQuestion.get().trim().isEmpty()){
            getFormatQuestion();
        }
        return formatQuestion;
    }


    /**
     * 获取符合格式的答案
     */
    public String getFormatAnswer() {
        return number.get() + "." + answer.get();
    }

    public void setValueList(ArrayList<String> valueList) {
        this.valueList = valueList;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercises exercises = (Exercises) o;
        return Objects.equals(getSimplestFormatQuestion(), exercises.getSimplestFormatQuestion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(simplestFormatQuestion);
    }
}
