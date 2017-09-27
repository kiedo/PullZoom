package com.example.apple.pullzoom;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.CharArrayReader;

/**
 * Created by apple on 16/8/23.
 */
public abstract class PullZoomBase<T extends View> extends LinearLayout {


    protected T rootView;
    protected int mScreenHeight;
    protected int mScreenWidth;
    boolean flag = true;
    private float lastX, lastY;
    private float mTouchSlop;//滑动最小偏移量

    public PullZoomBase(Context context) {
        this(context, null);

    }

    public PullZoomBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        ViewConfiguration config = ViewConfiguration.get(context);
        mTouchSlop = config.getScaledTouchSlop();

        rootView = getView();
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        mScreenHeight = localDisplayMetrics.heightPixels;
        mScreenWidth = localDisplayMetrics.widthPixels;
        init();


        this.addView(rootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public PullZoomBase(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                if (isPullStart()) {
                    lastX = ev.getX();
                    lastY = ev.getY();
                    flag = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isPullStart()) {
                    float cx = ev.getX(), cy = ev.getY();
                    float offsetY, offsetx;
                    offsetx = cx - lastX;
                    offsetY = cy - lastY;
                    if (offsetY > mTouchSlop && Math.abs(offsetY) > Math.abs(offsetx)) {
                        if (offsetY > 0 && isPullStart()) {
                            lastY = cy;
                            lastX = cx;
                            flag = true;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                flag = false;
                break;

        }
        return flag;


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isPullStart()) {
                    stopScroll();
                    lastY = event.getY();
                    lastX = event.getX();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                pollZoom(event);

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                scrolltotop();
                break;
        }
        return super.onTouchEvent(event);
    }

    public void pollZoom(MotionEvent event) {
        float offset = lastY - event.getY();
        headerZoom(Math.abs(Math.min(offset, 0)) / 2);

    }

    public abstract void headerZoom(float offset);

    protected abstract void scrolltotop();

    public abstract T getView();

    public abstract void init();

    protected abstract boolean isPullStart();

    protected abstract void stopScroll();


}
