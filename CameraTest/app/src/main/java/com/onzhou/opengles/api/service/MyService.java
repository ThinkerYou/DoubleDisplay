package com.onzhou.opengles.api.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.onzhou.opengles.IMyService;

import openapi.ainemo.com.cameratest.MyHandler;

public class MyService extends Service {
    private static final String TAG = MyService.class.getSimpleName();
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
