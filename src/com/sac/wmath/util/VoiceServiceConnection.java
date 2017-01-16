package com.sac.wmath.util;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * 用于与{@link com.sac.wmath.service.VoiceService}交互的{@link ServiceConnection}实现类
 *
 * 
 */
public class VoiceServiceConnection implements ServiceConnection {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
