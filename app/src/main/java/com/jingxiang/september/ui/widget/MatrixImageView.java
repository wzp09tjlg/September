package com.jingxiang.september.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.jingxiang.september.R;

/**
 * Created by wu on 2016/10/26.
 */
public class MatrixImageView extends ImageView {
    private Matrix mMatrix;
    private Bitmap mBitmap;
    public MatrixImageView(Context context) {
        super(context);
        mMatrix=new Matrix();
        mBitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        System.out.println("---> onDraw");
        //画原图
        canvas.drawBitmap(mBitmap, 0, 0, null);
        //画经过Matrix变化后的图
        canvas.drawBitmap(mBitmap, mMatrix, null);
        super.onDraw(canvas);
    }
    @Override
    public void setImageMatrix(Matrix matrix) {
        System.out.println("---> setImageMatrix");
        this.mMatrix.set(matrix);
        super.setImageMatrix(matrix);
    }

    public Bitmap getBitmap(){
        System.out.println("---> getBitmap");
        return mBitmap;
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        super.setOnLongClickListener(l);
    }


}
