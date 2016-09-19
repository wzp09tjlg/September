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
import com.jingxiang.september.ui.activity.NetworkActivity;
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_network:
                Bundle bundle = new Bundle();
                bundle.putString("TITLE","network");
                Intent intent = new Intent(getActivity(), NetworkActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}
