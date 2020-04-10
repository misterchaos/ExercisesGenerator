package com.gdut.generator.service.impl;

import com.gdut.generator.model.Exercises;
import com.gdut.generator.model.Result;
import com.gdut.generator.service.GenerateService;
import com.gdut.generator.util.CalculateUtil;
import com.gdut.generator.util.FileUtil;

import java.io.*;
import java.util.*;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 用于生成四则运算题目
 * @date 2020-03-27 16:08
 */
public class GenerateServiceImpl implements GenerateService {


    /**
     * 生成指定数目包含答案的有效题目
     *
     * @param exercisesNum
     * @param numRange
     * @return
     */
    @Override
    public List<Exercises> generateExercises(int exercisesNum, int numRange) throws IOException {
        BufferedWriter exercisesWriter = new BufferedWriter(new FileWriter(new File(System.getProperty("user.dir") + "/Exercises.txt")));
        BufferedWriter answerWriter = new BufferedWriter(new FileWriter(new File(System.getProperty("user.dir") + "/Answer.txt")));
        List<Exercises> exercisesList = new LinkedList<>();
        try {

            while (exercisesList.size() < exercisesNum) {
                Exercises exercises = generateQuestion(numRange);
                generateAnswer(exercises);
                //有效题目加入List
                if (validate(exercises, exercisesList)) {
                    //设置题号
                    exercises.setNumber(exercisesList.size() + 1);
                    //生成可以输出的题目样式
                    exercisesList.add(exercises);
                    //输出到文件
                    exercisesWriter.write(exercises.getFormatQuestion());
                    answerWriter.write(exercises.getFormatAnswer());
                    exercisesWriter.newLine();
                    answerWriter.newLine();
                }
            }
            //写入文件
            exercisesWriter.flush();
            answerWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exercisesWriter.close();
            answerWriter.close();
        }
        return exercisesList;
    }

    /**
     * 检查题目是否有效：是否重复，数值合理
     *
     * @param exercises
     * @return
     */
    private boolean validate(Exercises exercises, List<Exercises> exercisesList) {
        //如果计算结果为null说明计算过程出现负数或者假分数，不符合要求
        if (exercises.getAnswer() == null) {
            return false;
        }
        //题目重复
        if (exercisesList.contains(exercises)) {
            System.out.println("题目重复");
            return false;
        }
        return true;
    }


    /**
     * 生成答案
     */
    @Override
    public void generateAnswer(Exercises e) {
        Queue<String> queue = new LinkedList<>();
        ArrayList<String> eValueList = e.getValueList();

        //将所有运算数进队列
        for (int i = 2 * e.getOperatorNumber(); i > e.getOperatorNumber() - 1; i--) {
            queue.add(eValueList.get(i));
        }

        //取出每个运算符,再从队列取出两个数字进行运算，结果再放入队尾中，直到取完所有运算符，此时队列中的数字为最终答案
        for (int i = e.getOperatorNumber() - 1; i >= 0; i--) {
            String opSymbol = eValueList.get(i);

            //从队列取出两个数字
            String num1 = queue.remove();
            String num2 = queue.remove();
            //计算两数运算后结果
            String answer = OperatorEnum.getEnumByOpSymbol(opSymbol).op(num1, num2);

            //计算过程出现不符合条件的数值，就返回null
            if (answer == null) {
                e.setAnswer(null);
                return;
            }
            queue.add(answer);
        }
        e.setAnswer(queue.remove());
    }

    @Override
    public Result checkAnswer(List<Exercises> exercises) throws IOException {
        Result result = new Result();
        for (Exercises e : exercises) {
            generateAnswer(e);
            if (e.getAnswer().equalsIgnoreCase(e.getStudentAnswer())) {
                result.getCorrectList().add(e.getNumber());
            } else {
                result.getWrongList().add(e.getNumber());
            }
        }
        BufferedWriter resultWriter = new BufferedWriter(new FileWriter(new File(System.getProperty("user.dir") + "/Grade.txt")));
        List correctList = result.getCorrectList();
        resultWriter.write("Correct:"+correctList.size());
        resultWriter.newLine();
        StringBuilder stringBuilder = new StringBuilder("(");
        for (int i = 0; i < correctList.size(); i++) {
            if ((i>1&&(i+1)%10==0)||i==correctList.size()-1) {
                //写完一行
                stringBuilder.append(correctList.get(i)).append(")");
                resultWriter.write(stringBuilder.toString());
                resultWriter.newLine();
                stringBuilder = new StringBuilder("(");
            } else {
                stringBuilder.append(correctList.get(i)).append(",");
            }
        }
        List wrongList = result.getWrongList();
        resultWriter.write("Wrong:"+wrongList.size());
        resultWriter.newLine();
        stringBuilder = new StringBuilder("(");
        for (int i = 0; i < wrongList.size(); i++) {
            if ((i>1&&(i+1)%10==0)||i==wrongList.size()-1) {
                //写完一行
                stringBuilder.append(wrongList.get(i)).append(")");
                resultWriter.write(stringBuilder.toString());
                resultWriter.newLine();
                stringBuilder = new StringBuilder("(");
            } else {
                stringBuilder.append(wrongList.get(i)).append(",");
            }
        }
        resultWriter.flush();
        return result;
    }

