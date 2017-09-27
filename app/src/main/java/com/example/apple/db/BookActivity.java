package com.example.apple.db;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.apple.pullzoom.R;


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
