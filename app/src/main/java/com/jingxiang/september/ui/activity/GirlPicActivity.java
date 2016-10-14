package com.jingxiang.september.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jingxiang.september.R;
import com.jingxiang.september.network.NetworkManager;
import com.jingxiang.september.network.parse.GirlsBean;
import com.jingxiang.september.network.request.BaseRequest;
import com.jingxiang.september.ui.adapter.GirlsAdapter;
import com.jingxiang.september.ui.base.BaseFragmentActivity;
import com.jingxiang.september.ui.widget.GlobalToast.GloableToast;
import com.jingxiang.september.ui.widget.LoadListView;
import com.jingxiang.september.util.FinalUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wu on 2016/10/13.
 */
public class GirlPicActivity extends BaseFragmentActivity implements
   View.OnClickListener
{
    private final String TAG = GirlPicActivity.class.getSimpleName();
    /** View */
    private View viewTitle;
    private ImageView imgTitleBack;
    private TextView textTitle;
    private ImageView imgTitleMenu;

    private LoadListView listView;

    /** Data */
    private Context mContext;
    private String mTitle;
    private int mCurIndex = 1;
    private int mRequestIndex = 1;
    private List<GirlsBean.Girl> mList;

    private GirlsAdapter girlsAdapter;
    /****************************************************/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_girl_pic);
        getExtra(getIntent());
        initViews();
    }

    private void getExtra(Intent intent){
        Bundle bundle = intent.getExtras();
        mTitle = bundle.getString("TITLE");
    }

    private void initViews(){
        initTitle();

        listView = $(R.id.list);

        initData();
    }

    private void initTitle(){
        viewTitle = $(R.id.layout_title);
        imgTitleBack = $(R.id.title_left_img);
        textTitle = $(R.id.title_center_text);
        imgTitleMenu = $(R.id.title_right_img);

        textTitle.setText(mTitle);
        imgTitleBack.setOnClickListener(this);
    }

    private void initData(){
        mContext = this;
        mList = new ArrayList<>();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                doRequestGirlsData(mRequestIndex);
            }
        });
        girlsAdapter = new GirlsAdapter(mContext,mList);
        listView.setLoadLister(getLoadListener());
        listView.setAdapter(girlsAdapter);
        listView.setOnItemClickListener(getItemClickListener());
    }

    private void doRequestGirlsData(int requestIndex){
        if(requestIndex < 0) return;
        mRequestIndex = requestIndex;

        NetworkManager<GirlsBean> manager = new NetworkManager<>(GirlsBean.class,getDataListener());
        manager.setmRequestPage(requestIndex);
        //manager.cancelRequest(TAG);
        Map<String,String> params = new HashMap<>();
        params.put("showapi_appid",FinalUtil.SHOW_API_APP_KEY);
        params.put("num","10");
        params.put("page",String.valueOf(requestIndex));
        manager.sendGetRequest(FinalUtil.SHOW_API_GIRL_PIC,TAG,params);
    }

    private BaseRequest.Listener<GirlsBean> getDataListener(){
        final BaseRequest.Listener<GirlsBean> listener = new BaseRequest.Listener<GirlsBean>(){
            @Override
            public void onFailure(String errMsg, BaseRequest request) {
                GloableToast.show(errMsg);
            }

            @Override
            public void onSuccess(GirlsBean girlsBean, BaseRequest request) {
                if(request.getmRequestPage() != mRequestIndex) return;
                mCurIndex = mRequestIndex;
                if(girlsBean.newslist.size() == 10)
                    listView.setPullMore(true);
                else
                    listView.setPullMore(false);
                girlsAdapter.addData(girlsBean.newslist);
            }
        };
        return listener;
    }

    private LoadListView.OnLoadLister getLoadListener(){
        LoadListView.OnLoadLister lister = new LoadListView.OnLoadLister() {
            @Override
            public void onLoadMore() {
                doRequestGirlsData(mCurIndex + 1);
            }
        };
        return lister;
    }

    private AdapterView.OnItemClickListener getItemClickListener(){
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doOpenItemWebView(girlsAdapter.getItemPicUrl(position));
            }
        };
        return listener;
    }

    private void doOpenItemWebView(String url){
        if(TextUtils.isEmpty(url)) return;
        Bundle bundleWebView = new Bundle();
        bundleWebView.putString("TITLE","WEBVIEW");
        bundleWebView.putString("URL",url);
        Intent intentWebView = new Intent(GirlPicActivity.this,WebViewActivity.class);
        intentWebView.putExtras(bundleWebView);
        startActivity(intentWebView);
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
