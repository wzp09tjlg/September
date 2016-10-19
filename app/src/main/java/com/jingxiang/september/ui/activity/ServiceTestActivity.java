package com.jingxiang.september.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jingxiang.september.R;
import com.jingxiang.september.ui.base.BaseFragmentActivity;
import com.jingxiang.september.ui.service.PlayService;

/**
 * Created by wu on 2016/10/17.
 */
public class ServiceTestActivity extends BaseFragmentActivity implements
        View.OnClickListener
{
    /** View */
    private View viewTitle;
    private ImageView imgTitleBack;
    private ImageView imgTitleMenu;
    private TextView textTitle;

    private Button btnServiceStart;
    private Button btnServiceStop;
    private Button btnServiceBind;
    private Button btnServiceUnbind;

    /** Data */
    private Context mContext;
    private String mTitle;

    /*******************************************************/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        getExtra(getIntent());
        initViews();
    }

    private void getExtra(Intent intent){
        Bundle bundle = intent.getExtras();

        mTitle = bundle.getString("TITLE");
    }

    private void initViews(){
        initTitle();

        btnServiceStart = $(R.id.btn_start);
        btnServiceStop = $(R.id.btn_stop);
        btnServiceBind = $(R.id.btn_bind);
        btnServiceUnbind = $(R.id.btn_unbind);

        initData();
    }

    private void initTitle(){
        viewTitle = $(R.id.layout_title);
        imgTitleBack = $(R.id.title_left_img);
        imgTitleMenu = $(R.id.title_right_img);
        textTitle = $(R.id.title_center_text);

        textTitle.setText(mTitle);
    }

    private void initData(){
        mContext = this;

        initListener();
    }

    private void initListener(){
        imgTitleBack.setOnClickListener(this);

        btnServiceStart.setOnClickListener(this);
        btnServiceStop.setOnClickListener(this);
        btnServiceBind.setOnClickListener(this);
        btnServiceUnbind.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left_img:
                finish();
                break;
            case R.id.btn_start:
                doStartService();
                break;
            case R.id.btn_stop:
                doStopService();
                break;
            case R.id.btn_bind:
                break;
            case R.id.btn_unbind:
                break;
        }
    }

    private void doStartService(){
        Intent intentService = new Intent(ServiceTestActivity.this, PlayService.class);
        startService(intentService);
    }

    private void doStopService(){
        Intent intentService = new Intent(ServiceTestActivity.this, PlayService.class);
        stopService(intentService);
    }

    private void doBindService(){}

    private void doUnbindService(){}
}
