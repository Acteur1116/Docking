package com.renard.rhsdk;

import android.app.Activity;

import com.renard.rhsdk.log.Log;
import com.renard.rhsdk.plugin.UserPlugin;
import com.renard.rhsdk.sdk.RHSDKCode;
import com.renard.rhsdk.sdk.RHSDKManager;
import com.renard.rhsdk.user.UserAdapter;
import com.renard.rhsdk.user.UserExtraData;
import com.renard.rhsdk.util.Arrays;

/**
 * Created by Riven_rabbit on 2020/9/18
 *
 * @author suyanan
 */
public class ChannelUser extends UserAdapter implements UserPlugin {

    public String[] supportedMethods = {"login","logout","submitExtraData"};

    public ChannelUser(Activity activity) {
        Log.e("111","111");
        RHSDKManager.getInstance().onResult(
                RHSDKCode.CODE_INIT_SUCCESS, "init success");
    }

    @Override
    public boolean isSupportMethod(String methodName) {
        return Arrays.contain(supportedMethods, methodName);
    }

    @Override
    public void login() {
        Log.e("222","222");
    }

    @Override
    public void logout() {

    }

    @Override
    public void submitExtraData(UserExtraData extraData) {

    }
}
