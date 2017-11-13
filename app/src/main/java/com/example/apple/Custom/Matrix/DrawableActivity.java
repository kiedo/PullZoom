package com.example.apple.Custom.Matrix;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;

import com.example.apple.R;
public class DrawableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable);


    }

    void initRoundDrawable() {

//       ImageView image1 = (ImageView) findViewById(R.id.mm1);
//       ImageView image2 = (ImageView) findViewById(R.id.mm2);
//       ImageView image3 = (ImageView) findViewById(R.id.mm3);
//
//       RoundImageDrawable mRoundImageDrawable = new RoundImageDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.ns1));
//       RoundImageDrawable mRoundImageDrawable2 = new RoundImageDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.ns1));
//       RoundImageDrawable mRoundImageDrawable3 = new RoundImageDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.ns1));
//       mRoundImageDrawable.setisRound(true);
//
//       image1.setImageDrawable(mRoundImageDrawable);
//       mRoundImageDrawable2.setRadius(100);
//       image2.setImageDrawable(mRoundImageDrawable2);
//       mRoundImageDrawable3.setRadius(200);
    }


    //RoundedBitmapDrawable 圆角图片
    private Drawable createRoundImageWithBorder(Bitmap bitmap) {
        //原图宽度
        int bitmapWidth = bitmap.getWidth();
        //原图高度
        int bitmapHeight = bitmap.getHeight();
        //边框宽度 pixel
        int borderWidthHalf = 20;

        //转换为正方形后的宽高
        int bitmapSquareWidth = Math.min(bitmapWidth, bitmapHeight);

        //最终图像的宽高
        int newBitmapSquareWidth = bitmapSquareWidth + borderWidthHalf;

        Bitmap roundedBitmap = Bitmap.createBitmap(newBitmapSquareWidth, newBitmapSquareWidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundedBitmap);
        int x = borderWidthHalf + bitmapSquareWidth - bitmapWidth;
        int y = borderWidthHalf + bitmapSquareWidth - bitmapHeight;
        Paint borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        //裁剪后图像,注意X,Y要除以2 来进行一个中心裁剪
        canvas.drawBitmap(bitmap, x / 2, y / 2, borderPaint);

        borderPaint.setStrokeWidth(borderWidthHalf);
        borderPaint.setColor(Color.WHITE);

        //添加边框
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getWidth() / 2, newBitmapSquareWidth / 2, borderPaint);

        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), roundedBitmap);
        roundedBitmapDrawable.setGravity(Gravity.CENTER);
        roundedBitmapDrawable.setCircular(true);
        return roundedBitmapDrawable;
    }


}
