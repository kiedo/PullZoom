package com.example.apple.pullzoom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by apple on 16/8/28.
 */
public class ListViewPullZoom extends PullZoomBase<ListView> {


    final String TAG = "ListViewPullZoom";

    private int headerHight;
    private ValueAnimator mValueAnimator;

    public ListViewPullZoom(Context context) {
        super(context);
    }

    public ListViewPullZoom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewPullZoom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    FrameLayout headerView;

    @Override
    public void headerZoom(float offset) {

        ViewGroup.LayoutParams layout =   headerView.getLayoutParams();
        layout.height = (int)(headerHight + offset);
        headerView.setLayoutParams(layout);

    }

    @Override
    protected void scrolltotop() {
        int botton = headerView.getBottom();

        mValueAnimator = ValueAnimator.ofFloat(botton,botton - headerHight).setDuration(300);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
               float cy = (float) animation.getAnimatedValue();
               ViewGroup.LayoutParams lp =  headerView.getLayoutParams();
                if(cy< headerHight ){
                    cy = headerHight;
                }
                lp.height = (int)cy;
                headerView.setLayoutParams(lp);
            }
        });
        mValueAnimator.setInterpolator(new AccelerateInterpolator());
        mValueAnimator.start();
    }

    @Override
    public ListView getView() {
        ListView listView = new ListView(getContext());
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (headerView != null) {

                    int offset = headerHight - headerView.getBottom();
                    if (offset > 0) {
                        headerView.scrollTo(0, -(int) (offset * 0.5f));
                    } else {
                        if (headerView.getScrollY() != 0) {
                            headerView.scrollTo(0, 0);
                        }
                    }

                }
            }
        });


        return listView;
    }

    public void setAdapter(ListAdapter adapter) {
        rootView.setAdapter(adapter);
    }


    public void setOnItemClickListener(@Nullable AdapterView.OnItemClickListener listener) {
        rootView.setOnItemClickListener(listener);
    }


    @Override
    public void init() {

        headerView = new FrameLayout(getContext());
        rootView.addHeaderView(headerView);

    }

    @Override
    protected boolean isPullStart() {
        return isFirstItemVisible();
    }

    private boolean isFirstItemVisible() {
        final Adapter adapter = rootView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        } else {
            if (rootView.getFirstVisiblePosition() <= 0) {
                final View firstVisibleChild = rootView.getChildAt(0);
                if (firstVisibleChild != null) {
                    Log.e(TAG, "isFirstItemVisible: " + firstVisibleChild.getTop() + " -- " + rootView.getTop());
                    return firstVisibleChild.getTop() >= rootView.getTop();
                }
            }
        }

        return false;
    }


    @Override
    protected void stopScroll() {
        if(mValueAnimator != null && mValueAnimator.isRunning()){
            mValueAnimator.cancel();
        }
    }

    public void setZoomView(View zoomView) {

        this.headerView.addView(zoomView);
    }

    public void setHeaderLayoutParams(AbsListView.LayoutParams headerLayoutParams) {

        headerView.setLayoutParams(headerLayoutParams);
        headerHight = headerLayoutParams.height;
    }
}
