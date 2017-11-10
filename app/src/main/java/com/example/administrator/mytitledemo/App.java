package com.example.administrator.mytitledemo;

import android.app.Application;

/**
 * author:ggband
 * data:2017/11/8 000813:39
 * email:ggband520@163.com
 * desc:
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(getApplicationContext());
    }
}
