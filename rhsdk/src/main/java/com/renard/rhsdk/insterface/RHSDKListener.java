package com.renard.rhsdk.insterface;

import com.renard.rhsdk.verify.RHToken;

/**
 * Created by Riven_rabbit on 2020/9/15
 *
 * @author suyanan
 */
public interface RHSDKListener {

    public void onResult(int code, String msg);

    public void onLoginResult(String data);

    public void onSwitchAccount();

    public void onSwitchAccount(String data);

    public void onLogout();

    public void onAuthResult(RHToken authResult);
}
