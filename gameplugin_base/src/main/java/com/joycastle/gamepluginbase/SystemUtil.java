package com.joycastle.gamepluginbase;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by geekgy on 16/4/23.
 */
public class SystemUtil {
    private static final String TAG = "SystemUtil";
    private static SystemUtil instance = new SystemUtil();

    private Application application;
    private Activity activity;
    private KProgressHUD hud;
    private static final String PREFS_FILE = "device_id";
    private static final String PREFS_DEVICE_ID = "device_id";
    private static String uuid;

    public static SystemUtil getInstance() { return instance; }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Application getApplication() {
        return this.application;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return this.activity;
    }

    /**
     * 获取运行模式
     * @return
     */
    public int getDebugMode() {
        String isDebug = BuildConfig.DEBUG ? "0" : "1";
        //TODO: 1 DEBUG, 2 RELEASE, 3 SUBMISSION
        return 3;
    }

    /**
     * 获取manifest中meta-data的值
     * @param key
     * @return
     */
    public String getMetaData(String key) {
        String value = "";
        try {
            PackageManager packageManager = application.getPackageManager();
            String packageName = application.getPackageName();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            Bundle metaData = packageInfo.applicationInfo.metaData;
            Object obj = metaData.get(key);
            if (obj != null) {
                value = obj.toString();
            } else {
                Log.e(TAG, "meta-data: " + key + " didn't declare");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取包名
     * @return
     */
    public String getPackageName() {
        PackageManager packageManager = application.getPackageManager();
        return application.getPackageName();
    }

    /**
     * 获取App名称
     * @return
     */
    public String getAppName() {
        PackageManager pm = application.getPackageManager();
        return application.getApplicationInfo().loadLabel(pm).toString();
    }

    /**
     * 获取VersionName
     * @return
     */
    public String getVersionName() {
        String versionName = "default";
        try {
            PackageInfo packageInfo = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取VersionCode
     * @return
     */
    public int getVersionCode() {
        int versionCode = -1;
        try {
            PackageInfo packageInfo = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public String getDeviceName() {
        return Build.MODEL;
    }

    public String getDeviceModel() {
        return Build.MODEL;
    }

    public String getDeviceType() {
        return Build.ID;
    }

    public String getSystemName() {
        return "Android OS";
    }

    public String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    public String getUUID() {
        if (uuid == null) {
            synchronized (SystemUtil.class) {
                if (uuid == null) {
                    Context context = this.application.getApplicationContext();
                    final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);
                    final String id = prefs.getString(PREFS_DEVICE_ID, null);

                    if (id != null) {
                        // Use the ids previously computed and stored in the prefs file
                        uuid = id;
                    } else {

                        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

                        // Use the Android ID unless it's broken, in which case fallback on deviceId,
                        // unless it's not available, then fallback on a random number which we store
                        // to a prefs file
                        try {
                            if (!"9774d56d682e549c".equals(androidId)) {
                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
                            } else {
                                final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                                uuid = deviceId!=null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString() : UUID.randomUUID().toString();
                            }
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }

                        // Write the value out to the prefs file
                        prefs.edit().putString(PREFS_DEVICE_ID, uuid).commit();
                    }
                }
            }
        }
        return uuid;
    }

    public String getCountryCode() {
        return application.getResources().getConfiguration().locale.getCountry();
    }

    public String getLanguageCode() {
        return application.getResources().getConfiguration().locale.getLanguage();
    }

    /**
     * 获取启动时间
     * @return
     */
    public long getCpuTime() {
        return SystemClock.elapsedRealtime();
    }

    public String getNetworkState() {
        String ret = "NotReachable";
        ConnectivityManager manager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null) {
                int type = networkInfo.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    ret = "ReachableViaWiFi";
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    ret = "ReachableViaWWAN";
                }
            }
        }
        return ret;
    }

    public void showAlertDialog(String title, String message, String btnTitle1, String btnTitle2, final InvokeJavaMethodDelegate delegate) {
        assert(false);
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(btnTitle1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ArrayList<Object> arrayList = new ArrayList<>();
                        arrayList.add(true);
                        delegate.onFinish(arrayList);
                    }
                })
                .setNegativeButton(btnTitle2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ArrayList<Object> arrayList = new ArrayList<>();
                        arrayList.add(false);
                        delegate.onFinish(arrayList);
                    }
                })
                .setCancelable(false)
                .show();
    }

    public void showProgressDialog(String message, int percent) {
        assert(false);
    }

    public void hideProgressDialog() {
        assert(false);
    }

    public void showLoading(String message) {
        if (hud != null) {
            return;
        }
        hud = KProgressHUD.create(this.activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(message)
                .setCancellable(false)
                .show();
    }

    public void hideLoading() {
        if (hud == null) {
            return;
        }
        hud.dismiss();
        hud = null;
    }

    public void showMessage(String message) {
        assert(false);
    }

    public void vibrate() {
        Vibrator mVibrator = (Vibrator) application.getSystemService(Service.VIBRATOR_SERVICE);
        mVibrator.vibrate(new long[]{1000, 3000}, -1);
    }

    public void saveImage(String imgPath, String album) {
        assert(false);
    }

    public void sendEmail(String subject, ArrayList<String> toRecipients, String emailBody) {
        assert(false);
        String[] reciver = new String[] { "testgameplugin@gmail.com" };
        Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);
        myIntent.setType("plain/text");
        myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, reciver);
        myIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        myIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailBody);
        activity.startActivity(Intent.createChooser(myIntent, "mail test"));
    }

    public void setNotificationState(boolean enabled) {
        Context context = activity.getApplicationContext();
        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(context);
        mNotificationManager.cancelAll();
    }

    public void postNotication(JSONObject notifications) {
        Log.e(TAG, "postNotication: " + notifications.toString());
//
//        Context context = activity.getApplicationContext();
//
//        AlarmManager alarmMgr = (AlarmManager) context
//                .getSystemService(Context.ALARM_SERVICE);
//        //包装需要执行Service的Intent
//        Intent intent = new Intent(context, activity.getClass());
//        intent.setAction("notifcation");
//        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
//                intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        //
//        int triggerAtTime = 10;
//        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
//        {
//            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendingIntent);
//        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
//            alarmMgr.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendingIntent);
//        }else{
//            alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendingIntent);
//        }
//
//
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(activity.getApplicationContext())
//                        .setAutoCancel(true)
//                        .setOngoing(true)
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setDefaults(NotificationCompat.DEFAULT_ALL)
//                        .setNumber(0)
//                        .setContentTitle("blackjack")
//                        .setContentText("blackjack")
//                        .setWhen(System.currentTimeMillis());
//
//        Intent resultIntent = new Intent(context,activity.getClass());
//
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,0, resultIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        mBuilder.setContentIntent(resultPendingIntent);
//        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(context);
//        mNotificationManager.notify(0, mBuilder.build());
    }

    /*
      * name:通知名字，作为通知id使用
      * content：通知内容
      * time：倒时时（秒）
      * */
    public void addLocalNotication(String name, String content, int time)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.SECOND, (int) time);

        Intent intent = new Intent(activity, NotificationReceiver.class);
        intent.setClass(activity, NotificationReceiver.class);
        intent.setData(Uri.parse(name));
        intent.putExtra("msg", "blackjack");
        intent.putExtra("content", content);

        PendingIntent pi = PendingIntent.getBroadcast(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
    }

    public void copyToClipboard(String text){
        Context context = this.application.getApplicationContext();
        ClipboardManager clip = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        //clip.getText(); // 粘贴
        clip.setText(text); // 复制
    }

    public void setBadgeNum(int num) {
        if (num <= 0) {
            num = 0;
        } else {
            num = Math.max(0, Math.min(num, 99));
        }
        Context context = this.application.getApplicationContext();
        if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
            sendToXiaoMi(context,num);
        } else if (Build.MANUFACTURER.equalsIgnoreCase("huawei")) {
            sendToHuaWei(context,num);
        } else if (Build.MANUFACTURER.equalsIgnoreCase("sony")) {
            sendToSony(context,num);
        } else if (Build.MANUFACTURER.toLowerCase().contains("samsung")) {
            sendToSamsumg(context,num);
        } else {
            Log.d(TAG, "setBadgeNum  Not Support ");
        }
    }

    /**
     * 向华为手机发送未读消息广播
     */
    private static void sendToHuaWei(Context context,int count) {
        String launcherClassName = getLauncherClassName(context);
        Bundle localBundle = new Bundle();//需要存储的数据
        localBundle.putString("package", context.getPackageName());//包名
        localBundle.putString("class", launcherClassName);
        localBundle.putInt("badgenumber", count);//未读信息条数
        context.getContentResolver().call(
                Uri.parse("content://com.huawei.android.launcher.settings/badge/"),
                "change badge", null, localBundle);
    }
    /**
     * 向小米手机发送未读消息数广播
     * @param count
     */
    private static void sendToXiaoMi(Context context,int count) {
        try{
            Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
            Object miuiNotification = miuiNotificationClass.newInstance();
            Field field = miuiNotification.getClass().getDeclaredField("messageCount");
            field.setAccessible(true);
            field.set(miuiNotification,String.valueOf(count==0?"":count));
        }catch (Exception e){
            e.printStackTrace();
            // miui6之前
            Intent localIntent = new Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE");
            localIntent.putExtra("android.intent.extra.update_application_component_name",context.getPackageName()+"/."+"MainActivity");
            localIntent.putExtra("android.intent.extra.update_application_message_text",String.valueOf(count==0?"":count));
            context.sendBroadcast(localIntent);
        }
    }

    /**
     * 向索尼手机发送未读消息数广播<br/>
     * 据说：需添加权限：<uses-permission android:name="com.sonyericsson.home.permission.BROADCAST_BADGE" /> [未验证]
     * @param count
     */
    private static void sendToSony(Context context, int count){
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }

        boolean isShow = true;
        if (count == 0) {
            isShow = false;
        }
        Intent localIntent = new Intent();
        localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE",isShow);//是否显示
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME",launcherClassName );//启动页
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", String.valueOf(count));//数字
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.getPackageName());//包名
        context.sendBroadcast(localIntent);
    }

    /**
     * 向三星手机发送未读消息数广播
     * @param count
     */
    private static void sendToSamsumg(Context context, int count){
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }

    /**
     * Retrieve launcher activity name of the application from the context
     *
     * @param context The context of the application package.
     * @return launcher activity name of this application. From the
     *         "android:name" attribute.
     */
    private static String getLauncherClassName(Context context) {
        PackageManager packageManager = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        // To limit the components this Intent will resolve to, by setting an
        // explicit package name.
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        // All Application must have 1 Activity at least.
        // Launcher activity must be found!
        ResolveInfo info = packageManager
                .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        // get a ResolveInfo containing ACTION_MAIN, CATEGORY_LAUNCHER
        // if there is no Activity which has filtered by CATEGORY_DEFAULT
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }

        return info.activityInfo.name;
    }

    public void share() {
        assert(false);
    }

    public void keychainSet(String key, String value) {
        //TODO:
    }

    public String keychainGet(String key) {
        //TODO:
        return null;
    }

    /**
     * 判断手机是否ROOT
     */
    public boolean isRoot() {
        boolean root = false;
        try {
            if ((!new File("/system/bin/su").exists())
                    && (!new File("/system/xbin/su").exists())) {
                root = false;
            } else {
                root = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }

    public void copyToPasteboard(String str) {
        assert(false);
    }

    public void requestUrl(String requestType, String url, HashMap<String, Object> map, InvokeJavaMethodDelegate delegate) {
        assert(false);
    }
}

class NotificationReceiver extends BroadcastReceiver
{
    public NotificationReceiver(){}

    @Override
    public void onReceive(Context context, Intent itent) {
        String msg = itent.getStringExtra("msg");
        String content = itent.getStringExtra("content");
        //推送一条通知
        shownotification(context,msg,content);
        return;
    }

    public void shownotification(Context context,String title, String msg)
    {
        Activity activity = SystemUtil.getInstance().getActivity();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(activity.getApplicationContext())
                        .setAutoCancel(true)
                        .setOngoing(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setNumber(0)
                        .setContentTitle(title)
                        .setContentText(msg)
                        .setWhen(System.currentTimeMillis());

        Intent resultIntent = new Intent(context,activity.getClass());

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(context);
        mNotificationManager.notify(0, mBuilder.build());
    }
}

















