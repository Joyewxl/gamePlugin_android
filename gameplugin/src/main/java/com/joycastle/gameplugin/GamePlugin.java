package com.joycastle.gameplugin;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.joycastle.gamepluginbase.LifeCycleDelegate;
import com.joycastle.gamepluginbase.SystemUtil;
import com.joycastle.my_facebook.FacebookHelper;

/**
 * Created by geekgy on 16/5/11.
 */
public class GamePlugin implements LifeCycleDelegate {
    public static final String TAG = "GamePlugin";
    private static GamePlugin instance = new GamePlugin();
    public static GamePlugin getInstance() { return instance; }
    private GamePlugin() {}

    @Override
    public void init(Application application) {
        SystemUtil.setApplication(application);
        AnalyticHelper.getInstance().init(application);
        AdvertiseHelper.getInstance().init(application);
        IabHelper.getInstance().init(application);
        FacebookHelper.getInstance().init(application);
    }

    @Override
    public void onCreate(Activity activity, Bundle savedInstanceState) {
        AnalyticHelper.getInstance().onCreate(activity, savedInstanceState);
        AdvertiseHelper.getInstance().onCreate(activity, savedInstanceState);
        IabHelper.getInstance().onCreate(activity, savedInstanceState);
    }

    @Override
    public void onStart(Activity activity) {
        AnalyticHelper.getInstance().onStart(activity);
        AdvertiseHelper.getInstance().onStart(activity);
        IabHelper.getInstance().onStart(activity);
    }

    @Override
    public void onResume(Activity activity) {
        SystemUtil.setActivity(activity);
        AnalyticHelper.getInstance().onResume(activity);
        AdvertiseHelper.getInstance().onResume(activity);
        IabHelper.getInstance().onResume(activity);
    }

    @Override
    public void onPause(Activity activity) {
        AnalyticHelper.getInstance().onPause(activity);
        AdvertiseHelper.getInstance().onPause(activity);
        IabHelper.getInstance().onPause(activity);
    }

    @Override
    public void onStop(Activity activity) {
        AnalyticHelper.getInstance().onStop(activity);
        AdvertiseHelper.getInstance().onStop(activity);
        IabHelper.getInstance().onStop(activity);
    }

    @Override
    public void onDestroy(Activity activity) {
        AnalyticHelper.getInstance().onDestroy(activity);
        AdvertiseHelper.getInstance().onDestroy(activity);
        IabHelper.getInstance().onDestroy(activity);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        AnalyticHelper.getInstance().onActivityResult(activity, requestCode, resultCode, data);
        AdvertiseHelper.getInstance().onActivityResult(activity, requestCode, resultCode, data);
//        IabHelper.getInstance().onActivityResult(activity, requestCode, resultCode, data);

        FacebookHelper.getInstance().onActivityResult(activity, requestCode, resultCode, data);
    }
}
