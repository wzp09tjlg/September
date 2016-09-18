package com.jingxiang.september;

import android.app.Application;

import com.jingxiang.september.ui.base.BaseActivity;
import com.jingxiang.september.util.ComSharepref;

import java.util.Stack;

/**
 * Created by wu on 2016/9/12.
 */
public class MApplication extends Application {
    //全局变量
    public Stack<BaseActivity> stackActivity;
    public ComSharepref comSharepref;

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
        stackActivity = new Stack<>();
        comSharepref = ComSharepref.getInstance(getApplicationContext());
    }

    /** 对Activity的管控 */
    public void addActivity(BaseActivity activity){
        if(stackActivity == null)
            stackActivity = new Stack<>();
        stackActivity.push(activity);
    }

    public void removeActivity(BaseActivity activity){
        if(stackActivity != null && stackActivity.size() > 0)
            stackActivity.remove(activity);
    }

    public void removeAllActivity(){
        if(stackActivity != null && stackActivity.size() > 0){
            for(BaseActivity activity:stackActivity) {
                activity.finish();
                activity = null;
            }
            stackActivity = null;
        }
    }
}
