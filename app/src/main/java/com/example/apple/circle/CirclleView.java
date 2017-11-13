package com.example.apple.circle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.apple.R;

/**
 * Created by apple on 17/9/11.
 */

public class CirclleView extends View {

    int fistColor, secondeColor, speed;
    float circleWidth;

    public CirclleView(Context context) {
        this(context, null);


    }

    public CirclleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        setBackgroundColor(Color.GRAY);
    }


    public CirclleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CirclleView, defStyleAttr, 0);

        int indexCount = a.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = a.getIndex(i);
            switch (index) {
                case R.styleable.CirclleView_fistColor:
                    fistColor = a.getColor(index, Color.RED);
                    break;
                case R.styleable.CirclleView_secondeColor:
                    secondeColor = a.getColor(index, Color.RED);
                    break;
                case R.styleable.CirclleView_speed:
                    speed = a.getInt(index, 50);
                    break;
                case R.styleable.CirclleView_circleWidth:
                    circleWidth = a.getDimension(index, 100);
                    break;

            }
        }


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int Wmode = MeasureSpec.getMode(widthMeasureSpec);
        int Wsize = MeasureSpec.getSize(widthMeasureSpec);

        int Hmode = MeasureSpec.getMode(heightMeasureSpec);
        int Hsize = MeasureSpec.getSize(heightMeasureSpec);
        Log.e("onMeasure", "onMeasure: " + Wsize);
        if (Wmode == MeasureSpec.AT_MOST && Hmode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(200, 200);
        }
        if (Wmode == MeasureSpec.AT_MOST && Hmode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(200, Hsize);
        }
        if (Wmode == MeasureSpec.EXACTLY && Hmode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(Wsize, 200);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



    }
}