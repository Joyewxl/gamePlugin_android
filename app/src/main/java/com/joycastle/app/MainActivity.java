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
import android.widget.Toast;

import com.joycastle.gameplugin.AdvertiseHelper;
import com.joycastle.gameplugin.AnalyticHelper;
import com.joycastle.gameplugin.GamePlugin;
import com.joycastle.gameplugin.IabHelper;
import com.joycastle.gamepluginbase.AdvertiseDelegate;
import com.joycastle.gamepluginbase.IabDelegate;
import com.joycastle.gamepluginbase.SystemUtil;
import com.joycastle.my_facebook.FacebookHelper;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    private static final String TAG = "MainActivity";

    private ArrayList<HashMap<String, OnClickListener>> arrayList = null;

    interface OnClickListener {
        void onClick() throws JSONException;
    }

    private MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Log.e(TAG, "------------>"+ SystemUtil.getAppVersion());

        GamePlugin.getInstance().onCreate(this, savedInstanceState);




        instance = this;

        arrayList = new ArrayList<>();
        HashMap<String, OnClickListener> hashMap;

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("--------AnalyticHelper", new OnClickListener() {
            @Override
            public void onClick() {
                System.out.println("AnalyticHelper");
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("setAccoutInfo", new OnClickListener() {
            @Override
            public void onClick() {
                Map<String, String> map = new HashMap();
                map.put("userId", "001");
                map.put("gender", "female");
                map.put("age", "10");
                AnalyticHelper.getInstance().setAccoutInfo(map);
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("onEvent", new OnClickListener() {
            @Override
            public void onClick() {
                AnalyticHelper.getInstance().onEvent("eventId");
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("onEventWithLabel", new OnClickListener() {
            @Override
            public void onClick() {
                AnalyticHelper.getInstance().onEvent("eventId", "eventLabel");
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("onEventWithData", new OnClickListener() {
            @Override
            public void onClick() {
                Map<String, Object> map = new HashMap();
                map.put("userId", "001");
                map.put("gender", "female");
                map.put("age", "10");
                AnalyticHelper.getInstance().onEvent("eventId", map);
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("setLevel", new OnClickListener() {
            @Override
            public void onClick() {
                AnalyticHelper.getInstance().setLevel(10);
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("charge", new OnClickListener() {
            @Override
            public void onClick() {
                AnalyticHelper.getInstance().charge("coin1", 1, 10, 1000);
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("reward", new OnClickListener() {
            @Override
            public void onClick() {
                AnalyticHelper.getInstance().reward(10, 1000);
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("purchase", new OnClickListener() {
            @Override
            public void onClick() {
                AnalyticHelper.getInstance().purchase("helmet", 1, 10);
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("use", new OnClickListener() {
            @Override
            public void onClick() {
                AnalyticHelper.getInstance().use("helmet", 1, 10);
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("onMissionBegin", new OnClickListener() {
            @Override
            public void onClick() {
                AnalyticHelper.getInstance().onMissionBegin("collect");
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("onMissionCompleted", new OnClickListener() {
            @Override
            public void onClick() {
                AnalyticHelper.getInstance().onMissionCompleted("collect");
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("onMissionFailed", new OnClickListener() {
            @Override
            public void onClick() {
                AnalyticHelper.getInstance().onMissionFailed("collect", "timeout");
            }
        });


        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("--------FaceBook", new OnClickListener() {
            @Override
            public void onClick() {
                System.out.println("AnalyticHelper");
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("facebooklogin", new OnClickListener() {

            @Override
            public void onClick() {
                FacebookHelper.getInstance().login(instance, Arrays.asList("public_profile", "user_friends","email","user_birthday","user_status"));
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("facebooklogout", new OnClickListener() {

            @Override
            public void onClick() {
                FacebookHelper.getInstance().logout();
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("facebookIslogin", new OnClickListener() {

            @Override
            public void onClick() {
                Log.e("Facebook","isLogin"+FacebookHelper.getInstance().isLogin());

                Log.e("Facebook","name"+FacebookHelper.getInstance().getUserId());
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("getFacebookProfile", new OnClickListener() {

            @Override
            public void onClick() throws JSONException {
               String str = FacebookHelper.getInstance().getUserProfile(null,null);
            }
        });



        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("--------AdvertiseHelper", new OnClickListener() {
            @Override
            public void onClick() {
                System.out.println("AnalyticHelper");
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("showBannerAd", new OnClickListener() {
            @Override
            public void onClick() {
                AdvertiseHelper.getInstance().showBannerAd(true, true, new AdvertiseDelegate.BannerAdListener() {
                    @Override
                    public void onClick() {
                        Log.e(TAG, "BannerAd Clicked");
                    }
                });
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("hideBannerAd", new OnClickListener() {
            @Override
            public void onClick() {
                AdvertiseHelper.getInstance().hideBannerAd();
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("isInterstitialAdReady", new OnClickListener() {
            @Override
            public void onClick() {
                boolean result = AdvertiseHelper.getInstance().isInterstitialAdReady();
                Log.e(TAG, "isInterstitialAdReady: "+result);
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("showInterstitialAd", new OnClickListener() {
            @Override
            public void onClick() {
                boolean result = AdvertiseHelper.getInstance().showInterstitialAd(new AdvertiseDelegate.InterstitialAdListener() {
                    @Override
                    public void onResult(boolean result) {
                        Log.e(TAG, "InterstitialAd Clicked: "+result);
                    }
                });
                Log.e(TAG, "showInterstitialAd: "+result);
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("isVideoAdReady", new OnClickListener() {
            @Override
            public void onClick() {
                boolean result = AdvertiseHelper.getInstance().isVideoAdReady();
                Log.e(TAG, "isVideoAdReady: "+result);
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("showVideoAd", new OnClickListener() {
            @Override
            public void onClick() {
                boolean result = AdvertiseHelper.getInstance().showVideoAd(new AdvertiseDelegate.VideoAdListener() {
                    @Override
                    public void onResult(boolean viewed, boolean clicked) {
                        String message = "";
                        message = message + "VideoAd Viewed: "+viewed;
                        message = message + "VideoAd Clicked: "+clicked;
                        showAlert(message);
                    }
                });
                showToast("showVideoAd: "+result);
            }
        });
        IabHelper.getInstance().setRestoreHandler(new IabDelegate.RestoreDelegate() {
            @Override
            public void onResult(boolean result, String iapId, String message) {
                showAlert(result+"\n"+iapId+"\n"+message);
            }
        });
        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("purchase", new OnClickListener() {
            @Override
            public void onClick() {
                IabHelper.getInstance().purchase("managed_product", "user_001", new IabDelegate.PurchaseDelegate() {
                    @Override
                    public void onResult(boolean result, String message) {
                        showAlert(result+"\n"+message);
                    }
                });
            }
        });

        hashMap = new HashMap<>();
        arrayList.add(hashMap);
        hashMap.put("showLoading", new OnClickListener() {
            @Override
            public void onClick() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SystemUtil.showLoading("Loading...");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SystemUtil.hideLoading();
                            }
                        }, 5);
                    }
                }, 0);
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
        try {
            listener.onClick();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public void showAlert(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .create()
                .show();;
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}
