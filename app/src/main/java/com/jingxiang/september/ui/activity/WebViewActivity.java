package com.jingxiang.september.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jingxiang.september.R;
import com.jingxiang.september.ui.base.BaseFragmentActivity;

/**
 * Created by wu on 2016/10/13.
 */
public class WebViewActivity extends BaseFragmentActivity implements
    View.OnClickListener
{
    /** View */
    private View viewTitle;
    private ImageView imgTitleBack;
    private TextView textTitle;
    private ImageView imgTitleMenu;

    private SwipeRefreshLayout refreshLayout;
    private WebView webView;

    /** Data */
    private Context mContext;
    private String mTitle;
    private String mUrl;

    /***********************************************/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        getExtra(getIntent());
        initViews();
    }

    private void getExtra(Intent intent){
        Bundle bundle = intent.getExtras();
        mTitle = bundle.getString("TITLE");
        mUrl = bundle.getString("URL");
    }

    private void initViews(){
        initTitle();

        refreshLayout = $(R.id.refresh);
        webView = $(R.id.webview);

        initData();
    }

    private void initTitle(){
        viewTitle = $(R.id.layout_title);
        imgTitleBack = $(R.id.title_left_img);
        imgTitleMenu = $(R.id.title_right_img);
        textTitle = $(R.id.title_center_text);

        textTitle.setText(mTitle);
        imgTitleBack.setOnClickListener(this);
    }

    private void initData(){
        mContext = this;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
               webView.loadUrl(mUrl);
            }
        });
        refreshLayout.setOnRefreshListener(getRefresListener());
    }

    private SwipeRefreshLayout.OnRefreshListener getRefresListener(){
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
              webView.reload();
            }
        };
        return listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left_img:
                finish();
                break;
        }
    }
}
