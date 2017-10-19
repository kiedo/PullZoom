package com.example.apple.Custom.Matrix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.apple.pullzoom.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ZoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().post(new A(23));
    }

    @Subscribe
    public void  MainThread(A param) {
        Log.e("ZoomActivity", "onEventMainThread: " + param.geta());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void  PostThread(A param) {
        Log.e("ZoomActivity", "onEventPostThread: " + param.geta() + " 000 ");

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(A param) {
        Log.e("ZoomActivity", "onEventBackgroundThread: " + param);

    }

    @Subscribe
    public void onEventAsync(int param) {
        Log.e("ZoomActivity", "onEventAsync: " + param);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    class A {
        int a;

        public A(int a) {
            this.a = a;
        }

        int geta() {
            return a;
        }
    }
}
