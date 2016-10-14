package com.jingxiang.september.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jingxiang.september.R;
import com.jingxiang.september.ui.base.BaseFragmentActivity;
import com.jingxiang.september.ui.widget.LoadListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by wu on 2016/10/8.
 */
public class LoadActivity extends BaseFragmentActivity implements
   View.OnClickListener
{
    /** View */
    private View viewTitle;
    private ImageView imgTitleBack;
    private TextView textTitle;
    private ImageView imgTitleMenu;

    private LoadListView loadListView;
    /** Data */
    private Context mContext;
    private String mTitle;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> mData;
    private int count = 0;

    /**************************************************/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtral(getIntent());
        setContentView(R.layout.activity_load);
        initViews();
    }

    private void getExtral(Intent intent){
        Bundle bundle = intent.getExtras();

        mTitle = bundle.getString("TITLE");
    }

    private  void initViews(){
        initTitle();

        loadListView = $(R.id.loadlist);

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
        mData = new ArrayList<>();
        String tempString = "fdshafdhsjkaefds;afjdska;fanv;adfdshjalfdhsjkalfhdjskaewadjacnjsap";
        for(int i=0;i<15;i++){
            mData.add(
            tempString.substring(new Random().nextInt(5) + 1
                    , new Random().nextInt(10) + 6));
        }
        arrayAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,mData);

        loadListView.setAdapter(arrayAdapter);
        loadListView.setLoadLister(getLoadListener());
    }

    private LoadListView.OnLoadLister getLoadListener(){
        LoadListView.OnLoadLister lister = new LoadListView.OnLoadLister() {
            @Override
            public void onLoadMore() {
                count += 1;
                if(count > 11) return;

                String tempString = "fdshafdhsjkaefds;afjdska;fanv;adfdshjalfdhsjkalfhdjskaewadjacnjsap";
                for(int i=0;i<5;i++){
                    mData.add( " add ---- " +
                            tempString.substring(new Random().nextInt(5) + 1
                                    , new Random().nextInt(10) + 6));
                }

                if(count > 10) {
                    loadListView.setPullMore(false);
                }else{
                    loadListView.setPullMore(true);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        };
        return lister;
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
