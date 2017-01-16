package com.sac.wmath.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.sac.wmath.R;
import com.sac.wmath.service.VoiceService;
import com.sac.wmath.util.Constants;

/**
 * 设置界面,用于设置游戏过程中使用的参数
 *
 */
public class SettingActivity extends Activity {
    private RadioGroup mSpeedChoicesRG;
    private RadioGroup mRangeChoice;
    private SharedPreferences mSharedPreferences;
    private EditText mJudgeInputET;
    private EditText mCalculateInputET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initialize();
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


    /**
     * 确定按键相应事件
     *
     * @param v
     */
    public void onSureButtonClick(View v) {
        try {
            int judgeNumber = Integer.parseInt(mJudgeInputET.getText()
                    .toString());
            if (judgeNumber > 50 || judgeNumber < 10) {
                showErrorDialog();
                return;
            }
            mSharedPreferences.edit().putInt(Constants.SP_DATA_EXTRA_NUMBER_JUDGE, judgeNumber)
                    .commit();
            int calculateNumber = Integer.parseInt(mCalculateInputET.getText()
                    .toString());
            if (calculateNumber > 50 || calculateNumber < 10) {
                showErrorDialog();
                return;
            }
            mSharedPreferences.edit()
                    .putInt(Constants.SP_DATA_EXTRA_NUMBER_CALCULATE,
                            calculateNumber).commit();
        } catch (Exception e) {
            showErrorDialog();
        }
        finish();
    }

    /**
     * 显示出错信息
     */
    public void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                SettingActivity.this);
        builder.setTitle(R.string.activity_setting_dialog_title);
        builder.setMessage(R.string.activity_setting_dialog_message);
        builder.setPositiveButton(R.string.ok, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSharedPreferences.edit()
                        .putInt(Constants.SP_DATA_EXTRA_NUMBER_JUDGE,
                                Constants.NUMBER_JUDGE_DEFAULT)
                        .putInt(Constants.SP_DATA_EXTRA_NUMBER_CALCULATE,
                                Constants.NUMBER_CALCULATE_DEFAULT).commit();
                SettingActivity.this.finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 参数以及相关控件的初始化
     */
    public void initialize() {
        mSharedPreferences = getSharedPreferences(Constants.SP_DATA_NAME, Activity.MODE_PRIVATE);
        
        mSpeedChoicesRG = (RadioGroup) findViewById(R.id.activity_setting_menu_speed_choices);
        mRangeChoice = (RadioGroup) findViewById(R.id.activity_setting_menu_numRange_choices);
        //初始化range
        int range = mSharedPreferences.getInt(Constants.SP_DATA_EXTRA_RANGE, Constants.range_ten);
        switch(range){
        	case Constants.range_ten:
        		mRangeChoice.check(R.id.activity_setting_menu_numRange_choice_ten);
        		break;
        	case Constants.range_hundred:
        		mRangeChoice.check(R.id.activity_setting_menu_numRange_choice_hundred);
        		break;
        	
        		
        }
        
        
        
        //初始化speed
        int speed = mSharedPreferences.getInt(Constants.SP_DATA_EXTRA_SPEED,
                Constants.SPEED_SLOW);
        switch (speed) {
            case Constants.SPEED_SLOW:
                mSpeedChoicesRG.check(R.id.activity_setting_menu_speed_choice_slow);
                break;
            case Constants.SPEED_MIDDLE:
                mSpeedChoicesRG
                        .check(R.id.activity_setting_menu_speed_choice_middle);
                break;
            case Constants.SPEED_FAST:
                mSpeedChoicesRG.check(R.id.activity_setting_menu_speed_choice_fast);
                break;
        }
        
        mJudgeInputET = (EditText) findViewById(R.id.activity_setting_menu_number_judge_input);
        mCalculateInputET = (EditText) findViewById(R.id.activity_setting_menu_number_calculate_input);

        mJudgeInputET.setText(mSharedPreferences.getInt(Constants.SP_DATA_EXTRA_NUMBER_JUDGE,
                Constants.NUMBER_JUDGE_DEFAULT) + "");
        mCalculateInputET.setText(mSharedPreferences.getInt(
                Constants.SP_DATA_EXTRA_NUMBER_CALCULATE,
                Constants.NUMBER_CALCULATE_DEFAULT)
                + "");

        mSpeedChoicesRG.setOnCheckedChangeListener(onCheckedChangeListener);
        mRangeChoice.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    // 单选框监听者
    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                // 速度设置单选项状态改变响应逻辑
                case R.id.activity_setting_menu_speed_choice_slow:
                    mSharedPreferences.edit()
                            .putInt(Constants.SP_DATA_EXTRA_SPEED,
                                    Constants.SPEED_SLOW).commit();
                    break;
                case R.id.activity_setting_menu_speed_choice_middle:
                    mSharedPreferences.edit()
                            .putInt(Constants.SP_DATA_EXTRA_SPEED,
                                    Constants.SPEED_MIDDLE).commit();
                    break;
                case R.id.activity_setting_menu_speed_choice_fast:
                    mSharedPreferences.edit()
                            .putInt(Constants.SP_DATA_EXTRA_SPEED,
                                    Constants.SPEED_FAST).commit();
                    break;
                //范围设置
                case R.id.activity_setting_menu_numRange_choice_ten:
                    mSharedPreferences.edit()
                            .putInt(Constants.SP_DATA_EXTRA_RANGE,
                                    Constants.range_ten).commit();
                    break;
                case R.id.activity_setting_menu_numRange_choice_hundred:
                    mSharedPreferences.edit()
                            .putInt(Constants.SP_DATA_EXTRA_RANGE,
                                    Constants.range_hundred).commit();
                    break;
                default:
                    break;
            }
        }
    };
}
