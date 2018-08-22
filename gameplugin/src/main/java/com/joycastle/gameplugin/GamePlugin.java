package com.joycastle.gameplugin;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

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
        final  Application mApplication = application;

        mMainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AdvertiseHelper.getInstance().init(mApplication);
                FacebookHelper.getInstance().init(mApplication);
            }
        },6000);
        BackgroundThread.prepareThread();
        BackgroundThread.post(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                SystemUtil.getInstance().setApplication(mApplication);
                AnalyticHelper.getInstance().init(mApplication);
                GoogleIabHelper.getInstance().init(mApplication);
                KCAnalyticHelper.getInstance().init(mApplication);
                GameAnalyticsHelper.getInstance().init(mApplication);
            }
        });
    }

    @Override
    public void onCreate(Activity activity, Bundle savedInstanceState) {

        final Activity mActivity    = activity;
        final Bundle mState         = savedInstanceState;

        mMainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AdvertiseHelper.getInstance().onCreate(mActivity, mState);
                FacebookHelper.getInstance().onCreate(mActivity, mState);
            }
        },6000);

        BackgroundThread.post(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                SystemUtil.getInstance().setActivity(mActivity);
                AnalyticHelper.getInstance().onCreate(mActivity, mState);
                GoogleIabHelper.getInstance().onCreate(mActivity, mState);
                KCAnalyticHelper.getInstance().onCreate(mActivity, mState);
                GameAnalyticsHelper.getInstance().onCreate(mActivity, mState);
            }
        });
    }

    @Override
    public void onStart(Activity activity) {
        AnalyticHelper.getInstance().onStart(activity);
        AdvertiseHelper.getInstance().onStart(activity);
        FacebookHelper.getInstance().onStart(activity);
        GoogleIabHelper.getInstance().onStart(activity);
        KCAnalyticHelper.getInstance().onStart(activity);
        GameAnalyticsHelper.getInstance().onStart(activity);
    }

    @Override
    public void onResume(Activity activity) {
        AnalyticHelper.getInstance().onResume(activity);
        AdvertiseHelper.getInstance().onResume(activity);
        FacebookHelper.getInstance().onResume(activity);
        GoogleIabHelper.getInstance().onResume(activity);
        KCAnalyticHelper.getInstance().onResume(activity);
        GameAnalyticsHelper.getInstance().onResume(activity);
    }

    @Override
    public void onPause(Activity activity) {
        AnalyticHelper.getInstance().onPause(activity);
        AdvertiseHelper.getInstance().onPause(activity);
        FacebookHelper.getInstance().onPause(activity);
        GoogleIabHelper.getInstance().onPause(activity);
        KCAnalyticHelper.getInstance().onResume(activity);
        GameAnalyticsHelper.getInstance().onResume(activity);
    }

    @Override
    public void onStop(Activity activity) {
        AnalyticHelper.getInstance().onStop(activity);
        AdvertiseHelper.getInstance().onStop(activity);
        FacebookHelper.getInstance().onStop(activity);
        GoogleIabHelper.getInstance().onStop(activity);
        KCAnalyticHelper.getInstance().onStop(activity);
        GameAnalyticsHelper.getInstance().onStop(activity);
    }

    @Override
    public void onDestroy(Activity activity) {
        AnalyticHelper.getInstance().onDestroy(activity);
        AdvertiseHelper.getInstance().onDestroy(activity);
        FacebookHelper.getInstance().onDestroy(activity);
        GoogleIabHelper.getInstance().onDestroy(activity);
        KCAnalyticHelper.getInstance().onDestroy(activity);
        GameAnalyticsHelper.getInstance().onDestroy(activity);
        BackgroundThread.destroyThread();
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        AnalyticHelper.getInstance().onActivityResult(activity, requestCode, resultCode, data);
        AdvertiseHelper.getInstance().onActivityResult(activity, requestCode, resultCode, data);
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


    /**
     * 需要自己控制生命周期，在这个生命周期内都可以使用这个线程
     *
     */
    public static class BackgroundThread extends HandlerThread {
        private static BackgroundThread mInstance;
        private static Handler mHandler;

        public BackgroundThread() {
            super("ThreadName", android.os.Process.THREAD_PRIORITY_DEFAULT);
        }

        public static void prepareThread() {
            if (mInstance == null) {
                mInstance = new BackgroundThread();
                // 创建HandlerThread后一定要记得start()
                mInstance.start();
                // 获取HandlerThread的Looper,创建Handler，通过Looper初始化
                mHandler = new Handler(mInstance.getLooper());
            }
        }

        /**
         * 如果需要在后台线程做一件事情，那么直接调用post方法，使用非常方便
         */
        public static void post(final Runnable runnable) {
            mHandler.post(runnable);
        }

        public static void postDelayed(final Runnable runnable, long nDelay) {
            mHandler.postDelayed(runnable, nDelay);
        }

        /**
         * 退出HandlerThread
         */
        public static void destroyThread() {
            if (mInstance != null) {
                mInstance.quit();
                mInstance = null;
                mHandler = null;
            }
        }
    }


}
