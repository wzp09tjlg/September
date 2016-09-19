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
import com.jingxiang.september.network.NetworkManager;
import com.jingxiang.september.network.parse.PhoneNumBean;
import com.jingxiang.september.network.request.BaseRequest;
import com.jingxiang.september.ui.base.BaseActivity;
import com.jingxiang.september.util.FinalUtil;
import com.jingxiang.september.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wu on 2016/9/19.
 */
public class NetworkActivity extends BaseActivity implements
  View.OnClickListener
{
   private final String TAG = NetworkActivity.class.getSimpleName();
    /** View */
    private View viewTitle;
    private ImageView imgTitlBack;
    private TextView textTitle;
    private ImageView imgTitleMenu;

    private Button btnGet;
    private Button btnPost;

    /** Data */
    private Context mContext;
    private String mTitle;
    private NetworkManager<PhoneNumBean> manager = null;

    /*************************************************/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        getExtral(getIntent());
        initViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(manager != null)
            manager.cancelRequest(TAG);
    }

    private void getExtral(Intent intent){
        Bundle bundle = intent.getExtras();

        mTitle = bundle.getString("TITLE");
    }

    private void initViews(){
        initTitle();

        btnGet = $(R.id.btn_get);
        btnPost = $(R.id.btn_post);

        initData();
    }

    private void initTitle(){
        viewTitle = $(R.id.layout_title);
        imgTitlBack = $(R.id.title_left_img);
        textTitle = $(R.id.title_center_text);
        imgTitleMenu = $(R.id.title_right_img);

        imgTitleMenu.setVisibility(View.INVISIBLE);
        textTitle.setText(mTitle);
    }

    private void initData(){
         mContext = this;

         initListener();
    }

    private void initListener(){
        btnGet.setOnClickListener(this);
        btnPost.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_get:
                doQueryNumLocalInfo();
                break;
            case R.id.btn_post:
                doPostQueryNumLocalInfo();
                break;
        }
    }

    private void doQueryNumLocalInfo(){
        manager = new NetworkManager<>(PhoneNumBean.class
                ,getListener());
        manager.setmRequestPage(1);
        Map<String,String> param = new HashMap<>();
        param.put("showapi_appid",FinalUtil.SHOW_API_APP_KEY);
        param.put("num","15210521539");
        manager.sendGetRequest(FinalUtil.PATH_QUERY_PHONE,TAG,param);
    }

    private void doPostQueryNumLocalInfo(){
        manager = new NetworkManager<>(PhoneNumBean.class
                ,getListener());
        manager.setmRequestPage(1);
        Map<String,String> param = new HashMap<>();
        param.put("showapi_appid",FinalUtil.SHOW_API_APP_KEY);
        param.put("num","15210521539");
        manager.sendPostRequest(FinalUtil.PATH_QUERY_PHONE,TAG,param);
    }

    private BaseRequest.Listener<PhoneNumBean> getListener(){
        BaseRequest.Listener<PhoneNumBean> listener = new BaseRequest.Listener<PhoneNumBean>() {
            @Override
            public void onFailure(String errMsg, BaseRequest request) {
                LogUtil.e("onFailure: errMsg:" + errMsg);
            }

            @Override
            public void onSuccess(PhoneNumBean phoneNumBean, BaseRequest request) {
               LogUtil.e("onSuccess: phoneNumBean:" + phoneNumBean);
            }
        };
        return listener;
    }
}
