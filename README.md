# 1 GamePlugin

build.gradle

    compile project(path: ':gameplugin')


# 2 Analytics

## 2.1 Flurry

build.gradle    
    compile project(path: ':my_analytic_flurry')

strings.xml

    <string name="Flurry_AppKey">1111111111111111111111</string>


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
​    
    compile project(path: ':my_facebook')

AndroidManifest.xml

    <provider 
        android:authorities="com.facebook.app.FacebookContentProvider{APP_ID}"
        android:name="com.facebook.FacebookContentProvider"
        android:exported="true"/>

strings.xml
​    
    <string name="facebook_app_id">184666628540822</string>
    <string name="fb_login_protocol_scheme">fb184666628540822</string>
