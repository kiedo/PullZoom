package com.example.apple.Custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by apple on 17/9/24.
 */
public class GalleryHorizontalScrollView extends HorizontalScrollView implements View.OnClickListener {

    final String TAG = this.getClass().getSimpleName();

    LinearLayout mContent;
    private BaseAdapter adapter;
    private int childWidth, childHeight, widthPixels, count, totalSize, fistVisibleIndex, lastVisibleIndex;

    public GalleryHorizontalScrollView(Context context) {
        this(context, null);
    }

    public GalleryHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GalleryHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContent = new LinearLayout(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mContent.setLayoutParams(layoutParams);
        mContent.setOrientation(LinearLayout.HORIZONTAL);
        addView(mContent);
        widthPixels = context.getResources().getDisplayMetrics().widthPixels;
    }


    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        mContent.removeAllViews();
        if (childWidth == 0 && childHeight == 0) {
            if (adapter != null) {
                View view = adapter.getView(0, null, null);
                mContent.addView(view);
                measureChild(view, MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                childHeight = view.getMeasuredHeight();
                childWidth = view.getMeasuredWidth();
                count = widthPixels / childWidth + 2;
                Log.e(TAG, "setAdapter: \n width = " + childWidth +
                        "  height = " + childHeight +
                        "  widthPixels = " + widthPixels +
                        "  count = " + count);
                fillScreenView();
            }
        }
        requestLayout();
    }

    private void fillScreenView() {
        mContent.removeAllViews();
        for (int i = 0; i < count; i++) {
            View view = adapter.getView(i, null, mContent);
            mContent.addView(view);
            lastVisibleIndex = i;
            view.setOnClickListener(this);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {

            case MotionEvent.ACTION_MOVE:

                float scrollX = getScrollX();
                if (scrollX > childWidth) {
                    Log.e(TAG, " childWidth   next ");
                    loadNext();
                } else if (scrollX == 0) {
                    loadPre();
                    Log.e(TAG, " childWidth   pre ");
                }

                break;
        }
        return super.onTouchEvent(ev);
    }

    private void loadNext() {

        if (lastVisibleIndex == adapter.getCount() - 1)
            return;
        View child = mContent.getChildAt(0);
        mContent.removeView(child);
        lastVisibleIndex++;
        fistVisibleIndex++;
        child = adapter.getView(lastVisibleIndex, child, null);
        mContent.addView(child);
        child.setOnClickListener(this);
        scrollTo(0, 0);
    }

    private void loadPre() {

        if (fistVisibleIndex <= 0) return;
        View child = mContent.getChildAt(lastVisibleIndex);
        mContent.removeView(child);
        lastVisibleIndex--;
        fistVisibleIndex--;
        View view = adapter.getView(fistVisibleIndex, child, null);
        mContent.addView(view, 0);
        view.setOnClickListener(this);
        scrollTo(childWidth, 0);

    }

    @Override
    public void onClick(View v) {

    }
}
