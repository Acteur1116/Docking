package com.renard.rhsdk.analytics;

import android.app.Activity;

import com.renard.rhsdk.sdk.RHSDKManager;

/**
 * Created by Riven_rabbit on 2020/9/16
 * 该类主要用于上报统计用户
 * @author suyanan
 */
public class RHSubmit {
    private static RHSubmit instance;

    private RHSubmit() {
    }

    public static RHSubmit getInstance(){
        if (instance==null){
            instance=new RHSubmit();
        }
        return instance;
    }
    /**
     * 初始化
     * 统计功能，上报设备信息
     */
    public void init(Activity activity){
        MobileDevice mobileDevice=MobileManager.getinstance().collectDeviceInfo(activity,RHSDKManager.getInstance().getAppID(), RHSDKManager.getInstance().getCurrChannel(), RHSDKManager.getInstance().getSubChannel());
    }
}
