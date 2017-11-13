package com.example.apple.top;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.example.apple.R;

/**
 * Created by apple on 17/5/19.
 */

public class StickyLayout extends LinearLayout implements NestedScrollingParent {

    private static final String TAG = "StickyLayout";

    public StickyLayout(Context context) {
        super(context);
        init();
    }

    public StickyLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StickyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    RelativeLayout top ;
    TextView toobar ;
    MyListView sv;
    int mTopViewHeight;
    Scroller mScroller;

    void init() {
        this.setOrientation(LinearLayout.VERTICAL);
        mScroller = new Scroller(getContext());

    }


    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        super.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.e(TAG, "onStartNestedScroll child " + child.getClass().getSimpleName() + "  target " + target.getClass().getSimpleName() + "  nestedScrollAxes " + nestedScrollAxes);
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        Log.e(TAG, "onNestedScrollAccepted child " + child.getClass().getSimpleName() + "  target " + target.getClass().getSimpleName() + " nestedScrollAxes " + nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        Log.e(TAG, "onStopNestedScroll  " + target.getClass().getSimpleName());
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.e(TAG, "onNestedScroll" + target.getClass().getSimpleName() + " dxConsumed " + dxConsumed + " dyConsumed " + dyConsumed
                + " dxUnconsumed" + dxUnconsumed + "  dyUnconsumed " + dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        Log.e(TAG, "onNestedPreScroll  " + target.getClass().getSimpleName() + " dx " + dx + " dy " + dy + " consumed " + consumed.toString());
        boolean hiddenTop = dy > 0 && getScrollY() < mTopViewHeight;
        boolean showTop = dy < 0 && getScrollY() >= 0 && !ViewCompat.canScrollVertically(target, -1);

        if (hiddenTop || showTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.e(TAG, "onNestedFling  " + target.getClass().getSimpleName() + " velocityX " + velocityX + " velocityY " +
                velocityY + "consumed " + consumed);

        return false;
    }








    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //不限制顶部的高度
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getChildAt(0).measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        ViewGroup.LayoutParams params = sv.getLayoutParams();
        params.height = getMeasuredHeight() - toobar.getMeasuredHeight();
        setMeasuredDimension(getMeasuredWidth(), top.getMeasuredHeight() + toobar.getMeasuredHeight() + sv.getMeasuredHeight());

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = top.getMeasuredHeight();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        top = (RelativeLayout) findViewById(R.id.top);
        toobar = (TextView)findViewById(R.id.toobar);
        sv = (MyListView) findViewById(R.id.sv);

    }










    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight) {
            y = mTopViewHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }


}
