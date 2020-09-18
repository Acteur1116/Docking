package com.renard.rhsdk.user;

import android.util.Log;
import android.widget.Toast;

import com.renard.rhsdk.plugin.UserPlugin;
import com.renard.rhsdk.sdk.RHSDKCode;
import com.renard.rhsdk.sdk.RHSDKManager;

/**
 * Created by Riven_rabbit on 2020/9/17
 *
 * @author suyanan
 */
public class CloseUser implements UserPlugin {

    public CloseUser(){
        RHSDKManager.getInstance().onResult(
                RHSDKCode.CODE_INIT_SUCCESS, "init success");
        Log.d("RHSDK", "channel forbid.");
    }
    @Override
    public boolean isSupportMethod(String methodName) {

        return true;
    }

    @Override
    public void login() {
        Toast();
    }

    @Override
    public void loginCustom(String customData) {

    }

    @Override
    public void switchLogin() {
        Toast();
    }

    @Override
    public boolean showAccountCenter() {
        return false;
    }

    @Override
    public void logout() {
        Toast();
    }

    @Override
    public void submitExtraData(UserExtraData extraData) {
        Toast();
    }

    @Override
    public void exit() {
        Toast();
    }

    @Override
    public void postGiftCode(String code) {
        Toast();
    }

    @Override
    public void realNameRegister() {
        Toast();
    }

    @Override
    public void queryAntiAddiction() {
        Toast();
    }

    private void Toast(){
        String msg = RHSDKManager.getInstance().getMsg();
        if(msg==null||msg.length()==0){
            return;
        }
        Toast.makeText(RHSDKManager.getInstance().getContext(), msg, Toast.LENGTH_LONG).show();
    }
}
