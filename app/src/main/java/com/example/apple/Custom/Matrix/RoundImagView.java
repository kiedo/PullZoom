package com.example.apple.Custom.Matrix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.example.apple.pullzoom.R;

/**
 * @author apple
 * @date 17/10/16
 */

public class RoundImagView extends View {

    final String TAG = this.getClass().getSimpleName();
    private int width, height, min;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap roundbitmap;
    private Bitmap mbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ns5);

    public RoundImagView(Context context) {
        this(context, null);
    }

    public RoundImagView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wmode = MeasureSpec.getMode(widthMeasureSpec);
        int hmode = MeasureSpec.getMode(heightMeasureSpec);
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        if (wmode == MeasureSpec.EXACTLY && hmode == MeasureSpec.EXACTLY) {
            width = w;
            height = h;
        } else if (hmode == MeasureSpec.AT_MOST && wmode == MeasureSpec.AT_MOST) {
            width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
            height = width;
        } else {
            height = h;
            width = height;
        }
        min = Math.min(height - getPaddingTop() - getPaddingBottom(), width - getPaddingRight() - getPaddingLeft());
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        Bitmap bitmap = createBitmap();
        canvas.drawBitmap(bitmap, 0, 0, null);

    }

    public Bitmap createScaledBitmap(Bitmap src, int dstWidth, int dstHeight, boolean filter) {
        Matrix matrix = new Matrix();
        int width = src.getWidth();
        int height = src.getHeight();
        final float sx = dstWidth / width;
        final float sy = dstHeight / height;
        float min = Math.max(sx, sy);
        matrix.setScale(min, min);
        return Bitmap.createBitmap(src, 0, 0, width, height, matrix, filter);
    }


    private Bitmap createBitmap() {
        if (roundbitmap != null){
            return roundbitmap;
        }
        roundbitmap = Bitmap.createBitmap(getResources().getDisplayMetrics(), width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundbitmap);
        canvas.drawCircle(width / 2, height / 2, min / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Matrix matrix = new Matrix();
        int w = mbitmap.getWidth();
        int h = mbitmap.getHeight();
        final float sx = width * 1.0f / w;
        final float sy = height * 1.0f / h;
        float max = Math.max(sx, sy);
        matrix.setScale(max, max);
        canvas.drawBitmap(mbitmap, matrix, paint);
        return roundbitmap;
    }
}
