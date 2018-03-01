package com.joycastle.iab.googleplay;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.joycastle.gamepluginbase.InvokeJavaMethodDelegate;
import com.joycastle.gamepluginbase.LifeCycleDelegate;
import com.joycastle.gamepluginbase.SystemUtil;
import com.joycastle.iab.googleplay.util.IabBroadcastReceiver;
import com.joycastle.iab.googleplay.util.IabHelper;
import com.joycastle.iab.googleplay.util.IabResult;
import com.joycastle.iab.googleplay.util.Inventory;
import com.joycastle.iab.googleplay.util.Purchase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gaoyang on 10/9/16.
 */

public class GoogleIabHelper implements LifeCycleDelegate, IabBroadcastReceiver.IabBroadcastListener, IabHelper.QueryInventoryFinishedListener {
    private static String TAG = "GoogleIabHelper";

    private static GoogleIabHelper instance = new GoogleIabHelper();

    private IabHelper mHelper;
    private IabBroadcastReceiver mBroadcastReceiver;

    public static GoogleIabHelper getInstance() { return instance; }

    private GoogleIabHelper() {

    }

    @Override
    public void init(Application application) {

    }

    @Override
    public void onCreate(final Activity activity, Bundle savedInstanceState) {
        String base64PublicKey = SystemUtil.getInstance().getMetaData("google_iab_publickey");
        mHelper = new IabHelper(activity, base64PublicKey);
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
        IabHelper.QueryInventoryFinishedListener mGotInventoryListener;
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

    private boolean verifyDeveloperPayload(Purchase purchase) {
        return true;
    }

    @Override
    public void receivedBroadcast() {
        quertInventory();
    }

    @Override
    public void onQueryInventoryFinished(IabResult result, Inventory inv) {
//        if (mHelper == null) {
//            return;
//        }
//        if (result.isFailure()) {
//            Log.w(TAG, "Failed to query inventory: "+result);
//            return;
//        }
//
//        Log.d(TAG, "Query inventory was successful.");
//
//        if (mRestoreDelegate == null) {
//            return;
//        }
//        Toast.makeText(SystemUtil.getInstance().getApplication(), "Restore Purchase...", Toast.LENGTH_LONG).show();
//        List<Purchase> purchases = inv.getAllPurchases();
//        try {
//            mHelper.consumeAsync(purchases, new IabHelper.OnConsumeMultiFinishedListener() {
//                @Override
//                public void onConsumeMultiFinished(List<Purchase> purchases, List<IabResult> results) {
//                    for (int i=0; i<purchases.size(); i++) {
//                        Purchase purchase = purchases.get(i);
//                        IabResult iabResult = results.get(i);
//                        boolean iapResult = iabResult.isSuccess();
//                        String iapId = purchase.getSku();
//                        String payload = purchase.getDeveloperPayload();
//                        mRestoreDelegate.onResult(iapResult, iapId, payload);
//                    }
//                }
//            });
//        } catch (IabHelper.IabAsyncInProgressException e) {
//            Log.w(TAG, "Error comsume purchases. Another async operation in progress.");
//        }
    }


    public void purchase(String iapId, String payLoad, final InvokeJavaMethodDelegate delegate) {
        System.out.print("purchase");
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
                                boolean iapResult = result.isSuccess();
                                String iapId = purchase.getSku();
                                String payload = purchase.getDeveloperPayload();
                                ArrayList<Object> arrayList =  new ArrayList<>();
                                arrayList.add(iapResult);
                                arrayList.add(payload);
                                delegate.onFinish(arrayList);
                            }
                        });
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        Log.w(TAG, "Error comsume purchases. Another async operation in progress.");
                    }
                }
            }, payLoad);
        } catch (IabHelper.IabAsyncInProgressException e) {
            Log.w(TAG, "Error launch purchase. Another async operation in progress.");
        }
    }
}
