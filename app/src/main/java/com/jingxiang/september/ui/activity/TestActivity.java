package com.jingxiang.september.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;

import com.jingxiang.september.MApplication;
import com.jingxiang.september.R;
import com.jingxiang.september.download.DownloadManager2;
import com.jingxiang.september.network.parse.ChannelItem;
import com.jingxiang.september.ui.base.BaseFragmentActivity;
import com.jingxiang.september.ui.widget.RoundEditImageView;
import com.jingxiang.september.util.ComSharepref;
import com.jingxiang.september.util.CommonHelper;
import com.jingxiang.september.util.LogUtil;
import com.jingxiang.september.util.ThreadPool;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wu on 2016/8/8.
 * 基本的类中 尽量少写无用的静态类.积少成多，还是很耗费资源的
 */
public class TestActivity extends BaseFragmentActivity implements
        View.OnClickListener
{
    private final String TAG = "TestActivity";
    private final String TAG_NAME = "TAG_NAME";
    private final String TAG_TYPE = "TAG_TYPE";
    private final String TAG_AGE = "TAG_AGE";
    /** View */
    private Button btnTest1;
    private Button btnTest2;
    private Button btnTest3;
    private Button btnTest4;
    private Button btnTest5;
    private Button btnTest6;
    private Button btnTest7;
    private ImageView imgIcon;

    private RoundEditImageView roundEditImageView;

    /** Data */
    private Context mContext;
    private DownloadManager2 manager2 ;

    /**********************************************/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initViews();
    }

    private void initViews(){
        btnTest1 = $(R.id.btn_test1);
        btnTest2 = $(R.id.btn_test2);
        btnTest3 = $(R.id.btn_test3);
        btnTest4 = $(R.id.btn_test4);
        btnTest5 = $(R.id.btn_test5);
        btnTest6 = $(R.id.btn_test6);
        btnTest7 = $(R.id.btn_test7);
        imgIcon = $(R.id.img_icon);
        roundEditImageView = $(R.id.rimg_edit);

        initData();
    }


    private void initData(){
        mContext = this;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.icon_nice_girl_eat);
        roundEditImageView.setImageBitmap(bitmap);

        initListener();
    }

    private void initListener(){
        btnTest1.setOnClickListener(this);
        btnTest2.setOnClickListener(this);
        btnTest3.setOnClickListener(this);
        btnTest4.setOnClickListener(this);
        btnTest5.setOnClickListener(this);
        btnTest6.setOnClickListener(this);
        btnTest7.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left_img:
                finish();
                break;
            case R.id.btn_test1:
                //1.获取下本测试机的密度和分辨率
                //2.测试100px 到dp 在这个分辨率下的转换值
                int[] tempValue = CommonHelper.getDisplayValue(mContext);
                int px100value = CommonHelper.px2dp(mContext,100);
                LogUtil.e("tempValue:" + tempValue[0] +"*" + tempValue[1] );
                LogUtil.e("px100Value:" + px100value);
                // 测试同步和异步的网络请求
                doAsncOperate();
                // 保存本地的文件
                doSomeTestForSaveFile();
                break;
            case R.id.btn_test2:
                //3.测试100dp在当前的分辨率下转换成px是多少个点
                //4.获取本机的密度
                int dx100value = CommonHelper.dp2px(mContext,100);
                float desity = CommonHelper.getDisplayDesity(mContext);
                LogUtil.e("dx100value:" + dx100value);
                LogUtil.e("desity:" + desity);

                try {
                    Intent intent = new Intent();
                    ComponentName cmp = new ComponentName("com.sina.weibo","com.sina.weibo.SplashActivity");
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(cmp);
                    startActivity(intent);
                    //WeiboHelpter.appToLoginWeibo(LaunchWeiboActivity.this);
                }catch (Exception e){
                }
                // 获取本地保存文件
                doSomeTestGetFile();
                break;
            case R.id.btn_test3:
                //显示自定义的toast
                //GloableToast.show("dshajfhdsjahfslahf");
                //显示编辑之后的圆图
                getEditOvalIcon(200);//设置图像的大小是200
                //针对bitmap做圆角处理
                //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.icon_nice_girl_eat);
                //getRoundCornerBitmap(bitmap,30f);
                break;
            case R.id.btn_test4:
                //测试不开线程频繁访问数据库的操作
                //doOperateDBInMainThread();//耗费时间 3175ms
                // DispatchMessage 底层系统的功能函数,
                // 作用是将消息床底给操作系统，然后操作系统回调咱们的回调函数
                // 及Choreographer 调度页面绘制

                //测试开子线程频繁访问数据库的操作
                doOperateDBInSubThread(); //在主线程中耗费时间是 1ms,
                // 当然在子线程中 肯定还是需要很多的时间，只要对业务不影响 这种将工作是应该放置在子线程中去做的


                break;
            case R.id.btn_test5:
                //测试downloadmanager 下载文件的及对下载进度的监听
                String url = "http://count.liqucn.com/d.php?id=539643&urlos=android&from_type=web";
                manager2 = DownloadManager2.getInstance(mContext);
                manager2.startDownload(url,"September","Huasheng.apk",getDownloadListener());
                manager2.selectDownloadPercent(mContext);//想系统的contentprovider 注册监听
                break;
            case R.id.btn_test6:
                //测试使用service的类
                Bundle bundleServiceTest = new Bundle();
                bundleServiceTest.putString("TITLE","Service Test");
                Intent intentServiceTest = new Intent(TestActivity.this, ServiceTestActivity.class);
                intentServiceTest.putExtras(bundleServiceTest);
                startActivity(intentServiceTest);
                break;
            case R.id.btn_test7:
                break;
        }
    }

    private void doAsncOperate(){
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url("https://www.facebook.com").build();

        //同步请求
        new Thread(new Runnable() {
            @Override
            public void run() {
              try {
                  LogUtil.e("11");
                  Response responseAsnc = client.newCall(request).execute();
                  LogUtil.e("22");
                  LogUtil.e("response:" + responseAsnc.toString());
                  LogUtil.e("33");
              }catch (Exception e){
                  LogUtil.e("44 e:" + e.getMessage());
              }
            }
        }).start();

        //异步请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    private void doSomeTestForSaveFile(){
        ComSharepref.put(TAG_NAME,"zhangsan");
        ComSharepref.put(TAG_TYPE,true);
        ComSharepref.put(TAG_AGE,123);
        LogUtil.e("save success");
    }

    private void doSomeTestGetFile(){
        String name = ComSharepref.get(TAG_NAME,"lisi");
        boolean type = ComSharepref.get(TAG_TYPE,false);
        int age = ComSharepref.get(TAG_AGE,0);
        LogUtil.e("get success");
        LogUtil.e("name:" + name + "  type:" + type + "  age:" + age);
    }

    private void getEditOvalIcon(int size){
        Bitmap bitmap = roundEditImageView.extractBitmap(size);//参数的意思绘制图片的长宽大小
        imgIcon.setImageBitmap(bitmap);
    }

    private void doSomeClassLoader(){
        ClassLoader classLoader = getClassLoader();
        LogUtil.i("classLoader name:" + classLoader.toString() + "  parent name:"  + classLoader.getParent());
    }

    private void doViewLearn(){
        View view = new View(mContext);
    }

    private void getRoundCornerBitmap(Bitmap bitmap,float corner){
       Bitmap tempBitmap = CommonHelper.getRoundCornerBitmap(bitmap,corner);
       imgIcon.setImageBitmap(tempBitmap);
    }

    //数据库的操作在主线程中执行
    private void doOperateDBInMainThread(){
        long startTime = System.currentTimeMillis();
        LogUtil.i("doOperateDBInMainThread startTime:" + startTime);
        int tempCount = 100;//次数
        //操作的动作是 数据的查插删改
        int tempId = 0;
        ChannelItem itemInsert = new ChannelItem(8, "环太平洋战略", 8, 1);
        for(int i=0;i<tempCount;i++){
            tempId = i % 6 + 1;
            MApplication.mCommonDao.selectNewsById(tempId);//查询
            MApplication.mCommonDao.selectSortNewsItemList();//查询
            MApplication.mCommonDao.insertNewsItem(itemInsert);
            itemInsert.setName("环太平洋战略策略篇");
            MApplication.mCommonDao.updateNewsItem(itemInsert);
            MApplication.mCommonDao.deleteNewsItemById(8);
        }
        long stopTime = System.currentTimeMillis();
        LogUtil.i("doOperateDBInMainThread stopTime:" + stopTime);
        LogUtil.i("duration operate time is:" + (stopTime - startTime));
    }

    // 数据库的操作在子线程中进行操作
    private void doOperateDBInSubThread(){
        long startTime = System.currentTimeMillis();
        LogUtil.i("doOperateDBInSubThread startTime:" + startTime);
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                int tempCount = 100;//次数
                //操作的动作是 数据的查插删改
                int tempId = 0;
                ChannelItem itemInsert = new ChannelItem(8, "环太平洋战略", 8, 1);
                for(int i=0;i<tempCount;i++){
                    tempId = i % 6 + 1;
                    MApplication.mCommonDao.selectNewsById(tempId);//查询
                    MApplication.mCommonDao.selectSortNewsItemList();//查询
                    MApplication.mCommonDao.insertNewsItem(itemInsert);
                    itemInsert.setName("环太平洋战略策略篇");
                    MApplication.mCommonDao.updateNewsItem(itemInsert);
                    MApplication.mCommonDao.deleteNewsItemById(8);
                }
            }
        });
        long stopTime = System.currentTimeMillis();
        LogUtil.i("doOperateDBInSubThread stopTime:" + stopTime);
        LogUtil.i("duration operate time is:" + (stopTime - startTime));
    }

    private DownloadManager2.OnDownloadListener getDownloadListener(){
        DownloadManager2.OnDownloadListener listener = new DownloadManager2.OnDownloadListener() {
            @Override
            public void onDownloadListener(int percent) {
               LogUtil.i("onDownloadListener ---- percenter:" + percent);
            }

            @Override
            public void onCompleteListener() {
                if(manager2 != null)
                    manager2.stopDownload(mContext);
            }
        };
        return listener;
    }

    // 写一个动画的监听，重写了几个方法
    private void doSomeAnimation(){
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
            }

            @Override
            public boolean willChangeBounds() {
                return super.willChangeBounds();
            }
        };
    }

}
