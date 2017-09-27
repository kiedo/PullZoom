package com.example.apple.Custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by apple on 17/9/16.
 */

public class LockItem extends View {

    final String TAG = this.getClass().getSimpleName();

    private Mode states = Mode.NOR;
    private int width, hight;
    private int outerCircleWidth = 2, outerCircleRadius, innerCircleRadius, centerXY;

    //paint
    private Paint mouerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);//去锯齿
    private Paint minnerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //color
    private int outerNorColor, innerNorColor;
    private int outerPressColor, innerPressColor;
    private int outerUpColor, innerUpColor;

    //Arrow
    private int mArrowline;
    private Path mArrowPath = new Path();
    private int angle = -1;//角度

    enum Mode {NOR, PRESS, UP}

    public LockItem(Context context) {
        this(context, null);
    }

    public LockItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LockItem(Context context, int outerNorColor, int innerNorColor, int outerPressColor, int innerPressColor, int outerUpColor, int innerUpColor) {
        this(context);
        this.outerNorColor = outerNorColor;
        this.innerNorColor = innerNorColor;
        this.outerPressColor = outerPressColor;
        this.innerPressColor = innerPressColor;
        this.outerUpColor = outerUpColor;
        this.innerUpColor = innerUpColor;
    }

    public int getCenterXY() {
        return centerXY;
    }


    private void init() {
        mouerCirclePaint.setColor(outerNorColor);
        mouerCirclePaint.setStyle(Paint.Style.STROKE);//空心画笔
        mouerCirclePaint.setStrokeWidth(outerCircleWidth);
        minnerCirclePaint.setStyle(Paint.Style.FILL);//实心
        minnerCirclePaint.setColor(innerNorColor);
        mArrowPath.reset();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wsize = MeasureSpec.getSize(widthMeasureSpec);
        int hsize = MeasureSpec.getSize(heightMeasureSpec);
        width = hight = Math.min(wsize, hsize);
        centerXY = width / 2;
        outerCircleRadius = (width - 2) / 2;
        innerCircleRadius = width / 6;
        setMeasuredDimension(width, hight);
        mArrowPath.reset();
        mArrowline = (int) (width * 1.0 / 2 * 0.3);
        //指引三角形箭头
        mArrowPath.moveTo(width / 2 - mArrowline, centerXY - innerCircleRadius - 4);
        mArrowPath.lineTo(width / 2 + mArrowline, centerXY - innerCircleRadius - 4);
        mArrowPath.lineTo(width / 2, (outerCircleRadius - innerCircleRadius) / 3);
        mArrowPath.close();
    }

    public LockItem setMode(Mode mode) {
        states = mode;
        return this;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (states == Mode.NOR) {
            mouerCirclePaint.setColor(outerNorColor);
            minnerCirclePaint.setColor(innerNorColor);
        } else if (states == Mode.PRESS) {
            mouerCirclePaint.setColor(outerPressColor);
            minnerCirclePaint.setColor(innerPressColor);
        } else if (states == Mode.UP) {
            mouerCirclePaint.setColor(outerUpColor);
            minnerCirclePaint.setColor(innerUpColor);
        }

        canvas.drawCircle(centerXY, centerXY, outerCircleRadius, mouerCirclePaint);
        canvas.drawCircle(centerXY, centerXY, innerCircleRadius, minnerCirclePaint);
        if (angle != -1) {
            canvas.rotate(angle, centerXY, centerXY);
            canvas.drawPath(mArrowPath, mouerCirclePaint);
        }
    }

    public LockItem setAngle(int angle) {
        this.angle = angle;
        return this;
    }


}
