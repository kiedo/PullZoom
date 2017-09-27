package com.example.apple.pullzoom.reflash;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.apple.pullzoom.R;


public class MyScrllerView extends ScrollView {


    int state = 0;
    final int NONE = 0;//正常状态
    final int PULL = 1;//提示下拉状态
    final int RELESE = 2;//提示释放状态
    final int REFLASHING = 3;//正在刷新状态

    private TextView textView;
    private View headerView;
    int measureHight;
    private TextView tv_header;


    public MyScrllerView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public MyScrllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrllerView(Context context) {
        this(context, null);
    }


    public void setHeader() {
        headerView = findViewById(R.id.headerview);
        measureView(headerView);
        textView = (TextView) findViewById(R.id.tv_header);
        measureHight = headerView.getMeasuredHeight();
        headerView.setPadding(0, -measureHight, 0, 0);
        headerView.invalidate();
        tv_header = (TextView) findViewById(R.id.tv_header);
        Log.e("AAAAA", "onTouchEvent: sswwwwwwwww " + headerView.getMeasuredHeight());

    }

    private void measureView(View headerView) {
        ViewGroup.LayoutParams p = headerView.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int height;
        int tempHeight = p.height;
        if (tempHeight > 0) {
            height = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }

        headerView.measure(width, height);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        View chiled = this.getChildAt(0);

        Log.e("AAAA", l + "  -----  " + t + "  -----  " + oldl + "  -----  " + oldt + "  -- " + (getScrollY() + this.getHeight()) + " chiled " + chiled.getHeight());

    }

    private boolean isTop() {
        return getScrollY() == 0;
    }

    int starty;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (getScrollY() != 0) {
            Log.e("AAAAA", "onTouchEvent: ");
            return super.onTouchEvent(ev);
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                starty = (int) ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:

                move(ev);


                break;

            case MotionEvent.ACTION_UP:


                break;
        }


        return super.onTouchEvent(ev);
    }


    private void move(MotionEvent ev) {

        if (!isTop()) {
            Log.e("AAAAA", "onTouchEvent:  111");
            return;
        }

        int tempY = (int) ev.getY();
        int space = tempY - starty;
        space = space / 2;
        int topPadding = space - measureHight;
        Log.e("AAAAA", "onTouchEvent:  space " + space + " topPadding " + topPadding + " tempY " + tempY);
        switch (state) {
            case NONE:
                if (space > 0) {
                    state = PULL;
                    //headerView.setPadding(0, topPadding, 0, 0);
                    tv_header.setText("下来加载！！");
                }
                headerView.setPadding(0, topPadding, 0, 0);

                break;

            case PULL:
                tv_header.setText("下来加载！！");
                if (space > measureHight + 50) {
                    state = RELESE;
                    tv_header.setText("松开加载！！");
                }
                headerView.setPadding(0, topPadding, 0, 0);

                break;
            case RELESE:
                if (space < measureHight + 50) {
                    tv_header.setText("下来加载！！");
                    state = PULL;
                }
                headerView.setPadding(0, topPadding, 0, 0);

                break;
            case REFLASHING:


                break;

        }
//        if (topPadding > 0) {
//
//            headerView.setPadding(0, topPadding, 0, 0);
//        }


    }


}
