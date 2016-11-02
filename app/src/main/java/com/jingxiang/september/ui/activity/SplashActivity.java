package com.jingxiang.september.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.jingxiang.september.MApplication;
import com.jingxiang.september.R;
import com.jingxiang.september.network.NetworkManager;
import com.jingxiang.september.network.parse.UpdateBean;
import com.jingxiang.september.network.request.BaseRequest;
import com.jingxiang.september.ui.base.BaseFragmentActivity;
import com.jingxiang.september.ui.widget.GlobalToast.GloableToast;
import com.jingxiang.september.util.FinalUtil;
import com.jingxiang.september.util.GlideUtil;
import com.jingxiang.september.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wu on 2016/10/18.
 */
public class SplashActivity extends BaseFragmentActivity {
    private final String TAG = SplashActivity.class.getSimpleName();
    /** View */
    private ImageView imgSplash;
    /** Data */
    private Context mContext;
    private int mClickCount = 0;

    private LoadingRunnable mLoadingRunnable;
    private UpdateBean mUpdateBean;
    private NetworkManager<UpdateBean> manager ;

    /***************************************************/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initViews();
    }

    private void initViews(){
        imgSplash = $(R.id.img_splash);
        mContext = this;
        GlideUtil.setImage(mContext,imgSplash
                ,R.drawable.icon_splash,R.drawable.icon_splash
                ,R.drawable.icon_splash,R.drawable.icon_splash);
        mLoadingRunnable = new LoadingRunnable();
        mHandler.postDelayed(mLoadingRunnable,300);
        doCheckUpdate();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mClickCount += 1;
        if(mClickCount >= 2){
          mHandler.removeCallbacksAndMessages(null);//移除所有的callBack 和 Message
          if(manager != null)
              manager.cancelRequest(TAG);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(manager != null){
            manager.cancelRequest(TAG);
            manager = null;
        }
    }

    class LoadingRunnable implements Runnable{
        @Override
        public void run() {
            Intent intentLoading = new Intent(SplashActivity.this,LoadingActivity.class);
            startActivity(intentLoading);
            finish();
        }
    }

    private void doCheckUpdate(){
        manager = new NetworkManager<>(UpdateBean.class,getDataListener());
        Map<String,String> params = new HashMap<>();
        params.put("app_version","10");
        params.put("app_channel","Huasheng");
        manager.sendGetRequest(FinalUtil.HOST_DOWNLOAD,TAG,params);
    }

    private BaseRequest.Listener getDataListener(){
        BaseRequest.Listener<UpdateBean> listener = new BaseRequest.Listener<UpdateBean>() {
            @Override
            public void onFailure(String errMsg, BaseRequest request) {
                GloableToast.show("get update info is failure:" + errMsg);
            }

            @Override
            public void onSuccess(UpdateBean bean, BaseRequest request) {
                if(bean != null){
                    MApplication.mUpdateManager.setServerBean(bean);
                    LogUtil.e("onSuccess: bean:" + bean);
                    if(MApplication.mUpdateManager.checkUpdate(mContext)) {
                        LogUtil.e("after check is will update");
                        MApplication.mUpdateManager.doUpdate(mContext,Integer.parseInt(bean.version_code));
                    }
                }
            }
        };
        return listener;
    }
}
