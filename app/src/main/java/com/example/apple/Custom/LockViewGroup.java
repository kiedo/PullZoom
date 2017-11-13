package com.example.apple.Custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apple.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 17/9/16.
 */

public class LockViewGroup extends ViewGroup {

    final String TAG = this.getClass().getSimpleName();

    //密码元素的宽高
    private int lockItemWidth;
    //元素的个数
    private int count = 3;
    //装item容器
    private LockItem[] lockItems;
    //每一个item的间隔
    private int spacingWidth;
    private int width, hight;
    private List<Integer> selectLockViews = new ArrayList<>();
    private Integer[] pwd = new Integer[]{1, 4, 7, 8, 9};

    private int donwx, downy, scaledTouchSlop;
    //color
    private int outerNorColor = Color.RED, innerNorColor = Color.RED;
    private int outerPressColor = Color.BLUE, innerPressColor = Color.BLUE;
    private int outerUpColor = Color.YELLOW, innerUpColor = Color.YELLOW;
    private int mlineColor = Color.BLACK;

    private Paint mlinePait = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mlinePath = new Path();

    private int tempx, tempy;
    private LockItem lastView;
    private int pathCenterX, pathCenterY, lastPointX, lastPointY;

    public LockViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mlinePait.setColor(mlineColor);
        mlinePait.setStrokeWidth(30);
        mlinePait.setAlpha(150);
        mlinePait.setStyle(Paint.Style.STROKE);
        mlinePait.setStrokeCap(Paint.Cap.ROUND);
        mlinePait.setStrokeJoin(Paint.Join.ROUND);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LockViewGroup);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            switch (typedArray.getIndex(i)) {
                case R.styleable.LockViewGroup_count:
                    count = typedArray.getInt(typedArray.getIndex(i), 3);
                    break;
                case R.styleable.LockViewGroup_outerNorColor:
                    outerNorColor = typedArray.getColor(i, Color.RED);
                    break;
                case R.styleable.LockViewGroup_innerNorColor:
                    innerNorColor = typedArray.getColor(typedArray.getIndex(i), Color.RED);
                    break;
                case R.styleable.LockViewGroup_outerPressColor:
                    outerPressColor = typedArray.getColor(i, Color.BLUE);

                    break;
                case R.styleable.LockViewGroup_innerPressColor:
                    innerPressColor = typedArray.getColor(i, Color.BLUE);
                    break;
                case R.styleable.LockViewGroup_outerUpColor:
                    outerUpColor = typedArray.getColor(i, Color.YELLOW);
                    break;
                case R.styleable.LockViewGroup_innerUpColor:
                    mlineColor = typedArray.getColor(i, Color.YELLOW);
                    break;
                case R.styleable.LockViewGroup_mlineColor:
                    innerUpColor = typedArray.getColor(i, Color.YELLOW);
                    break;

                case R.styleable.LockViewGroup_width:
                    int w = (int) typedArray.getDimension(typedArray.getIndex(i), 50);
                    break;

                default:

            }

        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wsize = MeasureSpec.getSize(widthMeasureSpec);
        int hsize = MeasureSpec.getSize(heightMeasureSpec);
        width = hight = Math.min(wsize, hsize);
        lockItemWidth = (2 * width) / (3 * count + 1);// width / (count + (count + 1) / 2);间隔是item宽度的一半
        spacingWidth = lockItemWidth / 2;
        if (lockItems == null) {
            lockItems = new LockItem[count * count];
            removeAllViews();
            for (int i = 0, j = count * count; i < j; i++) {
                LockItem lockItem = new LockItem(getContext(), outerNorColor, innerNorColor, outerPressColor, innerPressColor, outerUpColor, innerUpColor);
                LayoutParams layoutParams = new LayoutParams(lockItemWidth, lockItemWidth);
                lockItem.setLayoutParams(layoutParams);
                lockItem.setTag(j - i);
                lockItems[i] = lockItem;
                addView(lockItem);
                measureChild(lockItem, lockItemWidth, lockItemWidth);
            }
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int mChildCount = getChildCount();
        long num = (long) Math.sqrt(mChildCount);
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                View childAt = getChildAt(--mChildCount);
                childAt.layout(
                        spacingWidth * (j + 1) + j * lockItemWidth,
                        spacingWidth * (i + 1) + i * lockItemWidth,
                        lockItemWidth * (j + 1) + spacingWidth * (j + 1),
                        lockItemWidth * (i + 1) + spacingWidth * (i + 1));
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawPath(mlinePath, mlinePait);
        if (pathCenterX > 0 && pathCenterY > 0) {
            canvas.drawLine(pathCenterX, pathCenterY, lastPointX, lastPointY, mlinePait);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        tempx = (int) event.getX();
        tempy = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                reset(tempx, tempy);
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = downy - tempy;
                int dx = donwx - tempx;
                if (Math.abs(dy) > scaledTouchSlop || Math.abs(dx) > scaledTouchSlop) {

                    donwx = tempx;
                    downy = tempy;
                    LockItem child = getChildByXY(tempx, tempy);
                    if (child != null) {
                        if (!selectLockViews.contains(child.getTag())) {
                            selectLockViews.add((Integer) child.getTag());
                            child.setMode(LockItem.Mode.PRESS);
                            pathCenterX = (child.getLeft() + child.getRight()) / 2;
                            pathCenterY = (child.getTop() + child.getBottom()) / 2;
                            if (selectLockViews.size() == 1) {
                                mlinePath.moveTo(pathCenterX, pathCenterY);
                            } else {
                                mlinePath.lineTo(pathCenterX, pathCenterY);
                            }
                            //箭头直接直接指向某一个小圆的圆心
                            changeArraw(child.getLeft() + child.getCenterXY(), child.getTop() + child.getCenterXY());
                            lastView = child;
                            child.invalidate();
                        }
                    }
                }
                //箭头随着手指改变方向
                changeArraw(tempx, tempy);
                lastPointX = tempx;
                lastPointY = tempy;
                break;
            case MotionEvent.ACTION_UP:
                lastView = null;
                pathCenterX = lastPointX;
                pathCenterY = lastPointY;
                if (selectLockViews.size() == 0) return true;
                if (selectLockViews.size() < 4) {
                    Toast.makeText(getContext(), "密码不能少于4个", Toast.LENGTH_LONG).show();
                    reset(tempx, tempy);
                    return true;
                }
                if (checkPwd()) {
                    Toast.makeText(getContext(), "亲，密码正确", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "密码错误", Toast.LENGTH_LONG).show();
                }
                changeMode();
                this.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        reset(tempx, tempy);
                    }
                }, 200);
                break;

            default:
        }
        invalidate();
        return true;
    }

    private void changeArraw(int x, int y) {
        if (lastView != null) {
            double v = getAngle(lastView.getLeft() + lastView.getCenterXY(), lastView.getTop() + lastView.getCenterXY(), x, y);
            lastView.setAngle((int) v);
            lastView.invalidate();
        }
    }

    /**
     * up的时候改变状态，更新view，用户可以根据自己的需要增加几个状态，如：密码错误和密码正确的颜色样式
     */
    private void changeMode() {
        if (lockItems == null) return;
        for (int i = 0; i < lockItems.length; i++) {
            lockItems[i].setMode(LockItem.Mode.UP).invalidate();
            if (lockItems[i].getTag() == selectLockViews.get(selectLockViews.size() - 1)) {
                //去掉最后一个箭头
                lockItems[i].setAngle(-1).invalidate();
            }
        }
    }

    /**
     * 检查密码是否正确
     *
     * @return
     */
    private boolean checkPwd() {
        if (selectLockViews.size() != pwd.length) return false;
        for (int i = 0; i < pwd.length; i++) {
            if (selectLockViews.get(i) != pwd[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 重置，将各种状态复原。
     *
     * @param x
     * @param y
     */
    private void reset(int x, int y) {
        selectLockViews.clear();
        mlinePath.reset();
        donwx = x;
        downy = y;
        pathCenterX = lastPointX = 0;
        pathCenterY = lastPointY = 0;
        for (int i = 0; i < lockItems.length; i++) {
            lockItems[i].setMode(LockItem.Mode.NOR).setAngle(-1).invalidate();
        }
        invalidate();
    }

    /**
     * 通过手指的坐标去检查当前点在哪一个子view上面
     *
     * @param x
     * @param y
     * @return
     */
    private LockItem getChildByXY(int x, int y) {
        if (lockItems == null) return null;
        for (int i = 0; i < lockItems.length; i++) {
            LockItem lockItem = lockItems[i];
            if (positionInView(lockItem, x, y)) {
                return lockItem;
            }
        }
        return null;
    }

    /**
     * 通过view的边界检查x。y是否在内部
     *
     * @param lockItem
     * @param x
     * @param y
     * @return
     */
    private boolean positionInView(LockItem lockItem, int x, int y) {
        if (x > lockItem.getLeft() && x < lockItem.getRight() && y > lockItem.getTop() && y < lockItem.getBottom()) {
            return true;
        }
        return false;
    }

    /**
     * 这个就是根据两个点坐标计算角度，通知箭头的方向。
     *
     * @param px1
     * @param py1
     * @param px2
     * @param py2
     * @return
     */
    double getAngle(int px1, int py1, int px2, int py2) {
        //两点的x、y值
        int x = px2 - px1;
        int y = py2 - py1;
        //斜边长度
        double hypotenuse = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        double sin = x / hypotenuse;
        double radian = Math.asin(sin);
        //求出弧度
        double angle = 180 / (Math.PI / radian);
        Log.e(TAG, "sin " + sin + " radian " + radian + "  angle " + angle);
        //用弧度算出角度
        if (y > 0) {
            angle = 180 - angle;
        }
        return angle;
    }
}
