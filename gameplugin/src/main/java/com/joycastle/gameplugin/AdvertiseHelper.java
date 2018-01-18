package com.joycastle.gameplugin;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.joycastle.gamepluginbase.AdvertiseDelegate;
import com.joycastle.gamepluginbase.InvokeJavaMethodDelegate;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by gaoyang on 9/29/16.
 */

public class AdvertiseHelper implements AdvertiseDelegate {
    public static final String TAG = "AdvertiseHelper";

    public static AdvertiseHelper instance = new AdvertiseHelper();

    private ArrayList<AdvertiseDelegate> delegates;

    public static AdvertiseHelper getInstance() { return instance; }

    private AdvertiseHelper() {
        delegates = new ArrayList<>();
    }

    @Override
    public int showBannerAd(boolean protrait, boolean bottom, BannerAdListener listener) {
        if (delegates.size() <= 0) {
            return 0;
        }
        return delegates.get(0).showBannerAd(protrait, bottom, listener);
    }

    @Override
    public void hideBannerAd() {
        if (delegates.size() <= 0) {
            return;
        }
        delegates.get(0).hideBannerAd();
    }

    @Override
    public boolean isInterstitialAdReady() {
        boolean result = false;
        for (AdvertiseDelegate delegate : delegates) {
            result = delegate.isInterstitialAdReady();
            if (result) {
                break;
            }
        }
        return result;
    }

    @Override
    public boolean showInterstitialAd(InterstitialAdListener listener) {
        boolean result = false;
        for (AdvertiseDelegate delegate : delegates) {
            result = delegate.showInterstitialAd(listener);
            if (result) {
                break;
            }
        }
        return result;
    }

    @Override
    public boolean isVideoAdReady() {
        boolean result = false;
        for (AdvertiseDelegate delegate : delegates) {
            result = delegate.isVideoAdReady();
            if (result) {
                break;
            }
        }
        return result;
    }

    @Override
    public boolean showVideoAd(InvokeJavaMethodDelegate listener) {
        boolean result = false;
        for (AdvertiseDelegate delegate : delegates) {
            result = delegate.showVideoAd(listener);
            if (result) {
                break;
            }
        }
        return result;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void init(Application application) {
        try {
            Class clazz = Class.forName("com.joycastle.advertise.admob.AMAdvertiseHelper");
            Method method = clazz.getMethod("getInstance");
            AdvertiseDelegate delegate = (AdvertiseDelegate) method.invoke(null);
            delegates.add(delegate);
            delegate.init(application);
        } catch (Exception e) {
            Log.e(TAG, "Admob is disable");
        }

//        try {
//            Class clazz = Class.forName("com.joycastle.advertise.adcolony.ACAdvertiseHelper");
//            Method method = clazz.getMethod("getInstance");
//            AdvertiseDelegate delegate = (AdvertiseDelegate) method.invoke(null);
//            delegates.add(delegate);
//            delegate.init(application);
//        } catch (Exception e) {
//            Log.e(TAG, "Adcolony is disable");
//        }
    }

    @Override
    public void onCreate(Activity activity, Bundle savedInstanceState) {
        for (AdvertiseDelegate delegate : delegates) {
            delegate.onCreate(activity, savedInstanceState);
        }
    }

    @Override
    public void onStart(Activity activity) {
        for (AdvertiseDelegate delegate : delegates) {
            delegate.onStart(activity);
        }
    }

    @Override
    public void onResume(Activity activity) {
        for (AdvertiseDelegate delegate : delegates) {
            delegate.onResume(activity);
        }
    }

    @Override
    public void onPause(Activity activity) {
        for (AdvertiseDelegate delegate : delegates) {
            delegate.onPause(activity);
        }
    }

    @Override
    public void onStop(Activity activity) {
        for (AdvertiseDelegate delegate : delegates) {
            delegate.onStop(activity);
        }
    }

    @Override
    public void onDestroy(Activity activity) {
        for (AdvertiseDelegate delegate : delegates) {
            delegate.onDestroy(activity);
        }
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        for (AdvertiseDelegate delegate : delegates) {
            delegate.onActivityResult(activity, requestCode, resultCode, data);
        }
    }
}
