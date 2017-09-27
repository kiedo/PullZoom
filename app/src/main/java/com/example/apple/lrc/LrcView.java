package com.example.apple.lrc;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

/**
 * 逐行歌词
 * Created by apple on 17/4/28.
 */

public class LrcView extends View {


    final int ANIMATION_TIME = 400;


    final String TAG = LrcView.class.getSimpleName();


    private List<LrcBean> mlrcs = new ArrayList<>();
    int width;
    int height;
    Paint mNormalpaint;
    Paint mHighLightpaint;
    Paint mLinePaint;

    int center;// 绘制歌词的中心点
    int wordHight; // 文本高度和歌词行距
    LrcDrawTool mLrcDrawTool;
    Scroller mScroller;
    int preLrc;
    int scaledTouchSlop;//滑动最小距离
    boolean isMoving, isClick = true;
    boolean isStop = false;//禁止滑动

    public static volatile long playTime = 20000;


    public LrcView(Context context) {
        super(context);
        init();
    }

    public LrcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LrcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {

        mNormalpaint = new Paint();
        mNormalpaint.setStrokeWidth(3);
        mNormalpaint.setTextSize(60);
        mNormalpaint.setColor(Color.BLACK);//#ff666666
        mNormalpaint.setAntiAlias(true);

        mHighLightpaint = new Paint();
        mHighLightpaint.setTextSize(60);
        mHighLightpaint.setStrokeWidth(3);
        mHighLightpaint.setColor(Color.RED);//#ff999999
        mHighLightpaint.setAntiAlias(true);
        wordHight = 2 * (int) mHighLightpaint.measureText("口");//行距
        mLrcDrawTool = new LrcDrawTool();
        mScroller = new Scroller(getContext());

        mLinePaint = new Paint();
        mLinePaint.setTextSize(40);
        mLinePaint.setStrokeWidth(3);
        mLinePaint.setColor(Color.BLUE);//#ff999999
        mLinePaint.setAntiAlias(true);

        scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLrcDrawTool.setParameter(mNormalpaint, mHighLightpaint,mLinePaint, width, height, wordHight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
        center = height / 2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLrcDrawTool != null) {
            mLrcDrawTool.onDraw(canvas);
            if (isMoving)
                mLrcDrawTool.onDrawLine(canvas, this);
        }

    }

    /**
     * 禁止滑动
     *
     * @param isStop
     */
    public void setIsStop(boolean isStop) {
        this.isStop = isStop;
    }

    float y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y = event.getY();
                isMoving = (isStop ? false : true);
                isClick = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float durY = event.getY() - y;
                if (Math.abs(durY) > scaledTouchSlop / 2) {
                    isClick = false;
                    if (mlrcs.size() > 0 && !isStop) {
                        if (getScrollY() < 0) {
                            durY = durY / 3;
                        } else if (getScrollY() > mlrcs.size() * wordHight) {
                            durY = durY / 3;
                        }
                        scrollBy(getScrollX(), -(int) durY);
                        y = event.getY();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                int old = getScrollY();
                if (old < 0) {
                    smootscroll(mLrcDrawTool.getIndexline() * (wordHight) - old, ANIMATION_TIME, old);
                } else if (getScrollY() > mlrcs.size() * wordHight) {
                    smootscroll(mLrcDrawTool.getIndexline() * (wordHight) - old, ANIMATION_TIME, old);
                }
                if (isClick) {
                    isClick = true;
                    if (mOnclicklisener != null) {
                        mOnclicklisener.onClick();
                    }
                }
                invalidate();
                isMoving = false;

                break;
        }
        return true;
    }

    public void reflash(int durtime) {

        if (isMoving) return;

        if (mScroller != null && mLrcDrawTool != null) {
            int indexline = mLrcDrawTool.getIndexline();
            if (preLrc != indexline) {
                int old = getScrollY();
                int offset = indexline * wordHight - old;
                smootscroll(offset, durtime, old);
                preLrc = indexline;
                invalidate();
            }


            if (playTime > 120000) {
                playTime = 20000;
            }
            playTime += 500;


        }
    }

    void smootscroll(int offset, int durtime, int old) {

        mScroller.startScroll(getScrollX(), old, getScrollX(), offset, durtime);
        invalidate();
    }

    public void computeScroll() {
        if (mScroller != null && mScroller.computeScrollOffset()) {
            int scrollY = getScrollY();
            int currY = mScroller.getCurrY();
            if (scrollY != currY) {
                scrollTo(getScrollX(), currY);
            }
            invalidate();
        }
        super.computeScroll();
    }


    public void setLrcs(List<LrcBean> lrcs) {

        if (mlrcs == null) {
            this.mlrcs = lrcs;
        } else {
            this.mlrcs.clear();
            this.mlrcs.addAll(lrcs);
        }


        if (mLrcDrawTool != null) {
            mLrcDrawTool.setData(lrcs);
        }
        invalidate();

    }

    Onclicklisener mOnclicklisener;

    public void setOnClickListener(@Nullable Onclicklisener l) {
        mOnclicklisener = l;
    }

    public interface Onclicklisener {
        void onClick();
    }
}
