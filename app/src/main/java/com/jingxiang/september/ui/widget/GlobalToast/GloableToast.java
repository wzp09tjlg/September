package com.jingxiang.september.ui.widget.GlobalToast;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by wu on 2016/9/30.
 */
public class GloableToast {
    private static BaseToast baseToast;
    private static GloableToast gloableToast;
    private static Context mContext;

    private GloableToast(Context context){
        baseToast = new BaseToast(context,false);
    }

    public static GloableToast getInsance(Context context){
        if(gloableToast == null){
            gloableToast = new GloableToast(context);
        }
        mContext = context;
        return gloableToast;
    }

    public static void show(String msg){
        show(msg,3000);
    }

    public static void show(String msg,int duration){
        LinearLayout mLayout=new LinearLayout(mContext);
        mLayout.setOrientation(LinearLayout.HORIZONTAL);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tv = new TextView(mContext);
        tv.setText(msg);
        tv.setTextColor(Color.rgb(0, 0, 0));
        tv.setGravity(Gravity.CENTER);
        mLayout.addView(tv,params);

        baseToast.setView(mLayout);
        baseToast.setDuration(duration);
        baseToast.show();
    }
}
