package com.joycastle.gamepluginbase;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by geekgy on 16/4/23.
 */
public class SystemUtil {
    private static final String TAG = "SystemUtil";

    public static Application application;
    public static Activity activity;
    private static KProgressHUD hud;

    private static SystemUtil instance = new SystemUtil();

    public static SystemUtil getInstance() { return instance; }

    public static void setApplication(Application application) {
        SystemUtil.application = application;
    }

    public static void setActivity(Activity activity) {
        SystemUtil.activity = activity;
    }

    /**
     * 获取启动时间
     * @return
     */
    public static long getCpuTime(JSONObject json) {
        return SystemClock.elapsedRealtime();
    }

    /**
     * 获取manifest中meta-data的值
     * @param context
     * @param key
     * @return
     */
    public static String getMetaData(Context context, String key) {
        String value = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            Bundle metaData = packageInfo.applicationInfo.metaData;
            Object obj = metaData.get(key);
            if (obj != null) {
                value = obj.toString();
            } else {
                Log.e(TAG, "meta-data: "+key+" didn't declare");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } finally {
            return value;
        }
    }

    public JSONObject getBundleId() {
        JSONObject respData = new JSONObject();
        try {
            respData.put("bundleId","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return respData;
    }

    public JSONObject getAppName() {
        JSONObject respData = new JSONObject();
        try {
            respData.put("appName","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return respData;
    }

    public static int getAppVersion() {
        int versionCode = 0;
        try {
            PackageInfo packageInfo = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public JSONObject getCountryCode() {
        JSONObject respData = new JSONObject();
        try {
            respData.put("countryCode","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return respData;
    }

    public JSONObject getLanguageCode() {
        JSONObject respData = new JSONObject();
        try {
            respData.put("languageCode","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return respData;
    }

    public JSONObject getDeviceName() {
        JSONObject respData = new JSONObject();
        try {
            respData.put("deviceName","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return respData;
    }

    public JSONObject getSystemVersion() {
        JSONObject respData = new JSONObject();
        try {
            respData.put("systemVersion","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return respData;
    }

    public JSONObject getNetworkState() {
        JSONObject respData = new JSONObject();
        try {
            respData.put("networkState","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return respData;
    }

    public static void showAlertDialog(JSONObject json, InvokeJavaMethodDelegate delegate) {
        Log.e(TAG, "showAlertDialog: "+json);
        try {
            delegate.onFinish(new JSONObject("{}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void showProgressDialog(String message, int percent) {

    }

    public static void hideProgressDialog() {

    }

    public static void showLoading(String message) {
        if (hud != null) {
            return;
        }
        hud = KProgressHUD.create(SystemUtil.activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(message)
                .setCancellable(false)
                .show();
    }

    public static void hideLoading() {
        if (hud == null) {
            return;
        }
        hud.dismiss();
        hud = null;
    }

    public static void showMessage(String message) {

    }

    public static void vibrate() {

    }

    public static void saveImage(String imgPath, String album) {

    }

    public static void sendEmail(String subject, ArrayList<String> toRecipients, String emailBody) {

    }

    public static void setNotificationState(boolean enabled) {

    }

    public static void postNotication(ArrayList<Map<String, Object>> notifications) {
        Log.e(TAG, "postNotication: "+notifications.toString());
//        Context mContext = application.getApplicationContext();
//        // 获取通知服务对象NotificationManager
//        NotificationManager notiManager = (NotificationManager)activity.getSystemService(mContext.NOTIFICATION_SERVICE);
//        Notification.Builder builder = new Notification.Builder(mContext);
////        Intent intent = new Intent(context, MainActivity.class);  //需要跳转指定的页面
////        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
////        builder.setContentIntent(pendingIntent);
////        builder.setSmallIcon(R.mipmap.ic_launcher);// 设置图标
//        builder.setContentTitle("标题");// 设置通知的标题
//        builder.setContentText("内容");// 设置通知的内容
//        builder.setWhen(System.currentTimeMillis());// 设置通知来到的时间
//        builder.setAutoCancel(true); //自己维护通知的消失
//        builder.setTicker("new message");// 第一次提示消失的时候显示在通知栏上的
//        builder.setOngoing(true);
//        builder.setNumber(20);
//
//        Notification notification = builder.build();
//        notification.flags = Notification.FLAG_NO_CLEAR;  //只有全部清除时，Notification才会清除
//        notiManager.notify(0,notification);

    }

    public static void share() {

    }

    public static void copyToPasteboard(String str) {

    }
}

















