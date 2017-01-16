package com.sac.wmath.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sac.wmath.R;
import com.sac.wmath.model.Operation;
import com.sac.wmath.service.VoiceService;
import com.sac.wmath.util.Constants;
import com.sac.wmath.util.CountDownListener;
import com.sac.wmath.widget.CountDownWidget;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 正常模式游戏界面
 *
 * 
 */
public class GameActivity extends Activity implements CountDownListener, View.OnClickListener {
    private CustomCountDownTimer mCustomCountDownTimer;
    private int mCountDownMillis;
    private int mCalculateNumber;
    private int mJudgeNumber;

    //分数
    private int mCorrectNumber;
    private int mErrorNumber;
    private int mPassNumber;

    
    //当前表达式
    private Operation mCurrentOperation;

    private CountDownWidget mCountDownWidget;
    private TextView mCorrectShowView, mPassShowView, mErrorShowView;

    //两个选项
    private ViewGroup mTwoChoiceViewContainer;
    private View mTwoChoiceCorrectView, mTwoChoiceErrorView;

    //四个选项
    private ViewGroup mFourChoiceViewContainer;
    private TextView mFourChoiceFirstView, mFourChoiceSecondView, mFourChoiceThirdView, mFourChoiceForthView;

    //表达式显示视图
    private TextView mOperandLeftView, mCalculateOperatorView, mOperandRightView, mResultView;
    
    //获取范围
    private int range;
    
    //问题列表
    private List<Operation> mAnswerList = new ArrayList<Operation>();
    
    //问题随机数,用于从问题列表中随机取数据
    private int[] randomQuestion;
    
    private void init() {
        //初始化每一道题目的时间
        SharedPreferences sp = getSharedPreferences(Constants.SP_DATA_NAME, Activity.MODE_PRIVATE);
        //getInt()取值，没有就用默认值代替
        int speed = sp.getInt(Constants.SP_DATA_EXTRA_SPEED, Constants.SPEED_SLOW);
        switch (speed) {
            case Constants.SPEED_SLOW:
                mCountDownMillis = 4000;
                break;
            case Constants.SPEED_MIDDLE:
                mCountDownMillis = 3000;
                break;
            case Constants.SPEED_FAST:
                mCountDownMillis = 2000;
                break;
            default:
                mCountDownMillis = 4000;
                break;
        }

        //初始化题目数量
        mCalculateNumber = sp.getInt(Constants.SP_DATA_EXTRA_NUMBER_CALCULATE,
                Constants.NUMBER_CALCULATE_DEFAULT);
        mJudgeNumber = sp.getInt(Constants.SP_DATA_EXTRA_NUMBER_JUDGE, Constants.NUMBER_JUDGE_DEFAULT);
        
        //初始化问题随机数
        randomQuestion = randomQuestionType(mCalculateNumber+mJudgeNumber);
        
        //获取范围
        range = sp.getInt(Constants.SP_DATA_EXTRA_RANGE, Constants.range_ten);
        
        //生成满足要求的表达式实例
        for (int offset = 0; offset < mJudgeNumber; offset++) {
            mAnswerList.add(Operation.createOperation(Constants.OPERATION_TYPE_JUDGE,range));
        }
        Random r = new Random();
        for (int offset = 0; offset < mCalculateNumber; offset++) {
            int type = r.nextInt(4);
            if (type == 0) {
                mAnswerList.add(Operation.createOperation(Constants.OPERATION_TYPE_FILL_OPERAND_LEFT,range));
            } else if (type == 1) {
                mAnswerList.add(Operation.createOperation(Constants.OPERATION_TYPE_FILL_OPERAND_RIGHT,range));
            } else if (type == 2) {
                mAnswerList.add(Operation.createOperation(Constants.OPERATION_TYPE_FILL_CALCULATE_OPERATOR,range));
            } else {
                mAnswerList.add(Operation.createOperation(Constants.OPERATION_TYPE_FILL_RESULT,range));
            }
        }
    }

