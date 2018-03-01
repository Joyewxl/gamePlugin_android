# 1 GamePlugin

build.gradle

```groovy
compile project(path: ':gameplugin')
```


# 2 Analytics

## 2.1 Flurry


strings.xml

```groovy
<string name="flurry_key">flurry_key</string>
```


# 3 Advertise

## 3.1 Admob

project level build.gradle

```groovy
allprojects {
    repositories {
        maven {
            url "https://maven.google.com"
        }
    }
}
```
strings.xml

```xml
    <string name="admob_app_id">ca-app-pub-9274282740568260~7977987594</string>
    <string name="admob_banner_id">banner_ad_unit_id</string>
    <string name="admob_interstitial_id">ca-app-pub-9274282740568260/1808342671</string>
    <string name="admob_video_id">ca-app-pub-9274282740568260/7235751563</string>
    <string name="admob_test_device_id">4258574fe1cc47ea897030ce840c886b</string>
```

### 3.1.1 Vungle-Adapter

strings.xml

```xml
    <string name="vungle_video_id">vungle_video_id</string>
    <string name="vungle_spot_id">vungle_spot_id</string>
```



# 4 Facebook

build.gradle
```groovy
compile project(path: ':my_facebook')
```

AndroidManifest.xml

```xml
<activity startActivity 
<intent-filter>
                <category android:name="android.intent.category.BROWSABLE" />
                <data android:scheme="@string/fb_login_protocol_scheme" />
            </intent-filter>
/>
<meta-data android:name="com.facebook.sdk.ApplicationId" android:value="@string/facebook_app_id"/>
        <activity android:name="com.facebook.FacebookActivity"
            android:configChanges=
                "keyboard|keyboardHidden|screenLayout|screenSize|orientation"
            android:label="@string/app_name" />
```

strings.xml
```xml
<string name="facebook_app_id">184666628540822</string>
<string name="fb_login_protocol_scheme">fb184666628540822</string>
```



# 5 iap_googleplay

AndroidManifest.xml

```xml
<uses-permission android:name="com.android.vending.BILLING" />

<meta-data android:name="google_iab_publickey" android:value="@string/google_iab_publickey"/>
```

Strings.xml

```xml
<string name="google_iab_publickey">XXXXXXXXXXXXX</string>
```

# 6 Gameplugin_Base

AndroidManifest.xml

```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.VIBRATE" />
<uses-permission android:name="com.huawei.android.launcher.permission.CHANGE_BADGE"/>

<receiver android:name="com.joycastle.gamepluginbase.SystemUtil$NotificationReceiver"/>
```

