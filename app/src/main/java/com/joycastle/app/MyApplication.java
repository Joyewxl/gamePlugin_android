package com.joycastle.app;

import android.app.Application;

import com.joycastle.gameplugin.GamePlugin;
import com.joycastle.my_gameplugin.GamePlugin;

/**
 * Created by geekgy on 16/4/23.
 */
public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        GamePlugin.getInstance().init(this);
    }
}
