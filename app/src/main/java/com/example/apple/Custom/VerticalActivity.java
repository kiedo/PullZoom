package com.example.apple.Custom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.apple.pullzoom.R;

public class VerticalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical);
        ((VerticalViewPager) findViewById(R.id.custom)).setPageChangeListener(new VerticalViewPager.OnPageChangeListener() {
            @Override
            public void onPageChange(int currentPage) {
                Log.e("onPageChange", "onPageChange: " + currentPage);
            }
        });

    }
}
