package com.example.apple.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by apple on 17/8/31.
 */

public class Db extends SQLiteOpenHelper {

    final static String tablename = "DBbook";


    public Db(Context context) {
        super(context, tablename, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not EXISTS  books  (id integer, name text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
