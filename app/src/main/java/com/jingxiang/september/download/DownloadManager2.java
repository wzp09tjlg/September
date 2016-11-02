package com.jingxiang.september.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.jingxiang.september.util.LogUtil;

/**
 * Created by wu on 2016/10/14.
 * 使用系统downloadmanager 下载的工具类(一次只能下载一个文件)
 * 1.条件: 网络权限
 * 2.url
 * 在注册广播是 添加权限
 *3.在使用downloadmanager之后记得一定要调用stopdownload的方法，因为如果不调用，就会存在内存溢出的问题。
 * observer 和 BroadcastReceiver是依附于context创建,如果在使用完下载。启动下载的activity就不能被销毁掉，
 * 造成内存溢出。
 *
 <receiver android:name="com.github.miao1007.konakanwallpaper.receiver.DownloadReceiver" >
 <intent-filter>
 < !--监听下载完成的那一瞬间-->
 <action android:name="android.intent.action.DOWNLOAD_COMPLETE" />
 </intent-filter>
 </receiver>
 */
public class DownloadManager2 {
    private final int DOWNLOAD = 0x10010;
    /** Data */
    private static DownloadManager2 downloadManager2;
    private static DownloadManager downloadManager;
    private DownloadManager.Request request;
    private String mUrl = "";        //下载路径
    private String mDirPath = "";    //下载文件保存的路径
    private String mFileName = "";   //下载文件的文件名
    private long   mDownloadId = -1; //当前下载任务的ID(可供查询下载的进度)
    private OnDownloadListener downloadListener;//传递下载进度的listener

    private String mTitle = ""; //下载时显示的标题
    private boolean mVisibleInDownloadsUi = true; //下载时是否对用户可见(如果不可见,需要申请权限)

    private CompleteReceiver completeReceiver; //查询下载进度的广播
    private DownloadObserver downloadObserver; //查询下载进度的观察者
    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case DOWNLOAD:
                    if(downloadListener != null)
                        downloadListener.onDownloadListener(msg.arg1);//将下载进度传递出来
                    break;
            }
        }
    };

    /********************************************/
    private DownloadManager2(Context context){
        downloadManager = (android.app.DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        completeReceiver = new CompleteReceiver();
        context.registerReceiver(completeReceiver, intentFilter);
    }

    public static DownloadManager2 getInstance(Context context){
        if(downloadManager2 == null){
            downloadManager2 = new DownloadManager2(context);
        }
        return downloadManager2;
    }

    public void startDownload(){
        request = new android.app.DownloadManager.Request(Uri.parse(mUrl));
        request.setDestinationInExternalPublicDir(mDirPath, mFileName);
        request.setTitle(mTitle);
        request.setVisibleInDownloadsUi(mVisibleInDownloadsUi);
        mDownloadId = downloadManager.enqueue(request);
    }

    public void startDownload(String url,String dirPath,String fileName){
        request = new android.app.DownloadManager.Request(Uri.parse(url));
        request.setDestinationInExternalPublicDir(dirPath, fileName);
        request.setTitle(mTitle);
        request.setVisibleInDownloadsUi(mVisibleInDownloadsUi);
        mDownloadId = downloadManager.enqueue(request);
    }

    public void startDownload(String url,String dirPath,String fileName,OnDownloadListener listener){
        request = new android.app.DownloadManager.Request(Uri.parse(url));
        request.setDestinationInExternalPublicDir(dirPath, fileName);
        request.setTitle(mTitle);
        request.setVisibleInDownloadsUi(mVisibleInDownloadsUi);
        mDownloadId = downloadManager.enqueue(request);
        downloadListener = listener;
    }

    public void selectDownloadPercent(Context context){
        if(mDownloadId < 0) return;
        downloadObserver= new DownloadObserver(handler,context,mDownloadId);
        context.getContentResolver().registerContentObserver(Uri.parse("content://downloads/"),true
                ,downloadObserver);
    }

    public void stopDownload(Context context){ //在某个地方调用downloadmanager之后 一定记得要在完成之后调用stopdownload
        context.unregisterReceiver(completeReceiver);
        context.getContentResolver().unregisterContentObserver(downloadObserver);
    }

    //设置属性
    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public void setmDirPath(String mDirPath) {
        this.mDirPath = mDirPath;
    }

    public void setmFileName(String mFileName) {
        this.mFileName = mFileName;
    }

    public void setDownloadListener(OnDownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmVisibleInDownloadsUi(boolean mVisibleInDownloadsUi) {
        this.mVisibleInDownloadsUi = mVisibleInDownloadsUi;
    }

    //监听下载进度的广播
    class CompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                LogUtil.i("download complete onReceive:" + completeDownloadId);
                if (completeDownloadId == mDownloadId) {
                    downloadListener.onCompleteListener();//下载完毕,回调完成的动作
                }
            }
        }
    }

    //监听下载进度的观察者
    class DownloadObserver extends ContentObserver {
        private long downid;
        private Handler handler;
        private Context context;

        public DownloadObserver(Handler handler, Context context, long downid) {
            super(handler);
            this.handler = handler;
            this.downid = downid;
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            //每当/data/data/com.android.providers.download/database/database.db变化后，触发onCHANGE，开始具体查询
            super.onChange(selfChange);
            //实例化查询类，这里需要一个刚刚的downid
            DownloadManager.Query query = new DownloadManager.Query().setFilterById(downid);
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            //这个就是数据库查询啦
            Cursor cursor = null;
            try{
                cursor = downloadManager.query(query);
                cursor.moveToFirst();
                int mDownload_so_far = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));//目前下载量
                int mDownload_all = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));          //总下载量
                int mProgress = (mDownload_so_far * 99) / mDownload_all;
                LogUtil.e( String.valueOf(mProgress));
                Message msg = Message.obtain();
                msg.what = DOWNLOAD;
                msg.arg1 = mProgress;
                handler.handleMessage(msg);
            }catch (Exception e){
            }finally {
                if(cursor != null && !cursor.isClosed())
                    cursor.close();
            }
        }
    }

    public interface OnDownloadListener{
        void onDownloadListener(int percent);
        void onCompleteListener();
    }
}
