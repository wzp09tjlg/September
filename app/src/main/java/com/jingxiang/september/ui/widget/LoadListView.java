package com.jingxiang.september.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by wu on 2016/10/8.
 * 加载数据的ListView，
 * //TODO 在第一次加载时没有加载的footer
 */
public class LoadListView extends ListView implements
        AbsListView.OnScrollListener
{
    /** View */
    private XListViewFooter mListViewFooter;
    /** Data */
    private boolean isMore = true; //是否有更多数据
    private boolean isLoading = false; //是否正在加载
    private int mTouchSlop; //系统认为是滑动的最短距离
    private int mYDown; //按下时的y坐标
    private int mLastY; //抬起时的y坐标, 与mYDown一起用于滑动到底部时判断是上拉还是下拉
    private OnLoadLister loadLister;
    private OnScrollListener scrollListener;
    /************************************************/

    public LoadListView(Context context){super(context);
        init(context);}

    public LoadListView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        init(context);
    }

    public LoadListView(Context context, AttributeSet attributeSet, int defStyleAttr){
        super(context,attributeSet,defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setOnScrollListener(this);
        mListViewFooter = new XListViewFooter(context);
        mListViewFooter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        addFooterView(mListViewFooter);
    }

    public void setScrollListener(OnScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public void setLoadLister(OnLoadLister loadLister) {
        this.loadLister = loadLister;
    }

    public void setPullMore(boolean isMore) {
        this.isMore = isMore;
        isLoading = false;
        mListViewFooter.setState(XListViewFooter.STATE_NORMAL);
        if (isMore) {
            if (!mListViewFooter.isShown()) {
                mListViewFooter.show();
            }
        } else {
            mListViewFooter.hide();
        }
    }

    public void setLoading(boolean loading){
        this.isLoading = loading;
        if (isLoading) {
            mListViewFooter.setState(XListViewFooter.STATE_LOADING);
        } else {
            mListViewFooter.setState(XListViewFooter.STATE_NORMAL);
            removeFooterView(mListViewFooter);
            mYDown = 0;
            mLastY = 0;
        }
    }

    private boolean canLoad() {
        return isMore && isBottom() && !isLoading && isPullUp();
    }

    private boolean isBottom() {
        if (getAdapter() != null) {
            int lastPositon = getLastVisiblePosition();
            int position = getAdapter().getCount() - 1;
            return lastPositon == position;
        }
        return false;
    }

    private boolean isPullUp() {
        return (mYDown - mLastY) >= mTouchSlop;
    }

    private void loadData() {
        if (loadLister != null) {
            // 设置状态
            setLoading(true);
            loadLister.onLoadMore();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 按下
                mYDown = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 移动
                mLastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    /** 监听listView的滚动事件 */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (canLoad() && scrollState == SCROLL_STATE_IDLE) {
            loadData();
        }

        if (scrollListener != null) {
            scrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (scrollListener != null) {
            scrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    public interface OnLoadLister{
        void onLoadMore();
    }
}
