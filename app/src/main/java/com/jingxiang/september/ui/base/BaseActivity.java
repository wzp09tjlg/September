package com.jingxiang.september.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.jingxiang.september.MApplication;

/**
 * Created by wu on 2016/9/12.
 */
public class BaseActivity extends FragmentActivity {

    /** Data */
    private Context mContext = this;
    protected Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MApplication)getApplication()).addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MApplication)getApplication()).removeActivity(this);
    }

    /** 1.0版本提供的基本方法 */
    public <T extends View>T $(int id){
       return (T)super.findViewById(id);
    }

    /** 1.0版本提供的基本方法*/
    public <T extends View>T $(View t,int id){
        return (T)t.findViewById(id);
    }
}
