package com.jingxiang.september.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jingxiang.september.ui.base.BaseFragment;
import com.jingxiang.september.ui.fragment.FragmentTab1;
import com.jingxiang.september.ui.fragment.FragmentTab2;
import com.jingxiang.september.ui.fragment.FragmentTab3;
import com.jingxiang.september.ui.fragment.FragmentTab4;
import com.jingxiang.september.ui.fragment.FragmentTab5;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wu on 2016/9/12.
 */
public class MainTabAdapter extends FragmentPagerAdapter {
    public static final int TAB_COUNT = 5;
    private List<BaseFragment> listFragment;

    public MainTabAdapter(FragmentManager fm){
        super(fm);
        listFragment = new ArrayList<>();
        listFragment.add(new FragmentTab1());
        listFragment.add(new FragmentTab2());
        listFragment.add(new FragmentTab3());
        listFragment.add(new FragmentTab4());
        listFragment.add(new FragmentTab5());
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }
}
