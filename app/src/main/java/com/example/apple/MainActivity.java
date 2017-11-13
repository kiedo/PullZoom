package com.example.apple;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.apple.pullzoom.ListviewMActiviy;
import com.example.apple.pullzoom.ScrollViewActivity;
import com.example.apple.pullzoom.reflash.ReflashActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void scrollview(View view) {

        startActivity(new Intent(this, ScrollViewActivity.class));

    }

    public void listview(View view) {

        startActivity(new Intent(this, ListviewMActiviy.class));
    }



    public void reflash(View v){
        startActivity(new Intent(this, ReflashActivity.class));
    }


}
