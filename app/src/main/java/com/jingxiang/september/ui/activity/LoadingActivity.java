package com.jingxiang.september.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.jingxiang.september.R;
import com.jingxiang.september.ui.MainActivity;
import com.jingxiang.september.ui.base.BaseFragmentActivity;

/**
 * Created by wu on 2016/10/18.
 */
public class LoadingActivity extends BaseFragmentActivity {
    /** View */
    private ImageView imgAD;
    private ImageView imgLogo;

    /** Data */
    private Context mContext;
    /****************************************************/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        initViews();
    }

    private void initViews(){
        imgAD = $(R.id.img_other_AD);
        imgLogo = $(R.id.img_september_logo);
        initData();
    }

    private void initData(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intentMain = new Intent(LoadingActivity.this, MainActivity.class);
                startActivity(intentMain);
                finish();
            }
        },3000);
    }
}
