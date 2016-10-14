package com.jingxiang.september.util;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by wu on 2016/10/7.
 * 自定义的view常用的六个工具类
 */
public class ViewUtil {
    private Configuration configuration;
    private ViewConfiguration viewConfiguration;
    private GestureDetector gestureDetector;
    private VelocityTracker velocityTracker;
    private Scroller scroller;
    private ViewDragHelper viewDragHelper;

    /** 日常开发中自定义view需要经常处理各种事件，能够使用上边的这几个类就能很方便的处理常用的事件 */
    private void getConfigration(Context context){
        //configuration 可以获取设备的配置信息。
        //比如用户的配置信息：locale和scaling等等
        //比如设备的相关信息：输入模式，屏幕大小， 屏幕方向等等
        configuration = context.getResources().getConfiguration();
        int desityDpi = configuration.densityDpi; //获取密度(当然获取密度也可以通过dispaly来获取)
        //int desityDpi = context.getResources().getDisplayMetrics().densityDpi;
        int countryCode = configuration.mcc;//国家编码
        int networkCode = configuration.mnc;//网络码
        int windowHorizal = configuration.orientation;//Configuration.ORIENTATION_PORTRAIT or Configuration.ORIENTATION_HORIZAL
    }

    private void getViewConfiguration(Context context){
        //提供了一些自定义控件用到的标准常量，比如尺寸大小，滑动距离，敏感度等等
        viewConfiguration = ViewConfiguration.get(context);
        //获取系统能够识别的被认为是滑动的最小距离
        int touchSlop = viewConfiguration.getScaledTouchSlop();
        //获取Fling速度的最小值和最大值
        int minVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        int maxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        //获取是否有物理按键
        boolean isHavePermanentMenuKey = viewConfiguration.hasPermanentMenuKey();
    }

    private void getGestureDetactor(Context context){
        gestureDetector = new GestureDetector(context,new GestureDetectorImp());
        View view = new View(context);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);//监听事件
                return false;
            }
        });
    }

    private void getVelocityTracker(MotionEvent event){
        velocityTracker = VelocityTracker.obtain();//类似消息一样,不必创建一个新的对象
        velocityTracker.addMovement(event);//监听event的事件

        velocityTracker.computeCurrentVelocity(1000);//设置VelocityTracker单位.1000表示1秒时间内运动的像素
        int xVelocity = (int)velocityTracker.getXVelocity();//获取X轴上的速度
        int yVelocity = (int)velocityTracker.getYVelocity();//获取Y轴上的速度

        if(velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    private void getScoller(Context context){
        scroller = new Scroller(context);//计算滚动的一些变量

        View view = new View(context){ //view中就提供了滚动的方法
            @Override
            public void scrollBy(int x, int y) {
                super.scrollBy(x, y);
            }

            @Override
            public void scrollTo(int x, int y) {
                super.scrollTo(x, y);
            }
        };
    }

    private void getViewDragHelper(Context context){
        //拖拽移动，越界，多手指的按下，加速度检测等等
        //ViewDragHelper可以极大的帮我们简化类似的处理，它提供了一系列用于处理用户拖拽子View的辅助方法和与其相关的状态记录
        DragView dragView = new DragView(context);
    }

    //实现对事件的动作的处理
    class GestureDetectorImp implements GestureDetector.OnGestureListener{
        @Override
        public boolean onDown(MotionEvent e) {
            //被按下时触发的事件
            LogUtil.i("onDown:" + e.getAction());
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            //手指在屏幕上按下,且未移动和松开时调用该方法
            LogUtil.i("onShowPress:" + e.getAction());
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            //轻击屏幕时调用该方法
            LogUtil.i("onSingleTapUp:" + e.getAction());
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //手指在屏幕上滚动时会调用该方法
            LogUtil.i("onScroll:" + e1.getAction());
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            //手指长按屏幕时均会调用该方法
            LogUtil.i("onLongPress:" + e.getAction());
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //手指在屏幕上拖动时会调用该方法
            LogUtil.i("onFling:" + e1.getAction());
            return false;
        }
    }

    class DragView extends LinearLayout{
        private ViewDragHelper viewDragHelper;

        public DragView(Context context){
            super(context);
            init();
        }

        public DragView(Context context, AttributeSet attributeSet){
            super(context,attributeSet);
            init();
        }

        public DragView(Context context,AttributeSet attributeSet,int defStyleAttr){
            super(context,attributeSet,defStyleAttr);
            init();
        }

        private void init(){//将在parent中的interceptTouchEvent 及 onTouchEvent中的事件抽出来 放在viewDragHelper中来实现，便于梳理和阅读
            viewDragHelper =  ViewDragHelper.create(this,1.0f,new ViewDragHelper.Callback(){
                @Override
                public boolean tryCaptureView(View child, int pointerId) {
                    return true;
                }

                //处理水平方向的越界
                @Override
                public int clampViewPositionHorizontal(View child, int left, int dx) {
                    int fixedLeft;
                    View parent = (View) child.getParent();
                    int leftBound = parent.getPaddingLeft();
                    int rightBound = parent.getWidth() - child.getWidth() - parent.getPaddingRight();

                    if (left < leftBound) {
                        fixedLeft = leftBound;
                    } else if (left > rightBound) {
                        fixedLeft = rightBound;
                    } else {
                        fixedLeft = left;
                    }
                    return fixedLeft;
                }

                //处理垂直方向的越界
                @Override
                public int clampViewPositionVertical(View child, int top, int dy) {
                    int fixedTop;
                    View parent = (View) child.getParent();
                    int topBound = getPaddingTop();
                    int bottomBound = getHeight() - child.getHeight() - parent.getPaddingBottom();
                    if (top < topBound) {
                        fixedTop = topBound;
                    } else if (top > bottomBound) {
                        fixedTop = bottomBound;
                    } else {
                        fixedTop = top;
                    }
                    return fixedTop;
                }

                //监听拖动状态的改变
                @Override
                public void onViewDragStateChanged(int state) {
                    super.onViewDragStateChanged(state);
                    switch (state) {
                        case ViewDragHelper.STATE_DRAGGING:
                            System.out.println("STATE_DRAGGING");
                            break;
                        case ViewDragHelper.STATE_IDLE:
                            System.out.println("STATE_IDLE");
                            break;
                        case ViewDragHelper.STATE_SETTLING:
                            System.out.println("STATE_SETTLING");
                            break;
                    }
                }

                //捕获View
                @Override
                public void onViewCaptured(View capturedChild, int activePointerId) {
                    super.onViewCaptured(capturedChild, activePointerId);
                    System.out.println("ViewCaptured");
                }

                //释放View
                @Override
                public void onViewReleased(View releasedChild, float xvel, float yvel) {
                    super.onViewReleased(releasedChild, xvel, yvel);
                    System.out.println("ViewReleased");
                }
            });
        }


        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            viewDragHelper.processTouchEvent(ev);
            return super.onInterceptTouchEvent(ev);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            viewDragHelper.processTouchEvent(event);
            return super.onTouchEvent(event);
        }
    }

}
