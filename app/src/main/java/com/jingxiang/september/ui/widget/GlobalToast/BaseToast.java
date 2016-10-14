package com.jingxiang.september.ui.widget.GlobalToast;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by wu on 2016/9/30.
 * 自定义的提示框,可以设置时间和取消
 */
public class BaseToast {
    public static final int LENGTH_SHORT = 1500;
    public static final int LENGTH_LONG  = 3000;
    /** View */
    private View mView;
    private View mNextView;
    /** Data */
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private int mDuration=LENGTH_SHORT;
    private int mGravity = Gravity.CENTER;
    private int mX, mY;
    private float mHorizontalMargin,mVerticalMargin;
    private boolean isShowing;
    private WindowManager mWM;
    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();

    /*********************************************************/
    public BaseToast(Context context) {
        init(context,false);
    }

    public BaseToast(Context context,boolean touchable){
        init(context,touchable);
    }

    private void init(Context context,boolean touchable){
        final WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        if(!touchable)
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE  //这个属性是针对toast 是否可以被触摸
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        else
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    //| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE  //这个属性是针对toast 是否可以被触摸
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.windowAnimations = android.R.style.Animation_Toast;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;

        mWM = (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
    }

    public void show(){
        mHandler.post(mShow);

        if(mDuration>0)
        {
            mHandler.postDelayed(mHide, mDuration);
        }
    }

    public void hide() {
        mHandler.post(mHide);
    }

    public void cancelShow(){
        if(isShowing)
            handleHide();
    }

    private final Runnable mShow = new Runnable() {
        public void run() {
            handleShow();
        }
    };

    private final Runnable mHide = new Runnable() {
        public void run() {
            handleHide();
        }
    };

    private void handleShow() {
        if (mView != mNextView) {
            handleHide();
            mView = mNextView;
            final int gravity = mGravity;
            mParams.gravity = gravity;
            if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.FILL_HORIZONTAL)
            {
                mParams.horizontalWeight = 1.0f;
            }
            if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.FILL_VERTICAL)
            {
                mParams.verticalWeight = 1.0f;
            }
            mParams.x = mX;
            mParams.y = mY;
            mParams.verticalMargin = mVerticalMargin;
            mParams.horizontalMargin = mHorizontalMargin;
            if (mView.getParent() != null)
            {
                mWM.removeView(mView);
            }
            mWM.addView(mView, mParams);
            isShowing = true;
        }
    }

    private void handleHide() {
        if (mView != null)
        {
            if (mView.getParent() != null)
            {
                mWM.removeView(mView);
            }
            mView = null;
            isShowing = false;
        }
    }

    public void setView(View view) {
        mNextView = view;
    }

    public View getView() {
        return mNextView;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setMargin(float horizontalMargin, float verticalMargin) {
        mHorizontalMargin = horizontalMargin;
        mVerticalMargin = verticalMargin;
    }

    public float getHorizontalMargin() {
        return mHorizontalMargin;
    }

    public float getVerticalMargin() {
        return mVerticalMargin;
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        mGravity = gravity;
        mX = xOffset;
        mY = yOffset;
    }

    public int getGravity() {
        return mGravity;
    }

    public int getXOffset() {
        return mX;
    }

    public int getYOffset() {
        return mY;
    }
}
