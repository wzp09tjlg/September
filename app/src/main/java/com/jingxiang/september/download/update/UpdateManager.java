package com.jingxiang.september.download.update;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;

import com.jingxiang.september.MApplication;
import com.jingxiang.september.network.parse.UpdateBean;
import com.jingxiang.september.util.DeviceInfoManager;
import com.jingxiang.september.util.LogUtil;
import com.jingxiang.september.util.NetUtil;
import com.jingxiang.september.util.ThreadPool;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wu on 2016/10/19.
 * 1.检查应用版本更新的工具类
 *  保存的路径 及 文件 是 .../Huasheng/Huasheng.apk
 */
public class UpdateManager {
    //下载状态
    public static final int DOWNLOAD_STATUS_UNSTART    = 1;//未开始
    public static final int DOWNLOAD_STATUS_UNFINISHED = 2;//未完成
    public static final int DOWNLOAD_STATUS_FINISHED   = 3;//已完成
    //下载保存文件路径
    public static final String DOWNLOAD_FILE_SAVE_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + File.separator + "Huasheng";
    //下载保存文件名
    public static final String DOWNLOAD_FILE_SAVE_NAME = "Huasheng.apk";

    /** Data */
    private static UpdateManager manager;
    private static UpdateBean mServerBean;//从服务获取的bean
    private UpdateBean mDBBean;    //从数据库中获取的bean
    private int mServerVersionCode = 0;
    private int mAppVersionCode = 0;

   /***************************/
    private UpdateManager(){}

    public static UpdateManager getInstance(){
        if(manager == null){
            manager = new UpdateManager();
        }
        return manager;
    }

    //检查是否需要更新
    public boolean checkUpdate(Context context){
        if(mServerBean != null){
            mServerVersionCode = Integer.parseInt(mServerBean.version_code);
            mAppVersionCode = DeviceInfoManager.getAppVersionCode(context);
            if(mServerVersionCode > mAppVersionCode&& mServerVersionCode > 0 && mAppVersionCode > 0){//需要下载
                mDBBean = MApplication.mCommonDao.selectUpdateBean(String.valueOf(mServerVersionCode));
                LogUtil.e("abcd","DBBean: mServerVersionCode:" + mServerVersionCode + " --> " + mDBBean );
                if(mDBBean != null && !TextUtils.isEmpty(mDBBean.version_code)){ //重新下载
                    int tempDBCode = Integer.parseInt(mDBBean.version_code);
                    if( tempDBCode != mServerVersionCode){//之前下载的版本与当前服务器上最新版本不一致,删除之前下载的版本
                         doDeleteLocalFile();
                         doDeleteDBRecord();
                         mDBBean = null;
                         LogUtil.e("abcd"," delete and do to download ");
                        return true;
                     }else{
                         LogUtil.e("abcd","stop download  :" + (mDBBean.end == mDBBean.finished));
                         if(mDBBean.end == mDBBean.finished)
                             return false;//表示之前已经下载完了，不需要再次下载
                     }
                }
                return true;
            }
            return false;
        }else
          return false;
    }

    //进行更新
    public void doUpdate(final Context context,final int versionCode){
        if(mDBBean == null || TextUtils.isEmpty(mDBBean.version_code)){
            ThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    long tempFileLength = doGetDownloadLength(mServerBean.download_link);
                    LogUtil.e("tempFileLength:" + tempFileLength);
                    mDBBean = new UpdateBean();
                    mDBBean.end = tempFileLength;
                    mDBBean.download_link = mServerBean.download_link;
                    mDBBean.version_code = mServerBean.version_code;
                    mDBBean.intro = mServerBean.intro;
                    boolean isInserted = doSaveDBRecord(mDBBean);

                    if(isInserted && NetUtil.isNetworkConnected(context) && NetUtil.isNetWorkWifi(context)){//有网 有无线网时进行下载
                        LogUtil.e("network is wifi ,and to prepare download");
                        if(mDBBean == null)//如果版本不一致,之前取得的数据就是无效数据,需要重新获取新的数据
                            mDBBean = MApplication.mCommonDao.selectUpdateBean(String.valueOf(versionCode));
                        LogUtil.e("DBBean: there need check weather the end is below or equal 0: " + mDBBean + "\n"
                                + "  versionCode:" + versionCode);
                        Intent intentStartDownload = new Intent(context,UpdateService.class);
                        intentStartDownload.putExtra("BEAN",mDBBean);
                        context.startService(intentStartDownload);
                    }
                }
            });
        }else if(mDBBean != null && !TextUtils.isEmpty(mDBBean.download_link)){
            ThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Intent intentStartDownload = new Intent(context,UpdateService.class);
                    intentStartDownload.putExtra("BEAN",mDBBean);
                    context.startService(intentStartDownload);
                }
            });
        }
    }

    //停止更新
    public void doStopUpdate(Context context){
        Intent intentStopDownload = new Intent(context,UpdateService.class);
        context.stopService(intentStopDownload);
    }

    //删除本地文件
    private void doDeleteLocalFile(){
        File tempFile = new File(DOWNLOAD_FILE_SAVE_PATH + File.separator + DOWNLOAD_FILE_SAVE_NAME);
        if(tempFile.exists()){
            try {
                tempFile.delete();
            }catch (Exception e){}
        }
    }

    //删除本地的文件
    private void doDeleteDBRecord(){
        MApplication.mCommonDao.deleteAllUpdateBean();
    }

    //获取下载文件的长度
    private long doGetDownloadLength(String urlPath){
        if(TextUtils.isEmpty(urlPath)) return 0;
        long tempFileLength = 0;
        try{
            URL url = new URL(urlPath);
            HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
            tempFileLength = urlcon.getContentLength();
        }catch (Exception e){}
        return tempFileLength;
    }

    //保存下载文件的记录
    private void doSaveDBRecord(String url,long fileLength,String versionCode){
        final UpdateBean bean = new UpdateBean();
        bean.end = fileLength;
        bean.download_link = url;
        bean.version_code = versionCode;
        LogUtil.e("doSaveDBRecord:" + bean);
        boolean isInsert =  MApplication.mCommonDao.insertUpdateBean(bean);
        LogUtil.e("isInsert:" + isInsert);
    }

    private boolean doSaveDBRecord(UpdateBean bean){
        boolean isInsert =  MApplication.mCommonDao.insertUpdateBean(bean);
        return isInsert;
    }

    public void setServerBean(UpdateBean mServerBean) {
        this.mServerBean = mServerBean;
    }
}
