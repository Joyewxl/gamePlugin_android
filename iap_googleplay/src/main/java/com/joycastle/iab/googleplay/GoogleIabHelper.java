package com.joycastle.iab.googleplay;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.joycastle.gamepluginbase.InvokeJavaMethodDelegate;
import com.joycastle.gamepluginbase.LifeCycleDelegate;
import com.joycastle.gamepluginbase.SystemUtil;
import com.joycastle.iab.googleplay.util.IabBroadcastReceiver;
import com.joycastle.iab.googleplay.util.IabHelper;
import com.joycastle.iab.googleplay.util.IabResult;
import com.joycastle.iab.googleplay.util.Inventory;
import com.joycastle.iab.googleplay.util.Purchase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by gaoyang on 10/9/16.
 */

public class GoogleIabHelper implements LifeCycleDelegate, IabBroadcastReceiver.IabBroadcastListener, IabHelper.QueryInventoryFinishedListener {
    private static String TAG = "GoogleIabHelper";

    private static GoogleIabHelper instance = new GoogleIabHelper();

    private IabHelper mHelper;
    private IabBroadcastReceiver mBroadcastReceiver;
    private String mVerifyUrl;
    private String mVerifySign;
    private SharedPreferences sharedPreferences;

    public static GoogleIabHelper getInstance() { return instance; }

    private GoogleIabHelper() {

    }

    @Override
    public void init(Application application) {
        sharedPreferences = application.getSharedPreferences("test", application.MODE_PRIVATE);
    }

    @Override
    public void onCreate(final Activity activity, Bundle savedInstanceState) {
        String base64PublicKey = SystemUtil.getInstance().getPlatCfgValue("google_iab_publickey");
        mHelper = new IabHelper(activity, base64PublicKey);
        mHelper.enableDebugLogging(true);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.e(TAG, "Problem setting up In-app Billing: " + result);
                    return;
                }
                if (mHelper == null) {
                    return;
                }
                mBroadcastReceiver = new IabBroadcastReceiver(GoogleIabHelper.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                activity.registerReceiver(mBroadcastReceiver, broadcastFilter);

                quertInventory();
            }
        });
    }

    @Override
    public void onStart(Activity activity) {

    }

    @Override
    public void onResume(Activity activity) {

    }

    @Override
    public void onPause(Activity activity) {

    }

    @Override
    public void onStop(Activity activity) {

    }

    @Override
    public void onDestroy(Activity activity) {
        if (mBroadcastReceiver != null) {
            activity.unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        mHelper.handleActivityResult(requestCode, resultCode, data);
    }

    private void quertInventory() {
        try {
            mHelper.queryInventoryAsync(this);
        } catch (IabHelper.IabAsyncInProgressException e) {
            Log.w(TAG, "Error querying inventory. Another async operation in progress.");
        }
    }

    @Override
    public void receivedBroadcast() {
        quertInventory();
    }

    @Override
    public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
        if (mHelper == null) {
            return;
        }
        if (result.isFailure()) {
            Log.w(TAG, "Failed to query inventory: "+result);
            return;
        }

        Log.d(TAG, "Query inventory was successful.");

        List<Purchase> purchases = inventory.getAllPurchases();
        if (purchases.size() <= 0)
            return;

        try {
            // 只处理一个，下次在处理剩余的
            Purchase purchase = purchases.get(0);
            mHelper.consumeAsync(purchase, new IabHelper.OnConsumeFinishedListener() {
                @Override
                public void onConsumeFinished(Purchase purchase, IabResult result) {
                    boolean iapResult = result.isSuccess();
                    String iapId = purchase.getSku();
                    Log.e(TAG, iapResult+" : "+iapId);
                }
            });
        } catch (IabHelper.IabAsyncInProgressException e) {
            Log.w(TAG, "Error comsume purchases. Another async operation in progress.");
        }
    }

    public void setIapVerifyUrlAndSign(String url, String sign) {
        this.mVerifyUrl = url;
        this.mVerifySign = sign;
    }

    public boolean canDoIap() {
        return true;
    }

    public HashMap getSuspensiveIap() {
        HashMap hashMap = new HashMap<>();
        try {
            String jsonStr = sharedPreferences.getString("suspensiveIap","");
            JSONObject iapinfo = new JSONObject(jsonStr);
            Iterator<String> keys = iapinfo.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                hashMap.put(key, iapinfo.getString(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hashMap;
    }

    public void setSuspensiveIap(HashMap iapInfo) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doIap(String iapId, String userId, final InvokeJavaMethodDelegate delegate) {
        try {
            mHelper.launchPurchaseFlow(SystemUtil.getInstance().getActivity(), iapId, 10001, new IabHelper.OnIabPurchaseFinishedListener() {
                @Override
                public void onIabPurchaseFinished(IabResult result, Purchase info) {
                    if (result.isFailure()) {
                        return;
                    }
                    try {
                        mHelper.consumeAsync(info, new IabHelper.OnConsumeFinishedListener() {
                            @Override
                            public void onConsumeFinished(Purchase purchase, IabResult result) {
                                ArrayList<Object> arrayList =  new ArrayList<>();
                                arrayList.add(result.isSuccess());
                                arrayList.add("ProductionSandbox");
                                delegate.onFinish(arrayList);
                                verifyIap();
                            }
                        });
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        Log.w(TAG, "Error comsume purchases. Another async operation in progress.");
                    }
                }
            }, userId);
        } catch (IabHelper.IabAsyncInProgressException e) {
            Log.w(TAG, "Error launch purchase. Another async operation in progress.");
        }
    }

    private void verifyIap() {
        HashMap hashMap = new HashMap();
        SystemUtil.getInstance().requestUrl("post", this.mVerifyUrl, hashMap, new InvokeJavaMethodDelegate() {
            @Override
            public void onFinish(ArrayList<Object> resArrayList) {
                boolean result = (boolean) resArrayList.get(0);
                if (!result) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            verifyIap();
                        }
                    }, 5000);
                } else {
                    String resJson = (String) resArrayList.get(1);
                    Log.e(TAG, resJson);
                }
            }
        });
    }
}
