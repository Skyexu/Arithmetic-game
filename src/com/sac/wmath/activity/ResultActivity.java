package com.sac.wmath.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sac.wmath.R;
import com.sac.wmath.service.VoiceService;
import com.sac.wmath.util.Constants;

/**
 * 普通模式结果展示界面
 *
 * 
 */
public class ResultActivity extends Activity {
    private int mCorrectNumber;
    private int mPassNumber;
    private int mErrorNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        if (intent == null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        initScore(intent);
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

    private void initScore(Intent intent) {
        mCorrectNumber = intent.getIntExtra(Constants.INTENT_EXTRA_SCORE_CORRECT_NUMBER, 0);
        mPassNumber = intent.getIntExtra(Constants.INTENT_EXTRA_SCORE_PASS_NUMBER, 0);
        mErrorNumber = intent.getIntExtra(Constants.INTENT_EXTRA_SCORE_ERROR_NUMBER, 0);
        
        double total = mCorrectNumber + mPassNumber + mErrorNumber;
        double flag = (double)(mCorrectNumber)/total;
        
        if(flag >= 0 && flag <= 0.2 ){
        	findViewById(R.id.star1).setVisibility(View.VISIBLE);
        	findViewById(R.id.star2).setVisibility(View.INVISIBLE);
        	findViewById(R.id.star3).setVisibility(View.INVISIBLE);
        	findViewById(R.id.star4).setVisibility(View.INVISIBLE);
        	findViewById(R.id.star5).setVisibility(View.INVISIBLE);
        	
        }else if(flag > 0.2 && flag <= 0.4){
        	findViewById(R.id.star1).setVisibility(View.VISIBLE);
        	findViewById(R.id.star2).setVisibility(View.VISIBLE);
        	findViewById(R.id.star3).setVisibility(View.INVISIBLE);
        	findViewById(R.id.star4).setVisibility(View.INVISIBLE);
        	findViewById(R.id.star5).setVisibility(View.INVISIBLE);
        }else if(flag > 0.4 && flag <= 0.6){
        	findViewById(R.id.star1).setVisibility(View.VISIBLE);
        	findViewById(R.id.star2).setVisibility(View.VISIBLE);
        	findViewById(R.id.star3).setVisibility(View.VISIBLE);
        	findViewById(R.id.star4).setVisibility(View.INVISIBLE);
        	findViewById(R.id.star5).setVisibility(View.INVISIBLE);
        }else if(flag > 0.6 && flag <= 0.8){
        	findViewById(R.id.star1).setVisibility(View.VISIBLE);
        	findViewById(R.id.star2).setVisibility(View.VISIBLE);
        	findViewById(R.id.star3).setVisibility(View.VISIBLE);
        	findViewById(R.id.star4).setVisibility(View.VISIBLE);
        	findViewById(R.id.star5).setVisibility(View.INVISIBLE);
        }else{
        	findViewById(R.id.star1).setVisibility(View.VISIBLE);
        	findViewById(R.id.star2).setVisibility(View.VISIBLE);
        	findViewById(R.id.star3).setVisibility(View.VISIBLE);
        	findViewById(R.id.star4).setVisibility(View.VISIBLE);
        	findViewById(R.id.star5).setVisibility(View.VISIBLE);
        }
        
        
        ((TextView) findViewById(R.id.activity_result_correct_view))
                .setText(getString(R.string.score_correct_hint) + mCorrectNumber);
        ((TextView) findViewById(R.id.activity_result_pass_view))
                .setText(getString(R.string.score_pass_hint) + mPassNumber);
        ((TextView) findViewById(R.id.activity_result_error_view))
                .setText(getString(R.string.score_error_hint) + mErrorNumber);
    }
    
    public void onAgainButtonClick(View v) {
        startActivity(new Intent(this, GameActivity.class));
        finish();
    }

    public void onQuitButtonClick(View v) {
    	startActivity(new Intent(this, HomeActivity.class));
    	finish();
    }
}
