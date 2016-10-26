package com.jingxiang.september.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jingxiang.september.R;
import com.jingxiang.september.ui.base.BaseFragmentActivity;
import com.jingxiang.september.util.LogUtil;

/**
 * Created by wu on 2016/10/13.
 */
public class WebViewActivity extends BaseFragmentActivity implements
    View.OnClickListener
{
    /** View */
    private ViewGroup viewRoot;

    private View viewTitle;
    private ImageView imgTitleBack;
    private TextView textTitle;
    private ImageView imgTitleMenu;

    private SwipeRefreshLayout refreshLayout;
    private WebView mWebView;

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
        LogUtil.e("onCreate " + mTitle);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getExtra(intent);
        initData();
        LogUtil.e("onNewIntent " + mTitle);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.e("onStart " + mTitle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e("onResume " + mTitle);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.e("onPause " + mTitle);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.e("onStop " + mTitle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("onDestroy " + mTitle);
        if (mWebView != null) {//activity的启动模式 是singleTask singleTop(此时activity在top上，如果不在top上
            // ，就不会走newIntent的方法)时，重启启动activity会调用newIent
            viewRoot.removeView(mWebView);
            mWebView.removeAllViews();
            mWebView.destroy();
        }
    }

    private void getExtra(Intent intent){
        Bundle bundle = intent.getExtras();
        mTitle = bundle.getString("TITLE");
        mUrl = bundle.getString("URL");
    }

    private void initViews(){
        initTitle();

        viewRoot = $(R.id.viewRoot);
        refreshLayout = $(R.id.refresh);
        mWebView = $(R.id.webview);

        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= 21) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //        mWebView.setBackgroundColor(0);
        //        if (mWebView.getBackground() != null)
        //            mWebView.getBackground().setAlpha(0);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }

        initData();
    }

    private void initTitle(){
        viewTitle = $(R.id.layout_title);
        imgTitleBack = $(R.id.title_left_img);
        imgTitleMenu = $(R.id.title_right_img);
        textTitle = $(R.id.title_center_text);

        textTitle.setText(mTitle);
        imgTitleBack.setOnClickListener(this);
        imgTitleMenu.setOnClickListener(this);
        imgTitleMenu.setVisibility(View.VISIBLE);
    }

    private void initData(){
        mContext = this;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl(mUrl);
            }
        });
        refreshLayout.setOnRefreshListener(getRefresListener());
    }

    private SwipeRefreshLayout.OnRefreshListener getRefresListener(){
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.reload();
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
            case R.id.title_right_img:
                //重启
                Bundle bundleReStart = new Bundle();
                bundleReStart.putString("TITLE","RESTART");
                bundleReStart.putString("URL","http://www.baidu.com/baidu?tn=monline_3_dg&ie=utf-8&wd=%E5%AE%89%E5%8D%93%E4%B8%ADonNewIntent+%E6%98%AF%E5%9C%A8%E4%BB%80%E4%B9%88%E6%83%85%E5%86%B5%E4%B8%8B%E8%BF%9B%E8%A1%8C%EF%BC%8C%E8%B5%B0%E5%93%AA%E4%BA%9B%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F%E6%96%B9%E6%B3%95");
                Intent intentReStartWebView = new Intent(WebViewActivity.this,TestActivity.class);
                intentReStartWebView.putExtras(bundleReStart);
                startActivity(intentReStartWebView);
                break;
        }
    }
}
