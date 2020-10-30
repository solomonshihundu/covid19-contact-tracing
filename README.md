# Covid-19 Contact Tracing

This is a simple android application that prompts a user to periodically and voluntarily share thier Covid-19 status, 
and utilizes the internal GPS system of an android device to aquire the users location for contact tracing.

## Setting Up
 To be able to connect to a realtime firebase database add your google api client json configuration file in `covid19-contact-tracing/app/`<br/>
 You will need to add the following gradle dependency in your build.gradle(module:app) file <br/>
 
```yaml 
dependencies {
    implementation 'com.google.firebase:firebase-bom:25.12.0'
    implementation 'com.google.firebase:firebase-analytics:17.2.2'
    implementation 'com.google.firebase:firebase-auth:19.3.0'
    implementation 'com.google.firebase:firebase-database:19.2.1'
    implementation 'com.google.android.gms:play-services-location:17.0.0'
} 
```
  Requires minSdkVersion 21
 
 ## Permissions
 ```
  <uses-permission android:name="android.permission.INTERNET"/>
  <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
  <uses-permission android:name="android.permission.CALL_PHONE" />
 ```
 ## App Preview
 ### Intro page
 ![alt text](https://github.com/solomonshihundu/covid19-contact-tracing/blob/readme/screenshots/intro_page.jpg)  
 ### Status Update
 ![alt text](https://github.com/solomonshihundu/covid19-contact-tracing/blob/readme/screenshots/update_status_page.jpg) 
 ### Closing page
 ![alt text](https://github.com/solomonshihundu/covid19-contact-tracing/blob/readme/screenshots/final_page.jpg) 
