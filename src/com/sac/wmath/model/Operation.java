package com.sac.wmath.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.sac.wmath.util.Constants;

/**
 * 表达式封装类,用于封装操作表达式基本的参数和方法
 *
 */
public class Operation {
    private int mOperandLeft;// 左操作数
    private int mOperandRight;// 右操作数
    private char mCalculateOperator;// 计算操作符
    private int mResult;// 表达式运算结果

    private int mType;// 表达式种类

    public Operation(int operandLeft, int operandRight, char calculateOperator,
                     int result, int type) {
        mOperandLeft = operandLeft;
        mOperandRight = operandRight;
        mCalculateOperator = calculateOperator;
        mResult = result;
        mType = type;
    }

    /**
     * 判断表达式是否成立
     *
     * @return 判断结果
     * <ul>
     * <li>true:表达式成立</li>
     * <li>false:表达式不成立</li>
     * </ul>
     */
    public boolean judgeOperationResult() {
        if (Constants.CALCULATE_OPERATOR_PLUS == mCalculateOperator) {
            return mResult == mOperandLeft + mOperandRight;
        }
        if (Constants.CALCULATE_OPERATOR_MINUS == mCalculateOperator) {
            return mResult == mOperandLeft - mOperandRight;
        }
        if (Constants.CALCULATE_OPERATOR_MULTIPLY == mCalculateOperator) {
            return mResult == mOperandLeft * mOperandRight;
        }
        if (Constants.CALCULATE_OPERATOR_DIVISION == mCalculateOperator) {
            return mOperandLeft == mOperandRight * mResult;
        }
        return false;
    }

    /**
     * 判断选择的表达式能否让等式成立
     *
     * @param operator 运算符常量
     * @return 判断结果
     * <ul>
     * <li>true:表达式成立</li>
     * <li>false:表达式不成立</li>
     * </ul>
     */
    public boolean judgeOperationResult(int operator) {
        if (Constants.CALCULATE_OPERATOR_PLUS == operator) {
            return mResult == mOperandLeft + mOperandRight;
        }
        if (Constants.CALCULATE_OPERATOR_MINUS == operator) {
            return mResult == mOperandLeft - mOperandRight;
        }
        if (Constants.CALCULATE_OPERATOR_MULTIPLY == operator) {
            return mResult == mOperandLeft * mOperandRight;
        }
        if (Constants.CALCULATE_OPERATOR_DIVISION == operator) {
            return mOperandLeft == mOperandRight * mResult;
        }
        return false;
    }

    /**
     * 判断填入响应的内容(包括运算数和运算符和比较符)之后,表达式是否成立
     *
     * @param fill 填充内容
     * @return 判断结果
     * <ul>
     * <li>true:表达式成立</li>
     * <li>false:表达式不成立</li>
     * </ul>
     */
    public boolean judgeOperationResult(String fill) {
        if (fill == null || fill.trim().equals("")) {
            return judgeOperationResult();
        }
        // 先更改对应数据为用户所选择或者输入的数据,然后对修改后的表达式进行判断
        // 为了避免出现数据格式转换错误造成的异常,需要添加异常捕获
        //java.lang.String.trim() 方法返回一个字符串副本，并忽略(去除)开头和结尾的空白
        try {
            switch (mType) {
                case Constants.OPERATION_TYPE_FILL_OPERAND_LEFT:
                    this.mOperandLeft = Integer.parseInt(fill.trim());
                    break;
                case Constants.OPERATION_TYPE_FILL_CALCULATE_OPERATOR:
                    this.mCalculateOperator = fill.trim().charAt(0);
                    break;
                case Constants.OPERATION_TYPE_FILL_OPERAND_RIGHT:
                    this.mOperandRight = Integer.parseInt(fill.trim());
                    break;
                case Constants.OPERATION_TYPE_FILL_RESULT:
                    this.mResult = Integer.parseInt(fill.trim());
                    break;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
        return judgeOperationResult();
    }

    public int getOperandLeft() {
        return mOperandLeft;
    }

    public int getOperandRight() {
        return mOperandRight;
    }

    public char getCalculateOperator() {
        return mCalculateOperator;
    }

    public int getResult() {
        return mResult;
    }

    public int getType() {
        return mType;
    }

    /**
     * 加入变量   rangeChoice 10以内   加减乘除    100以内加减
     * @param type
     * @return
     */
    public static Operation createOperation(int type,int rangeChoice) {    
        Random r = new Random();							
        Operation operation;
        
        int range = 9;
        if(rangeChoice==2){
        	range = 99;
        }
        int calculateOperatorType = r.nextInt(4);
        if(rangeChoice==2){
        	calculateOperatorType = r.nextInt(2);
        }
        int offset = r.nextInt(3) - 1;     
        //使用result+offset给出判断题的  result-1 result+1 和 result+0 三种结果 
        if (calculateOperatorType == 0) {
            char operator = Constants.CALCULATE_OPERATOR_PLUS;
            int operandLeft = r.nextInt(range) + 1;
            int operandRight = r.nextInt(range) + 1;
            int result = operandLeft + operandRight;
            operation = new Operation(operandLeft, operandRight, operator,
                    type == Constants.OPERATION_TYPE_JUDGE ? (result + offset) : result, type);
        } else if (calculateOperatorType == 1) {
            char operator = Constants.CALCULATE_OPERATOR_MINUS;
            int operandLeft = r.nextInt(range) + 1;
            int operandRight = r.nextInt(range) + 1;
            int result = operandLeft - operandRight;
            operation = new Operation(operandLeft, operandRight, operator,
                    type == Constants.OPERATION_TYPE_JUDGE ? (result + offset) : result, type);
        } else if (calculateOperatorType == 2) {
            char operator = Constants.CALCULATE_OPERATOR_MULTIPLY;
            int operandLeft = r.nextInt(range) + 1;
            int operandRight = r.nextInt(range) + 1;
            int result = operandLeft * operandRight;
            operation = new Operation(operandLeft, operandRight, operator,
                    type == Constants.OPERATION_TYPE_JUDGE ? (result + offset) : result, type);
        } else {
            char operator = Constants.CALCULATE_OPERATOR_DIVISION;
            int result = r.nextInt(range) + 1;
            int operandRight = r.nextInt(range) + 1;
            int operandLeft = result * operandRight;
            operation = new Operation(operandLeft, operandRight, operator,
                    type == Constants.OPERATION_TYPE_JUDGE ? (result + offset) : result, type);
        }
        return operation;
    }

    public static List<Integer> getChoices(Operation operation) {
        List<Integer> choices = new ArrayList<Integer>();
        if (operation == null || operation.getType() == Constants.OPERATION_TYPE_JUDGE
                || operation.getType() == Constants.OPERATION_TYPE_FILL_CALCULATE_OPERATOR) {
            return choices;
        }
        int correctChoice = 0;
        switch (operation.getType()) {
            case Constants.OPERATION_TYPE_FILL_OPERAND_LEFT:
                correctChoice = operation.getOperandLeft();
                break;
            case Constants.OPERATION_TYPE_FILL_OPERAND_RIGHT:
                correctChoice = operation.getOperandRight();
                break;
            default:
                correctChoice = operation.getResult();
                break;
        }
        int offset = new Random().nextInt(4);
        choices.add(correctChoice - offset);
        choices.add(correctChoice - offset + 1);
        choices.add(correctChoice - offset + 2);
        choices.add(correctChoice - offset + 3);
        return choices;
    }
}
