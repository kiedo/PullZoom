package com.example.apple.Custom.Matrix;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by apple on 17/10/13.
 */

public class ZoomView extends AppCompatImageView implements ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {

    final String TAG = this.getClass().getSimpleName();

    private float drawablew, drawableh, initScale;
    private float[] values = new float[9];
    private RectF mRectF = new RectF();

    private Matrix matrix;
    private boolean once = true;
    private ScaleGestureDetector mScaleGestureDetector;
    int scaledTouchSlop;

    public ZoomView(Context context) {
        this(context, null);
    }

    public ZoomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public ZoomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Drawable drawable = getDrawable();

        if (drawable != null) {
            drawablew = drawable.getIntrinsicWidth();
            drawableh = drawable.getIntrinsicHeight();
            matrix = new Matrix();
            mScaleGestureDetector = new ScaleGestureDetector(getContext(), this);
            setOnTouchListener(this);
            scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizew = MeasureSpec.getSize(widthMeasureSpec);
        int sizeh = MeasureSpec.getSize(heightMeasureSpec);
        if (drawablew == 0 || drawableh == 0) return;
        if (once) {
            once = false;
            if (sizew > drawablew && sizeh > drawableh) {
                initScale = Math.min(drawablew * 1.0f / sizew, drawableh * 1.0f / sizeh);
            } else if (sizew > drawablew && sizeh < drawableh) {
                initScale = sizeh * 1.0f / drawableh;
            } else if (sizew < drawablew && sizeh > drawableh) {
                initScale = sizew * 1.0f / drawablew;
            } else if (sizew < drawablew && sizeh < drawableh) {
                initScale = Math.max(sizew * 1.0f / drawablew, sizeh * 1.0f / drawableh);
            }
            matrix.postTranslate((sizew - drawablew) / 2, (sizeh - drawableh) / 2);
            matrix.postScale(initScale, initScale, sizew / 2, sizeh / 2);
            setImageMatrix(matrix);

            Log.e(TAG, "onMeasure: " + sizew + " sizeh  " + sizeh + "   initScale " + initScale + " matrix  " + matrix.toShortString());
        }
    }

    float downx, downy;

    int lastCount;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        mScaleGestureDetector.onTouchEvent(event);

        float tempx = 0;
        float tempy = 0;
        int pointerCount = event.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            tempx += event.getX(i);
            tempy += event.getY(i);
        }
        tempx = tempx / pointerCount;
        tempy = tempy / pointerCount;

        if (lastCount != pointerCount) {
            downx = tempx;
            downy = tempy;
            lastCount = pointerCount;
        }
        switch (event.getAction()) {

            case MotionEvent.ACTION_MOVE:

                float offx = tempx - downx, offy = tempy - downy;

                if (Math.sqrt(offx * offx + offy * offy) > scaledTouchSlop) {
                    Drawable drawable = getDrawable();
                    if (drawable != null) {
                        getRect(drawable);
                        if (getWidth() > mRectF.width()) {
                            offx = 0;
                        }
                        if (getHeight() > mRectF.height()) {
                            offy = 0;
                        }
                        matrix.postTranslate(offx, offy);
                        setImageMatrix(matrix);
                        checkMatrixBounds();
                        downx = tempx;
                        downy = tempy;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                lastCount = 0;
                break;
        }


        return true;
    }

    private void checkMatrixBounds() {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            getRect(drawable);
            checkBounds();
        }
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {

        float scaleFactor = detector.getScaleFactor();

        Drawable drawable = getDrawable();
        if (drawable == null) return true;
        matrix.getValues(values);
        float value = values[Matrix.MSCALE_X];
        if (value > 0.5 && scaleFactor < 1 || value < 4 && scaleFactor > 1) {

            getRect(drawable);
            checkBounds();
            matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            setImageMatrix(matrix);
        }


        return true;
    }

    private void checkBounds () {
        float offsetx = 0, offsety = 0;
        if (getWidth() <= mRectF.width()) {
            if (mRectF.left > 0) {
                offsetx = -mRectF.left;
            }
            if (mRectF.right < getWidth()) {
                offsetx = getWidth() - mRectF.right;
            }
        }
        if (getHeight() <= mRectF.height()) {
            if (mRectF.top > 0) {
                offsety = -mRectF.top;
            }
            if (mRectF.bottom < getHeight()) {
                offsety = getHeight() - mRectF.bottom;
            }
        }
        if (getHeight() > mRectF.height()) {
            offsety = getHeight() / 2 - mRectF.bottom + mRectF.height() / 2;
        }
        if (getWidth() > mRectF.width()) {
            offsetx = getWidth() / 2 - mRectF.right + mRectF.width() / 2;
        }
        matrix.postTranslate(offsetx, offsety);
        setImageMatrix(matrix);

    }

    private void getRect(Drawable drawable) {
        mRectF.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        matrix.mapRect(mRectF);
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }


}
