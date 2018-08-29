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
import android.util.Log;

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

    private static long acInitTime  = 0;
    private static long fbInitTime  = 0;
    private static long iabInitTime = 0;
    private static long kcInitTime  = 0;
    private static long gaInitTime  = 0;

    private GamePlugin() {}

    @Override
    public void init(Application application) {
        final  Application mApplication = application;

        mMainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AdvertiseHelper.getInstance().init(mApplication);
            }
        },6000);

        final long st1 = System.currentTimeMillis();
        AnalyticHelper.getInstance().init(mApplication);

        BackgroundThread.prepareThread();
        BackgroundThread.post(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                SystemUtil.getInstance().setApplication(mApplication);

                long st2 = System.currentTimeMillis();
                acInitTime = st2 - st1;
                FacebookHelper.getInstance().init(mApplication);
                long st3 = System.currentTimeMillis();
                fbInitTime = st3 - st2;
                GoogleIabHelper.getInstance().init(mApplication);
                long st4 = System.currentTimeMillis();
                iabInitTime = st4 - st3;
                KCAnalyticHelper.getInstance().init(mApplication);
                long st5 = System.currentTimeMillis();
                kcInitTime = st5 - st4;
                GameAnalyticsHelper.getInstance().init(mApplication);
                long st6 = System.currentTimeMillis();
                gaInitTime = st6 - st5;
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
            }
        },6000);

        long st1 = System.currentTimeMillis();
        AnalyticHelper.getInstance().onCreate(activity, savedInstanceState);
        final long st2 = System.currentTimeMillis();

        HashMap<String, Object> eventData = new HashMap<>();
        eventData.put("AnalyticHelperInitTime", String.valueOf(acInitTime));
        AnalyticHelper.getInstance().onEvent("GameStartTime", eventData);
        Log.i(TAG, "AnalyticHelperInitTime: "+acInitTime);

        HashMap<String, Object> eventData2 = new HashMap<>();
        eventData2.put("FacebookHelperInitTime", String.valueOf(fbInitTime));
        AnalyticHelper.getInstance().onEvent("GameStartTime", eventData2);
        Log.i(TAG, "FacebookHelperInitTime: "+fbInitTime);

        HashMap<String, Object> eventData3 = new HashMap<>();
        eventData3.put("GoogleIabHelperInitTime", String.valueOf(iabInitTime));
        AnalyticHelper.getInstance().onEvent("GameStartTime", eventData3);
        Log.i(TAG, "GoogleIabHelperInitTime: "+iabInitTime);

        HashMap<String, Object> eventData4 = new HashMap<>();
        eventData4.put("KCAnalyticHelperInitTime", String.valueOf(kcInitTime));
        AnalyticHelper.getInstance().onEvent("GameStartTime", eventData4);
        Log.i(TAG, "KCAnalyticHelperInitTime: "+kcInitTime);

        HashMap<String, Object> eventData5 = new HashMap<>();
        eventData5.put("GameAnalyticsHelperInitTime", String.valueOf(gaInitTime));
        AnalyticHelper.getInstance().onEvent("GameStartTime", eventData5);
        Log.i(TAG, "GameAnalyticsHelperInitTime: "+gaInitTime);

        HashMap<String, Object> eventData6 = new HashMap<>();
        eventData6.put("AnalyticHelperOnCreateTime", String.valueOf(st2-st1));
        AnalyticHelper.getInstance().onEvent("GameStartTime", eventData);
        Log.i(TAG, "AnalyticHelperOnCreateTime: "+(st2-st1));

        BackgroundThread.prepareThread();
        BackgroundThread.post(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                SystemUtil.getInstance().setActivity(mActivity);
                FacebookHelper.getInstance().onCreate(mActivity, mState);
                long st3 = System.currentTimeMillis();
                GoogleIabHelper.getInstance().onCreate(mActivity, mState);
                long st4 = System.currentTimeMillis();
                KCAnalyticHelper.getInstance().onCreate(mActivity, mState);
                long st5 = System.currentTimeMillis();
                GameAnalyticsHelper.getInstance().onCreate(mActivity, mState);
                long st6 = System.currentTimeMillis();

                HashMap<String, Object> eventData = new HashMap<>();
                eventData.put("FacebookHelperOnCreateTime", String.valueOf(st3-st2));
                AnalyticHelper.getInstance().onEvent("GameStartTime", eventData);
                Log.i(TAG, "FacebookHelperOnCreateTime: "+(st3-st2));

                HashMap<String, Object> eventData1 = new HashMap<>();
                eventData1.put("GoogleIabHelperOnCreateTime", String.valueOf(st4-st3));
                AnalyticHelper.getInstance().onEvent("GameStartTime", eventData1);
                Log.i(TAG, "GoogleIabHelperOnCreateTime: "+(st4-st3));

                HashMap<String, Object> eventData2 = new HashMap<>();
                eventData2.put("KCAnalyticHelperOnCreateTime", String.valueOf(st5-st4));
                AnalyticHelper.getInstance().onEvent("GameStartTime", eventData2);
                Log.i(TAG, "KCAnalyticHelperOnCreateTime: "+(st5-st4));

                HashMap<String, Object> eventData3 = new HashMap<>();
                eventData3.put("GameAnalyticsHelperOnCreateTime", String.valueOf(st6-st5));
                AnalyticHelper.getInstance().onEvent("GameStartTime", eventData3);
                Log.i(TAG, "GameAnalyticsHelperOnCreateTime: "+(st6-st5));
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
            if(mHandler != null)
                mHandler.post(runnable);
        }

        public static void postDelayed(final Runnable runnable, long nDelay) {
            if(mHandler != null)
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
