package com.example.apple.pullzoom;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by apple on 16/8/23.
 */
public class ScrollViewPullZoom extends PullZoomBase<ScrollViewPullZoom.MyScrollView> {

    FrameLayout headerView;
    LinearLayout centerView;
    int headerHight;
    ValueAnimator valueanimator;

    public ScrollViewPullZoom(Context context) {
        super(context);
    }

    public ScrollViewPullZoom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewPullZoom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    public void headerZoom(float offset) {
        ViewGroup.LayoutParams layoutParams = headerView.getLayoutParams();
        layoutParams.height = (int) (headerHight + offset);
        headerView.setLayoutParams(layoutParams);

    }

    public void stopScroll() {
        if (valueanimator != null && valueanimator.isRunning()) {

            valueanimator.cancel();

        }

    }


    @Override
    protected void scrolltotop() {

        int botton = headerView.getBottom();

        valueanimator = ValueAnimator.ofFloat(botton, botton - headerHight).setDuration(200);

        valueanimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float c = (float) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = headerView.getLayoutParams();
                layoutParams.height = Math.max((int) c, headerHight);
                headerView.setLayoutParams(layoutParams);
            }
        });
        valueanimator.setInterpolator(new AccelerateInterpolator());
        valueanimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueanimator.start();


    }

    @Override
    public MyScrollView getView() {

        MyScrollView scrollView = new MyScrollView(getContext());

        scrollView.setOnScrollViewChangedListener(new OnScrollViewChangedListener() {
            @Override
            public void onScrollChanged(int left, int top, int oldLeft, int oldTop) {

                int h = rootView.getScrollY();
                if ((h > 0.0F) && (h < headerHight)) {
                    int i = (int) (0.5 * h);
                    headerView.scrollTo(0, -i);
                } else if (headerView.getScrollY() != 0) {
                    headerView.scrollTo(0, 0);
                }

            }
        });
        return scrollView;
    }

    @Override
    public void init() {
        headerView = new FrameLayout(getContext());
        centerView = new LinearLayout(getContext());
        centerView.setOrientation(LinearLayout.VERTICAL);

        centerView.addView(headerView);
        rootView.addView(centerView);

    }

    @Override
    public boolean isPullStart() {
        return rootView.getScrollY() == 0;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (headerHight == 0 && headerView != null) {
            headerHight = headerView.getHeight();
        }
    }

    public void setContentView(View contentview) {
        centerView.addView(contentview);
    }

    public void setZoomView(View contentview) {
        headerView.addView(contentview);
    }


    public void setHeaderLayoutParams(LayoutParams localObject) {
        headerView.setLayoutParams(localObject);
        headerHight = localObject.height;
    }

    public class MyScrollView extends ScrollView {
        OnScrollViewChangedListener lis;

        public MyScrollView(Context context) {
            super(context);
        }

        public MyScrollView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            super.onScrollChanged(l, t, oldl, oldt);
            lis.onScrollChanged(l, t, oldl, oldt);

        }

        public void setOnScrollViewChangedListener(OnScrollViewChangedListener lis) {
            this.lis = lis;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return false;
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            return super.onTouchEvent(ev);
        }
    }

    protected interface OnScrollViewChangedListener {
        void onScrollChanged(int left, int top, int oldLeft, int oldTop);
    }

}
