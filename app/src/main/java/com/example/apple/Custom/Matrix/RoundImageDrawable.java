package com.example.apple.Custom.Matrix;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author apple
 * @date 17/10/16
 */

public class RoundImageDrawable extends Drawable {

    private Paint paint;
    private RectF rectF;
    private Bitmap mBitmap;
    private float radius;
    private boolean isRound;

    public RoundImageDrawable(Bitmap bitmap) {
        this.mBitmap = bitmap;
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(bitmapShader);
    }


    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        if (isRound) {
            rectF = new RectF(left, top, Math.min(right, bottom), Math.min(right, bottom));
        } else {
            rectF = new RectF(left, top, right, bottom);
        }

    }

    public void setisRound(boolean round) {
        if (isRound = round) {
            radius = Math.min(getIntrinsicHeight(), getIntrinsicWidth()) / 2;
        }

    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawRoundRect(rectF, radius, radius, paint);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmap.getWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmap.getHeight();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
