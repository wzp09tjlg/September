package com.jingxiang.september.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by wu on 2016/9/26.
 */
public class DownService extends Service {
    /** Data */

    /*****************************************/
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
