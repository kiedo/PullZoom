package com.example.apple;

import android.app.Application;
import android.content.pm.ApplicationInfo;

import com.example.apple.orm.bean.dao.DaoMaster;
import com.example.apple.orm.bean.dao.DaoSession;
import com.example.apple.orm.bean.dao.UserDao;
import com.facebook.stetho.Stetho;

import org.greenrobot.greendao.annotation.Entity;

/**
 * Created by apple on 17/5/16.
 */

public class App extends Application {

    static App mApp;

    public static Application getApplication() {
        return mApp;
    }

    DaoSession daoSession;

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        Stetho.initializeWithDefaults(this);

        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), "MMCD.db", null); //SQLiteOpenHelper
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        daoSession = daoMaster.newSession();
    }
}
