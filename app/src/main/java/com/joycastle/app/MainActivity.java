package com.joycastle.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.joycastle.gameplugin.AdvertiseHelper;
import com.joycastle.gameplugin.AnalyticHelper;
import com.joycastle.gameplugin.GamePlugin;
import com.joycastle.gamepluginbase.InvokeJavaMethodDelegate;
import com.joycastle.gamepluginbase.SystemUtil;
import com.joycastle.iab.googleplay.util.IabHelper;
import com.joycastle.my_facebook.FacebookHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    static {
        System.loadLibrary("cocos2dlua");
    }

    private static final String TAG = "MainActivity";

    private ArrayList<HashMap<String, OnClickListener>> arrayList = null;

    interface OnClickListener {
        void onClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        GamePlugin.getInstance().onCreate(this, savedInstanceState);

        arrayList = new ArrayList<>();
        HashMap<String, OnClickListener> hashMap;

        ///////////////////////////////Analytic///////////////////////////////
        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("--------AnalyticHelper", null);

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("setAccoutInfo", new OnClickListener() {
            @Override
            public void onClick() {
                HashMap map = new HashMap();
                map.put("userId", "00001");
                map.put("gender", "male");
                map.put("age", 20);
                AnalyticHelper.getInstance().setAccoutInfo(map);
//                JSONObject reqData = new JSONObject();
//                JSONArray json =new JSONArray();
//                JSONObject jso = new JSONObject();
//                jso.put("test","hello");
//                json.put(jso);
//                reqData.put("json", json);
//                System.out.print("request json : "+reqData.toString());
//                NativeUtil.invokeJavaMethod("com.joycastle.gamepluginbase.SystemUtil","showAlertDialog",reqData.toString(),0);
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("onEvent", new OnClickListener() {
            @Override
            public void onClick() {
                AnalyticHelper.getInstance().onEvent("dead");
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("onEventWithLabel", new OnClickListener() {
            @Override
            public void onClick() {
                AnalyticHelper.getInstance().onEvent("dead", "10");
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("onEventWithData", new OnClickListener() {
            @Override
            public void onClick() {
                HashMap<String, String> map = new HashMap<>();
                map.put("level", "10");
                map.put("score", "100");
                AnalyticHelper.getInstance().onEvent("dead", map);
            }
        });

        ///////////////////////////////Facebook///////////////////////////////
        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("--------FaceBook", null);

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("inviteFBFriend", new OnClickListener() {

            @Override
            public void onClick() {
//                JSONObject reqData = new JSONObject();
//                JSONArray json =new JSONArray();
//                JSONObject jso = new JSONObject();
//                try {
//                    json.put(jso);
//                    json.put("blackjack");
//                    json.put("blackjack");
//                    reqData.put("json", json);
//                } catch (JSONException e) {`
//                    e.printStackTrace();
//                }
//                System.out.print("request json : "+reqData.toString());
//                NativeUtil.invokeJavaMethod("com.joycastle.my_facebook.FacebookHelper","confirmRequest",reqData.toString(),1);

                FacebookHelper.getInstance().confirmRequest(null, "black", "black", new InvokeJavaMethodDelegate() {
                    @Override
                    public void onFinish(ArrayList<Object> resArrayList) {

                    }

                });
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("facebooklogin", new OnClickListener() {

            @Override
            public void onClick() {
                FacebookHelper.getInstance().login();
//                JSONObject reqData = new JSONObject();
//                JSONArray json =new JSONArray();
//                JSONObject jso = new JSONObject();
//                try {
//                    json.put(jso);
//                    reqData.put("json", json);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                System.out.print("request json : "+reqData.toString());
//                NativeUtil.invokeJavaMethod("com.joycastle.my_facebook.FacebookHelper","login",reqData.toString(),-1);
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("facebooklogout", new OnClickListener() {

            @Override
            public void onClick() {
                FacebookHelper.getInstance().logout();
//                NativeUtil.invokeJavaMethod("com.joycastle.my_facebook.FacebookHelper","logout","{}",-1);
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("facebookIslogin", new OnClickListener() {

            @Override
            public void onClick() {
                boolean islogin = FacebookHelper.getInstance().isLogin();
                Log.i(TAG, "isLogin: "+islogin);
//                NativeUtil.invokeJavaMethod("com.joycastle.my_facebook.FacebookHelper","isLogin","{}",-1);
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("facebookGetUserId", new OnClickListener() {

            @Override
            public void onClick() {

                String uid = FacebookHelper.getInstance().getUserID();
                Log.i(TAG, "getUserId: "+uid);
//                NativeUtil.invokeJavaMethod("com.joycastle.my_facebook.FacebookHelper","getUserId","{}",-1);
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("getFacebookProfile", new OnClickListener() {

            @Override
            public void onClick() {
//               String str = FacebookHelper.getInstance().getUserProfile(null,null);
                FacebookHelper.getInstance().getUserProfile("",0, new InvokeJavaMethodDelegate() {
                    @Override
                    public void onFinish(ArrayList<Object> resArrayList) {

                    }
                });

//                NativeUtil.invokeJavaMethod("com.joycastle.my_facebook.FacebookHelper","getUserProfile","{}",1);
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("getAccessToken", new OnClickListener() {

            @Override
            public void onClick() {
                String token = FacebookHelper.getInstance().getAccessToken();
                Log.i(TAG, "getAccessToken: "+token);
//                NativeUtil.invokeJavaMethod("com.joycastle.my_facebook.FacebookHelper","getAccessToken","{}",-1);
            }
        });


        ///////////////////////////////Advertise///////////////////////////////
        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("--------AdvertiseHelper", null);

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("showBannerAd", new OnClickListener() {
            @Override
            public void onClick() {
                int height = AdvertiseHelper.getInstance().showBannerAd(true, true);
                Log.e(TAG, height+"");
//                NativeUtil.invokeJavaMethod("com.joycastle.gameplugin.AdvertiseHelper","showBannerAd","{}",-1);
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("hideBannerAd", new OnClickListener() {
            @Override
            public void onClick() {
                AdvertiseHelper.getInstance().hideBannerAd();
//                NativeUtil.invokeJavaMethod("com.joycastle.gameplugin.AdvertiseHelper","hideBannerAd","{}",-1);
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("isInterstitialAdReady", new OnClickListener() {
            @Override
            public void onClick() {
                boolean result = AdvertiseHelper.getInstance().isInterstitialAdReady();
                Log.e(TAG, "isInterstitialAdReady: "+result);
//                NativeUtil.invokeJavaMethod("com.joycastle.gameplugin.AdvertiseHelper","isInterstitialAdReady","{}",-1);
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("showInterstitialAd", new OnClickListener() {
            @Override
            public void onClick() {
                boolean result = AdvertiseHelper.getInstance().showInterstitialAd(new InvokeJavaMethodDelegate() {
                    @Override
                    public void onFinish(ArrayList<Object> resArrayList) {
                        Log.e(TAG, "showInterstitialAd Result: "+resArrayList);
                    }
                });
                Log.e(TAG, "showInterstitialAd: "+result);

//                JSONObject reqData = new JSONObject();
//                JSONArray json = new JSONArray();
//                try {
//                    reqData.put("json", json);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                NativeUtil.invokeJavaMethod("com.joycastle.gameplugin.AdvertiseHelper","showInterstitialAd",reqData.toString(),0);
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("isVideoAdReady", new OnClickListener() {
            @Override
            public void onClick() {
                boolean result = AdvertiseHelper.getInstance().isVideoAdReady();
                Log.e(TAG, "isVideoAdReady: "+result);

//                JSONObject reqData = new JSONObject();
//                JSONArray json =new JSONArray();
//                try {
//                    reqData.put("json", json);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                NativeUtil.invokeJavaMethod("com.joycastle.gameplugin.AdvertiseHelper","isVideoAdReady",reqData.toString(),-1);
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("showVideoAd", new OnClickListener() {
            @Override
            public void onClick() {
                boolean result = AdvertiseHelper.getInstance().showVideoAd(new InvokeJavaMethodDelegate() {
                    @Override
                    public void onFinish(ArrayList<Object> resArrayList) {
                        Log.e(TAG, "showVideoAd Result: "+resArrayList);
                    }
                });
                Log.e(TAG, "showVideoAd: "+result);

//                JSONObject reqData = new JSONObject();
//                JSONArray json =new JSONArray();
//                try {
//                    reqData.put("json", json);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                NativeUtil.invokeJavaMethod("com.joycastle.gameplugin.AdvertiseHelper","showVideoAd",reqData.toString(),0);
            }
        });

        ///////////////////////////////IAB///////////////////////////////
        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("--------IAB", null);

        GamePlugin.getInstance().setNotifyHandler(new InvokeJavaMethodDelegate() {
            @Override
            public void onFinish(ArrayList<Object> resArrayList) {

            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("purchase", new OnClickListener() {
            @Override
            public void onClick() {
//                IabHelper.getInstance().purchase("managed_product", "user_001", new IabDelegate.PurchaseDelegate() {
//                    @Override
//                    public void onResult(boolean result, String message) {
//                        showAlert(result+"\n"+message);
//                    }
//                });

                GamePlugin.getInstance().doIap("blackjack.chip1", "user_001", new InvokeJavaMethodDelegate() {
                    @Override
                    public void onFinish(ArrayList<Object> resArrayList) {

                    }
                });
            }
        });

        ///////////////////////////////GamePlugin///////////////////////////////
        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("--------GamePlugin", null);

        ///////////////////////////////SystemUtil///////////////////////////////
        addToArrayList("--------SystemUtil", null);
        addToArrayList("getDebugMode", new OnClickListener() {
            @Override
            public void onClick() {
                int debugMode = SystemUtil.getInstance().getDebugMode();
                showAlert(String.valueOf(debugMode));
            }
        });
        addToArrayList("getMetaData", new OnClickListener() {
            @Override
            public void onClick() {
                String value = SystemUtil.getInstance().getMetaData("flurry_key");
                showAlert(value);
            }
        });
        addToArrayList("getPackageName", new OnClickListener() {
            @Override
            public void onClick() {
                String packageName = SystemUtil.getInstance().getPackageName();
                showAlert(packageName);
            }
        });
        addToArrayList("getAppName", new OnClickListener() {
            @Override
            public void onClick() {
                String appName = SystemUtil.getInstance().getAppName();
                showAlert(appName);
            }
        });
        addToArrayList("getVersionName", new OnClickListener() {
            @Override
            public void onClick() {
                String versionName = SystemUtil.getInstance().getVersionName();
                showAlert(versionName);
            }
        });
        addToArrayList("getVersionCode", new OnClickListener() {
            @Override
            public void onClick() {
                int versionCode = SystemUtil.getInstance().getVersionCode();
                showAlert(String.valueOf(versionCode));
            }
        });
        addToArrayList("getDeviceName", new OnClickListener() {
            @Override
            public void onClick() {
                String deviceName = SystemUtil.getInstance().getDeviceName();
                showAlert(deviceName);
            }
        });
        addToArrayList("getDeviceModel", new OnClickListener() {
            @Override
            public void onClick() {
                String deviceModel = SystemUtil.getInstance().getDeviceModel();
                showAlert(deviceModel);
            }
        });
        addToArrayList("getDeviceType", new OnClickListener() {
            @Override
            public void onClick() {
                String deviceType = SystemUtil.getInstance().getDeviceType();
                showAlert(deviceType);
            }
        });
        addToArrayList("getSystemName", new OnClickListener() {
            @Override
            public void onClick() {
                String systemName = SystemUtil.getInstance().getSystemName();
                showAlert(systemName);
            }
        });
        addToArrayList("getSystemVersion", new OnClickListener() {
            @Override
            public void onClick() {
                String systemVersion = SystemUtil.getInstance().getSystemVersion();
                showAlert(systemVersion);
            }
        });
        addToArrayList("getUUID", new OnClickListener() {
            @Override
            public void onClick() {
                String uuid = SystemUtil.getInstance().getUUID();
                showAlert(uuid);
            }
        });
        addToArrayList("getCountryCode", new OnClickListener() {
            @Override
            public void onClick() {
                String countryCode = SystemUtil.getInstance().getCountryCode();
                showAlert(countryCode);
            }
        });
        addToArrayList("getLanguageCode", new OnClickListener() {
            @Override
            public void onClick() {
                String languageCode = SystemUtil.getInstance().getLanguageCode();
                showAlert(languageCode);
            }
        });
        addToArrayList("getCpuTime", new OnClickListener() {
            @Override
            public void onClick() {
                long cpuTime = SystemUtil.getInstance().getCpuTime();
                showAlert(String.valueOf(cpuTime));
            }
        });
        addToArrayList("showLoading & hideLoading", new OnClickListener() {
            @Override
            public void onClick() {
                SystemUtil.getInstance().showLoading("Loading...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SystemUtil.getInstance().hideLoading();
                    }
                }, 5000);
            }
        });
        addToArrayList("vibrate", new OnClickListener() {
            @Override
            public void onClick() {
                SystemUtil.getInstance().vibrate();
            }
        });
        addToArrayList("setNotificationState: true", new OnClickListener() {
            @Override
            public void onClick() {
                SystemUtil.getInstance().setNotificationState(true);
            }
        });
        addToArrayList("setNotificationState: false", new OnClickListener() {
            @Override
            public void onClick() {
                SystemUtil.getInstance().setNotificationState(false);
            }
        });
        addToArrayList("postNotication", new OnClickListener() {
            @Override
            public void onClick() {
                HashMap<String,Object> reqData = new HashMap<>();
                reqData.put("message","hello world ！！！");
                reqData.put("delay",10);
                SystemUtil.getInstance().postNotication(reqData);
            }
        });
        addToArrayList("copyToClipboard", new OnClickListener() {
            @Override
            public void onClick() {
                SystemUtil.getInstance().copyToPasteboard("hello world");
            }
        });
        addToArrayList("setBadgeNum", new OnClickListener() {
            @Override
            public void onClick() {
                SystemUtil.getInstance().setBadgeNum(3);
            }
        });
        addToArrayList("keychainSet", new OnClickListener() {
            @Override
            public void onClick() {
                SystemUtil.getInstance().keychainSet("hahaha", "hehehe");
            }
        });
        addToArrayList("keychainGet", new OnClickListener() {
            @Override
            public void onClick() {
                String value = SystemUtil.getInstance().keychainGet("hahaha");
                showAlert(value);
            }
        });
        addToArrayList("isRoot", new OnClickListener() {
            @Override
            public void onClick() {
                boolean isRoot = SystemUtil.getInstance().isJailbroken();
                showAlert(String.valueOf(isRoot));
            }
        });

        List<String> data = new ArrayList<>();
        for (HashMap<String, OnClickListener> item : arrayList) {
            data.add(item.entrySet().iterator().next().getKey());
        }
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, data));
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        HashMap<String, OnClickListener> hashMap = arrayList.get(i);
        OnClickListener listener = hashMap.entrySet().iterator().next().getValue();
        if (listener != null)
            listener.onClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GamePlugin.getInstance().onStart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GamePlugin.getInstance().onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        GamePlugin.getInstance().onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GamePlugin.getInstance().onStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GamePlugin.getInstance().onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GamePlugin.getInstance().onActivityResult(this, requestCode, resultCode, data);
    }

    public void addToArrayList(String name, OnClickListener listener) {
        HashMap<String, OnClickListener> hashMap = new HashMap<>();
        hashMap.put(name, listener);
        arrayList.add(hashMap);
    }

    public void showAlert(String message) {
        message = message==null ? "null" : message;
        new AlertDialog.Builder(this)
                .setMessage(message)
                .create()
                .show();
        Log.e(TAG, message);
    }
}
