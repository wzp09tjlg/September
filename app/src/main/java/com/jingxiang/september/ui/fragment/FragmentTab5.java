package com.jingxiang.september.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.jingxiang.september.R;
import com.jingxiang.september.ui.activity.DBActivity;
import com.jingxiang.september.ui.activity.GirlPicActivity;
import com.jingxiang.september.ui.activity.HandlerActivity;
import com.jingxiang.september.ui.activity.LoadActivity;
import com.jingxiang.september.ui.activity.MapJsonActivity;
import com.jingxiang.september.ui.activity.NetworkActivity;
import com.jingxiang.september.ui.activity.TestActivity;
import com.jingxiang.september.ui.base.BaseFragment;
import com.jingxiang.september.util.GlideUtil;

/**
 * Created by wu on 2016/9/12.
 */
public class FragmentTab5 extends BaseFragment implements
        View.OnClickListener
{
    /** View*/
    private ImageView imgIcon;
    private Button btnNetwork;
    private Button btnHandler;
    private Button btnMapJson;
    private Button btnLoad;
    private Button btnDB;
    private Button btnGirls;
    private Button btnTest;

    /** Data*/

    /*****************************************/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab5,null);
        initViews(view);
        return view;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void initViews(View view){
        initTitle(view);

        imgIcon = $(view,R.id.img_girl);
        btnNetwork = $(view,R.id.btn_network);
        btnHandler = $(view,R.id.btn_handler);
        btnMapJson = $(view,R.id.btn_MapJson);
        btnLoad = $(view,R.id.btn_Load);
        btnDB = $(view,R.id.btn_DB);
        btnGirls = $(view,R.id.btn_Girls);
        btnTest = $(view,R.id.btn_Test);

        initData();
    }

    private void initTitle(View view){}

    private void initData(){
        GlideUtil.setImage(getContext(),imgIcon,R.drawable.icon_nice_girl_in_sorry
                ,R.drawable.icon_loading,R.drawable.icon_loading_failure,R.drawable.icon_loading_no_wifi);

        initListener();
    }

    private void initListener(){
        btnNetwork.setOnClickListener(this);
        btnHandler.setOnClickListener(this);
        btnMapJson.setOnClickListener(this);
        btnLoad.setOnClickListener(this);
        btnDB.setOnClickListener(this);
        btnGirls.setOnClickListener(this);
        btnTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_network:
                Bundle bundleNetwork = new Bundle();
                bundleNetwork.putString("TITLE","network");
                Intent intentNetwork = new Intent(getActivity(), NetworkActivity.class);
                intentNetwork.putExtras(bundleNetwork);
                startActivity(intentNetwork);
                break;
            case R.id.btn_handler:
                Bundle bundleHandler = new Bundle();
                bundleHandler.putString("TITLE","handler");
                Intent intentHandler = new Intent(getActivity(), HandlerActivity.class);
                intentHandler.putExtras(bundleHandler);
                startActivity(intentHandler);
                break;
            case R.id.btn_MapJson:
                Bundle bundleMapJson = new Bundle();
                bundleMapJson.putString("TITLE","MapJson");
                Intent intentMapJson = new Intent(getActivity(), MapJsonActivity.class);
                intentMapJson.putExtras(bundleMapJson);
                startActivity(intentMapJson);
                break;
            case R.id.btn_Load:
                Bundle bundleLoad = new Bundle();
                bundleLoad.putString("TITLE","Load");
                Intent intentLoad = new Intent(getActivity(), LoadActivity.class);
                intentLoad.putExtras(bundleLoad);
                startActivity(intentLoad);
                break;
            case R.id.btn_DB:
                Bundle bundleDB = new Bundle();
                bundleDB.putString("TITLE","DB");
                Intent intentDB = new Intent(getActivity(), DBActivity.class);
                intentDB.putExtras(bundleDB);
                startActivity(intentDB);
                break;
            case R.id.btn_Girls:
                Bundle bundleGirls = new Bundle();
                bundleGirls.putString("TITLE","Girls");
                Intent intentGirls = new Intent(getActivity(), GirlPicActivity.class);
                intentGirls.putExtras(bundleGirls);
                startActivity(intentGirls);
                break;
            case R.id.btn_Test:
                Bundle bundleTest = new Bundle();
                bundleTest.putString("TITLE","Test");
                Intent intentTest = new Intent(getActivity(), TestActivity.class);
                intentTest.putExtras(bundleTest);
                startActivity(intentTest);
                break;
        }
    }
}
