package com.jingxiang.september.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jingxiang.september.R;
import com.jingxiang.september.ui.adapter.MainTabAdapter;
import com.jingxiang.september.ui.base.BaseFragmentActivity;
import com.jingxiang.september.ui.base.BaseFragment;

public class MainActivity extends BaseFragmentActivity {

    /** View*/
    private FrameLayout layoutConain;
    private TabLayout tabs;

    /** Data*/
    private Context mContext;
    private MainTabAdapter mainTabAdapter;

    /****************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getExtra(getIntent());
        initViews();
    }

    private void getExtra(Intent intent){
        Bundle bundle = intent.getExtras();
    }

    private void initViews(){
        initTitle();

        mContext = this;
        layoutConain = $(R.id.contain);
        tabs = $(R.id.tabs);
        initTabs();

        initData();
    }

    private void initTitle(){}

    private void initTabs(){
        int tempCount = MainTabAdapter.TAB_COUNT;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        for(int i=0;i<tempCount;i++){
            View view = inflater.inflate(R.layout.view_tab_main,null);
            ImageView img = $(view,R.id.img_tab);
            TextView text = $(view,R.id.text_tab);
            ColorStateList csl = getResources().getColorStateList(R.color.color_tab_text_selector);
            text.setTextColor(csl);
            if(i == 0){
                img.setImageResource(R.drawable.icon_choice);
                text.setText(getResources().getString(R.string.main_tab1));
            }else if(i == 1){
                img.setImageResource(R.drawable.icon_favorite);
                text.setText(getResources().getString(R.string.main_tab2));
            }else if(i == 2){
                img.setImageResource(R.drawable.icon_huasheng);
                text.setText(getResources().getString(R.string.main_tab3));
            }else if(i == 3){
                img.setImageResource(R.drawable.icon_find);
                text.setText(getResources().getString(R.string.main_tab4));
            }else{
                img.setImageResource(R.drawable.icon_user);
                text.setText(getResources().getString(R.string.main_tab5));
            }

            TabLayout.Tab tab = tabs.newTab();
            tab.setCustomView(view);

            tabs.addTab(tab, i == 0 ? true : false);
        }
        tabs.setOnTabSelectedListener(getTabSelectedListener());
        tabs.setTabMode(TabLayout.MODE_FIXED);
        tabs.setBackgroundColor(getResources().getColor(R.color.color_title_bar));
        tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.color_title_bar));
    }

    private void initData(){
        mainTabAdapter = new MainTabAdapter(getSupportFragmentManager());
        setCurrentFragment(0);
    }

    private TabLayout.OnTabSelectedListener getTabSelectedListener(){
        TabLayout.OnTabSelectedListener listener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        };
        return listener;
    }

    private void setCurrentFragment(int position){
        BaseFragment fragment = (BaseFragment) mainTabAdapter.instantiateItem(layoutConain,position);
        mainTabAdapter.setPrimaryItem(layoutConain, position, fragment);
        mainTabAdapter.finishUpdate(layoutConain);
    }
}
