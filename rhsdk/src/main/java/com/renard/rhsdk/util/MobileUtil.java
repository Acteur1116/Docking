package com.renard.rhsdk.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * Created by Riven_rabbit on 2020/9/16
 *
 * @author suyanan
 */
public class MobileUtil {
    protected static String uuid;

    public static String getDeviceID(Context context){
        if(uuid == null){
            generateDeviceID(context);
        }
        return uuid;
    }

    public static void generateDeviceID(Context context){

        if( uuid ==null ) {
            synchronized (MobileUtil.class) {
                if( uuid == null) {
                    final SharedPreferences prefs = context.getSharedPreferences("g_device_id.xml", 0);
                    final String id = prefs.getString("device_id", null );

                    if (id != null) {
                        // Use the ids previously computed and stored in the prefs file
                        uuid = id;
                    } else {
                        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

                        // Use the Android ID unless it's broken, in which case fallback on deviceId,
                        // unless it's not available, then fallback on a random number which we store
                        // to a prefs file
                        try {
                            if (!"9774d56d682e549c".equals(androidId)) {
                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
                            } else {
                                final String deviceId = ((TelephonyManager) context.getSystemService( Context.TELEPHONY_SERVICE )).getDeviceId();
                                uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString() : UUID.randomUUID().toString();
                            }
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }

                        // Write the value out to the prefs file
                        prefs.edit().putString("device_id", uuid.toString()).commit();

                    }
                }
            }
        }

    }

    /**
     * 获取mac地址
     * @param context
     * @return
     */
    public static String getMacAddress(Context context)
    {
        WifiManager localWifiManager = (WifiManager)context.getSystemService("wifi");
        WifiInfo localWifiInfo = localWifiManager == null ? null : localWifiManager.getConnectionInfo();
        if (localWifiInfo != null)
        {
            String str = localWifiInfo.getMacAddress();
            if ((str == null) || (str.equals(""))) {
                str = "null";
            }
            return str;
        }
        return "null";
    }

    /**
     * 获取屏幕分辨率
     * @param activity
     * @return
     */
    public static String getScreenDpi(Activity activity){

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        return dm.widthPixels + "×" + dm.heightPixels;

    }
}
