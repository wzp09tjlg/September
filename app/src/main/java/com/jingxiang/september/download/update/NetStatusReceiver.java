package com.jingxiang.september.download.update;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jingxiang.september.ui.service.DownService;
import com.jingxiang.september.util.NetUtil;
import com.jingxiang.september.util.ThreadPool;

/**
 * Created by wu on 2016/10/19.
 * 监听网络切换状态的广播
 * 1.自己的网 和 wifi的网 切换
 * 2.无网 到 有网的切换
 */
public class NetStatusReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        boolean hasNet = false;
       if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
           ConnectivityManager cm= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
           if (cm!=null) {
               NetworkInfo[] networkInfos=cm.getAllNetworkInfo();
               for (int i = 0; i < networkInfos.length; i++) {
                   NetworkInfo.State state=networkInfos[i].getState();
                   if (NetworkInfo.State.CONNECTED==state) {
                       hasNet = true;
                       doOperateConnect(context);
                       System.out.println("------------> Network is ok");
                       return;
                   }
               }

               if(!hasNet){
                   UpdateService.isPause = true;
               }
           }
       }
    }

    private void doOperateConnect(final Context context){
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
