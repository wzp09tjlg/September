package com.jingxiang.september.download.update;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.jingxiang.september.ui.service.DownService;
import com.jingxiang.september.util.NetUtil;
import com.jingxiang.september.util.ThreadPool;

/**
 * Created by wu on 2016/10/19.
 * 监听网络切换状态的广播
 */
public class NetSatusReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
       if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
          boolean isWifi =  NetUtil.isNetWorkWifi(context);
           if(isWifi){
               ThreadPool.execute(new Runnable() {
                   @Override
                   public void run() {
                       Intent intentStartDownload = new Intent(context, DownService.class);
                       context.startService(intentStartDownload);
                   }
               });
           }else{
               ThreadPool.execute(new Runnable() {
                   @Override
                   public void run() {
                       Intent intentStartDownload = new Intent(context, DownService.class);
                       context.stopService(intentStartDownload);
                   }
               });
           }
       }
    }
}
