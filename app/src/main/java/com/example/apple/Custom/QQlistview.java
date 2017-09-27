package com.example.apple.Custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.apple.pullzoom.R;

import java.io.BufferedReader;

/**
 * Created by apple on 17/9/15.
 */

public class QQlistview extends ListView {

    private OnClickListener flagListenner;

    public QQlistview(Context context) {
        super(context);
        init();
    }

    public QQlistview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QQlistview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void setFlagListenner(OnClickListener flagListenner) {
        this.flagListenner = flagListenner;
    }

    PopupWindow pp;

    private void init() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.qq_item, null);
        inflate.findViewById(R.id.btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pp != null && pp.isShowing()) {
                    pp.dismiss();
                }
                flagListenner.onClick(v);
            }
        });

        pp = new PopupWindow(inflate, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        pp.getContentView().measure(0, 0);

    }

    int currentItem = -1;
    int x = 0;
    int y = 0;
    boolean isSliding;
    View childAt;

    int[] lacation = new int[2];


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isSliding;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                if (pp != null && pp.isShowing()) {
                    pp.dismiss();
                    return false;
                }
                x = (int) ev.getX();
                y = (int) ev.getY();
                currentItem = pointToPosition(x, y);
                if (currentItem != -1) {
                    childAt = getChildAt(currentItem - getFirstVisiblePosition());
                    childAt.getLocationOnScreen(lacation);
                }


                break;
            case MotionEvent.ACTION_MOVE:
                int tx = (int) ev.getX();
                int ty = (int) ev.getY();

                if (ev.getX() < x && Math.abs(tx - x) > Math.abs(ty - y) && Math.abs(tx - x) > 20) {
                    isSliding = true;

                }

                break;

            case MotionEvent.ACTION_UP:
                isSliding = false;

                break;
        }


        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (isSliding && childAt != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    if (isSliding && childAt != null) {
                        childAt.getLocationOnScreen(lacation);
                        pp.update();
                        pp.showAtLocation(childAt,
                                Gravity.LEFT | Gravity.TOP,
                                lacation[0] + childAt.getWidth(),
                                lacation[1] + (childAt.getHeight() - pp.getContentView().getMeasuredHeight()) / 2);

                    }

                    break;
            }
            return true;
        }
        return true;
    }
}
