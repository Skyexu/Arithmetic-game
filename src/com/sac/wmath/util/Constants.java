package com.sac.wmath.util;

/**
 * 常量封装类,用于封装一些多处使用的常量
 *
 * @author Skye
 */
public class Constants {
    // 表达式常量
    // 表达式运算操作符常量
    public static final char CALCULATE_OPERATOR_PLUS = '+';
    public static final char CALCULATE_OPERATOR_MINUS = '-';
    public static final char CALCULATE_OPERATOR_MULTIPLY = '×';
    public static final char CALCULATE_OPERATOR_DIVISION = '÷';

    // 表达式种类常量
    public static final int OPERATION_TYPE_JUDGE = 1;// 判断算式是否正确
    public static final int OPERATION_TYPE_FILL_CALCULATE_OPERATOR = 2;// 填写计算操作符
    public static final int OPERATION_TYPE_FILL_OPERAND_LEFT = 3;// 填写左边操作数
    public static final int OPERATION_TYPE_FILL_OPERAND_RIGHT = 4;// 填写右边操作数
    public static final int OPERATION_TYPE_FILL_RESULT = 5;// 填写算式结果

    // SharedPreferences使用的常量
    public static final String SP_DATA_NAME = "sp_data_name";
    public static final String high_score = "high_score";
    
    public static final String SP_DATA_EXTRA_RANGE = "sp_data_extra_range";
    public static final int range_ten = 1;
    public static final int range_hundred = 2;
    
    public static final String SP_DATA_EXTRA_SPEED = "sp_data_extra_speed";
    public static final int SPEED_SLOW = 1;
    public static final int SPEED_MIDDLE = 2;
    public static final int SPEED_FAST = 3;

    public static final String SP_DATA_EXTRA_NUMBER_JUDGE = "sp_data_extra_number_judge";
    public static final int NUMBER_JUDGE_DEFAULT = 10;
    public static final String SP_DATA_EXTRA_NUMBER_CALCULATE = "sp_data_extra_number_calculate";
    public static final int NUMBER_CALCULATE_DEFAULT = 20;

    //Intent使用的数据
    public static final String INTENT_EXTRA_SCORE_CORRECT_NUMBER = "intent_extra_score_correct_number";
    public static final String INTENT_EXTRA_SCORE_PASS_NUMBER = "intent_extra_score_pass_number";
    public static final String INTENT_EXTRA_SCORE_ERROR_NUMBER = "intent_extra_score_error_number";
}
