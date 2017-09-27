package com.example.apple.top;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by apple on 17/5/22.
 */

public class MyListView extends LinearLayout implements NestedScrollingChild {

    private NestedScrollingChildHelper mScrollingChildHelper;

    public static final String Tag = "MyNestedScrollChildL";

    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];
    private final int[] mNestedOffsets = new int[2];

    private int mLastTouchX;
    private int mLastTouchY;
    private int showHeight;

    public MyListView(Context context) {
        super(context);
        init();
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.setOrientation(VERTICAL);
        if (mScrollingChildHelper == null) {
            mScrollingChildHelper = new NestedScrollingChildHelper(this);
            mScrollingChildHelper.setNestedScrollingEnabled(true);
        }
    }


    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        Log.i(Tag, "setNestedScrollingEnabled:" + enabled);
        mScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        Log.i(Tag, "isNestedScrollingEnabled");
        return mScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {

        return mScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        Log.i(Tag, "stopNestedScroll");
        mScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        Log.i(Tag, "hasNestedScrollingParent");
        return mScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        Log.i(Tag, "dispatchNestedScroll()");
        return mScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        Log.i(Tag, "dispatchNestedPreScroll()");
        return mScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        Log.i(Tag, "dispatchNestedPreFling()");
        return mScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                mLastTouchY = (int) (event.getRawY() + 0.5f);
                int nestedScrollAxis = ViewCompat.SCROLL_AXIS_NONE;
                nestedScrollAxis |= ViewCompat.SCROLL_AXIS_HORIZONTAL;  //按位或运算

                Log.i(Tag, "nestedScrollAxis：" + nestedScrollAxis);
                startNestedScroll(nestedScrollAxis);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(Tag, "Child--getRawY:" + event.getRawY());

                int x = (int) (event.getX() + 0.5f);
                int y = (int) (event.getRawY() + 0.5f);
                int dx = mLastTouchX - x;
                int dy = mLastTouchY - y;

                Log.i(Tag, "child:dy:" + dy + ",mLastTouchY:" + mLastTouchY + ",y;" + y);

                mLastTouchY = y;
                mLastTouchX = x;
                if (dispatchNestedPreScroll(dx, dy, mScrollConsumed, mScrollOffset)) {
                    dy -= mScrollConsumed[1];
                    if (dy == 0) {
                        return true;
                    }
                } else {
                    scrollBy(0, dy);

                }
                break;
        }
        return true;
    }

    @Override
    public void scrollTo(int x, int y) {

        int MaxY = getMeasuredHeight() - showHeight;
        if (y > MaxY) {
            y = MaxY;
        }
        if (y < 0) {
            y = 0;
        }
        super.scrollTo(x, y);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (showHeight <= 0) {
            showHeight = getMeasuredHeight();
        } else {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(1000000, MeasureSpec.UNSPECIFIED);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
