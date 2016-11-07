package com.jingxiang.september.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jingxiang.september.MApplication;
import com.jingxiang.september.R;
import com.jingxiang.september.stats.EventManager;
import com.jingxiang.september.ui.widget.CommonDialog;
import com.jingxiang.september.util.LogUtil;

/**
 * Created by wu on 2016/9/12.
 * 在创建Activity时注意一下几点
 * 1.判断是否activity的启动模式,是否需要重写 newIntent()方法
 * 2.在activity中进行的网络访问,尽量在activity销毁的时候取消网络请求
 * 3.在启动activity的时候尽量将activity生命周期的方法放置在一块，便于查找和管理。在生命周期中的方法中尽量减少耗时的操作(网络请求一般做延迟发送)
 * 4.如果在activity中存在fragment,判断是否需要将OnActivityResult方法传递到fragment中
 */
public class BaseFragmentActivity extends FragmentActivity {
    /** View */
    private Dialog mProgressDialog;

    /** Data */
    private Context mContext = this;
    protected Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MApplication)getApplication()).addActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventManager.getInstance().startEvent();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventManager.getInstance().stopEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MApplication)getApplication()).removeActivity(this);
        MApplication.getRefWatcher(this).watch(this);
    }

    /** 1.0版本提供的基本方法 */
    public <T extends View>T $(int id){
       return (T)super.findViewById(id);
    }

    /** 1.0版本提供的基本方法*/
    public <T extends View>T $(View t,int id){
        return (T)t.findViewById(id);
    }

    /** 提供的弹框 */
    public void showProgress(int resId, boolean cancelAble) {
        showProgress(getString(resId), cancelAble, null);
    }

    public void showProgress(String message, boolean cancelAble) {
        showProgress(message, cancelAble, null);
    }

    public void showProgress(int resId, boolean cancelAble, DialogInterface.OnKeyListener listener) {
        showProgress(getString(resId), cancelAble, listener);
    }

    public void showProgress(String message, boolean cancelAble, DialogInterface.OnKeyListener listener) {
        if(mContext == null) return;
        if (mProgressDialog == null) {
            mProgressDialog = new CommonDialog(mContext);
            View view = LayoutInflater.from(mContext).inflate(R.layout.view_dialog_progress, null);
            if (!TextUtils.isEmpty(message)) {
                ((TextView)$(view,R.id.progress_text)).setText(message);
            }
            mProgressDialog.setContentView(view);
            mProgressDialog.setCancelable(cancelAble);
            mProgressDialog.setOnKeyListener(listener);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.findViewById(R.id.root_view).setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_dialog_custom_bg));
            try{
                mProgressDialog.show();
            }catch (Exception e){//做异常处理,防止出现极端情况,在显示的时候就退出activity导致dailog所依附的window为空
                LogUtil.e("e.msg;" + e.getMessage());
            }
        }
    }

    public void dismissProgress() {
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            try{
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }catch (Exception e){//做一场处理,防止极端情况,调用显示之后立马退出.
                LogUtil.e("e.msg;" + e.getMessage());
            }
        }
    }
}
