package com.example.apple.pullzoom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class ScrollViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scro_viiew);

        ScrollViewPullZoom scrollViewPullZoom = (ScrollViewPullZoom) findViewById(R.id.scrollview);

        View contentview = LayoutInflater.from(this).inflate(R.layout.content_layout,null);
        View zoomview = LayoutInflater.from(this).inflate(R.layout.zoom_view,null);

        scrollViewPullZoom.setContentView(contentview);
        scrollViewPullZoom.setZoomView(zoomview);
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (1.0F * (mScreenHeight / 4.0F)));
        scrollViewPullZoom.setHeaderLayoutParams(localObject);
    }
}
