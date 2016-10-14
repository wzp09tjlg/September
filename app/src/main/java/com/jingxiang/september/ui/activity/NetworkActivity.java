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
import com.jingxiang.september.network.parse.EntertainmentVideoList;
import com.jingxiang.september.network.parse.IDCardBean;
import com.jingxiang.september.network.parse.PhoneNumBean;
import com.jingxiang.september.network.request.BaseRequest;
import com.jingxiang.september.ui.base.BaseFragmentActivity;
import com.jingxiang.september.util.FinalUtil;
import com.jingxiang.september.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wu on 2016/9/19.
 */
public class NetworkActivity extends BaseFragmentActivity implements
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
                //1.使用get方法获取手机号码的归属地信息
                doQueryNumLocalInfo();
                //2.使用get方法获取娱乐信息
                //doGetEntertainmentVideoListInfo();
                //3.使用get方法获取身份证信息
                //doGetIDCardInfo();
                break;
            case R.id.btn_post:
                //使用post的方法获取手机号码的归属地信息
                doPostQueryNumLocalInfo();
                //使用post的方法获取娱乐信息
                //doPostEntertainmentVideoListInfo();
                break;
        }
    }

    // get方式请求电话号码归属地
    private void doQueryNumLocalInfo(){
        manager = new NetworkManager<>(PhoneNumBean.class
                ,getListener());
        manager.setmRequestPage(1);
        Map<String,String> param = new HashMap<>();
        param.put("showapi_appid",FinalUtil.SHOW_API_APP_KEY);
        param.put("num","15210521539");
        manager.sendGetRequest(FinalUtil.SHOW_API_QUERY_PHONE,TAG,param);
    }

    // post方式请求电话号码归属地
    private void doPostQueryNumLocalInfo(){
        manager = new NetworkManager<>(PhoneNumBean.class
                ,getListener());
        manager.setmRequestPage(1);
        Map<String,String> param = new HashMap<>();
        param.put("showapi_appid",FinalUtil.SHOW_API_APP_KEY);
        param.put("num","15210521539");
        manager.sendPostRequest(FinalUtil.SHOW_API_QUERY_PHONE,TAG,param);
    }

    //返回单个的数据的listener
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

    //使用get方式获取娱乐资讯
    private void doGetEntertainmentVideoListInfo(){
        NetworkManager<EntertainmentVideoList> manager = new NetworkManager<>(EntertainmentVideoList.class
                ,getVideoListener());
        manager.setmRequestPage(1);
        Map<String,String> param = new HashMap<>();
        param.put("showapi_appid",FinalUtil.SHOW_API_APP_KEY);
        manager.sendGetRequest(FinalUtil.SHOW_API_VIDEO_LIST,TAG,param);
    }

    //使用post方式获取娱乐资讯
    private void doPostEntertainmentVideoListInfo(){
        NetworkManager<EntertainmentVideoList> manager = new NetworkManager<>(EntertainmentVideoList.class
                ,getVideoListener());
        manager.setmRequestPage(1);
        Map<String,String> param = new HashMap<>();
        param.put("showapi_appid",FinalUtil.SHOW_API_APP_KEY);
        manager.sendPostRequest(FinalUtil.SHOW_API_VIDEO_LIST,TAG,param);
    }

    //娱乐资讯的回调
    private BaseRequest.Listener<EntertainmentVideoList> getVideoListener(){
        BaseRequest.Listener<EntertainmentVideoList> listListener = new BaseRequest.Listener<EntertainmentVideoList>() {
            @Override
            public void onFailure(String errMsg, BaseRequest request) {
              LogUtil.e("onFailure: errMsg:" + errMsg);
            }

            @Override
            public void onSuccess(EntertainmentVideoList entertainmentVideoList, BaseRequest request) {
                LogUtil.e("onSuccess: entertainmentVideoList:" + entertainmentVideoList);
            }
        };
        return listListener;
    }

    private void doGetIDCardInfo(){
        NetworkManager<IDCardBean> manager = new NetworkManager<>(IDCardBean.class
                ,getIDCardListener());
        manager.setmRequestPage(1);
        Map<String,String> param = new HashMap<>();
        param.put("showapi_appid",FinalUtil.SHOW_API_APP_KEY);
        param.put("id","620403198402262859");//"433130198904172915");
        manager.sendGetRequest(FinalUtil.SHOW_API_IDCARD_INFO,TAG,param);
    }

    private BaseRequest.Listener<IDCardBean> getIDCardListener(){
        BaseRequest.Listener<IDCardBean> listener = new BaseRequest.Listener<IDCardBean>() {
            @Override
            public void onFailure(String errMsg, BaseRequest request) {
               LogUtil.e("onFailure:  errMsg:" + errMsg);
            }

            @Override
            public void onSuccess(IDCardBean idCardBean, BaseRequest request) {
                LogUtil.e("onSuccess:  " + idCardBean.toString());
            }
        };
        return listener;
    }
}
