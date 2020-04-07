package com.gdut.generator.util;

import com.gdut.generator.model.Exercises;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用于处理计算
 */
public class CalculateUtil {

    /**
     * 将带分数数化成假分数形式,如过传入的不是带分数，则不做改动，原数返回
     * @param operand 操作数
     * @return 假分数
     */
    public static String getImproperFraction(String operand){
        //检测是否为真分数
        if(BeanUtil.searchStr("'",operand) != 0){
            String[] str = operand.split("['/]");
            int numerator = Integer.parseInt(str[0]) * Integer.parseInt(str[2]) + Integer.parseInt(str[1]); //分子
            int denominator = Integer.parseInt(str[2]); //分母
            return numerator+"/"+denominator;
        }
        //否则不做改动
        else return operand;
    }

    /**
     * 获取分数的分子
     * @param num 分数
     * @return 分子
     */
    public static int getNumerator(String num) {
        int numerator;
        String[] str = null;

        if (BeanUtil.searchStr("'", num) > 0) {
            str = num.split("['/]");
            numerator = Integer.parseInt(str[0]) * Integer.parseInt(str[2]) + Integer.parseInt(str[1]);
            return numerator;
        } else if (BeanUtil.searchStr("/", num) > 0) {
            str = num.split("/");
            numerator = Integer.parseInt(str[0]);
            return numerator;
        }
        return 1;
    }

    /**
     * 获取分数的分母
     * @param num 分数
     * @return 分母
     */
    public static int getDenominator(String num) {
        int denominator;
        String[] str = null;

        if(BeanUtil.searchStr("'", num) > 0) {//如果传入的是带分数
            str = num.split("['/]");
            denominator = Integer.parseInt(str[2]);
            return denominator;
        }else if(BeanUtil.searchStr("/", num) > 0){//如果传入的是分数
            str = num.split("/");
            denominator = Integer.parseInt(str[1]);
            return denominator;
        }
        return 1;
    }

    /**
     * 将两数通分
     * @param num_1 需通分数字一
     * @param num_2 需通分数字二
     * @return 通分后 str[0]对应num_1, str[1]对应num_2
     */
    public static String[] switchToCommonDenominator(String num_1, String num_2){
        String[] str = new String[2];
        int denominator_1;
        int denominator_2;
        int numerator_1;
        int numerator_2;
        //如果a为分数
        if(BeanUtil.searchStr("/", num_1) > 0){
            num_1 = getImproperFraction(num_1); //则a转换为假分数

            if(BeanUtil.searchStr("/", num_2) > 0){             //如果a、b为分数
                num_2 = getImproperFraction(num_2); //则b转换为假分数
                //获取分母
                denominator_1 = getDenominator(num_1);
                denominator_2 = getDenominator(num_2);
                if(denominator_1==denominator_2){
                    str[0] = num_1;
                    str[1] = num_2;
                    return str;
                }
                //获取分子
                numerator_1 = getNumerator(num_1);
                numerator_2 = getNumerator(num_2);
                str[0] = numerator_1*denominator_2 +"/"+ denominator_1*denominator_2;
                str[1] = numerator_2*denominator_1 +"/"+ denominator_1*denominator_2;
                return str;

            }else{                                                      //a为分数，b为整数
                num_1 = getImproperFraction(num_1);
                denominator_1 = getDenominator(num_1);
                num_2 = Integer.parseInt(num_2)*denominator_1 +"/"+ Integer.parseInt(num_2)*denominator_1;
                str[0] = num_1;
                str[1] = num_2;
                return str;
            }
        }
        //如果a为整数
        else{
            if(BeanUtil.searchStr("/", num_2) > 0){             //如果a为整数，b为分数
                num_2 = getImproperFraction(num_2);
                denominator_2 = getDenominator(num_2);
                num_1 = Integer.parseInt(num_1)*denominator_2 +"/"+ Integer.parseInt(num_1)*denominator_2;
                str[0] = num_1;
                str[1] = num_2;
                return str;
            }
            else {                                                     //如果a、b都为整数
                str[0] = num_1;
                str[1] = num_2;
                return str;
            }
        }
    }

    /**
     * 求两数的最大公约数
     * @param numerator 分子
     * @param denominator 分母
     * @return 最大公约数
     */
    public static int getMaxCommonDivisor(int numerator, int denominator){
        //辗转相除法
        while(numerator*denominator!=0){ //任意一个为0时终止循环
            if(numerator>denominator){
                numerator %= denominator;
            }
            else if(numerator<denominator){
                denominator %= numerator;
            }
        }
        return Math.max(numerator, denominator);
    }

    /**
     * 判断题目是否有重复
     * @param eList 题目列表
     * @return 是/否
     */
    public static Boolean isRepeat(Exercises[] eList){
        return false;
    }

    /**
     * 随机生成指定范围内数字
     * @param min 下界，可取
     * @param max 上界，可取
     * @return 随机生成数
     */
    public static int getRandomNum(int min, int max){
        Random random = new Random();
        int randomNum = random.nextInt(max)%(max-min+1) + min;
        return randomNum;
    }
}
