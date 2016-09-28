package com.jingxiang.september.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jingxiang.september.MApplication;

/**
 * Created by wu on 2016/9/12.
 */
public class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MApplication.getRefWatcher(getActivity()).watch(this);
    }

    /** 1.0版本提供的基本方法*/
    public <T extends View>T $(View v,int id){
        return (T)v.findViewById(id);
    }
}
