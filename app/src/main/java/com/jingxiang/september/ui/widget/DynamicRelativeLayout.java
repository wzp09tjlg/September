package com.jingxiang.september.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.jingxiang.september.R;

/**
 * Created by wu on 2016/10/17.
 * 在花生应用中 使用这个widget 会出现OOM的情况,检查下是什么原因导致的这个问题
 */
public class DynamicRelativeLayout extends RelativeLayout {
    /** Data */
    private float mRate;
    /**********************************************************/
    public DynamicRelativeLayout(Context context){
        super(context);
        init(context,null);
    }

    public DynamicRelativeLayout(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context,attrs);
    }

    public DynamicRelativeLayout(Context context, AttributeSet attrs,int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){
        try{
            TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.AspectratioView);
            mRate = type.getFloat(R.styleable.AspectratioView_height_radio, 0.0f);
            type.recycle();
        }catch (Exception e) {}
    }

    public void setmRate(float rate) {
        if(rate != mRate){
            this.mRate = rate;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mRate > 0.0) {
            super.onMeasure(widthMeasureSpec
                    , MeasureSpec.makeMeasureSpec((int) (MeasureSpec.getSize(widthMeasureSpec) * mRate)
                            , MeasureSpec.getMode(widthMeasureSpec)));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
