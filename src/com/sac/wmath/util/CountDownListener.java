package com.sac.wmath.util;

/**
 * 倒计时监听接口，当倒计时组件完成倒计时或者完成一次倒计时间隔的时候会通过这个接口通知监听者.
 *
 * 
 */
public interface CountDownListener {
    void onFinish();

    void onTrick(long millisUntilFinished);
}

