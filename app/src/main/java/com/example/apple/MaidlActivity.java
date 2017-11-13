package com.example.apple;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.apple.db.AIDLService;
import com.example.apple.db.IMaidl;

public class MaidlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maidl);

        bindService(new Intent(this, AIDLService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                IMaidl iMaidl = IMaidl.Stub.asInterface(service);
                try {
                    String name1 = iMaidl.getName();
                    Log.e("onServiceConnected", "onServiceConnected: "+name1 );
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Log.e("onServiceConnected", "onServiceConnected: "+e.getMessage() );
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, BIND_AUTO_CREATE);


    }
}
