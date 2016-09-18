package com.jingxiang.september.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jingxiang.september.R;
import com.jingxiang.september.ui.base.BaseFragment;
import com.jingxiang.september.util.GlideUtil;

/**
 * Created by wu on 2016/9/12.
 */
public class FragmentTab4 extends BaseFragment {
    /** View*/
    private ImageView imgIcon;

    /** Data*/

    /*****************************************/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab4,null);
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

        initData();
    }

    private void initTitle(View view){}

    private void initData(){
        GlideUtil.setImage(getContext(),imgIcon,R.drawable.icon_nice_girl_in_snow
                ,R.drawable.icon_loading,R.drawable.icon_loading_failure,R.drawable.icon_loading_no_wifi);
    }
}
