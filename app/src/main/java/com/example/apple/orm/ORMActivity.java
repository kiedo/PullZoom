package com.example.apple.orm;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Binder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.apple.App;
import com.example.apple.orm.bean.User;
import com.example.apple.orm.bean.dao.DaoSession;
import com.example.apple.orm.bean.dao.UserDao;
import com.example.apple.pullzoom.R;

import java.util.List;

public class ORMActivity extends AppCompatActivity {

    String TAG = "ORMActivity";
    DaoSession daoSession;
    User muser;
    UserDao mUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orm);
        App application = (App) App.getApplication();
        daoSession = application.getDaoSession();
        muser = new User(null, "jeck", "nikename10", "uname", "Ab");
        mUserDao = daoSession.getUserDao();
        View v;
        LinearLayout ll;
        Binder mb = new Binder();

    }

    public void add(View v) {

        String  startColor = "#ffffff";
        int startRed = Integer.parseInt(startColor.substring(1, 3), 16);
        int startGreen = Integer.parseInt(startColor.substring(3, 5), 16);
        int startBlue = Integer.parseInt(startColor.substring(5, 7), 16);

        Log.e(TAG, "add: "+startRed + "  "+ startGreen + "  "+startBlue );


        ValueAnimator mValueAnimator =  ObjectAnimator.ofFloat(findViewById(R.id.add), "alpha", 1f, 0f, 1f);
        mValueAnimator .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.e(TAG, "onAnimationUpdate: "+animation.getAnimatedValue());
            }
        });
        mValueAnimator.setDuration(1000);
        mValueAnimator.start();

//        Log.d(TAG, "add: ");
//        daoSession.insert(muser);
//        Log.d(TAG, "add: "+  daoSession.loadAll(muser.getClass()));
    }

    public void delete(View v) {
        Log.d(TAG, "delete: ");
        muser = new User(null, "jeck11", "nikename10", "uname", "Ab");
//        daoSession.getUserDao().delete(muser);
        List<User> users = mUserDao.loadAll();
        mUserDao.deleteInTx(users);
    }

    public void updata(View v) {
        Log.d(TAG, "updata: ");
        muser.setNickname("彩讯");
        mUserDao.update(muser);
    }

    public void getAll(View v) {
        List<User> users = mUserDao.loadAll();
        Log.d(TAG, "add: " + users);
    }
}
