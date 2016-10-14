package com.jingxiang.september.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wu on 2016/9/30.
 * 给定一张图片，可以编辑，生成指定大小的圆图片
 */
public class RoundEditImageView extends View {
    /** Data */
    private Bitmap bitmap;
    RectF clipBounds, dst, src;
    Paint clearPaint;

    // 控件宽高
    private int w, h;
    // 选区半径
    private float radius = 150f;

    // 圆形选区的边界
    private RectF circleBounds;

    // 最大放大倍数
    private float max_scale = 2.0f;

    // 双指的距离
    private float distance;
    private float x0, y0;
    private boolean doublePoint;

    /*******************************************************/
    public RoundEditImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RoundEditImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundEditImageView(Context context) {
        super(context);
        init();
    }

    private void init() {
        clearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        clearPaint.setColor(Color.GRAY);
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, null, dst, null);//每次invalidate通过改变dst达到缩放平移的目的
        }
        // 保存图层,以免清除时清除了bitmap
        int count = canvas.saveLayer(clipBounds, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawColor(0x80000000);
        canvas.drawCircle(w / 2, h / 2, radius, clearPaint);// 清除半透明黑色,留下一个透明圆
        canvas.restoreToCount(count);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
        // 记录view所占的矩形
        clipBounds = new RectF(0, 0, w, h);
        float l, r, t, b;
        if (bitmap != null) {
            // 判断长宽比,当长宽比太长会太宽是,以fitCenter的方式初始化图片
            if (w / (float) h > bitmap.getWidth() / (float) bitmap.getHeight()) {
                // 图片太高
                float w_ = h * bitmap.getWidth() / (float) bitmap.getHeight();
                l = w / 2f - w_ / 2f;
                r = w / 2f + w_ / 2f;
                t = 0;
                b = h;
            } else {
                // 图片太长,或跟view一样长
                float h_ = w * bitmap.getHeight() / (float) bitmap.getWidth();
                l = 0;
                r = w;
                t = h / 2f - h_ / 2f;
                b = h / 2f + h_ / 2f;
            }
            dst = new RectF(l, t, r, b);// 这个矩形用来变换
            src = new RectF(l, t, r, b);// 这个矩形仅为保存第一次的状态

            max_scale = Math.max(max_scale, bitmap.getWidth() / src.width());
        }
        circleBounds = new RectF(w / 2 - radius, h / 2 - radius, w / 2 + radius, h / 2 + radius);
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        invalidate();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (e.getPointerCount() > 2) {// 不接受多于两指的事件
            return false;
        } else if (e.getPointerCount() == 2) {
            doublePoint = true;// 标志位,记录两指事件处理后,抬起一只手也不处理拖动
            handleDoubleMove(e);
        } else {
            // 处理单指的拖动
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x0 = e.getX();
                    y0 = e.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (doublePoint) {
                        break;
                    }
                    float x = e.getX();
                    float y = e.getY();
                    float w = dst.width();
                    float h = dst.height();

                    // 不允许拖过圆形区域,不能使圆形区域内空白
                    dst.left += x - x0;
                    if (dst.left > circleBounds.left) {
                        dst.left = circleBounds.left;
                    } else if (dst.left < circleBounds.right - w) {
                        dst.left = circleBounds.right - w;
                    }
                    dst.right = dst.left + w;

                    // 不允许拖过圆形区域,不能使圆形区域内空白
                    dst.top += y - y0;
                    if (dst.top > circleBounds.top) {
                        dst.top = circleBounds.top;
                    } else if (dst.top < circleBounds.bottom - h) {
                        dst.top = circleBounds.bottom - h;
                    }
                    dst.bottom = dst.top + h;

                    x0 = x;
                    y0 = y;
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    doublePoint = false;// 恢复标志位
                    break;
            }
        }

        return true;
    }

    // 处理双指事件
    private void handleDoubleMove(MotionEvent e) {
        switch (e.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:
                distance = sqrt(e);
                Log.d("px", "down:distance=" + distance);
                break;
            case MotionEvent.ACTION_MOVE:
                scale(e);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
    }

    private void scale(MotionEvent e) {
        float dis = sqrt(e);
        // 以双指中心作为图片缩放的支点
        float pX = e.getX(0) / 2f + e.getX(1) / 2f;
        float pY = e.getY(0) / 2f + e.getY(1) / 2f;
        float scale = dis / distance;
        Log.d("px", "move:distance=" + dis + ",scale to" + scale);
        float w = dst.width();
        float h = dst.height();

        if (w * scale < radius * 2 || h * scale < radius * 2 || w * scale > src.width() * max_scale) {
            // 无法缩小到比选区还小,或到达最大倍数
            return;
        }
        // 把dst区域放大scale倍
        dst.left = (dst.left - pX) * scale + pX;
        dst.right = (dst.right - pX) * scale + pX;
        dst.top = (dst.top - pY) * scale + pY;
        dst.bottom = (dst.bottom - pY) * scale + pY;

        // 缩放同样不允许使圆形区域空白
        if (dst.left > circleBounds.left) {
            dst.left = circleBounds.left;
            dst.right = dst.left + w * scale;
        } else if (dst.right < circleBounds.right) {
            dst.right = circleBounds.right;
            dst.left = dst.right - w * scale;
        }

        if (dst.top > circleBounds.top) {
            dst.top = circleBounds.top;
            dst.bottom = dst.top + h * scale;
        } else if (dst.bottom < circleBounds.bottom) {
            dst.bottom = circleBounds.bottom;
            dst.top = dst.bottom - h * scale;
        }
        invalidate();

        distance = dis;
    }

    private float sqrt(MotionEvent e) {
        return (float) Math.sqrt((e.getX(0) - e.getX(1)) * (e.getX(0) - e.getX(1)) + (e.getY(0) - e.getY(1)) * (e.getY(0) - e.getY(1)));
    }

    // 生成目前选区指定大小的圆形bitmap
    public Bitmap extractBitmap(int width) {
        Bitmap outBitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.GRAY);
        canvas.drawCircle(width / 2, width / 2, width / 2, p);
        float scale = dst.width() / bitmap.getWidth();
        int w = (int) (circleBounds.width() / scale);
        int l = (int) ((circleBounds.left - dst.left) / scale);
        int r = l + w;
        int t = (int) ((circleBounds.top - dst.top) / scale);
        int b = t + w;
        Rect resRect = new Rect(l, t, r, b);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//图片的截取 待细究
        canvas.drawBitmap(bitmap, resRect, canvas.getClipBounds(), paint);
        return outBitmap;
    }
}
