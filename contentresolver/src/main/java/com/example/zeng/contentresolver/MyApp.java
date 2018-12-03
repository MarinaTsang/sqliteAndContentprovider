package com.example.zeng.contentresolver;

import android.app.Application;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;

import java.util.HashMap;
import java.util.Map;


/**
 * 仅仅是测试 ，忽略
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppsFlyerLib.getInstance().init("",null, getApplicationContext());
        AppsFlyerLib.getInstance().startTracking(this);

        Map<String, Object> eventValue = new HashMap<String, Object>();
        eventValue.put(AFInAppEventParameterName.LEVEL,9);
        eventValue.put(AFInAppEventParameterName.SCORE,100);
        AppsFlyerLib.getInstance().trackEvent(this, AFInAppEventType.LEVEL_ACHIEVED,eventValue);
    }
}
