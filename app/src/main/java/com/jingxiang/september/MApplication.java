package com.jingxiang.september;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.jingxiang.september.database.CommonDao;
import com.jingxiang.september.download.update.UpdateManager;
import com.jingxiang.september.download.update.UpdateService;
import com.jingxiang.september.stats.EventManager;
import com.jingxiang.september.ui.base.BaseFragmentActivity;
import com.jingxiang.september.ui.widget.GlobalToast.GloableToast;
import com.jingxiang.september.util.ComSharepref;
import com.jingxiang.september.util.FinalUtil;
import com.jingxiang.september.util.LogUtil;
import com.jingxiang.september.util.ThreadPool;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.Stack;

/**
 * Created by wu on 2016/9/12.
 */
public class MApplication extends Application {
    /** Data */
    public static Context mContext;
    //全局变量
    public Stack<BaseFragmentActivity> stackActivity;
    public ComSharepref comSharepref;
    private RefWatcher refWatcher;
    public static CommonDao mCommonDao;
    public static UpdateManager mUpdateManager;

    /*********************************************/
    @Override
    public void onCreate() {
        super.onCreate();

        //全局的属性设置
        initGlobalVar();

    }

    /** 仿activity生命周期的一个方法 */
    public void destory(){
        LogUtil.e("application destory");
        destoryGoableVar();
        removeAllActivity();
    }

    private void initGlobalVar(){
        mContext = this;
        stackActivity = new Stack<>();
        comSharepref = ComSharepref.getInstance(getApplicationContext());
        refWatcher = LeakCanary.install(this);
        GloableToast.getInsance(mContext);               //可以控制显示时间的toast
        mCommonDao = new CommonDao(mContext);            //数据库的公共类
        mUpdateManager = UpdateManager.getInstance();    //版本更新工具类

        EventManager.init(mContext, FinalUtil.PATH);     //数据统计模块

        ThreadPool.init();                               //线程池的初始化
    }

    // 销毁全局变量
    private void destoryGoableVar(){//在主页销毁时 销毁全局变量?是否合适 和必要?
        stopUpdateService();
        mCommonDao.closeDb();       //关闭数据库
        ThreadPool.shutdown();      //关闭线程池
    }

    /** 对Activity的管控 */
    public void addActivity(BaseFragmentActivity activity){
        if(stackActivity == null)
            stackActivity = new Stack<>();
        stackActivity.push(activity);
    }

    public void removeActivity(BaseFragmentActivity activity){
        if(stackActivity != null && stackActivity.size() > 0)
            stackActivity.remove(activity);
    }

    public void removeAllActivity(){
        if(stackActivity != null && stackActivity.size() > 0){
            for(BaseFragmentActivity activity:stackActivity) {
                activity.finish();
                activity = null;
            }
            stackActivity = null;
        }
    }

    /** 获取检测内存泄漏的watcher */
    public static RefWatcher getRefWatcher(Context context){
        MApplication application = (MApplication)context.getApplicationContext();
        return application.refWatcher;
    }

    /** 关闭下载服务 *///每次启动应用都不一定会开启下载服务,但是在每次启动应用的时候 都应该需要关闭下载更新的服务
    private void stopUpdateService(){
        Intent intentStopUpdateService = new Intent(mContext, UpdateService.class);
        stopService(intentStopUpdateService);
    }
}
