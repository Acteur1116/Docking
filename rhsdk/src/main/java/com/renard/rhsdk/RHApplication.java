package com.renard.rhsdk;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.renard.rhsdk.sdk.RHSDKManager;

/**
 * Created by Riven_rabbit on 2020/9/17
 *
 * @author suyanan
 */
public class RHApplication extends Application {
    public void onCreate(){
		super.onCreate();
		RHSDKManager.getInstance().onAppCreate(this);
	}

	public void attachBaseContext(Context base){
		super.attachBaseContext(base);
        RHSDKManager.getInstance().onAppAttachBaseContext(this, base);
	}

	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);

        RHSDKManager.getInstance().onAppConfigurationChanged(this, newConfig);
	}

	public void onTerminate() {
		super.onTerminate();
		RHSDKManager.getInstance().onTerminate();

	}
}