    private void initView() {
        mCountDownWidget = (CountDownWidget) findViewById(R.id.activity_game_count_down);
        mCorrectShowView = (TextView) findViewById(R.id.activity_game_score_correct);
        mPassShowView = (TextView) findViewById(R.id.activity_game_score_pass);
        mErrorShowView = (TextView) findViewById(R.id.activity_game_score_error);

        mTwoChoiceViewContainer = (ViewGroup) findViewById(R.id.activity_game_operation_two_choice);
        mTwoChoiceCorrectView = findViewById(R.id.activity_game_operation_two_choice_correct);
        mTwoChoiceErrorView = findViewById(R.id.activity_game_operation_two_choice_error);
        mTwoChoiceCorrectView.setOnClickListener(this);
        mTwoChoiceErrorView.setOnClickListener(this);

        mFourChoiceViewContainer = (ViewGroup) findViewById(R.id.activity_game_operation_four_choice);
        mFourChoiceFirstView = (TextView) findViewById(R.id.activity_game_operation_four_choice_one);
        mFourChoiceSecondView = (TextView) findViewById(R.id.activity_game_operation_four_choice_two);
        mFourChoiceThirdView = (TextView) findViewById(R.id.activity_game_operation_four_choice_three);
        mFourChoiceForthView = (TextView) findViewById(R.id.activity_game_operation_four_choice_four);
        mFourChoiceFirstView.setOnClickListener(this);
        mFourChoiceSecondView.setOnClickListener(this);
        mFourChoiceThirdView.setOnClickListener(this);
        mFourChoiceForthView.setOnClickListener(this);

        mOperandLeftView = (TextView) findViewById(R.id.activity_game_operation_operand_left);
        mCalculateOperatorView = (TextView) findViewById(R.id.activity_game_operation_calculate_operator);
        mOperandRightView = (TextView) findViewById(R.id.activity_game_operation_operand_right);
        mResultView = (TextView) findViewById(R.id.activity_game_operation_result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        init();
        initView();
        next();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(this, VoiceService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, VoiceService.class));
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mCustomCountDownTimer != null) {
                mCustomCountDownTimer.cancel();
            }
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFinish() {
        mPassNumber++;
        next();
    }

    @Override
    public void onTrick(long millisUntilFinished) {
        mCountDownWidget.changeCurrentNumber();
    }

    private synchronized void next() {
        updateScore();
        if (mCustomCountDownTimer != null) {
            mCustomCountDownTimer.cancel();
        }
        int currentIndex = mCorrectNumber + mPassNumber + mErrorNumber;
        if (currentIndex == mJudgeNumber + mCalculateNumber) {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra(Constants.INTENT_EXTRA_SCORE_CORRECT_NUMBER, mCorrectNumber);
            intent.putExtra(Constants.INTENT_EXTRA_SCORE_PASS_NUMBER, mPassNumber);
            intent.putExtra(Constants.INTENT_EXTRA_SCORE_ERROR_NUMBER, mErrorNumber);
            startActivity(intent);
            finish();
            return;
        }
        if (currentIndex < mJudgeNumber + mCalculateNumber) {
            mCustomCountDownTimer = new CustomCountDownTimer(mCountDownMillis, mCountDownMillis / 20, this);
            mCurrentOperation = mAnswerList.get(randomQuestion[currentIndex]);
            updateOperation(mCurrentOperation);
            mCountDownWidget.reset();
            mCustomCountDownTimer.start();
        }
    }
    
    /**
     * 
     * @param range  随机数范围  0~9 或者  0~99
     * @return  随机数集
     */
    private int[] randomQuestionType(int range){

        Random rand = new Random();
        int[] result = new int[range];
        boolean[]  bool = new boolean[range];

        int randInt = 0;

        for(int i = 0; i < range ; i++) {

             do {
                 randInt  = rand.nextInt(range);

             }while(bool[randInt]);

            bool[randInt] = true;
            result[i] = randInt;
       }
        return result;
        
    }
    private synchronized void updateScore() {
        mCorrectShowView.setText(getString(R.string.score_correct_hint) + mCorrectNumber);
        mPassShowView.setText(getString(R.string.score_pass_hint) + mPassNumber);
        mErrorShowView.setText(getString(R.string.score_error_hint) + mErrorNumber);
    }

