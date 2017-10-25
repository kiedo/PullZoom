package com.example.apple.Custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apple.pullzoom.R;


/**
 * @author
 */
public class TabIndicator2 extends HorizontalScrollView implements OnPageChangeListener {

    final String TAG = this.getClass().getSimpleName();

    private LayoutInflater mLayoutInflater;
    private ViewPager pager;
    /**
     * item容器
     */
    private LinearLayout tabsContent;

    /**
     * item数量
     */
    private int tabCount, scrollOffset, currentitem;


    /**
     * 滑块区域
     */
    private Rect indicatorRect;
    private Paint textPait;


    /**
     * 滑块Drawable
     */
    private Drawable indicatorBG;
    private OnPageChangeListener listener;
    private int itemLayout;


    public TabIndicator2(Context context) {
        this(context, null);
    }

    public TabIndicator2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabIndicator2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFillViewport(true);

        mLayoutInflater = LayoutInflater.from(context);
        indicatorRect = new Rect();
        textPait = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPait.setColor(Color.WHITE);

        tabsContent = new LinearLayout(context);
        tabsContent.setOrientation(LinearLayout.HORIZONTAL);
        addView(tabsContent, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        DisplayMetrics dm = getResources().getDisplayMetrics();
        scrollOffset = dm.widthPixels;
        itemLayout = R.layout.news_tab;

        indicatorBG = getResources().getDrawable(R.drawable.bg_indicator, context.getTheme());
    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter");
        }
        pager.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        tabsContent.removeAllViews();
        tabCount = pager.getAdapter().getCount();
        for (int i = 0; i < tabCount; i++) {
            addTab(i, pager.getAdapter().getPageTitle(i).toString());
        }

        changeStats();
    }

    private void addTab(final int position, String title) {
        ViewGroup tab = (ViewGroup) mLayoutInflater.inflate(itemLayout, this, false);
        TextView newsText = (TextView) tab.getChildAt(0);
        newsText.setText(title);
        newsText.setGravity(Gravity.CENTER);
        newsText.setTextColor(Color.GRAY);
        newsText.setSingleLine();
        textPait.setTextSize(newsText.getTextSize());
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });
        tabsContent.addView(tab);
    }

    private void changeStats() {
        for (int i = 0; i < tabCount; i++) {
            ViewGroup childAt = (ViewGroup) tabsContent.getChildAt(i);
            TextView newsText = (TextView) childAt.getChildAt(0);
            if (i == currentitem) {
                newsText.setBackground(indicatorBG);
            } else {
                newsText.setBackground(null);
            }
        }
        scrolltoChild();
    }

    private void scrolltoChild() {
        ViewGroup checkView = (ViewGroup) tabsContent.getChildAt(currentitem);
        if (checkView == null) {
            return;
        }
        int offset =  scrollOffset / 2 - checkView.getWidth() / 2;
        smoothScrollTo(checkView.getLeft() - offset, 0);

    }


    /**
     * @param position             当前item
     * @param positionOffset       0和1区间变化
     * @param positionOffsetPixels 像素偏移量
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (listener != null) {
            listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }


    @Override
    public void onPageScrollStateChanged(int state) {
        if (listener != null) {
            listener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (listener != null) {
            listener.onPageSelected(position);
        }
        currentitem = position;
        changeStats();

    }

    /**
     * 设置viewpage滑动监听
     *
     * @param listener
     */
    public void addPageListener(OnPageChangeListener listener) {
        this.listener = listener;
    }

    public void setItemLayout(int lay) {
        itemLayout = lay;
    }


}
