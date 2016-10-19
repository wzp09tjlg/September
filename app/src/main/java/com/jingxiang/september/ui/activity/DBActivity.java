package com.jingxiang.september.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jingxiang.september.MApplication;
import com.jingxiang.september.R;
import com.jingxiang.september.database.CommonDB;
import com.jingxiang.september.database.CommonDao;
import com.jingxiang.september.network.parse.ChannelItem;
import com.jingxiang.september.ui.base.BaseFragmentActivity;
import com.jingxiang.september.ui.widget.GlobalToast.GloableToast;
import com.jingxiang.september.util.LogUtil;
import com.jingxiang.september.util.ThreadPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wu on 2016/10/9.
 */
public class DBActivity extends BaseFragmentActivity implements
    View.OnClickListener
{
    /** View */
    private View viewTitle;
    private ImageView imgTitlBack;
    private TextView textTitle;
    private ImageView imgTitleMenu;

    private Button btnCreateBD;
    private Button btnGetReadDB;
    private Button btnGetWriteDB;
    private Button btnUpdateDB;
    private Button btnUpdatedGetDB;
    private Button btnTestMethodDB;

    /** Data */
    private Context mContext;
    private String mTitle;
    private int mTestCount = 0;
    private List<ChannelItem> mListNews;
    private List<ChannelItem> mListVideos;

    /**********************************************/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
        getExtra(getIntent());
        initViews();
    }

    private void getExtra(Intent intent){
        Bundle bundle = intent.getExtras();
        mTitle = bundle.getString("TITLE");
    }

    private void initViews(){
        initTitle();

        btnCreateBD = $(R.id.btn_createDB);
        btnGetReadDB = $(R.id.btn_getReadDB);
        btnGetWriteDB = $(R.id.btn_getWriteDB);
        btnUpdateDB = $(R.id.btn_updateDB);
        btnUpdatedGetDB = $(R.id.btn_updated_getDB);
        btnTestMethodDB = $(R.id.btn_testOtherMethod);

        initData();
    }

    private void initTitle(){
        viewTitle = $(R.id.layout_title);
        imgTitlBack = $(R.id.title_left_img);
        textTitle = $(R.id.title_center_text);
        imgTitleMenu = $(R.id.title_right_img);

        textTitle.setText(mTitle);
    }

    private void initData(){
        mContext = this;
        mListNews = new ArrayList<>();
        mListVideos = new ArrayList<>();

        mListNews.add(new ChannelItem(1, "推荐", 1, 1));
        mListNews.add(new ChannelItem(2, "热点", 2, 1));
        mListNews.add(new ChannelItem(3, "娱乐", 3, 1));
        mListNews.add(new ChannelItem(4, "时尚", 4, 1));
        mListNews.add(new ChannelItem(5, "科技", 5, 1));
        mListNews.add(new ChannelItem(6, "体育", 6, 1));
        mListNews.add(new ChannelItem(7, "军事", 7, 1));

        mListVideos.add(new ChannelItem(8, "财经", 1, 0));
        mListVideos.add(new ChannelItem(9, "汽车", 2, 0));
        mListVideos.add(new ChannelItem(10, "房产", 3, 0));
        mListVideos.add(new ChannelItem(11, "社会", 4, 0));
        mListVideos.add(new ChannelItem(12, "情感", 5, 0));
        mListVideos.add(new ChannelItem(13, "女人", 6, 0));
        mListVideos.add(new ChannelItem(14, "旅游", 7, 0));
        mListVideos.add(new ChannelItem(15, "健康", 8, 0));
        mListVideos.add(new ChannelItem(16, "美女", 9, 0));
        mListVideos.add(new ChannelItem(17, "游戏", 10, 0));
        mListVideos.add(new ChannelItem(18, "数码", 11, 0));
        mListVideos.add(new ChannelItem(19, "在线直播", 12, 0));

        initListener();
    }

    private void initListener(){
        imgTitlBack.setOnClickListener(this);
        btnCreateBD.setOnClickListener(this);
        btnGetReadDB.setOnClickListener(this);
        btnGetWriteDB.setOnClickListener(this);
        btnUpdateDB.setOnClickListener(this);
        btnUpdatedGetDB.setOnClickListener(this);
        btnTestMethodDB.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left_img:
                finish();
                break;
            case R.id.btn_createDB:
                doCreateDB();
                break;
            case R.id.btn_getReadDB:
                doGetReadDB();
                break;
            case R.id.btn_getWriteDB:
                doGetWriteDB();
                break;
            case R.id.btn_updateDB:
                doUpdateDB();
                break;
            case R.id.btn_updated_getDB:
                doUpdatedDB();
                break;
            case R.id.btn_testOtherMethod:
                mTestCount = (mTestCount + 1) % 6;
                switch (mTestCount){
                    case 1:
                        LogUtil.i("testMethod: select");
                        doSelectNewsItem(2);
                        break;
                    case 2:
                        LogUtil.i("testMethod: insert");
                        ChannelItem itemInsert = new ChannelItem(8, "环太平洋战略", 8, 1);
                        doInsertNewsItem(itemInsert);
                        break;
                    case 3:
                        LogUtil.i("testMethod: update");
                        ChannelItem itemUpdate = new ChannelItem(8, "环太平洋战略之策略篇", 9, 1);
                        doUpdateNewsItem(itemUpdate);
                        break;
                    case 4:
                        LogUtil.i("testMethod: delete");
                        doDeleteNewsItem(8);
                        break;
                    case 5:
                        LogUtil.i("testMethod: sort");
                        doSortNewsItems();
                        break;
                    default:
                        GloableToast.show("there is no method to test!");
                        break;
                }
                break;
        }
    }

    private void doCreateDB(){
        LogUtil.i("doCreateDB");
        if(MApplication.mCommonDao == null){
            MApplication.mCommonDao = new CommonDao(MApplication.mContext);
        }
    }

    private void doGetReadDB(){
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                LogUtil.i("1 current Thread id:" + Thread.currentThread().getId());
                final List<ChannelItem> list = MApplication.mCommonDao.selectSortNewsItemList();
                if(list != null && list.size() > 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LogUtil.i("2 current Thread id:" + Thread.currentThread().getId());
                            GloableToast.show(list.get(0).toString());
                        }
                    });
                }
            }
        });
    }

    private void doGetWriteDB(){
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                LogUtil.i("1 current thread id:" + Thread.currentThread().getId());
                final boolean tempResult = MApplication.mCommonDao.insertNewsItem(mListNews);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.i("2 current thread id:" + Thread.currentThread().getId());
                       if(tempResult)
                           GloableToast.show("insert data success");
                        else
                           GloableToast.show("insert data failure");
                    }
                });
            }
        });
    }

    private void doUpdateDB(){
        CommonDB.VERSION = 2;//这里测试数据库的升级
        MApplication.mCommonDao = new CommonDao(MApplication.mContext);
    }

    private void doUpdatedDB(){
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                LogUtil.i("1 current thread id:" + Thread.currentThread().getId());
                final boolean tempResult = MApplication.mCommonDao.insertVideoItem(mListVideos);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.i("2 current thread id:" + Thread.currentThread().getId());
                        if(tempResult)
                            GloableToast.show("insert data success");
                        else
                            GloableToast.show("insert data failure");
                    }
                });
            }
        });
    }

    /** 以下是在数据建立完成之后,测试其中的方法是否正确 */
    // 对于News表
    // 查询
    private void doSelectNewsItem(final int id){
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ChannelItem item = MApplication.mCommonDao.selectNewsById(id);
                GloableToast.show("Item:" + item.toString());
            }
        });
    }

    // 插入
    private void doInsertNewsItem(final ChannelItem item){
      ThreadPool.execute(new Runnable() {
          @Override
          public void run() {
              boolean tempResult = MApplication.mCommonDao.insertNewsItem(item);
              GloableToast.show("result is:" + tempResult);
          }
      });
    }

    // 更新
    private void doUpdateNewsItem(final ChannelItem item){
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
               boolean tempResult = MApplication.mCommonDao.updateNewsItem(item);
               GloableToast.show("update result is:" + tempResult);
            }
        });
    }

    // 删除
    private void doDeleteNewsItem(final int id){
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
               boolean tempResult = MApplication.mCommonDao.deleteNewsItemById(id);
                GloableToast.show("delete result is:" + tempResult);
            }
        });
    }

    // 排序
    private void doSortNewsItems(){
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
               List<ChannelItem> list = MApplication.mCommonDao.selectSortNewsItemList();
               if(list != null && list.size() > 0){
                   StringBuilder builder = new StringBuilder();
                   int tempLen = list.size();
                   for(int i=0;i<tempLen;i++)
                   builder.append( (i+1) + ": " + list.get(i) + "\n");
                   GloableToast.show(builder.toString());
               }else
                   GloableToast.show("do sort News Item size is: 0");
            }
        });
    }

    //对查Video表的方法类似News的方法

    //其他的处理
    private void doSomeThing(){
    }
}
