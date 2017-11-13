package com.example.apple.Custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author apple
 * @date 17/10/30
 */

public class CircleMenuLayout extends ViewGroup {


    private int mRadiu;

    public CircleMenuLayout(Context context) {
        this(context, null);
    }

    public CircleMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int modew = MeasureSpec.getMode(widthMeasureSpec);
        int sizew = MeasureSpec.getSize(widthMeasureSpec);


        int sizeh = MeasureSpec.getSize(heightMeasureSpec);
        int modeh = MeasureSpec.getMode(widthMeasureSpec);

        if (modew != MeasureSpec.EXACTLY) {
            sizew = sizeh = Math.min(sizew, sizeh);
        }
        setMeasuredDimension(sizew, sizeh);
        mRadiu = Math.min(sizew, sizeh) / 2;
        int childW = mRadiu/3;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            int cw = MeasureSpec.makeMeasureSpec(childW, MeasureSpec.EXACTLY);
            childAt.measure(cw,cw);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

    }
}
