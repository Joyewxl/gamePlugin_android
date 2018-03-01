package com.joycastle.gameplugin;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.joycastle.gamepluginbase.IabDelegate;
import com.joycastle.gamepluginbase.InvokeJavaMethodDelegate;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by gaoyang on 10/9/16.
 */

public class IabHelper implements IabDelegate {
    private static final String TAG = "IabHelper";

    private static IabHelper instance = new IabHelper();

    private ArrayList<IabDelegate> delegates;

    public static IabHelper getInstance() { return instance; }

    private IabHelper() {
        delegates = new ArrayList<>();
    }

    @Override
    public void init(Application application) {
        try {
            Class clazz = Class.forName("com.joycastle.iab.googleplay.GoogleIabHelper");
            Method method = clazz.getMethod("getInstance");
            IabDelegate delegate = (IabDelegate) method.invoke(null);
            delegates.add(delegate);
            delegate.init(application);
        } catch (Exception e) {
            Log.e(TAG, "GoogleIab is disable");
        }
    }

    @Override
    public void onCreate(Activity activity, Bundle savedInstanceState) {
        for (IabDelegate delegate : delegates) {
            delegate.onCreate(activity, savedInstanceState);
        }
    }

    @Override
    public void onStart(Activity activity) {
        for (IabDelegate delegate : delegates) {
            delegate.onStart(activity);
        }
    }

    @Override
    public void onResume(Activity activity) {
        for (IabDelegate delegate : delegates) {
            delegate.onResume(activity);
        }
    }

    @Override
    public void onPause(Activity activity) {
        for (IabDelegate delegate : delegates) {
            delegate.onPause(activity);
        }
    }

    @Override
    public void onStop(Activity activity) {
        for (IabDelegate delegate : delegates) {
            delegate.onStop(activity);
        }
    }

    @Override
    public void onDestroy(Activity activity) {
        for (IabDelegate delegate : delegates) {
            delegate.onDestroy(activity);
        }
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        for (IabDelegate delegate : delegates) {
            delegate.onActivityResult(activity, requestCode, resultCode, data);
        }
    }

    @Override
    public void setRestoreHandler(RestoreDelegate delegate) {
        for (IabDelegate iabDelegate : delegates) {
            iabDelegate.setRestoreHandler(delegate);
        }
    }

    @Override
    public void purchase(String iapId, String payLoad, InvokeJavaMethodDelegate delegate) {
        for (IabDelegate iabDelegate : delegates) {
            iabDelegate.purchase(iapId, payLoad, delegate);
        }
    }
}
