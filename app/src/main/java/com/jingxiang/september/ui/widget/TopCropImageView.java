package com.jingxiang.september.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jingxiang.september.R;

/**
 * from HSVRead
 */
public class TopCropImageView extends ImageView {

    private final String TAG = getClass().getSimpleName();
    private float mHeightRatio;

    public TopCropImageView(Context context) {
        super(context);
        init(context,null);
    }

    public TopCropImageView(Context context, AttributeSet attrs) {
        super(context,attrs);
        init(context,attrs);
    }

    public TopCropImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs){
        try{
            TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.AspectratioView);
            mHeightRatio = type.getFloat(R.styleable.AspectratioView_height_radio, 0.0f);
            type.recycle();
        }catch (Exception e){}
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Matrix matrix = getImageMatrix();
        float[] values = new float[9];
        matrix.getValues(values);
        ScaleType scaleType = getScaleType();
        if (ScaleType.CENTER_CROP == scaleType) {
            matrix.postTranslate(-values[2], -values[5]);
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHeightRatio > 0.0) {
            int tempHeight = (int) (MeasureSpec.getSize(widthMeasureSpec) * mHeightRatio);
            int tempMode = MeasureSpec.getMode(widthMeasureSpec);
            int tempHeightMeasureSpec = MeasureSpec.makeMeasureSpec(tempHeight, tempMode);
            super.onMeasure(widthMeasureSpec, tempHeightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
