package com.onzhou.opengles;

import android.app.Application;

/**
 * @anchor: andy
 * @date: 2018-11-07
 * @description:
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCore.getInstance().init(this);
    }
}
