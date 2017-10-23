package com.example.apple.Custom;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * @author Created by apple
 */

public class ViewPagerIndicator extends HorizontalScrollView implements ViewPager.OnPageChangeListener, View.OnClickListener {


    private final int DEFOULT_COLOR = Color.RED;
    private final String TAG = this.getClass().getSimpleName();

    private ViewPager viewPager;
    LinearLayout linearLayoutContent;
    ViewPager.OnPageChangeListener listener;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, @Nullable AttributeSet attrs) {
        setHorizontalScrollBarEnabled(false);
        setFillViewport(true);
        linearLayoutContent = new LinearLayout(context);
        linearLayoutContent.setOrientation(LinearLayout.HORIZONTAL);
        this.addView(linearLayoutContent, new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
    }

    public void setViewPager(ViewPager viewPager) {
        setViewPager(viewPager, 0);
    }

    public void setViewPager(ViewPager viewPager, int item) {
        if (viewPager == null) {
            return;
        }
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
        PagerAdapter adapter = viewPager.getAdapter();
        if (adapter != null) {
            linearLayoutContent.removeAllViews();
            int count = adapter.getCount();
            for (int i = 0; i < count; i++) {
                CharSequence pageTitle = adapter.getPageTitle(i);
                linearLayoutContent.addView(addNewsTab(i, pageTitle));
            }

        }
        setCurrent(item);
        requestLayout();
    }

    /**
     * 添加每一个tab
     *
     * @param i
     * @param pageTitle
     */
    private View addNewsTab(int i, CharSequence pageTitle) {
        TextView title = new TextView(getContext());
        title.setText(pageTitle);
        title.setTag(i);
        title.setGravity(Gravity.CENTER);
        title.setPadding(20, 0, 20, 0);
        title.setOnClickListener(this);
        title.setTextSize(20);
        title.setTextColor(DEFOULT_COLOR);
        addLayout(title);
        return title;
    }

    private void setCurrent(int item) {
        int childCount = linearLayoutContent.getChildCount();
        if (item <= childCount - 1) {
            for (int i = 0; i < childCount; i++) {
                View child = linearLayoutContent.getChildAt(i);
                if (item == i) {
                    animation(child, 1f, 1f);
                    scrolltotab(item);
                } else {
                    animation(child, 0.8f, 0.5f);
                }
            }
        }
    }

    private void animation(View view, float scaleXY, float alpha) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", scaleXY);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", scaleXY);
        ObjectAnimator fade = ObjectAnimator.ofFloat(view, "alpha", alpha);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scaleX).with(scaleY).with(fade);
        animSet.setDuration(200);
        animSet.start();
    }

    private void scrolltotab(int item) {
        View child = linearLayoutContent.getChildAt(item);
        final int scrollPos = child.getLeft() - (getWidth() - child.getWidth()) / 2;
        smoothScrollTo(scrollPos, 0);
    }

    private void addLayout(TextView textView) {
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        textView.setLayoutParams(layoutParams);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (listener != null) {
            listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        setCurrent(position);
        if (listener != null) {
            listener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (listener != null) {
            listener.onPageScrollStateChanged(state);
        }
    }


    /**
     * tab 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        Integer po = (Integer) tag;
        setCurrent(po);
        if (viewPager != null) {
            viewPager.setCurrentItem(po);
        }
    }
    public void setPageChageListener(ViewPager.OnPageChangeListener listener) {
        this.listener = listener;
    }

}
