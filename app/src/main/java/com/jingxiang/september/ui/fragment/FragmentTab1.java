package com.jingxiang.september.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jingxiang.september.R;
import com.jingxiang.september.ui.base.BaseFragment;
import com.jingxiang.september.util.GlideUtil;
import com.jingxiang.september.util.LogUtil;

/**
 * Created by wu on 2016/9/12.
 * 1.检查下在fragment中创建使用Handler 是否会报错。在花生故事中存在这种错误 就是在fragment中创建handler ,奇怪
 */
public class FragmentTab1 extends BaseFragment implements
        View.OnClickListener
{
    private static final int TYPE_ICON = 0X1010;
    private static final int TYPE_TEXT = 0X1011;
    private static final int TYPE_MUSIC = 0X1012;
    private static final int TYPE_OTHER = 0X1013;
    /** View*/
    private View viewRoot;
    private RelativeLayout layoutRoot;

    private ImageView imgIcon;
    private Button mBtnHandler;

    private Window window;
    /** Data*/
    private Handler handler = new Handler(){ //
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TYPE_ICON:
                    imgIcon.setImageResource(R.drawable.icon_nice_girl_eat);
                    break;
                case TYPE_TEXT:
                    break;
                case TYPE_MUSIC:
                    break;
                case TYPE_OTHER:
                    break;
            }
        }
    };

    /*****************************************/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_tab1,null);
        initViews(viewRoot);
        return viewRoot;
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

        layoutRoot = $(view,R.id.layout_root);
        imgIcon = $(view,R.id.img_girl);
        mBtnHandler = $(view,R.id.btn_handler);

        initData();
    }

    private void initTitle(View view){}

    private void initData(){
        GlideUtil.setImage(getContext(),imgIcon,R.drawable.icon_nice_girl_in_tree
                ,R.drawable.icon_loading,R.drawable.icon_loading_failure,R.drawable.icon_loading_no_wifi);
        mBtnHandler.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_handler:
                Message msg = Message.obtain();
                msg.what = TYPE_ICON;
                handler.handleMessage(msg);

                LogUtil.e("abcd","VIEWROOT ID:" + viewRoot.getId());
                LogUtil.e("abcd","LAYOUTROOT ID:" + layoutRoot.getId());

                break;
        }
    }
}
