package com.example.apple.Custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Created by apple on 17/9/16.
 */

public class VerticalViewPager extends ViewGroup {

    OnPageChangeListener lis;
    int mScreenHeight;//屏幕的高
    Scroller mscroller;
    int startScroll;
    int downY;
    int lastPos = -1;
    boolean isTouchDown;


    public VerticalViewPager(Context context) {
        super(context);
        init(context);
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VerticalViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
        mscroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; ++i) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int childCount = getChildCount();
            MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
            lp.height = mScreenHeight * childCount;
            setLayoutParams(lp);
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() != View.GONE) {
                    child.layout(l, i * mScreenHeight, r, (i + 1) * mScreenHeight);// 调用每个自布局的layout
                }
            }

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int cy = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mscroller.isFinished())
                    return true;
                downY = (int) event.getY();
                startScroll = getScrollY();
                isTouchDown = true;
                break;

            case MotionEvent.ACTION_MOVE:
                int scrollY = getScrollY();
                int dy = downY - cy;
                if (dy < 0 && scrollY + dy < 0) {
                    dy = -scrollY;
                }
                if (dy > 0 && scrollY + dy > getHeight() - mScreenHeight) {
                    dy = getHeight() - mScreenHeight - scrollY;
                }
                scrollBy(0, dy);
                downY = cy;
                break;

            case MotionEvent.ACTION_UP:

                int endScrolly = getScrollY();
                int durscrolly = endScrolly - startScroll;
                if (endScrolly > startScroll) {
                    if (durscrolly > mScreenHeight / 2) {
                        mscroller.startScroll(0, getScrollY(), 0, mScreenHeight - durscrolly);
                    } else {
                        mscroller.startScroll(0, getScrollY(), 0, -durscrolly);
                    }
                }

                if (startScroll > endScrolly) {
                    if (Math.abs(durscrolly) > mScreenHeight / 2) {
                        mscroller.startScroll(0, getScrollY(), 0, -durscrolly - mScreenHeight);
                    } else {
                        mscroller.startScroll(0, getScrollY(), 0, -durscrolly);
                    }
                }
                postInvalidate();
                isTouchDown = false;
                break;
        }

        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mscroller != null && mscroller.computeScrollOffset()) {
            scrollTo(0, mscroller.getCurrY());
            postInvalidate();
        } else {
            if (isTouchDown) return;
            int scrollY = getScrollY();
            int i = scrollY / mScreenHeight;
            if (lastPos == i) return;
            if (lis != null) {
                lis.onPageChange(i);
            }
            lastPos = i;
        }

    }

    public void setPageChangeListener(OnPageChangeListener lis) {
        this.lis = lis;
    }

    public interface OnPageChangeListener {
        void onPageChange(int currentPage);
    }

}
