package com.gdut.generator.service.impl;

import com.gdut.generator.service.OperationService;

/***********************************************************
 *                         运算符
 **********************************************************/

//op()方法没写完

public enum OperatorEnum implements OperationService {
    ADD("+",0,2){
        @Override
        public String op(String num_1, String num_2) {
            //将分数转换成假分数形式

            return null;
        }
    },
    MINUS("-",1, 2) {
        @Override
        public String op(String num_1, String num_2) {
            return null;
        }
    },
    MULTIPLY("×",2, 1) {
        @Override
        public String op(String num_1, String num_2) {
            return null;
        }
    },
    DIVIDE("÷",3, 1) {
        @Override
        public String op(String num_1, String num_2) {
            return null;
        }
    };

    //field
    private final String opSymbol; //运算符标志
    private final int opValue;     //运算符对应索引
    private final int priority;    //运算符优先级，数字小则优先级高

    //constructor
    private OperatorEnum(String opSymbol, int opValue, int priority) {
        this.opSymbol = opSymbol;
        this.opValue = opValue;
        this.priority = priority;
    }

    //methods
    public String getOpSymbol(){ return opSymbol; }
    public int getOpValue(){ return opValue; }
    public int getPriority(){ return priority; }

    /**
     * 通过索引获取枚举
     * @param opNum 操作符索引
     * @return OperatorEnum
     */
    public static OperatorEnum getEnumByOpValue(int opNum){
        for(OperatorEnum operatorEnum : OperatorEnum.values()){
            if(operatorEnum.getOpValue() == opNum){
                return operatorEnum;
            }
        }
        return null;
    }

    /**
     * 通过运算符标志获取枚举
     * @param opSymbol 运算符标志
     * @return OperatorEnum
     */
    public static OperatorEnum getEnumByOpSymbol(String opSymbol) {
        for(OperatorEnum operatorEnum : OperatorEnum.values()){
            if(operatorEnum.getOpSymbol().equals(opSymbol)){
                return operatorEnum;
            }
        }
        return null;
    }

}
