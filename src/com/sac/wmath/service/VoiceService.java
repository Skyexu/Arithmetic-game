package com.sac.wmath.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.sac.wmath.R;

/**
 * 声音文件播放服务
 *
 * 
 */
public class VoiceService extends Service {
    private IBinder mBinder = new VoiceServiceBinder();
    private MediaPlayer mMediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mMediaPlayer == null) {
            // 添加容错机制,如果声音文件读取错误则不作处理,
            // 避免错误的出现另程序终止
            try {
                mMediaPlayer = MediaPlayer.create(this, R.raw.music_main);
                if (!mMediaPlayer.isLooping()) {
                    mMediaPlayer.setLooping(true);// 设置声音循环播放
                }
                mMediaPlayer.start();
            } catch (Exception unimportant) {
                unimportant.printStackTrace();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
            } catch (Exception unimportant) {
                unimportant.printStackTrace();
            } finally {
                mMediaPlayer = null;
            }
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        onDestroy();
    }

    public class VoiceServiceBinder extends Binder {
        public VoiceService getService() {
            return VoiceService.this;
        }
    }

}
