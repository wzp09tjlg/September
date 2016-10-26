package com.jingxiang.september.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jingxiang.september.R;
import com.jingxiang.september.ui.base.BaseFragmentActivity;
import com.jingxiang.september.ui.widget.ClipImageView;

/**
 * Created by wu on 2016/10/22.
 */
public class EmptyActivity extends BaseFragmentActivity {
    /** View */
   private ClipImageView clipImg;
    /** Data */
    /****************************************************/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        initViews();
    }

    private void initViews(){
        clipImg = $(R.id.clip_img);

        initData();
    }

    private void initData(){
        clipImg.setImageResource(R.drawable.icon_nice_girl_eat);
    }
}
