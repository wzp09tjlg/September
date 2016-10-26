package com.jingxiang.september.ui.widget.PageView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wu on 2016/10/26.
 * 这个方法中主要使用的是GestureListener 针对事件的处理和监听做处理
 */
public class PageView extends View implements View.OnTouchListener {

    private static String TAG = PageView.class.getSimpleName();
    private GestureDetector gestureDetector;
    private MyOnGestureListener listener;


    public PageView(Context context){
        super(context);
        init(context);
    }

    public PageView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        init(context);
    }

    public PageView(Context context,AttributeSet attributeSet,int flag){
        super(context,attributeSet,flag);
        init(context);
    }

    private void init(Context context){
        setOnTouchListener(this);
        listener = new MyOnGestureListener();
        gestureDetector = new GestureDetector(context, listener);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    //对应simple** 安卓中会对用存在complex** 无论是布局还是其他的什么方法实现，有时间可以看看复杂的类
    class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            Log.e(TAG,"onDown------------>");
            return true;
        }
        @Override
        public void onShowPress(MotionEvent e) {
            Log.e(TAG,"onShowPress------------>");
        }
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.e(TAG,"onSingleTapUp------------>");
            return false;
        }
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.e(TAG,"onScroll------------>");
            return false;
        }
        @Override
        public void onLongPress(MotionEvent e) {
            Log.e(TAG,"onLongPress------------>");
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.e(TAG,"onFling------------>");
            return false;
        }
    }
}
