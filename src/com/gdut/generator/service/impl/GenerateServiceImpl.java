package com.gdut.generator.service.impl;

import com.gdut.generator.model.Exercises;
import com.gdut.generator.service.GenerateService;
import com.gdut.generator.util.CalculateUtil;

import java.util.*;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 用于生成四则运算题目
 * @date 2020-03-27 16:08
 */
public class GenerateServiceImpl implements GenerateService {

    /**
     * 生成运算式
     * @param e
     * @return
     */
    @Override
    public Exercises generateExercises(Exercises e, int numRange) {
        int signalNum = CalculateUtil.getRandomNum(1,3);
        e.setSignalNum(signalNum);
        for (int i=0; i<e.getSignalNum(); i++){
            e.addValue(i,generateOperator()); //添加运算符

        }
        for(int i=e.getSignalNum(); i<2*e.getSignalNum()+1; i++){
            e.addValue(i, generateNum(numRange)); //添加运算数
        }
        return e;
    }

    /**
     * 生成运算数
     * @param numRange 随机生成数的范围
     * @return
     */
    @Override
    public String generateNum(int numRange) {
        Random random = new Random();
        int numerator = random.nextInt(numRange); //分子
        int denominator = random.nextInt(numRange); //分母

        //如果分母/分子为0则重新生成
        while (denominator==0 || numerator==0){
            if(denominator==0) denominator = random.nextInt(numRange);
            if(numerator==0) numerator = random.nextInt(numRange);
        }

        //如果为整数
        if(numerator % denominator == 0) return String.valueOf(numerator/denominator);
        //如果为分数
        else{
            //如果为假分数，则转换成带分数
            int l;
            //化为带分数之后新的分子
            int newNumerator;
            if(numerator > denominator){
                //约分
                int maxCommonDivisor = CalculateUtil.getMaxCommonDivisor(numerator, denominator);//获取最大公约数
                if(maxCommonDivisor != 1){
                    numerator /= maxCommonDivisor;
                    denominator /= maxCommonDivisor;
                }
                //计算新分子和带数
                newNumerator = numerator % denominator;
                l = (numerator - newNumerator) / denominator;

                return l+ "'" +newNumerator+"/"+denominator;
            }
            //如果为真分数，则不做处理，直接约分
            else {
                //约分
                int maxCommonDivisor = CalculateUtil.getMaxCommonDivisor(numerator, denominator);//获取最大公约数
                if (maxCommonDivisor != 1) {
                    numerator /= maxCommonDivisor;
                    denominator /= maxCommonDivisor;
                }
                return numerator+"/"+denominator;
            }
        }
    }

    /**
     * 生成运算符
     * @return
     */
    @Override
    public String generateOperator() {
        Random random = new Random();
        int opNum = random.nextInt(4);//随机生成操作符索引
        OperatorEnum operatorEnum = OperatorEnum.getEnumByOpValue(opNum);
        return operatorEnum.getOpSymbol();
    }

    /**
     * 生成答案
     * @return
     */
    @Override
    public Exercises generateAnswer(Exercises e) {
        Queue<String> queue = new LinkedList<String>();
        ArrayList<String> eValueList= e.getValueList();

        //将所有运算数进队列
        for(int i=2*e.getSignalNum(); i>e.getSignalNum()-1; i--){
            queue.add(eValueList.get(i));
        }

        //取出每个运算符,再从队列取出两个数字进行运算，结果再放入队尾中，直到取完所有运算符，此时队列中的数字为最终答案
        for(int i=e.getSignalNum()-1; i>=0; i--){
            String opSymbol = eValueList.get(i);

            //从队列取出两个数字
            String num_1 = queue.remove();
            String num_2 = queue.remove();

            //计算两数运算后结果
            String answer = OperatorEnum.getEnumByOpSymbol(opSymbol).op(num_1, num_2);
            queue.add(answer);
        }

        e.setAnswer(queue.remove());
        return e;
    }
}
