package com.sac.wmath.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.mob.tools.utils.UIHandler;
import com.sac.wmath.R;
import com.sac.wmath.model.Score;
import com.sac.wmath.service.VoiceService;
import com.sac.wmath.util.Constants;

/**
 * 无限模式结果展示界面
 *
 * 
 */
public class EndlessModeResultActivity extends Activity implements  PlatformActionListener, Callback{
    private int mCorrectNumber;
    private int mPassNumber;
    private int mErrorNumber;

    //++
	protected PlatformActionListener paListener;
	private static final int MSG_TOAST = 1;
	private static final int MSG_ACTION_CCALLBACK = 2;
	private static final int MSG_CANCEL_NOTIFY = 3;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endlessresult);

        Intent intent = getIntent();
        if (intent == null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        initScore(intent);
     // 初始化ShareSDK
        ShareSDK.initSDK(this);
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
        if(mCorrectNumber < 10){
        ((TextView) findViewById(R.id.activity_result_correct_view))
                .setText(getString(R.string.activity_result_badScore) +"\n"+ getString(R.string.score_correct_hint) + mCorrectNumber);
        }else{
        	((TextView) findViewById(R.id.activity_result_correct_view))
            .setText(getString(R.string.activity_result_goodScore) +"\n"+ getString(R.string.score_correct_hint) + mCorrectNumber);
        }
        ((TextView) findViewById(R.id.activity_result_pass_view))
                .setVisibility(View.INVISIBLE);
        ((TextView) findViewById(R.id.activity_result_error_view))
              .setVisibility(View.INVISIBLE);
        setHighScore(mCorrectNumber);
    }
    
  //set high score
  	private void setHighScore(int mCorrectNumber){
  		int exScore = mCorrectNumber;
  		SharedPreferences gamePrefs = getSharedPreferences(Constants.high_score, Activity.MODE_PRIVATE);;
  		if(exScore>0){
  			//we have a valid score	
  			SharedPreferences.Editor scoreEdit = gamePrefs.edit();
  			DateFormat dateForm = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
  			String dateOutput = dateForm.format(new Date());
  			//get existing scores
  			String scores = gamePrefs.getString("highScores", "");
  			//check for scores
  			if(scores.length()>0){
  				//we have existing scores
  				List<Score> scoreStrings = new ArrayList<Score>();
  				//split scores
  				String[] exScores = scores.split("\\|");
  				//add score object for each
  				for(String eSc : exScores){
  					String[] parts = eSc.split(" - ");
  					scoreStrings.add(new Score(parts[0], Integer.parseInt(parts[1])));
  				}
  				//new score
  				Score newScore = new Score(dateOutput, exScore);
  				scoreStrings.add(newScore);
  				//sort
  				Collections.sort(scoreStrings);
  				//get top ten
  				StringBuilder scoreBuild = new StringBuilder("");
  				for(int s=0; s<scoreStrings.size(); s++){
  					if(s>=10) break;
  					if(s>0) scoreBuild.append("|");
  					scoreBuild.append(scoreStrings.get(s).getScoreText());
  				}
  				//write to prefs
  				scoreEdit.putString("highScores", scoreBuild.toString());
  				scoreEdit.commit();

  			}
  			else{
  				//no existing scores
  				scoreEdit.putString("highScores", ""+dateOutput+" - "+exScore);
  				scoreEdit.commit();
  			}
  		}
  	}
    public void onShareButtonClick(View v) {
        //startActivity(new Intent(this, EndlessGameActivity.class));
    	showShare();
    	//startActivity(new Intent(this, MainActivity.class));
       // finish();
    }

    public void onQuitButtonClick(View v) {
        startActivity(new Intent(this, HomeActivity.class));
    	finish();
    }
    private void showShare() {
    	
    	 OnekeyShare oks = new OnekeyShare();
    	 //关闭sso授权
    	 oks.disableSSOWhenAuthorize(); 
    	 
    
    	 String title = "算你狠！";
    	 String text = "我在无限模式游戏中答对了" + mCorrectNumber + "道题。真的太棒了！" + "下载地址：http://dwz.cn/3nwl4q";
    	// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
    	 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
    	 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
    	 oks.setTitle(title);
    	 // text是分享文本，所有平台都需要这个字段
    	//标题对应的网址，如果没有可以不设置
    	// oks.setTitleUrl("http://mob.com");
    	 
    	 oks.setText(text);
    	 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
    	 //oks.setImagePath("/sdcard/home.jpg");//确保SDcard下面存在此张图片
    	 // url仅在微信（包括好友和朋友圈）中使用
    	 //oks.setUrl("http://mob.com");
    	
    	 oks.setImageUrl("http://imglf1.nosdn.127.net/img/YTZkTWJCRi91MjVMeVg2clR6elZ0Q0RFbHQ1V0M0V0xadENqcWxQVnFXMVNmNW4rNkRHQ0hBPT0.jpg");

    	// 启动分享GUI
    	 oks.show(this);
    	 
    	
    	 /**
    	ShareParams wechatMoments = new ShareParams();
		wechatMoments.setTitle("分享标题");	
		wechatMoments.setText("分享文本");
		wechatMoments.setUrl("http://mob.com");
		wechatMoments.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
		wechatMoments.setMusicUrl("http://www.zhlongyin.com/UploadFiles/xrxz/2011/5/201105051307513619.mp3");
		wechatMoments.setShareType(Platform.SHARE_MUSIC);
		Platform weixin = ShareSDK.getPlatform(EndlessModeResultActivity.this, WechatMoments.NAME);
		weixin.setPlatformActionListener(EndlessModeResultActivity.this);
		weixin.share(wechatMoments);
		**/
    	 }



	@Override
	public void onCancel(Platform platform, int action) {
		// TODO Auto-generated method stub
		// 取消
				Message msg = new Message();
				msg.what = MSG_ACTION_CCALLBACK;
				msg.arg1 = 3;
				msg.arg2 = action;
				msg.obj = platform;
				UIHandler.sendMessage(msg, this);
	}


	@Override
	public void onComplete(Platform platform, int action, HashMap<String, Object> arg2) {
		// 成功
				Message msg = new Message();
				msg.what = MSG_ACTION_CCALLBACK;
				msg.arg1 = 1;
				msg.arg2 = action;
				msg.obj = platform;
				UIHandler.sendMessage(msg,this);
	}

	@Override
	public void onError(Platform platform, int action, Throwable t) {
		// 失敗
		//打印错误信息,print the error msg
		t.printStackTrace();
		//错误监听,handle the error msg
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = t;
		UIHandler.sendMessage(msg, this);
		
	}
	
	public boolean handleMessage(Message msg) {	
		switch (msg.arg1) {
			case 1: {
				// 成功
				Toast.makeText(EndlessModeResultActivity.this,"分享成功", 10000).show();
				System.out.println("分享回调成功------------");
			}
			break;
			case 2: {
				// 失败
				Toast.makeText(EndlessModeResultActivity.this,"分享失败", 10000).show();
			}
			break;
			case 3: {
				// 取消
				Toast.makeText(EndlessModeResultActivity.this,"分享取消", 10000).show();
			}
			break;
		}
		
			return false;
	
}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
    
}
