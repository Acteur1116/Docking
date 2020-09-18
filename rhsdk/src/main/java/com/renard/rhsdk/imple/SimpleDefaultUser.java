package com.renard.rhsdk.imple;

import android.widget.Toast;

import com.renard.rhsdk.plugin.UserPlugin;
import com.renard.rhsdk.sdk.RHSDKManager;
import com.renard.rhsdk.user.UserExtraData;

/**
 * Created by Riven_rabbit on 2020/9/17
 *
 * @author suyanan
 */
public class SimpleDefaultUser implements UserPlugin {
    @Override
    public boolean isSupportMethod(String methodName) {

        return true;
    }

    @Override
    public void login() {
        Toast("调用[登录]接口成功，权限申请如果没问题，Application需要继承RHApplication");
    }

    @Override
    public void loginCustom(String customData) {

    }

    @Override
    public void switchLogin() {
        Toast("调用[切换帐号]接口成功，权限申请如果没问题，Application需要继承RHApplication");
    }

    @Override
    public boolean showAccountCenter() {

        Toast("调用[个人中心]接口成功，权限申请如果没问题，Application需要继承RHApplication");

        return true;
    }

    @Override
    public void logout() {
        Toast("调用[登出接口]接口成功，权限申请如果没问，Application需要继承RHApplication");
    }

    @Override
    public void submitExtraData(UserExtraData extraData) {
        Toast("调用[提交扩展数据]接口成功，权限申请如果没问题，Application需要继承RHApplication");
    }

    @Override
    public void exit() {
        Toast("调用[退出游戏确认]接口成功，权限申请如果没问题，Application需要继承RHApplication");
    }

    @Override
    public void realNameRegister() {
        Toast("游戏中暂时不需要调用该接口");
    }

    @Override
    public void queryAntiAddiction() {
        Toast("游戏中暂时不需要调用该接口");
    }

    private void Toast(String msg){
        Toast.makeText(RHSDKManager.getInstance().getContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void postGiftCode(String code) {
        Toast("调用[上传礼包兑换码]接口成功，权限申请如果没问题，Application需要继承RHApplication");
    }
}
