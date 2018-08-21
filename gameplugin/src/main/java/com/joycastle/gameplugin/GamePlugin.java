package com.joycastle.gameplugin;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;


import com.joycastle.gamepluginbase.InvokeJavaMethodDelegate;
import com.joycastle.gamepluginbase.LifeCycleDelegate;
import com.joycastle.gamepluginbase.SystemUtil;
import com.joycastle.iab.googleplay.GoogleIabHelper;
import com.joycastle.my_facebook.FacebookHelper;
import com.joycastle.analytic.kochava.KCAnalyticHelper;
import com.joycastle.analytic.gameanalytics.GameAnalyticsHelper;


import java.util.HashMap;

/**
 * Created by geekgy on 16/5/11.
 */
public class GamePlugin implements LifeCycleDelegate {
    private static final String TAG = "GamePlugin";
    private static GamePlugin instance = new GamePlugin();

    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private InvokeJavaMethodDelegate mNotifyDelegate;

    public static GamePlugin getInstance() { return instance; }

    private GamePlugin() {}

    @Override
    public void init(Application application) {
        SystemUtil.getInstance().setApplication(application);
        AnalyticHelper.getInstance().init(application);
//        AdvertiseHelper.getInstance().init(application);
        FacebookHelper.getInstance().init(application);
        GoogleIabHelper.getInstance().init(application);
        KCAnalyticHelper.getInstance().init(application);
        GameAnalyticsHelper.getInstance().init(application);
    }

    @Override
    public void onCreate(Activity activity, Bundle savedInstanceState) {
        SystemUtil.getInstance().setActivity(activity);
        AnalyticHelper.getInstance().onCreate(activity, savedInstanceState);
//        AdvertiseHelper.getInstance().onCreate(activity, savedInstanceState);
        FacebookHelper.getInstance().onCreate(activity, savedInstanceState);
        GoogleIabHelper.getInstance().onCreate(activity, savedInstanceState);
        KCAnalyticHelper.getInstance().onCreate(activity, savedInstanceState);
        GameAnalyticsHelper.getInstance().onCreate(activity, savedInstanceState);
    }

    @Override
    public void onStart(Activity activity) {
        AnalyticHelper.getInstance().onStart(activity);
//        AdvertiseHelper.getInstance().onStart(activity);
        FacebookHelper.getInstance().onStart(activity);
        GoogleIabHelper.getInstance().onStart(activity);
        KCAnalyticHelper.getInstance().onStart(activity);
        GameAnalyticsHelper.getInstance().onStart(activity);
    }

    @Override
    public void onResume(Activity activity) {
        AnalyticHelper.getInstance().onResume(activity);
//        AdvertiseHelper.getInstance().onResume(activity);
        FacebookHelper.getInstance().onResume(activity);
        GoogleIabHelper.getInstance().onResume(activity);
        KCAnalyticHelper.getInstance().onResume(activity);
        GameAnalyticsHelper.getInstance().onResume(activity);
    }

    @Override
    public void onPause(Activity activity) {
        AnalyticHelper.getInstance().onPause(activity);
//        AdvertiseHelper.getInstance().onPause(activity);
        FacebookHelper.getInstance().onPause(activity);
        GoogleIabHelper.getInstance().onPause(activity);
        KCAnalyticHelper.getInstance().onResume(activity);
        GameAnalyticsHelper.getInstance().onResume(activity);
    }

    @Override
    public void onStop(Activity activity) {
        AnalyticHelper.getInstance().onStop(activity);
//        AdvertiseHelper.getInstance().onStop(activity);
        FacebookHelper.getInstance().onStop(activity);
        GoogleIabHelper.getInstance().onStop(activity);
        KCAnalyticHelper.getInstance().onStop(activity);
        GameAnalyticsHelper.getInstance().onStop(activity);
    }

    @Override
    public void onDestroy(Activity activity) {
        AnalyticHelper.getInstance().onDestroy(activity);
//        AdvertiseHelper.getInstance().onDestroy(activity);
        FacebookHelper.getInstance().onDestroy(activity);
        GoogleIabHelper.getInstance().onDestroy(activity);
        KCAnalyticHelper.getInstance().onDestroy(activity);
        GameAnalyticsHelper.getInstance().onDestroy(activity);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        AnalyticHelper.getInstance().onActivityResult(activity, requestCode, resultCode, data);
//        AdvertiseHelper.getInstance().onActivityResult(activity, requestCode, resultCode, data);
        FacebookHelper.getInstance().onActivityResult(activity, requestCode, resultCode, data);
        GoogleIabHelper.getInstance().onActivityResult(activity, requestCode, resultCode, data);
        KCAnalyticHelper.getInstance().onActivityResult(activity, requestCode, resultCode, data);
        GameAnalyticsHelper.getInstance().onActivityResult(activity, requestCode, resultCode, data);
    }

    public void setNotifyHandler(InvokeJavaMethodDelegate delegate) {
        mNotifyDelegate = delegate;
    }

    public void setIapVerifyUrlAndSign(String url, String sign) {
        GoogleIabHelper.getInstance().setIapVerifyUrlAndSign(url, sign);
    }

    public boolean canDoIap() {
        return GoogleIabHelper.getInstance().canDoIap();
    }

    public HashMap getSuspensiveIap() {
        return GoogleIabHelper.getInstance().getSuspensiveIap();
    }

    public void setSuspensiveIap(HashMap iapInfo) {
        GoogleIabHelper.getInstance().setSuspensiveIap(iapInfo);
    }

    public void doIap(final String iapId, final String userId, final InvokeJavaMethodDelegate delegate) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                GoogleIabHelper.getInstance().doIap(iapId, userId, delegate);
            };
        });
    }

    public void rateGame() {
        Activity activity = SystemUtil.getInstance().getActivity();
        final String appPackageName = SystemUtil.getInstance().getAppBundleId(); // getAppBundleId() from Context or Activity object
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

}
