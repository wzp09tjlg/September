package com.jingxiang.september;

import android.app.Application;
import android.content.Context;

import com.jingxiang.september.database.CommonDao;
import com.jingxiang.september.ui.base.BaseFragmentActivity;
import com.jingxiang.september.ui.widget.GlobalToast.GloableToast;
import com.jingxiang.september.util.ComSharepref;
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
    /*********************************************/
    @Override
    public void onCreate() {
        super.onCreate();

        //全局的属性设置
        initGlobalVar();

    }

    /** 仿activity生命周期的一个方法 */
    public void destory(){
        removeAllActivity();
    }

    private void initGlobalVar(){
        mContext = this;
        stackActivity = new Stack<>();
        comSharepref = ComSharepref.getInstance(getApplicationContext());
        refWatcher = LeakCanary.install(this);
        GloableToast.getInsance(mContext);               //可以控制显示时间的toast
        mCommonDao = new CommonDao(mContext);            //数据库的公共类

        ThreadPool.init();                               //线程池的初始化
    }

    // 销毁全局变量
    private void destoryGoableVar(){//在主页销毁时 销毁全局变量?是否合适 和必要?
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
}
