package com.jingxiang.september.ui.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jingxiang.september.R;
import com.jingxiang.september.util.LogUtil;

/**
 * Created by wu on 2016/10/17.
 * 1.简单的测试service的使用和与其他控件的交互
 */
public class PlayService extends Service {
    /** Data */
    private String mTitle;
    private MediaPlayer mediaPlayer;

    /************************************/
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.e("onCreate:");
        init(this);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        LogUtil.e("onStart");
        mediaPlayer.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.e("onStartCommand");

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.e("onBind");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.e("onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        LogUtil.e("onDestroy");
        super.onDestroy();
        mediaPlayer.stop();
    }

    private void init(Context context){
        mediaPlayer = MediaPlayer.create(context, R.raw.music);
    }

}
