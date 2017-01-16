package com.sac.wmath.activity;


import com.sac.wmath.service.VoiceService;
import com.sac.wmath.util.Constants;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import com.sac.wmath.R;

public class RankActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rank);

		//get text view
		TextView scoreView = (TextView)findViewById(R.id.high_scores_list);
		//get shared prefs
		SharedPreferences scorePrefs = getSharedPreferences(Constants.high_score, 0);
		//get scores
		String[] savedScores = scorePrefs.getString("highScores", "").split("\\|");
		//build string
		StringBuilder scoreBuild = new StringBuilder("");
		for(String score : savedScores){
			scoreBuild.append(score+"\n");
		}
		//display scores
		scoreView.setText(scoreBuild.toString());
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
