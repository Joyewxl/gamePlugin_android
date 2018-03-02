package com.joycastle.gameplugin;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.joycastle.gamepluginbase.InvokeJavaMethodDelegate;
import com.joycastle.gamepluginbase.LifeCycleDelegate;
import com.joycastle.gamepluginbase.SystemUtil;
import com.joycastle.iab.googleplay.GoogleIabHelper;
import com.joycastle.my_facebook.FacebookHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by geekgy on 16/5/11.
 */
public class GamePlugin implements LifeCycleDelegate {
    private static final String TAG = "GamePlugin";

    private static GamePlugin instance = new GamePlugin();

    public static GamePlugin getInstance() { return instance; }

    private SharedPreferences sharedPreferences;
    private String _iapVerifyUrl;
    private String _iapVerifySign;
    private InvokeJavaMethodDelegate notifyDelegate;

    private GamePlugin() {}

    @Override
    public void init(Application application) {
        SystemUtil.getInstance().setApplication(application);
        AnalyticHelper.getInstance().init(application);
        AdvertiseHelper.getInstance().init(application);
        FacebookHelper.getInstance().init(application);
        GoogleIabHelper.getInstance().init(application);
    }

    @Override
    public void onCreate(Activity activity, Bundle savedInstanceState) {
        String name = null;
        assert(name!=null);

        Log.i("GamePlugin","GamePlugin onCreate");
        SystemUtil.getInstance().setActivity(activity);
        AnalyticHelper.getInstance().onCreate(activity, savedInstanceState);
        AdvertiseHelper.getInstance().onCreate(activity, savedInstanceState);
        FacebookHelper.getInstance().onCreate(activity, savedInstanceState);
        GoogleIabHelper.getInstance().onCreate(activity, savedInstanceState);

        sharedPreferences = activity.getSharedPreferences("test", activity.MODE_PRIVATE);
    }

    @Override
    public void onStart(Activity activity) {
        AnalyticHelper.getInstance().onStart(activity);
        AdvertiseHelper.getInstance().onStart(activity);
        FacebookHelper.getInstance().onStart(activity);
        GoogleIabHelper.getInstance().onStart(activity);
    }

    @Override
    public void onResume(Activity activity) {
        AnalyticHelper.getInstance().onResume(activity);
        AdvertiseHelper.getInstance().onResume(activity);
        FacebookHelper.getInstance().onResume(activity);
        GoogleIabHelper.getInstance().onResume(activity);
    }

    @Override
    public void onPause(Activity activity) {
        AnalyticHelper.getInstance().onPause(activity);
        AdvertiseHelper.getInstance().onPause(activity);
        FacebookHelper.getInstance().onPause(activity);
        GoogleIabHelper.getInstance().onPause(activity);
    }

    @Override
    public void onStop(Activity activity) {
        AnalyticHelper.getInstance().onStop(activity);
        AdvertiseHelper.getInstance().onStop(activity);
        FacebookHelper.getInstance().onStop(activity);
        GoogleIabHelper.getInstance().onStop(activity);
    }

    @Override
    public void onDestroy(Activity activity) {
        AnalyticHelper.getInstance().onDestroy(activity);
        AdvertiseHelper.getInstance().onDestroy(activity);
        FacebookHelper.getInstance().onDestroy(activity);
        GoogleIabHelper.getInstance().onDestroy(activity);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        AnalyticHelper.getInstance().onActivityResult(activity, requestCode, resultCode, data);
        AdvertiseHelper.getInstance().onActivityResult(activity, requestCode, resultCode, data);
        FacebookHelper.getInstance().onActivityResult(activity, requestCode, resultCode, data);
        GoogleIabHelper.getInstance().onActivityResult(activity, requestCode, resultCode, data);
    }

    public void setNotifyHandler(InvokeJavaMethodDelegate delegate) {
        notifyDelegate = delegate;
    }
    public void setIapVerifyUrlAndSign(String url,String sign) {
        _iapVerifyUrl = url;
        _iapVerifySign = sign;
    }
    public boolean canDoIap() {
        //TODO
        return true;
    }
    public HashMap getSuspensiveIap() throws JSONException {
        String jsonStr = sharedPreferences.getString("suspensiveIap",""));
        JSONObject iapinfo = new JSONObject(jsonStr);
        HashMap hashMap = new HashMap<>();
        Iterator<String> keys = iapinfo.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            hashMap.put(key, iapinfo.getString(key));
        }
        return hashMap;
    }
    public void setSuspensiveIap(HashMap iapInfo) throws JSONException {

        JSONObject jsonObject = new JSONObject();
        Iterator it = iapInfo.keySet().iterator();
        while (it.hasNext()) {
            String key = (String)it.next();
            Object val = iapInfo.get(key);
            jsonObject.put(key, val);
        }
        //得到SharedPreferences.Editor对象，并保存数据到该对象中
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("suspensiveIap", jsonObject.toString());
        //保存key-value对到文件中
        editor.commit();
    }
    public void doIap(String iapId, String payLoad, InvokeJavaMethodDelegate delegate) {
        GoogleIabHelper.getInstance().purchase(iapId,payLoad,delegate);
    }
    public void rateGame() {
        Activity activity = SystemUtil.getInstance().getActivity();
        final String appPackageName = SystemUtil.getInstance().getPackageName(); // getPackageName() from Context or Activity object
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

}
