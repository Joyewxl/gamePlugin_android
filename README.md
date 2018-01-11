# GamePlugin

build.gradle

    compile project(path: ':gameplugin')

# Analytics

## Flurry

build.gradle
    
    compile project(path: ':my_analytic_flurry')

strings.xml

    <string name="Flurry_AppKey">1111111111111111111111</string>

# Advertise

## Admob
    
build.gradle

    classpath 'com.google.gms:google-services:3.0.0'

build.gradle

    apply plugin: 'com.google.gms.google-services'

strings.xml

    <string name="banner_ad_unit_id">ca-app-pub-9274282740568260/4259450900</string>
    <string name="interstitial_ad_unit_id">ca-app-pub-9274282740568260/3965072900</string>
    <string name="admob_test_device_id">43EDC6D96834B689D5D0AAF8CFF44CCB</string>

# Facebook

build.gradle
    
    compile project(path: ':my_facebook')

AndroidManifest.xml

    <provider 
        android:authorities="com.facebook.app.FacebookContentProvider{APP_ID}"
        android:name="com.facebook.FacebookContentProvider"
        android:exported="true"/>

strings.xml
    
    <string name="facebook_app_id">184666628540822</string>
    <string name="fb_login_protocol_scheme">fb184666628540822</string>
