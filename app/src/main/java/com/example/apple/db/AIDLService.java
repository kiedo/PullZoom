package com.example.apple.db;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by apple on 17/9/3.
 */

public class AIDLService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    IMaidl.Stub binder = new IMaidl.Stub() {
        @Override
        public String getName() throws RemoteException {
            return getServiceName();
        }


    };

    String getServiceName() {
        return "这是服务端给的信息";
    }


}