    /**
     * 读取文件并转成题目对象
     *
     * @param exercisesFile
     * @param answerFile
     * @return
     * @throws Exception
     */
    @Override
    public List<Exercises> readFile(File exercisesFile, File answerFile) throws Exception {
        BufferedReader exercisesReader = FileUtil.getBufferedReader(exercisesFile);
        BufferedReader answerReader = FileUtil.getBufferedReader(answerFile);
        String question;
        List<Exercises> exercisesList = new LinkedList<>();
        while ((question = exercisesReader.readLine()) != null) {
            String answer = answerReader.readLine();
            //解析成Exercises对象
            Exercises exercises = CalculateUtil.parseExercises(question);
            //填入学生答案
            exercises.setStudentAnswer(CalculateUtil.parseAnswer(answer));
            exercisesList.add(exercises);
        }
        return exercisesList;
    }


    /**
     * 生成运算式
     *
     * @return
     */
    private Exercises generateQuestion(int numRange) {
        Exercises e = new Exercises();
        int signalNum = CalculateUtil.getRandomNum(1, 3);
        e.setOperatorNumber(signalNum);
        for (int i = 0; i < e.getOperatorNumber(); i++) {
            //添加运算符
            e.addValue(i, generateOperator());

        }
        for (int i = e.getOperatorNumber(); i < 2 * e.getOperatorNumber() + 1; i++) {
            //添加运算数
            e.addValue(i, generateNum(numRange));
        }

        return e;
    }

    /**
     * 生成运算符
     *
     * @return
     */
    public String generateOperator() {
        Random random = new Random();
        //随机生成操作符索引
        int opNum = random.nextInt(4);
        OperatorEnum operatorEnum = OperatorEnum.getEnumByOpValue(opNum);
        if (operatorEnum != null) {
            return operatorEnum.getOpSymbol();
        } else {
            throw new RuntimeException("不存在下表标对应的运算符");
        }
    }

    /**
     * 生成运算数
     *
     * @param numRange 随机生成数的范围
     * @return
     */
    private String generateNum(int numRange) {
        //最小范围是2
        if (numRange < 2) {
            throw new RuntimeException("最小的范围是2以内的自然数");
        }
        Random random = new Random();
        //分子
        int numerator = 0;
        //分母
        int denominator = 0;


        //如果分母为0则重新生成
        while (denominator == 0 || (numerator / (float) denominator) > numRange) {
            denominator = random.nextInt(10);
            numerator = random.nextInt(10);
        }

        //如果为整数
        if (numerator % denominator == 0) {
            return String.valueOf(numerator / denominator);
        }
        //如果为分数
        else {
            //如果为假分数，则转换成带分数
            int l;
            //化为带分数之后新的分子
            int newNumerator;
            if (numerator > denominator) {
                //约分
                //获取最大公约数
                int maxCommonDivisor = CalculateUtil.getMaxCommonDivisor(numerator, denominator);
                if (maxCommonDivisor != 1) {
                    numerator /= maxCommonDivisor;
                    denominator /= maxCommonDivisor;
                }
                //计算新分子和带数
                newNumerator = numerator % denominator;
                l = (numerator - newNumerator) / denominator;

                return l + "'" + newNumerator + "/" + denominator;
            }
            //如果为真分数，则不做处理，直接约分
            else {
                //约分
                //获取最大公约数
                int maxCommonDivisor = CalculateUtil.getMaxCommonDivisor(numerator, denominator);
                if (maxCommonDivisor != 1) {
                    numerator /= maxCommonDivisor;
                    denominator /= maxCommonDivisor;
                }
                return numerator + "/" + denominator;
            }
        }
    }


}
