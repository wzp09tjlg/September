package com.jingxiang.september.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jingxiang.september.MApplication;
import com.jingxiang.september.R;
import com.jingxiang.september.download.update.UpdateManager;
import com.jingxiang.september.network.parse.UpdateBean;
import com.jingxiang.september.ui.adapter.MainTabAdapter;
import com.jingxiang.september.ui.base.BaseFragment;
import com.jingxiang.september.ui.base.BaseFragmentActivity;
import com.jingxiang.september.ui.widget.CommonDialog;
import com.jingxiang.september.util.DeviceInfoManager;
import com.jingxiang.september.util.LogUtil;
import com.jingxiang.september.util.ThreadPool;

import java.io.File;

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
            LogUtil.e("abcd","keydown back:" + keyCode);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
            LogUtil.e("abcd","keyup back:" + keyCode);
        return super.onKeyUp(keyCode, event);
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

        checkNeedUpdate();
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

    private CommonDialog commonDialog = null;
    private boolean forceInstall = false;
    private UpdateBean bean = null;

    private void checkNeedUpdate(){
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                bean = MApplication.mCommonDao.selectUpdateBean();
                int curVercode =  DeviceInfoManager.getAppVersionCode(mContext);
                int tempVersionCode = 0;
                if(bean != null && !TextUtils.isEmpty(bean.version_code))
                    tempVersionCode = Integer.parseInt(bean.version_code);
                if(bean != null &&  tempVersionCode <= curVercode){
                    deleteUpdateDBandFile();
                }
                if(tempVersionCode > curVercode && bean != null && bean.end == bean.finished && bean.status == 3){ //在下一版中添加这个状态 1未下载 2下载未完成 3下载完成
                    doUpdateOperate();
                }
            }
        });
    }

    private void deleteUpdateDBandFile(){
        try{
            MApplication.mCommonDao.deleteUpdateBean(bean.version_code);
            File tempFile = new File(UpdateManager.DOWNLOAD_FILE_SAVE_PATH + File.separator
                    + UpdateManager.DOWNLOAD_FILE_SAVE_NAME);
            if(tempFile != null)
                tempFile.delete();//如果下载的文件已经和安装的一个版本 就直接删除数据文件和下载的文件
            return;
        }catch (Exception e){}
    }

    private void doUpdateOperate(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                commonDialog = new CommonDialog(mContext);
                commonDialog.setCanceledOnTouchOutside(false);
                commonDialog.setShowTitle(false);
                commonDialog.setShowClose(false);
                commonDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            if (commonDialog.isShowing() && forceInstall) {
                                //退出应用程序
                                Toast.makeText(MainActivity.this, "抱歉，您需要安装最新版才能试用", Toast.LENGTH_SHORT).show();
                                commonDialog.dismiss();
                                exit();
                                return true;
                            } else if (commonDialog.isShowing() && !forceInstall) {
                                commonDialog.dismiss();
                            }
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                View view = getLayoutInflater().inflate(R.layout.view_update_dialog, null);
                ((TextView) view.findViewById(R.id.umeng_update_content)).setText(bean.intro);
                if (forceInstall) {
                    view.findViewById(R.id.ll_not_forceinstall).setVisibility(View.GONE);
                    view.findViewById(R.id.ll_forceinstall).setVisibility(View.VISIBLE);
                }
                commonDialog.setContainer(view);
                view.findViewById(R.id.umeng_update_id_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commonDialog.dismiss();
                        //设置你的操作事项
                        //安装应用程序
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        File file = new File(UpdateManager.DOWNLOAD_FILE_SAVE_PATH + File.separator + UpdateManager.DOWNLOAD_FILE_SAVE_NAME);
                        intent.setDataAndType(Uri.fromFile(file),
                                "application/vnd.android.package-archive");
                        startActivity(intent);
                    }
                });

                view.findViewById(R.id.umeng_update_id_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commonDialog.dismiss();
                    }
                });

                view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commonDialog.dismiss();
                        //设置你的操作事项
                        //安装应用程序
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        File file = new File(UpdateManager.DOWNLOAD_FILE_SAVE_PATH + File.separator
                                + UpdateManager.DOWNLOAD_FILE_SAVE_NAME);
                        intent.setDataAndType(Uri.fromFile(file),
                                "application/vnd.android.package-archive");
                        startActivity(intent);
                    }
                });
                File file = new File(UpdateManager.DOWNLOAD_FILE_SAVE_PATH + File.separator + UpdateManager.DOWNLOAD_FILE_SAVE_NAME);
                if(file != null && file.exists()){
                    commonDialog.showDialog();
                }else{
                    ThreadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            LogUtil.e(" file is empty and not exist ,do delete DB record");
                            MApplication.mCommonDao.deleteUpdateBean(bean.version_code);
                        }
                    });
                }
            }
        });
    }

    private void exit(){
        finish();
        try {
            ((MApplication)getApplication()).destory();
        }catch (Exception e){
            LogUtil.e("mainactivity desteory e:" + e.getMessage());
        }
    }

}
