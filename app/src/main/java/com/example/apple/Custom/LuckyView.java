package com.example.apple.Custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.example.apple.R;

/**
 * @author apple
 * @date 17/11/3
 * 抽奖转盘
 */

public class LuckyView extends View {

    final String TAG = this.getClass().getSimpleName();
    int width, hight, mRadius, centerX, centerY, startAngle = 0, size;
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint bgIndcator = new Paint(Paint.ANTI_ALIAS_FLAG);

    float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15f, getResources().getDisplayMetrics());
    int[] icons = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    Bitmap[] bitmaps = null;
    int[] mArcColors = {0xFFFFC300, 0XFFF17E01, 0xFFFFC300, 0XFFF17E01, 0xFFFFC300, 0XFFF17E01};
    String[] titles = {"中国银行", "建设银行", "农业银行", "工商银行", "邮政", "成都银行"};
    RectF mRectf = new RectF();
    private boolean isStart;
    double speed = 0;
    private int sweepAngle;
    private int tagetCenter;


    public LuckyView(Context context) {
        this(context, null);
    }

    public LuckyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LuckyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initBitmap();
    }

    public void setIcons(int[] icons, int[] colors, String[] titles) {
        this.icons = icons;
        this.mArcColors = colors;
        this.titles = titles;
        initBitmap();
    }

    private void initBitmap() {
        if (icons != null && icons.length > 2) {
            bitmaps = new Bitmap[icons.length];
            size = icons.length;
            for (int i = 0; i < icons.length; i++) {
                bitmaps[i] = BitmapFactory.decodeResource(getResources(), icons[i]);
            }
        }
    }

    private void init() {


        mTextPaint.setDither(true);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(Color.WHITE);

        mArcPaint.setDither(true);

        bgIndcator.setColor(Color.RED);
        bgIndcator.setStrokeCap(Paint.Cap.ROUND);
        bgIndcator.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));

        bgPaint.setColor(Color.BLACK);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        hight = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.UNSPECIFIED) {
            hight = width;
        }
        mRadius = (width - getPaddingLeft() - getPaddingRight()) / 2;
        centerX = width / 2;
        centerY = hight / 2;
        mRectf.set(getPaddingLeft(), getPaddingTop(),
                width - getPaddingRight(),
                hight - getPaddingBottom());
        setMeasuredDimension(width, hight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.restore();
        if (icons != null && icons.length > 2 && mArcColors != null && mArcColors.length > 0) {
            drowBG(canvas);
            luckydraw(canvas);
            drawIndecator(canvas);
        }

    }

    public void start(int index) {
        if (index <= 0 || index > size) {
            return;
        }
        int tagetFrom = 360 + 270 - index * sweepAngle;
        tagetCenter = tagetFrom + sweepAngle / 2;
//        speed = Math.sqrt(tagetCenter * 2);
        speed =   (float) (Math.sqrt(1 * 1 + 8 * 1 * tagetCenter) - 1) / 2;
        startAngle = 0;
        Log.e(TAG, "start: 目标 " + tagetCenter);
        if (isStart) {
            isStart = false;
            handler.sendEmptyMessage(2);
        } else {
            isStart = true;
            handler.sendEmptyMessage(1);
        }

    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    private void luckydraw(Canvas canvas) {
        sweepAngle = 360 / size;
        int tempAngle = startAngle;
        for (int i = 0; i < size; i++) {
            mArcPaint.setColor(mArcColors[i % mArcColors.length]);
            canvas.drawArc(mRectf, tempAngle, sweepAngle, true, mArcPaint);
            drawText(canvas, titles[i], tempAngle);
            drawIcon(canvas, bitmaps[i], tempAngle);
            tempAngle += sweepAngle;
        }

    }

    private void drawIndecator(Canvas canvas) {
        canvas.drawLine(centerX, centerY, centerX, centerY / 2, bgIndcator);
        canvas.drawCircle(centerX, centerY, mRadius / 5, bgIndcator);
    }


    private void drawIcon(Canvas canvas, Bitmap bitmap, int tempAngle) {
        int imgWidth = mRadius / 4;
        float angle = (float) ((tempAngle + 360 / (2 * size)) * Math.PI / 180);
        int x = (int) (centerX + mRadius * 2 / 3 * Math.cos(angle));
        int y = (int) (centerY + mRadius * 2 / 3 * Math.sin(angle));
        Rect rect = new Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth / 2, y + imgWidth / 2);
        canvas.drawBitmap(bitmap, null, rect, null);
    }


    private void drawText(Canvas canvas, String title, int tempAngle) {
        Path mPath = new Path();
        mPath.addArc(mRectf, tempAngle, sweepAngle);
        float w = mTextPaint.measureText(title);
        int h = (int) ((2 * Math.PI * mRadius / size - w) / 2);
        canvas.drawTextOnPath(title, mPath, h, mRadius / 8, mTextPaint);
    }

    private void drowBG(Canvas canvas) {
        canvas.drawCircle(centerX, centerY, mRadius, bgPaint);
    }


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (isStart) {
                        startAngle += speed;
                        handler.sendEmptyMessageDelayed(1, 100);
                    }
                    break;
                case 2:
                    if (isStart) return false;
                    startAngle += speed;
                    if (speed > 0) {
                        speed--;
//                        if (startAngle < tagetCenter) {
//                            if (speed < 2) {
//                                speed = 1;
//                            }
//                        }
                    }
                    if (speed <= 0) {
                        speed = 0;
                        isStart = false;
                        return isStart;
                    }
                    handler.sendEmptyMessageDelayed(2, 100);
                    break;
                default:
            }
            invalidate();

            return false;
        }
    });

}
