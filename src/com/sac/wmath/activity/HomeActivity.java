package com.sac.wmath.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sac.wmath.R;
import com.sac.wmath.service.VoiceService;

/**
 * 主界面
 *
 * 
 */
public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void onNormalModeButtonClick(View v) {
        startActivity(new Intent(this, GameActivity.class));
        finish();
    }
    public void onEndlessModeButtonClick(View v){
    	startActivity(new Intent(this, EndlessGameActivity.class));
    	finish();
    }
    
    public void onRankButtonClick(View v){
    	startActivity(new Intent(this, RankActivity.class));
    	
    }
    public void onHowToPlayButtonClick(View v){
    	startActivity(new Intent(this,HowToPlayActivity.class));
    }
    public void onQuitButtonClick(View v) {
        finish();
    }

    public void onSettingButtonClick(View v) {
        startActivity(new Intent(this, SettingActivity.class));
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
}
