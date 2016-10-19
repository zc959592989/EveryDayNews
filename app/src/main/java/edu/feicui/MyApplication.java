package edu.feicui;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * 整个应用程序的入口
 * Created by zhaoCe on 2016/10/9.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
