package com.joycastle.gamepluginbase;

import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.kaopiz.kprogresshud.KProgressHUD;
import org.json.JSONException;
import org.json.JSONObject;



import java.util.ArrayList;
import java.util.Locale;

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

    public String getBundleId() {
        return "v1.0.0";
    }

    public String getAppName() {
        PackageManager pm = application.getPackageManager();
        String appName = application.getApplicationInfo().loadLabel(pm).toString();
        return appName;
    }

    public int getAppVer(){
        int appVer = getAppVersion();
        return appVer;
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

    public String getCountryCode() {
        String countryCode = application.getResources().getConfiguration().locale.getCountry();
        return countryCode;
    }

    public String getLanguageCode() {
        String languageCode = application.getResources().getConfiguration().locale.getLanguage();
        return languageCode;
    }

    public String getDeviceName() {
        String phoneName = android.os.Build.MODEL ;
        return phoneName;
    }

    public String getSystemVersion() {
        String phoneVersion = android.os.Build.VERSION.RELEASE ;
        return phoneVersion;
    }

    public String getNetworkState() {
        String netState = String.valueOf(getAPNType(this.application.getApplicationContext()));
        return netState;
    }

    /**
     * 获取当前的网络状态 ：没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
     * 自定义
     *
     * @param context
     * @return
     */
    public static int getAPNType(Context context) {
        //结果返回值
        int netType = 0;
        //获取手机所有连接管理对象
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取NetworkInfo对象
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //NetworkInfo对象为空 则代表没有网络
        if (networkInfo == null) {
            return netType;
        }
        //否则 NetworkInfo对象不为空 则获取该networkInfo的类型
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            //WIFI
            netType = 1;
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //3G   联通的3G为UMTS或HSDPA 电信的3G为EVDO
            if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                    && !telephonyManager.isNetworkRoaming()) {
                netType = 4;
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    && !telephonyManager.isNetworkRoaming()) {
                netType = 3;
                //2G 移动和联通的2G为GPRS或EGDE，电信的2G为CDMA
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                    || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                    || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                    && !telephonyManager.isNetworkRoaming()) {
                netType = 2;
            } else {
                netType = 2;
            }
        }
        return netType;
    }

    public void showAlertDialog(JSONObject json, InvokeJavaMethodDelegate delegate) {
        Log.e(TAG, "showAlertDialog: "+json);
        try {
            delegate.onFinish(new JSONObject("{}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public  void showProgressDialog(String message, int percent) {

    }

    public  void hideProgressDialog() {

    }

    public  void showLoading(String message) {
        if (hud != null) {
            return;
        }
        hud = KProgressHUD.create(SystemUtil.activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(message)
                .setCancellable(false)
                .show();
    }

    public  void hideLoading() {
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

        String[] reciver = new String[] { "wangxiaolong@joycastle.mobi" };
        Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);
        myIntent.setType("plain/text");
//        myIntent.setType("message/rfc822");
        myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, reciver);
        myIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        myIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailBody);
        activity.startActivity(Intent.createChooser(myIntent, "mail test"));
    }

    public static void setNotificationState(boolean enabled) {

    }

    //ArrayList<Map<String, Object>> notifications
    public static void postNotication(JSONObject notifications) {
        Log.e(TAG, "postNotication: " + notifications.toString());

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(activity.getApplicationContext())
                        .setAutoCancel(true)
                        .setOngoing(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setNumber(0)
                        .setContentTitle("blackjack")
                        .setContentText("blackjack")
                        .setWhen(System.currentTimeMillis());

        Intent resultIntent = new Intent(activity.getApplicationContext(),activity.getClass());

        PendingIntent resultPendingIntent = PendingIntent.getActivity(activity.getApplicationContext(),0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(activity.getApplicationContext());
        mNotificationManager.notify(0, mBuilder.build());
    }

    public static void share() {
    }

    public static void copyToPasteboard(String str) {
    }
}

















