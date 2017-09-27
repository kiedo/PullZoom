package com.example.apple.lrc;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.apple.pullzoom.R;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class LrcActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler);

        final LrcView viewById = (LrcView) findViewById(R.id.lrc);


        viewById.setOnClickListener(new LrcView.Onclicklisener() {
            @Override
            public void onClick() {
                Toast.makeText(LrcActivity.this, "点击了", Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LrcActivity.this, "223322", Toast.LENGTH_LONG).show();

                Paint mPaint = new Paint();
                mPaint.setTextSize(50);
                LrcParse mLrcParse = LrcParse.getLrcParse();
                mLrcParse.readFile(LrcActivity.this, mPaint);
                List<LrcBean> lrcs = mLrcParse.getLrcs();
                viewById.setLrcs(lrcs);

            }
        });


        ScheduledExecutorService scheduledExecutorService;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                LrcActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewById.reflash(2000);

                    }
                });


            }
        }, 200, 200, TimeUnit.MILLISECONDS);

    }


}
