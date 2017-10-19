package com.example.apple.Custom.Matrix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.apple.pullzoom.R;

/**
 * Created by apple on 17/10/10.
 * 颜色矩阵
 */

public class ColorMatrixImgeView extends View {

    final static String TAG = "ColorMatrixImg";

    Paint paint = new Paint();

    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_img_profile_bg);

    public ColorMatrixImgeView(Context context) {
        this(context, null);
    }

    public ColorMatrixImgeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorMatrixImgeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int modeW = MeasureSpec.getMode(widthMeasureSpec);
        int modeH = MeasureSpec.getMode(heightMeasureSpec);
        int sizeW = MeasureSpec.getSize(widthMeasureSpec);
        int sizeH = MeasureSpec.getSize(heightMeasureSpec);

        int w = 0;
        int h = 0;
        if (modeW == MeasureSpec.AT_MOST || modeH == MeasureSpec.AT_MOST) {
            h = w = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
        }
        if (modeW == MeasureSpec.EXACTLY) {
            w = sizeW;
        }
        if (modeH == MeasureSpec.EXACTLY) {
            h = sizeH;
        }
        //setMeasuredDimension(w, h);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap, 0, 50, paint);
        Matrix x;
        float[] floats = {

                1, 0, 0, 0, 0,
                0, 1, 0, 0, 200,
                0, 0, 1, 0, 0,
                0, 0, 0, 1, 0
        };
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setScale(1,2,2,1);
//        LightingColorFilter 过滤颜色，并添加颜色，LightingColorFilter(0xffff00ff,0xffff0000));过滤绿色，添加红色
        paint.setColorFilter(new LightingColorFilter(0xffff00ff,0xffff0000));
        canvas.drawBitmap(bitmap, 0, bitmap.getHeight() + 100, paint);

    }
}
