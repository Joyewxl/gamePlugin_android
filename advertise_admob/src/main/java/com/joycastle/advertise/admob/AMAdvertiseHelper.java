package com.joycastle.advertise.admob;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.mediation.MediationAdapter;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.joycastle.gamepluginbase.AdvertiseDelegate;
import com.joycastle.gamepluginbase.InvokeJavaMethodDelegate;
import com.joycastle.gamepluginbase.SystemUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

/**
 * Created by gaoyang on 9/29/16.
 */

public class AMAdvertiseHelper implements AdvertiseDelegate {
    private static final String TAG = "AMAdvertiseHelper";

    private static AMAdvertiseHelper instance = new AMAdvertiseHelper();
    public static AMAdvertiseHelper getInstance() {
        return instance;
    }

    private Class<MediationAdapter> vungleClass = null;
    private Bundle vungleExtras = null;

    private AMAdvertiseHelper() {}
    private Boolean isLoadAD = false;
    private Boolean isLoadVideoAD = false;
    private String testDeviceId = null;

    private AdView bannerAd = null;
    private InterstitialAd interstitialAd = null;
    private RewardedVideoAd mRewardedVideoAd = null;
    private BannerAdListener bannerAdListener = null;
    private boolean interstitialAdClicked = false;
    private InvokeJavaMethodDelegate interstitialAdListener = null;
    private InvokeJavaMethodDelegate rewardAdListener = null;

    public void setVungle(Class clazz , Bundle extras){
        vungleClass = clazz;
        vungleExtras = extras;
    }

    @Override
    public int showBannerAd(boolean protrait, boolean bottom, BannerAdListener listener) {
        bannerAdListener = listener;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) bannerAd.getLayoutParams();
        layoutParams.gravity = bottom ? Gravity.BOTTOM : Gravity.TOP;
        bannerAd.setLayoutParams(layoutParams);
        bannerAd.setVisibility(View.VISIBLE);
        return bannerAd.getHeight();
    }

    @Override
    public void hideBannerAd() {
        bannerAd.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean isInterstitialAdReady() {
        return isLoadAD;
    }

    @Override
    public boolean showInterstitialAd(InvokeJavaMethodDelegate listener) {
        if (this.isInterstitialAdReady()) {
            interstitialAd.show();
            interstitialAdListener = listener;
            return true;
        }
        return false;
    }

    @Override
    public boolean isVideoAdReady() {
//        Log.i(TAG, "didn't support");
        return isLoadVideoAD;
    }

    @Override
    public boolean showVideoAd(InvokeJavaMethodDelegate listener) {
//       Log.i(TAG, "didn't support");
        if (mRewardedVideoAd == null) return false;
        if(isLoadVideoAD) {
            rewardAdListener = listener;
            SystemUtil.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRewardedVideoAd.show();
                }
            });
        }else{
            this.requestNewVideo();
        }
        return isLoadVideoAD;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void init(Application application) {
        try {
            Class clazz = Class.forName("com.joycastle.advertise.admob.adapter.vungle.VGAdvertiseHelper");
            Method getInstanceMethod = clazz.getMethod("getInstance");
            Object instance = getInstanceMethod.invoke(null);
            Method method = clazz.getMethod("init", Application.class);
            method.invoke(instance, application);
        } catch (Exception e) {
            Log.e(TAG, "vungle is disable");
            e.printStackTrace();
        }

        Log.i(TAG, "Admob installed");
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(testDeviceId)
                .build();
        interstitialAd.loadAd(adRequest);
    }

    private void requestNewBanner() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(testDeviceId)
                .build();
        bannerAd.loadAd(adRequest);
    }

    private void requestNewVideo(){
        SystemUtil.activity.runOnUiThread(new Runnable() {
            @Override public void run() {
                AdRequest.Builder builder = new AdRequest.Builder();
                builder.addTestDevice(testDeviceId);
                if (vungleClass != null && vungleExtras != null) {
                    builder.addNetworkExtrasBundle(vungleClass,vungleExtras);
                }
                AdRequest adRequest = builder.build();
                String videoId = SystemUtil.getMetaData(SystemUtil.activity, "video_ad_unit_id");
                mRewardedVideoAd.loadAd(videoId,adRequest);
            }
        });
    }

    @Override
    public void onCreate(Activity activity, Bundle savedInstanceState) {
        String bannerId = SystemUtil.getMetaData(activity, "banner_ad_unit_id");
        String interstitialId = SystemUtil.getMetaData(activity, "interstitial_ad_unit_id");
        testDeviceId = SystemUtil.getMetaData(activity, "admob_test_device_id");

        // init banner ad
        bannerAd = new AdView(activity);
        bannerAd.setAdSize(AdSize.SMART_BANNER);
        bannerAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                requestNewBanner();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                bannerAdListener.onClick();
            }
        });
        bannerAd.setAdUnitId(bannerId);

        ViewGroup viewGroup = (ViewGroup) activity.findViewById(android.R.id.content);
        AdSize adSize = bannerAd.getAdSize();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(adSize.getWidth(), adSize.getHeight());
        viewGroup.addView(bannerAd, layoutParams);
        bannerAd.setVisibility(View.INVISIBLE);
        requestNewBanner();

        //init reward video ad
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(activity);
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                isLoadVideoAD = true;
            }

            @Override
            public void onRewardedVideoAdOpened() {
                isLoadVideoAD = false;
                Log.e(TAG, "onRewardedVideoAdOpened: ");
            }

            @Override
            public void onRewardedVideoStarted() {
                Log.e(TAG, "onRewardedVideoStarted: ");
            }

            @Override
            public void onRewardedVideoAdClosed() {
                isLoadVideoAD = false;
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                Log.e(TAG, "onRewarded: ");
                JSONObject respData = new JSONObject();
                try {
                    respData.put("reward",true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rewardAdListener.onFinish(respData);
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                Log.e(TAG, "onRewardedVideoAdLeftApplication: ");

                JSONObject respData = new JSONObject();
                try {
                    respData.put("click",true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rewardAdListener.onFinish(respData);
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }
        });
        requestNewVideo();

        // init interstitial Ad
        interstitialAd = new InterstitialAd(activity);
        interstitialAd.setAdUnitId(interstitialId);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                requestNewInterstitial();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("click", interstitialAdClicked);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                interstitialAdListener.onFinish(jsonObject);
            }
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                requestNewInterstitial();

            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                interstitialAdClicked = true;
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                interstitialAdClicked = false;
            }
            @Override
            public void onAdLoaded() {
                super.onAdOpened();
                isLoadAD = true;
            }
        });
        requestNewInterstitial();
    }

    @Override
    public void onStart(Activity activity) {

    }

    @Override
    public void onResume(Activity activity) {
//        mRewardedVideoAd.resume(activity);
    }

    @Override
    public void onPause(Activity activity) {
//        mRewardedVideoAd.pause(activity);
    }

    @Override
    public void onStop(Activity activity) {

    }

    @Override
    public void onDestroy(Activity activity) {
//        mRewardedVideoAd.destroy(activity);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

    }
}
