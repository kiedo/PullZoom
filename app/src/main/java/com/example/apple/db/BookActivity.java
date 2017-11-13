package com.example.apple.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.apple.R;


/**
 * Created by apple on 17/8/31.
 */

public class BookActivity extends AppCompatActivity {
    IMaidl iMaidl;

    final String TAG = this.getClass().getSimpleName();
    SQLiteDatabase writableDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_book);


    }


}
