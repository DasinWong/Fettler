package com.dasinwong.fettlerpro;

import android.app.Application;
import android.content.Context;

import com.dasinwong.fettler.Fettler;

public class BaseApplication extends Application {

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        Fettler.init(context);
    }
}
