package com.jingxiang.september.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jingxiang.september.R;
import com.jingxiang.september.network.NetworkManager;
import com.jingxiang.september.network.parse.SportsNewsBeanList;
import com.jingxiang.september.network.request.BaseRequest;
import com.jingxiang.september.ui.adapter.SportsNewsAdapter;
import com.jingxiang.september.ui.base.BaseFragmentActivity;
import com.jingxiang.september.ui.widget.GlobalToast.GloableToast;
import com.jingxiang.september.ui.widget.LoadListView;
import com.jingxiang.september.util.FinalUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wu on 2016/10/17.
 */
public class AuthorActivity extends BaseFragmentActivity implements
   View.OnClickListener
{
    private static final String TAG = AuthorActivity.class.getSimpleName();
    /** View */
    private View viewTitle;
    private ImageView imgTitleBack;
    private ImageView imgTitleMenu;
    private TextView  textTitle;

    private SwipeRefreshLayout mRefresh;
    private View mLayoutHead;
    private LoadListView mLoadList;
    /** Data */
    private Context mContext;
    private String  mTitle;

    private int mRequestIndex = 1;
    private int mCurIndex = 1;

    private List<SportsNewsBeanList.SportsNewsBean> mList;
    private SportsNewsAdapter sportsNewsAdapter;

    /*****************************************************/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);
        getExtra(getIntent());
        initViews();
    }

    private void getExtra(Intent intent){
        Bundle bundle = intent.getExtras();

        mTitle = bundle.getString("TITLE");
    }

    private void initViews(){
        initTitle();

        mRefresh = $(R.id.refresh);
        mRefresh.setOnRefreshListener(getRefreshListener());
        mLayoutHead = LayoutInflater.from(this).inflate(R.layout.view_dynamic_layout,null);
        mLoadList = $(R.id.list);
        mLoadList.addHeaderView(mLayoutHead);
        mLoadList.setLoadLister(getLoadListener());

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
        mList = new ArrayList<>();
        sportsNewsAdapter = new SportsNewsAdapter(mContext,mList);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                requestSportsNewsData(mCurIndex);
            }
        });
        mLoadList.setAdapter(sportsNewsAdapter);
    }

    private void requestSportsNewsData(int requestPage){
        if(requestPage < 0) return;
        mRequestIndex = requestPage;
        NetworkManager<SportsNewsBeanList> manager = new NetworkManager<>(SportsNewsBeanList.class
                ,getDataListener());
        manager.setmRequestPage(mRequestIndex); //必须设置请求的page
        Map<String,String> params = new HashMap<>();
        params.put("showapi_appid", FinalUtil.SHOW_API_APP_KEY);
        params.put("num","10");
        params.put("page",String.valueOf(mRequestIndex));
        manager.sendGetRequest(FinalUtil.SHOW_API_SPORTS_NEWS,TAG,params);
    }

    private BaseRequest.Listener<SportsNewsBeanList> getDataListener(){
        BaseRequest.Listener<SportsNewsBeanList> listener = new BaseRequest.Listener<SportsNewsBeanList>() {
            @Override
            public void onFailure(String errMsg, BaseRequest request) {
                GloableToast.show("onFailure:" + errMsg);
            }

            @Override
            public void onSuccess(SportsNewsBeanList sportsNewsBeanList, BaseRequest request) {
                mRefresh.setRefreshing(false);
                if(request.getmRequestPage() != mRequestIndex) return;
                mCurIndex = mRequestIndex;
                if(sportsNewsBeanList != null
                        && sportsNewsBeanList.newslist != null
                        && sportsNewsBeanList.newslist.size() >0)
                sportsNewsAdapter.addData(sportsNewsBeanList.newslist);
            }
        };
        return listener;
    }

    private LoadListView.OnLoadLister getLoadListener(){
        LoadListView.OnLoadLister listener = new LoadListView.OnLoadLister() {
            @Override
            public void onLoadMore() {
              requestSportsNewsData(mCurIndex + 1);
            }
        };
        return listener;
    }

    private SwipeRefreshLayout.OnRefreshListener getRefreshListener(){
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sportsNewsAdapter.clear();
                mCurIndex = 1;
                requestSportsNewsData(mCurIndex);
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
