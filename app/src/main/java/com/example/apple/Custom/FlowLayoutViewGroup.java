package com.example.apple.Custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by apple on 17/9/27.
 * 流式布局
 */

public class FlowLayoutViewGroup extends ViewGroup {

    final String TAG = this.getClass().getSimpleName();

    public FlowLayoutViewGroup(Context context) {
        this(context, null);
    }

    public FlowLayoutViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayoutViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int wmode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int hmode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int linew = 0, lineh = 0, temph = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();


            int marginTop = layoutParams.topMargin;
            int marginLeft = layoutParams.leftMargin;
            int marginRight = layoutParams.rightMargin;
            int marginBottom = layoutParams.bottomMargin;

            int childw = child.getMeasuredWidth() + marginLeft + marginRight;
            int childh = child.getMeasuredHeight() + marginBottom + marginTop;

            if (linew + childw > width) {
                lineh += temph;
                linew = childw;
                temph = childh;
            } else {
                linew += childw;
                temph = Math.max(childh, temph);
            }
            if (i == childCount - 1) {
                lineh += temph;
            }

        }
        setMeasuredDimension(width, hmode == MeasureSpec.EXACTLY ? height : lineh);


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int childCount = getChildCount();
            int width = getMeasuredWidth();
            int linew = 0, lineh = 0, temph = 0;
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();

                int marginTop = layoutParams.topMargin;
                int marginLeft = layoutParams.leftMargin;
                int marginRight = layoutParams.rightMargin;
                int marginBottom = layoutParams.bottomMargin;

                int childw = child.getMeasuredWidth();
                int childh = child.getMeasuredHeight();

                if (linew + childw + marginLeft + marginRight > width) {
                    lineh += temph;
                    child.layout(marginLeft, lineh + marginTop, childw + marginRight, lineh + childh + marginBottom);
                    linew = marginLeft + marginRight + childw;

                } else {

                    child.layout(linew + marginLeft, lineh + marginTop, linew + childw + marginRight, lineh + childh + marginBottom);
                    linew += (childw + marginLeft + marginRight);
                    temph = Math.max(childh + marginTop + marginBottom, temph);
                }


            }
        }
    }
}
