package com.example.apple.Custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.example.apple.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;


/**
 * Created by apple on 17/9/21.
 */

public class PlanetViewGroup extends ViewGroup implements View.OnClickListener {

    final String TAG = this.getClass().getSimpleName();

    CustomLayout mlayout = CustomLayout.left_top;
    MenuStats menuStats = MenuStats.open;
    private int count = 0;
    View centerView;
    MenuOnClickListener lis;

    int cl, ct, cr, cb, childWidth, childHeight;
    /**
     * 半径
     */
    private int radius = 100;
    private double angle;


    public enum CustomLayout {
        left_top, right_top, right_bottom, left_bottom, bottom_center
    }

    public enum MenuStats {
        open, close;
    }

    int measuredHeight, measuredWidth;

    public PlanetViewGroup(Context context) {
        this(context, null);
    }

    public PlanetViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlanetViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PlanetViewGroup);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = typedArray.getIndex(i);
            switch (index) {

                case R.styleable.PlanetViewGroup_radius:
                    radius = typedArray.getDimensionPixelSize(index, 50);
                    break;

                case R.styleable.PlanetViewGroup_MenuStats:
                    int anInt = typedArray.getInt(index, 0);
                    switch (anInt) {
                        case 0:
                            menuStats = MenuStats.open;
                            break;
                        case 1:
                            menuStats = MenuStats.close;
                            break;
                    }
                    break;

                case R.styleable.PlanetViewGroup_CustomLayout:
                    anInt = typedArray.getInt(index, 0);
                    switch (anInt) {
                        case 0:
                            mlayout = CustomLayout.left_top;
                            break;
                        case 1:
                            mlayout = CustomLayout.right_top;
                            break;

                        case 2:
                            mlayout = CustomLayout.right_bottom;
                            break;
                        case 3:
                            mlayout = CustomLayout.left_bottom;
                            break;
                        case 4:
                            mlayout = CustomLayout.bottom_center;
                            break;

                    }


                    break;
            }

        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        measuredHeight = getMeasuredHeight();
        measuredWidth = getMeasuredWidth();
        angle = Math.PI / (2 * (count - 2));
        radius = Math.min(radius, measuredWidth / 2);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!changed) return;
        if (count < 3) return;//里面控件太少，菜单显示没有意思
        //最后一个view作为主控件view
        centerView = getChildAt(count - 1);
        controllerView();
        subViewLayout(false);

    }

    /**
     * 计算子view坐标
     *
     * @param showAnim
     */
    private void subViewLayout(boolean showAnim) {
        View child;
        for (int i = 0; i < count - 1; i++) {
            child = getChildAt(i);
            if (!showAnim) {
                if (menuStats == MenuStats.close) {
                    child.setVisibility(GONE);
                } else {
                    child.setVisibility(VISIBLE);
                }
            }
            final int index = i + 1;
            if (lis != null) {
                child.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lis != null) {
                            lis.menuItem(index);
                        }
                        menuStats = MenuStats.close;
                        viewVisibleStats(GONE);
                        alphaAndScale(getChildAt(index - 1));
                    }
                });
            }
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();
            if (mlayout == CustomLayout.left_top) {
                cl = (int) (radius * Math.sin((i) * angle));
                ct = (int) (radius * Math.cos((i) * angle));
            } else if (mlayout == CustomLayout.right_top) {
                cl = measuredWidth - childWidth - (int) (radius * Math.sin((i) * angle));
                ct = (int) (radius * Math.cos((i) * angle));

            } else if (mlayout == CustomLayout.right_bottom) {
                cl = measuredWidth - childWidth - (int) (radius * Math.sin((i) * angle));
                ct = measuredHeight - childHeight - (int) (radius * Math.cos((i) * angle));

            } else if (mlayout == CustomLayout.left_bottom) {
                cl = (int) (radius * Math.sin((i) * angle));
                ct = measuredHeight - childHeight - (int) (radius * Math.cos((i) * angle));

            } else if (mlayout == CustomLayout.bottom_center) {
                cl = (measuredWidth - childWidth) / 2 + (int) (radius * Math.cos((i) * (Math.PI / (count - 2))));
                ct = measuredHeight - childHeight - (int) (radius * Math.sin((i) * (Math.PI / (count - 2))));
            }
            if (showAnim) {
                if (menuStats == MenuStats.close) {
                    hidePlanetMenu(child, index);
                } else {
                    showPlanetMenu(child, index);
                }
            }
            cr = cl + childWidth;
            cb = ct + childHeight;
            child.layout(cl, ct, cr, cb);
        }
    }


    /**
     * 对view缩放和透明度变化
     *
     * @param child
     */
    private void alphaAndScale(final View child) {
        child.setVisibility(VISIBLE);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(child, "alpha", 1, 0);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(child, "scaleX", 1, 2);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(child, "scaleY", 1, 2);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.play(alpha).with(scaleX).with(scaleY);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                child.setScaleX(1);
                child.setScaleY(1);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();

    }


    /***
     * 子菜单动画
     */
    private void showPlanetMenu(View child, int index) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(child, "y", centerView.getTop(), ct);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(child, "x", centerView.getLeft(), cl);
        ObjectAnimator rotation = ObjectAnimator.ofFloat(child, "rotation", 0, 360);
        translationY.setInterpolator(new OvershootInterpolator());
        translationX.setInterpolator(new OvershootInterpolator());
        AnimatorSet set = new AnimatorSet();
        set.playTogether(translationY, translationX, rotation);
        set.setDuration(300 + index * 100);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                viewVisibleStats(VISIBLE);
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
        set.start();
    }

    /***
     * 子菜单动画
     */
    private void hidePlanetMenu(View child, int index) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(child, "y", ct, centerView.getTop());
        ObjectAnimator translationX = ObjectAnimator.ofFloat(child, "x", cl, centerView.getLeft());
        ObjectAnimator rotation = ObjectAnimator.ofFloat(child, "rotation", 0, 360);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(300 + index * 100);
        set.setInterpolator(new OvershootInterpolator());
        set.playTogether(translationY, translationX, rotation);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                viewVisibleStats(GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }

    /**
     * 主菜单控制按钮
     */
    private void controllerView() {
        MarginLayoutParams marginLayoutParams = null;
        LayoutParams layoutParams = centerView.getLayoutParams();
        if(layoutParams instanceof MarginLayoutParams){
            marginLayoutParams = (MarginLayoutParams)layoutParams;
        }
        childWidth = centerView.getMeasuredWidth();
        childHeight = centerView.getMeasuredHeight();
        if (mlayout == CustomLayout.left_top) {
            cl = 0;
            ct = 0;
            cr = childWidth;
            cb = childHeight;
        } else if (mlayout == CustomLayout.right_top) {
            cl = measuredWidth - childWidth;
            ct = 0;
            cr = measuredWidth;
            cb = childHeight;
        } else if (mlayout == CustomLayout.right_bottom) {
            cl = measuredWidth - childWidth;
            ct = measuredHeight - childHeight;
            cr = measuredWidth;
            cb = measuredHeight;
        } else if (mlayout == CustomLayout.left_bottom) {
            cl = 0;
            ct = measuredHeight - childHeight;
            cr = childWidth;
            cb = measuredHeight;
        } else if (mlayout == CustomLayout.bottom_center) {
            cl = (measuredWidth - childWidth) / 2;
            ct = measuredHeight - childHeight;
            cr = cl + childWidth;
            cb = measuredHeight;
        }
        if(marginLayoutParams == null){
            centerView.layout(cl, ct , cr , cb );
        }else{
            centerView.layout(cl + marginLayoutParams.leftMargin, ct + marginLayoutParams.topMargin, cr + marginLayoutParams.rightMargin, cb + marginLayoutParams.bottomMargin);
        }
        centerView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        ObjectAnimator rotation = ObjectAnimator.ofFloat(v, "rotation", 0, 360).setDuration(500);
        rotation.setDuration(500);
        rotation.start();
        if (menuStats == MenuStats.open) {
            menuStats = MenuStats.close;
            subViewLayout(true);
        } else {
            menuStats = MenuStats.open;
            subViewLayout(true);
        }
    }

    /**
     * 子view是否可见
     *
     * @param visible
     */
    private void viewVisibleStats(int visible) {
        for (int i = 0; i < count - 1; i++) {
            getChildAt(i).setVisibility(visible);
            getChildAt(i).setAlpha(1);
        }
    }

    public void setMenuOnClickListener(MenuOnClickListener lis) {
        this.lis = lis;
    }


    public interface MenuOnClickListener {
        void menuItem(int pos);
    }

}
