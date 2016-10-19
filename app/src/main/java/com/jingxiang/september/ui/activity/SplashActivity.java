package com.jingxiang.september.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.jingxiang.september.R;
import com.jingxiang.september.ui.base.BaseFragmentActivity;
import com.jingxiang.september.util.GlideUtil;

/**
 * Created by wu on 2016/10/18.
 */
public class SplashActivity extends BaseFragmentActivity {
    /** View */
    private ImageView imgSplash;
    /** Data */
    private Context mContext;
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
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Intent intentLoading = new Intent(SplashActivity.this,LoadingActivity.class);
                startActivity(intentLoading);
                finish();
            }
        });
    }
}
