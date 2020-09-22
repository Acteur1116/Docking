package com.renard.rhsdk.plugin;

import com.renard.rhsdk.analytics.RHSubmit;
import com.renard.rhsdk.imple.SimpleDefaultUser;
import com.renard.rhsdk.sdk.RHSDKManager;
import com.renard.rhsdk.imple.CloseUser;
import com.renard.rhsdk.user.RHSDKFlag;
import com.renard.rhsdk.user.UserExtraData;

/**
 * Created by Riven_rabbit on 2020/9/17
 *
 * @author suyanan
 */
public class KSUser {
    public static KSUser instance;
    private UserPlugin userPlugin;

    public KSUser() {
    }

    public static KSUser getInstance(){
        if (instance==null){
            instance=new KSUser();
        }
        return instance;
    }

    public void init(){
        int sdkFlag = RHSDKManager.getInstance().getSdkFlag();
        if(sdkFlag== RHSDKFlag.OPEN){
            this.userPlugin = (UserPlugin)PluginFactory.getInstance().initPlugin(UserPlugin.PLUGIN_TYPE);
        }else if(sdkFlag==RHSDKFlag.SWITCH){
            this.userPlugin = (UserPlugin)PluginFactory.getInstance().initPlugin(Constants.PLUGIN_TYPE_USER_SW);
            if(this.userPlugin == null){
                this.userPlugin = (UserPlugin)PluginFactory.getInstance().initPlugin(UserPlugin.PLUGIN_TYPE);
            }
        }else{
            //封禁状态码 RHSDKFlag.CLOSE;
            this.userPlugin = new CloseUser();
        }

        if(this.userPlugin == null){
            this.userPlugin = new SimpleDefaultUser();
        }
    }
    /**
     * 加载渠道User类
     * @param method
     */
    public boolean isSupport(String method){
        if(userPlugin == null){
            return false;
        }
        return userPlugin.isSupportMethod(method);
    }

    /**
     * 登录接口
     */
    public void login(){
        if(userPlugin==null){
            return;
        }

        userPlugin.login();
    }

    public void login(String customData){
        if(userPlugin == null){
            return;
        }
        userPlugin.loginCustom(customData);
    }

    public void switchLogin(){
        if(userPlugin == null){
            return;
        }

        userPlugin.switchLogin();
    }

    public void showAccountCenter(){
        if(userPlugin == null){
            return;
        }

        userPlugin.showAccountCenter();
    }

    /**
     * 退出当前帐号
     */
    public void logout() {
        if (userPlugin == null) {
            return;
        }

        userPlugin.logout();
    }

    /***
     * 提交扩展数据，角色登录成功之后，需要调用
     * @param extraData
     */
    public void submitExtraData(UserExtraData extraData){
        if(this.userPlugin == null){
            return;
        }

        if(RHSDKManager.getInstance().isUseSDKAnalytics()){
            RHSubmit.getInstance().submitUserInfo(RHSDKManager.getInstance().getContext(), extraData);
        }


        userPlugin.submitExtraData(extraData);
    }

    /**
     * SDK退出接口，有的SDK需要在退出的时候，弹出SDK的退出确认界面。
     * 如果SDK不需要退出确认界面，则弹出游戏自己的退出确认界面
     */
    public void exit(){
        if(this.userPlugin == null){
            return;
        }
        userPlugin.exit();
    }

    /**
     * 上传礼包兑换码
     * @param code
     */
    public void postGiftCode(String code){
        if(this.userPlugin == null){
            return;
        }
        userPlugin.postGiftCode(code);
    }
}
