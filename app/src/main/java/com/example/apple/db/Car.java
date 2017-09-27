package com.example.apple.db;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 17/9/3.
 */

public class Car implements Parcelable {


    int ID;
    String name;


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

}