    private synchronized void updateOperation(Operation operation) {
        if (operation.getType() == Constants.OPERATION_TYPE_JUDGE) {
            mOperandLeftView.setText(operation.getOperandLeft() + "");
            mCalculateOperatorView.setText(operation.getCalculateOperator() + "");
            mOperandRightView.setText(operation.getOperandRight() + "");
            mResultView.setText(operation.getResult() + "");
            mTwoChoiceViewContainer.setVisibility(View.VISIBLE);
            mFourChoiceViewContainer.setVisibility(View.GONE);
        } else {
            mTwoChoiceViewContainer.setVisibility(View.GONE);
            mFourChoiceViewContainer.setVisibility(View.VISIBLE);
            if (operation.getType() == Constants.OPERATION_TYPE_FILL_OPERAND_LEFT) {
                mOperandLeftView.setText("?");
                mCalculateOperatorView.setText(operation.getCalculateOperator() + "");
                mOperandRightView.setText(operation.getOperandRight() + "");
                mResultView.setText(operation.getResult() + "");
            } else if (operation.getType() == Constants.OPERATION_TYPE_FILL_CALCULATE_OPERATOR) {
                mOperandLeftView.setText(operation.getOperandLeft() + "");
                mCalculateOperatorView.setText("?");
                mOperandRightView.setText(operation.getOperandRight() + "");
                mResultView.setText(operation.getResult() + "");
            } else if (operation.getType() == Constants.OPERATION_TYPE_FILL_OPERAND_RIGHT) {
                mOperandLeftView.setText(operation.getOperandLeft() + "");
                mCalculateOperatorView.setText(operation.getCalculateOperator() + "");
                mOperandRightView.setText("?");
                mResultView.setText(operation.getResult() + "");
            } else {
                mOperandLeftView.setText(operation.getOperandLeft() + "");
                mCalculateOperatorView.setText(operation.getCalculateOperator() + "");
                mOperandRightView.setText(operation.getOperandRight() + "");
                mResultView.setText("?");
            }
        }

        if (operation.getType() == Constants.OPERATION_TYPE_FILL_OPERAND_LEFT
                || operation.getType() == Constants.OPERATION_TYPE_FILL_OPERAND_RIGHT
                || operation.getType() == Constants.OPERATION_TYPE_FILL_RESULT) {
            List<Integer> choices = Operation.getChoices(operation);
           
            if (choices.size() == 4) {
                mFourChoiceFirstView.setText(choices.get(0) + "");
                mFourChoiceSecondView.setText(choices.get(1) + "");
                mFourChoiceThirdView.setText(choices.get(2) + "");
                mFourChoiceForthView.setText(choices.get(3) + "");
            }
            choices.clear();
        } else {
        	
        		 mFourChoiceFirstView.setText(R.string.activity_game_operation_plus);
                 mFourChoiceSecondView.setText(R.string.activity_game_operation_minus);
                 mFourChoiceThirdView.setText(R.string.activity_game_operation_multiply);
                 mFourChoiceForthView.setText(R.string.activity_game_operation_division);
        	
           
            
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.activity_game_operation_two_choice_correct) {
            if (mCurrentOperation.judgeOperationResult()) {
                mCorrectNumber++;
            } else {
                mErrorNumber++;
            }
            next();
            return;
        } else if (v.getId() == R.id.activity_game_operation_two_choice_error) {
            if (!mCurrentOperation.judgeOperationResult()) {
                mCorrectNumber++;
            } else {
                mErrorNumber++;
            }
            next();
            return;
        }
        String fill;
        switch (v.getId()) {
            case R.id.activity_game_operation_four_choice_one:
                fill = mFourChoiceFirstView.getText().toString();
                break;
            case R.id.activity_game_operation_four_choice_two:
                fill = mFourChoiceSecondView.getText().toString();
                break;
            case R.id.activity_game_operation_four_choice_three:
                fill = mFourChoiceThirdView.getText().toString();
                break;
            default:
                fill = mFourChoiceForthView.getText().toString();
                break;
        }
        if (mCurrentOperation.judgeOperationResult(fill)) {
            mCorrectNumber++;
        } else {
            mErrorNumber++;
        }
        next();
    }

    /**
     * 自定义倒计时封装类
     *
     * 
     */
    class CustomCountDownTimer extends CountDownTimer {
        private final WeakReference<CountDownListener> mListener;

        public CustomCountDownTimer(long millisInFuture, long countDownInterval, CountDownListener listener) {
            super(millisInFuture, countDownInterval);
            mListener = new WeakReference<CountDownListener>(listener);
        }

        @Override
        public void onFinish() {
            CountDownListener listener = mListener.get();
            if (listener != null) {
                listener.onFinish();
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            CountDownListener listener = mListener.get();
            if (listener != null) {
                listener.onTrick(millisUntilFinished);
            }
        }

    }
}
