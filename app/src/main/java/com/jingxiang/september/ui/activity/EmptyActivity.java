package com.jingxiang.september.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jingxiang.september.R;
import com.jingxiang.september.ui.base.BaseFragmentActivity;

/**
 * Created by wu on 2016/10/22.
 */
public class EmptyActivity extends BaseFragmentActivity {
    /** View */

    /** Data */
    /****************************************************/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
    }

    private void initViews(){}

    private void initData(){}
}
