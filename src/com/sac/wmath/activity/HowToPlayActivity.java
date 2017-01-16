package com.sac.wmath.activity;

import com.sac.wmath.R;
import com.sac.wmath.service.VoiceService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
/**
 * 使用说明
 * @author Skye
 *
 */
public class HowToPlayActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_howtoplay);
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
