package com.sxhxjy.roadmonitor.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.sxhxjy.roadmonitor.R;

/**
 * 2016/9/13
 *
 * @author Michael Zhao
 */
public class PullRefreshLoadLayout extends LinearLayout implements NestedScrollingParent, ValueAnimator.AnimatorUpdateListener {
    private int mRefreshLayoutHeight;
    private int mLoadMoreLayoutHeight;
    private boolean mInitialed;
    private OnRefreshListener mOnRefreshListener;
    private OnLoadMoreListener mOnLoadMoreListener;
    private ValueAnimator mValueAnimator = new ValueAnimator();
    private boolean mRefreshing;
    private boolean mLoading;
    private boolean mIsBeingDragged;
    private LayoutInflater mLayoutInflater;

    public PullRefreshLoadLayout(Context context) {
        super(context);
    }

    public PullRefreshLoadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public PullRefreshLoadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        View refreshView = mLayoutInflater.inflate(R.layout.refresh_layout, this, false);
        addView(refreshView, 0);
        // load more layout must specify weight in layout otherwise measuredHeight will be 0
        // because of height of recyclerView is MATCH_PARENT in linearLayout
        View loadMoreView = mLayoutInflater.inflate(R.layout.load_more_layout, this, false);
        addView(loadMoreView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mRefreshLayoutHeight = getChildAt(0).getMeasuredHeight();
        mLoadMoreLayoutHeight = getChildAt(2).getMeasuredHeight();
        if (!mInitialed) {
            mInitialed = true;
            setPadding(0, -mRefreshLayoutHeight, 0, -mLoadMoreLayoutHeight);
            mValueAnimator.addUpdateListener(this);
        }
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {

    }

    @Override
    public void onStopNestedScroll(View target) {

        if (getScrollY() == 0) return;

        if ((getScrollY() < 0 && -getScrollY() < mRefreshLayoutHeight) // pulled height is insufficient
                || (getScrollY() > 0 && getScrollY() < mLoadMoreLayoutHeight)) {
            mValueAnimator.setIntValues(getScrollY(), 0); // reset
        } else {
            if (getScrollY() < 0) { // pull down
                mValueAnimator.setIntValues(getScrollY(), -mRefreshLayoutHeight);
                if (!mRefreshing && mOnRefreshListener != null) {
                    mRefreshing = true;
                    mLoading = false;
                    mOnRefreshListener.onRefresh();
                }
            } else { // pull up
                mValueAnimator.setIntValues(getScrollY(), mLoadMoreLayoutHeight);
                if (!mLoading && mOnLoadMoreListener != null) {
                    mLoading = true;
                    mRefreshing = false;
                    mOnLoadMoreListener.onLoadMore();
                }
            }
        }
        mValueAnimator.start();
        mIsBeingDragged = false;
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        RecyclerView recyclerView = (RecyclerView) target;
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if ((linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0 && dy < 0)
                || (linearLayoutManager.findLastCompletelyVisibleItemPosition() == recyclerView.getAdapter().getItemCount() - 1 && dy > 0)
                || mIsBeingDragged) {
            scrollBy(0, dy / 2);
            mIsBeingDragged = true;
            consumed[0] = 0;
            consumed[1] = dy;
        }
    }
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return 0;
    }

    public void setOnRefreshListener(OnRefreshListener l) {
        mOnRefreshListener = l;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener l) {
        mOnLoadMoreListener = l;
    }

    public void refreshEnd() {
        mValueAnimator.setIntValues(getScrollY(), 0);
        mValueAnimator.start();
        mRefreshing = false;
    }

    public void refreshBegin() {
        mValueAnimator.setIntValues(getScrollY(), - mRefreshLayoutHeight);
        mValueAnimator.start();
        mRefreshing = true;
    }

    public void loadMoreEnd() {
        mValueAnimator.setIntValues(getScrollY(), 0);
        mValueAnimator.start();
        mLoading = false;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        int var = (Integer) animation.getAnimatedValue();
        scrollTo(0, var);
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
