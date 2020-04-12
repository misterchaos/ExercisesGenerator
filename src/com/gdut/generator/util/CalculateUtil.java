package com.gdut.generator.util;

import com.gdut.generator.constant.Constant;
import com.gdut.generator.model.Exercises;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * 用于处理计算
 */
public class CalculateUtil {
    //用于测试增加的mian方法
    public static void main(String[] args) {
        String s = getImproperFraction("1'1/2");
        System.out.println(s);
        int n = getNumerator("1'1/2");
        System.out.println(n);
        int d = getDenominator("1'1/2");
        System.out.println(d);
        String[] str = switchToCommonDenominator("1'1/2", "2/3");
        System.out.println(str[0] + "\n" + str[1]);
    }

    /**
     * 将带分数数化成假分数形式,如过传入的不是带分数，则不做改动，原数返回
     *
     * @param operand 操作数
     * @return 假分数
     */
    public static String getImproperFraction(String operand) {
        //检测是否为真分数
        if (BeanUtil.searchStr("'", operand) != 0) {
            String[] str = operand.split("['/]");
            int numerator = Integer.parseInt(str[0]) * Integer.parseInt(str[2]) + Integer.parseInt(str[1]); //分子
            int denominator = Integer.parseInt(str[2]); //分母
            return numerator + "/" + denominator;
        }
        //否则不做改动
        else return operand;
    }

    /**
     * 获取分数的分子
     *
     * @param num 分数
     * @return 分子
     */
    public static int getNumerator(String num) {
        int numerator;
        String[] str;
        //如果是带分数,则获取的是转换为假分数之后的分子
        if (BeanUtil.searchStr("'", num) > 0) {
            str = num.split("['/]");
            numerator = Integer.parseInt(str[0]) * Integer.parseInt(str[2]) + Integer.parseInt(str[1]);
            return numerator;
        }
        //如果是真分数/假分数
        else if (BeanUtil.searchStr("/", num) > 0) {
            str = num.split("/");
            numerator = Integer.parseInt(str[0]);
            return numerator;
        }
        //如果是整数，则原数返回
        return Integer.parseInt(num);
    }

    /**
     * 获取分数的分母
     *
     * @param num 分数
     * @return 分母
     */
    public static int getDenominator(String num) {
        int denominator;
        String[] str;

        //如果是带分数
        if (BeanUtil.searchStr("'", num) > 0) {//如果传入的是带分数
            str = num.split("['/]");
            denominator = Integer.parseInt(str[2]);
            return denominator;
        }
        //如果是真分数/假分数
        else if (BeanUtil.searchStr("/", num) > 0) {//如果传入的是分数
            str = num.split("/");
            denominator = Integer.parseInt(str[1]);
            return denominator;
        }
        //如果是整数
        return 1;
    }

    /**
     * 将两数通分
     *
     * @param num1 需通分数字一
     * @param num2 需通分数字二
     * @return 通分后 str[0]对应num_1, str[1]对应num_2
     */
    public static String[] switchToCommonDenominator(String num1, String num2) {
        //将分数转换成假分数形式
        num1 = getImproperFraction(num1);
        num2 = getImproperFraction(num2);

        String[] str = new String[2];
        int denominator_1;
        int denominator_2;
        int numerator_1;
        int numerator_2;
        //如果a为分数
        if (BeanUtil.searchStr("/", num1) > 0) {
            //如果a为带分数，则转换为假分数
            if (BeanUtil.searchStr("'", num1) > 0) num1 = getImproperFraction(num1);

            //如果a、b为分数
            if (BeanUtil.searchStr("/", num2) > 0) {
                //如果b为带分数，则转换为假分数
                if (BeanUtil.searchStr("'", num2) > 0) num2 = getImproperFraction(num2);
                //获取分母
                denominator_1 = getDenominator(num1);
                denominator_2 = getDenominator(num2);
                //如果a、b分母相同则不做处理
                if (denominator_1 == denominator_2) {
                    str[0] = num1;
                    str[1] = num2;
                    return str;
                }
                //获取分子
                numerator_1 = getNumerator(num1);
                numerator_2 = getNumerator(num2);
                //通分处理
                str[0] = numerator_1 * denominator_2 + "/" + denominator_1 * denominator_2;
                str[1] = numerator_2 * denominator_1 + "/" + denominator_1 * denominator_2;
                return str;
            }
            //a为分数，b为整数
            else {
                num1 = getImproperFraction(num1);
                denominator_1 = getDenominator(num1);
                num2 = Integer.parseInt(num2) * denominator_1 + "/" + Integer.parseInt(num2) * denominator_1;
                str[0] = num1;
                str[1] = num2;
            }
            return str;
        }
        //如果a为整数
        else {
            //如果a为整数，b为分数
            if (BeanUtil.searchStr("/", num2) > 0) {
                num2 = getImproperFraction(num2);
                denominator_2 = getDenominator(num2);
                num1 = Integer.parseInt(num1) * denominator_2 + "/" + denominator_2;
                str[0] = num1;
                str[1] = num2;
                return str;
            }
            //如果a、b都为整数
            else {
                str[0] = num1;
                str[1] = num2;
                return str;
            }
        }
    }

