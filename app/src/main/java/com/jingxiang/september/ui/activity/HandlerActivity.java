package com.jingxiang.september.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jingxiang.september.R;
import com.jingxiang.september.ui.base.BaseActivity;
import com.jingxiang.september.util.LogUtil;

/**
 * Created by wu on 2016/9/20.
 */
public class HandlerActivity extends BaseActivity implements
        View.OnClickListener
{
    private final int MSG_WHAT1 = 0X1001;
    private final int MSG_WHAT2 = 0X1002;
    /** View */
    private View viewTitle;
    private ImageView imgTitleBack;
    private TextView textTitle;
    private ImageView imgTitleMenu;

    private Button btnCreate;
    private Button btnTest;
    /** Data */
    private Context mContext;
    private String mTitle;

    //ActivityThread thread;

    private MyThread myThread = new MyThread();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LogUtil.e("mHandler thred id:" + Thread.currentThread().getId());
            switch (msg.what){
                case MSG_WHAT1:
                    LogUtil.e("mainThread msg.what:" + MSG_WHAT1);
                    break;
                case MSG_WHAT2:
                    LogUtil.e("mainThread msg.what:" + MSG_WHAT2);
                    break;
            }
        }
    };

    /*********************************************/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
        getExtral(getIntent());
        initViews();
    }

    private void getExtral(Intent intent){
        Bundle bundle = intent.getExtras();

        mTitle = bundle.getString("TITLE");
    }

    private void initViews(){
        initTitle();

        btnCreate = $(R.id.btn_create);
        btnTest = $(R.id.btn_test);

        initData();
    }

    private void initTitle(){
        viewTitle = $(R.id.layout_title);
        imgTitleBack = $(R.id.title_left_img);
        textTitle = $(R.id.title_center_text);
        imgTitleMenu = $(R.id.title_right_img);

        imgTitleMenu.setVisibility(View.INVISIBLE);
        textTitle.setText(mTitle);
    }

    private void initData(){
        btnCreate.setOnClickListener(this);
        btnTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_create:
                postOnNoUIThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            LogUtil.e("thread start 1");
                            Thread.sleep(2000);
                            LogUtil.e("thread end 2");
                        }catch (Exception e){}
                    }
                });

                postOnNoUIThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            LogUtil.e("thread start 3");
                            Thread.sleep(3000);
                            LogUtil.e("thread end 4");
                        }catch (Exception e){}
                    }
                });
                break;
            case R.id.btn_test:
                try{
                    //postOnUIThread(new MyTestRunnable()); //在主线程中的handler处理UI 的更新,正常的漏记处理。
                     postOnNoUIThread(new MyTestRunnable());//在子线程中的handler处理ui的更新,会报错.
                }catch (Exception e){
                    LogUtil.e("e:msg:" + e.getMessage());
                }
                break;
        }
    }

    private void postOnUIThread(Runnable runnable){
        mHandler.post(runnable);
        mHandler.sendEmptyMessage(MSG_WHAT1);
        mHandler.sendEmptyMessage(MSG_WHAT2);
    }

    private void postOnNoUIThread(Runnable runnable){
        while (myThread.mHandler == null){}//如果子线程的handler 为空，等待创建完成之后再执行下边的代码
        myThread.mHandler.post(runnable);
        myThread.mHandler.sendEmptyMessage(MSG_WHAT1);
        myThread.mHandler.sendEmptyMessage(MSG_WHAT2);
    }

    // 创建子线程,及在子线程中定义一个handler.子线程中创建的handler 指向的是子线程中的消息队列.
    class MyThread extends Thread{
        {
            start();//线程创建就启动
        }

        Handler mHandler ;

        public Handler getmHandler() {
            return mHandler;
        }

        @Override
        public void run() {
           //while (true){
               Looper.prepare();//准备

               mHandler = new Handler(Looper.myLooper())//Looper.getMainLooper())//Looper.myLooper())//获取的是Looper自己的looper 不是系统的looper
               {
                   @Override
                   public void handleMessage(Message msg) {
                       super.handleMessage(msg);
                       LogUtil.e("myThread.mHandler  thred id:" + Thread.currentThread().getId());
                       switch (msg.what){
                           case 0X1001:
                               LogUtil.e("0X1001");
                               break;
                           case 0X1002:
                               LogUtil.e("0X1002");
                               break;
                       }
                   }
               };
               Looper.loop();//死循环操作
           }
        //}
    }

    class MyTestRunnable implements Runnable{
        @Override
        public void run() {
            btnTest.setText("testtest");
        }
    }
}
