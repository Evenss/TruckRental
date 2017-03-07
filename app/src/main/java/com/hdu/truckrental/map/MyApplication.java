package com.hdu.truckrental.map;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by Even on 2017/3/7.
 * 百度地初始化
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
    }
}