    /**
     * 求两数的最大公约数
     *
     * @param numerator   分子
     * @param denominator 分母
     * @return 最大公约数
     */
    public static int getMaxCommonDivisor(int numerator, int denominator) {
        //辗转相除法
        while (numerator * denominator != 0) { //任意一个为0时终止循环
            if (numerator > denominator) {
                numerator %= denominator;
            } else if (numerator < denominator) {
                denominator %= numerator;
            } else if (numerator == denominator) {
                return numerator;
            }
        }
        return Math.max(numerator, denominator);
    }


    /**
     * 随机生成指定范围内数字
     *
     * @param min 下界，可取
     * @param max 上界，可取
     * @return 随机生成数
     */
    public static int getRandomNum(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }


    /**
     * 将一个分数约分
     *
     * @return
     */
    public static String reduction(int numerator, int denominator) {
        if (numerator * denominator == 0) {
            return String.valueOf(0);
        }
        int maxCommonDivisor = getMaxCommonDivisor(numerator, denominator);
        //上下都可整除
        if (numerator % maxCommonDivisor == 0 && denominator % maxCommonDivisor == 0) {
            numerator /= maxCommonDivisor;
            denominator /= maxCommonDivisor;
        }
        //如果分母为1，返回整数
        if (denominator == 1) {
            return String.valueOf(numerator);
        }
        return numerator + "/" + denominator;
    }

    /**
     * 判断是否真分数
     *
     * @return
     */
    public static boolean isProperFraction(String number) {
        //获取分母分子
        int denominator = CalculateUtil.getDenominator(number);
        int numerator = CalculateUtil.getNumerator(number);
        return numerator < denominator;
    }

    /**
     * 从一个(e1+e2)形式的字符串中解析出 e1 + e2
     * e1和e2可以是子表达式
     *
     * @param expression
     * @return
     */
    public static String[] getOperatorAndNumber(String expression) {
        //去除最外层括号
        expression = expression.replaceFirst("\\(", "").substring(0, expression.lastIndexOf(")") - 1);
        String num1 = null;
        String num2 = null;
        String operator = "(\\" + Constant.PLUS + "|\\" + Constant.MINUS + "|\\" + Constant.MULTIPLY + "|\\" + Constant.DIVIDE + ")";
        //三运算符表达式
        int operatorNum = BeanUtil.searchStr(operator, expression);
        String reg;
        if (operatorNum == 3) {
            reg = "\\)" + operator + "\\(";
        }
        //两运算符表达式
        else if (operatorNum == 2) {
            reg = operator + "\\(";
        }
        //单运算符表达式
        else {
            reg = operator;
        }
        num1 = expression.split(reg)[0];
        num2 = expression.split(reg)[1];
        if(isExpression(num1)){
            num1=num1+")";
        }
        if(isExpression(num2)){
            num2="("+num2;
        }
        //必须先移除num2,因为num2可以是表达式，num1可能是num2的一部分
        String op = expression.substring(num1.length(),num1.length()+1);
        return new String[]{num1, op, num2};
    }


    /**
     * 是否是表达式
     *
     * @return
     */
    public static boolean isExpression(String str) {
        //如果有括号就是表达式
        return str.contains("(") || str.contains(")");
    }


    /**
     * 将字符串解析成Exercises对象
     *
     * @param question
     */
    public static Exercises parseExercises(String question) {
        Exercises exercises = new Exercises();
        //将题目解析成Excecises对象的属性
        parseQuestion(exercises, question);
        //解析题目中的题号
        parseNumber(exercises, question);
        return exercises;
    }

    /**
     * 返回不带题号的答案
     * @param answer
     * @return
     */
    public static String parseAnswer(String answer){
        return answer.split("\\.")[1];
    }

    /**
     * 将题目解析成Excecises对象的属性
     *
     * @param question
     * @return
     */
    private static void parseQuestion(Exercises exercises, String question) {
        String expression = question.split("\\.")[1].replace("\\=", "");
        Queue<String> queue = new LinkedList();
        ArrayList<String> valueList = new ArrayList<>();
        queue.add(expression);
        while (true) {
            expression = queue.remove();
            //如果是运算符则继续解析
            if (CalculateUtil.isExpression(expression)) {
                String[] operatorAndNumber = CalculateUtil.getOperatorAndNumber(expression);
                //保存运算符
                valueList.add(operatorAndNumber[1]);
                //将操作时或者子表达式入队
                queue.add(operatorAndNumber[2]);
                queue.add(operatorAndNumber[0]);
            }
            //如果是数字，则结束
            else {
                //还没加入操作数时valuelist的大小时运算符个数
                exercises.setOperatorNumber(valueList.size());
                //第一个元素已经被remove所以单独add
                valueList.add(expression);
                valueList.addAll(queue);
                exercises.setValueList(valueList);
                break;
            }
        }
    }

    /**
     * 解析题目中的题号
     *
     * @param exercises
     * @param question
     */
    private static void parseNumber(Exercises exercises, String question) {
        exercises.setNumber(Integer.parseInt(question.split("\\.")[0]));
    }

    /**
     * 返回等价的最简式
     * @return
     */
    public static String getEqualsExpression(String expression){
        String operator = "(\\" + Constant.PLUS + "|\\" + Constant.MINUS + "|\\" + Constant.MULTIPLY + "|\\" + Constant.DIVIDE + ")";
        //操作数交换
        return expression.split(operator)[1]+BeanUtil.getStr(operator,expression)+expression.split(operator)[0];
    }

}
