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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apple.R;


/**
 * @author
 */
public class TabIndicator extends HorizontalScrollView implements OnPageChangeListener {

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
    private int tabCount;

    /**
     * 当前显示item
     */
    private int currentPosition = 0;

    /**
     * 偏移量
     */
    private float currentPositionOffset = 0f;

    /**
     * 滑块区域
     */
    private Rect indicatorRect;
    private Paint textPait;


    /**
     * 滑块停留屏幕偏移量
     */
    private int scrollOffset = 20;
    private int offset = 1;
    private int lastScrollX = 0;

    /**
     * 滑块Drawable
     */
    private Drawable indicatorBG;
    private OnPageChangeListener listener;
    private int itemLayout;


    public TabIndicator(Context context) {
        this(context, null);
    }

    public TabIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabIndicator(Context context, AttributeSet attrs, int defStyle) {
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
        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        offset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, offset, dm);
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


    /**
     * 滑块的区域Rect
     */
    private void initIndicatorRect() {

        ViewGroup currentTab = (ViewGroup) tabsContent.getChildAt(currentPosition);
        TextView newsText = (TextView) currentTab.findViewById(R.id.news_text);

        float left = currentTab.getLeft() + newsText.getLeft();
        float right = newsText.getWidth() + left;

        if (currentPositionOffset > 0 && currentPosition < tabCount - 1) {
            ViewGroup nextTab = (ViewGroup) tabsContent.getChildAt(currentPosition + 1);
            TextView nextNewsText = (TextView) nextTab.findViewById(R.id.news_text);
            float nextLeft = nextTab.getLeft() + nextNewsText.getLeft();
            left = left + (nextLeft - left) * currentPositionOffset;
            right = left + newsText.getWidth() * (1.0f - currentPositionOffset) + currentPositionOffset * nextNewsText.getWidth();
        }

        indicatorRect.set(
                ((int) left) + getPaddingLeft(),
                getPaddingTop() + currentTab.getTop() + newsText.getTop(),
                (int) (right + getPaddingLeft()),
                newsText.getHeight() + currentTab.getTop() + getPaddingTop() + newsText.getTop());

    }


    private void scrollToItem() {

        if (tabCount == 0) {
            return;
        }
        initIndicatorRect();
        int newScrollX = lastScrollX;
        if (indicatorRect.left < getScrollX() + scrollOffset) {
            newScrollX = indicatorRect.left - scrollOffset;

        } else if (indicatorRect.right > getScrollX() + getWidth() - scrollOffset) {
            newScrollX = indicatorRect.right - getWidth() + scrollOffset;

        }
        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if(tabCount == 0){
            return;
        }
        initIndicatorRect();
        /**
         * 滑块绘制
         */
        if (indicatorBG != null) {
            indicatorBG.setBounds(indicatorRect);
            indicatorBG.draw(canvas);
        }
        for (int i = 0; i < tabCount; i++) {
            ViewGroup tab = (ViewGroup) tabsContent.getChildAt(i);
            TextView newsText = (TextView) tab.findViewById(R.id.news_text);
            if (newsText != null) {
                initIndicatorRect();
                canvas.clipRect(indicatorRect);
                canvas.drawText(newsText.getText().toString(),
                        tab.getLeft() + newsText.getLeft() + newsText.getPaddingLeft(),
                        getPaddingTop() + tab.getTop() + newsText.getTop() + newsText.getPaddingTop() + newsText.getMeasuredHeight() / 2 + offset,
                        textPait);
            }
        }

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
        currentPosition = position;
        currentPositionOffset = positionOffset;
        scrollToItem();
        invalidate();

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
