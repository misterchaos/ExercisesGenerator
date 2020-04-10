package com.gdut.generator.service.impl;

import com.gdut.generator.constant.Constant;
import com.gdut.generator.service.OperationService;
import com.gdut.generator.util.CalculateUtil;

/***********************************************************
 *                         运算符
 **********************************************************/

//op()方法没写完

public enum OperatorEnum implements OperationService {
    /**
     * 加法
     */
    ADD(Constant.PLUS, 0, 2) {
        @Override
        public String op(String num1, String num2) {
            //通分
            String numbers[] = CalculateUtil.switchToCommonDenominator(num1, num2);
            //获取分母分子
            int denominator = CalculateUtil.getDenominator(numbers[0]);
            int numerator = CalculateUtil.getNumerator(numbers[0]) + CalculateUtil.getNumerator(numbers[1]);
            return CalculateUtil.reduction(numerator, denominator);
        }
    },
    MINUS(Constant.MINUS, 1, 2) {
        @Override
        public String op(String num1, String num2) {
            //通分
            String numbers[] = CalculateUtil.switchToCommonDenominator(num1, num2);
            //获取分母分子
            int denominator = CalculateUtil.getDenominator(numbers[0]);
            int numerator = CalculateUtil.getNumerator(numbers[0]) - CalculateUtil.getNumerator(numbers[1]);
            //出现负数返回null
            if (numerator < 0) {
                return null;
            } else {
                return CalculateUtil.reduction(numerator, denominator);
            }
        }
    },
    MULTIPLY(Constant.MULTIPLY, 2, 1) {
        @Override
        public String op(String num1, String num2) {

            //获取分母分子
            int denominator1 = CalculateUtil.getDenominator(num1);
            int numerator1 = CalculateUtil.getNumerator(num1);
            int denominator2 = CalculateUtil.getDenominator(num2);
            int numerator2 = CalculateUtil.getNumerator(num2);

            String result = CalculateUtil.reduction(numerator1 * numerator2, denominator1 * denominator2);
            return result;
        }
    },
    DIVIDE(Constant.DIVIDE, 3, 1) {
        @Override
        public String op(String num1, String num2) {
            //获取分母分子
            int denominator1 = CalculateUtil.getDenominator(num1);
            int numerator1 = CalculateUtil.getNumerator(num1);
            //获取倒数
            String reciprocal = denominator1 + "/" + numerator1;
            //做乘法
            String result = MULTIPLY.op(reciprocal, num2);
            //如果不是真分数返回Null
            if (CalculateUtil.isProperFraction(reciprocal)) {
                return result;
            } else {
                return null;
            }
        }
    };

    /**
     * field
     * 运算符标志
     */
    private final String opSymbol;
    /**
     * 运算符对应索引
     */
    private final int opValue;
    /**
     * 运算符优先级，数字小则优先级高
     */
    private final int priority;

    /**
     * constructor
     */
    private OperatorEnum(String opSymbol, int opValue, int priority) {
        this.opSymbol = opSymbol;
        this.opValue = opValue;
        this.priority = priority;
    }

    //methods
    public String getOpSymbol() {
        return opSymbol;
    }

    public int getOpValue() {
        return opValue;
    }

    public int getPriority() {
        return priority;
    }

    /**
     * 通过索引获取枚举
     *
     * @param opNum 操作符索引
     * @return OperatorEnum
     */
    public static OperatorEnum getEnumByOpValue(int opNum) {
        for (OperatorEnum operatorEnum : OperatorEnum.values()) {
            if (operatorEnum.getOpValue() == opNum) {
                return operatorEnum;
            }
        }
        return null;
    }

    /**
     * 通过运算符标志获取枚举
     *
     * @param opSymbol 运算符标志
     * @return OperatorEnum
     */
    public static OperatorEnum getEnumByOpSymbol(String opSymbol) {
        for (OperatorEnum operatorEnum : OperatorEnum.values()) {
            if (operatorEnum.getOpSymbol().equalsIgnoreCase(opSymbol)) {
                return operatorEnum;
            }
        }
        return null;
    }

}
