package com.joycastle.gamepluginbase;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.kaopiz.kprogresshud.KProgressHUD;

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

    public static String getBundleId() {
        return "";
    }

    public static String getAppName() {
        return "";
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

    public static String getCountryCode() {
        return "";
    }

    public static String getLanguageCode() {
        return "";
    }

    public static String getDeviceName() {
        return "";
    }

    public static String getSystemVersion() {
        return "";
    }

    public static String getNetworkState() {
        return "";
    }

    public static void showAlertDialog(String title,
                                       String message,
                                       String cancelBtnTitle,
                                       ArrayList<String> otherBtnTitles) {

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

    }

    public static void share() {

    }

    public static void copyToPasteboard(String str) {

    }
}

















