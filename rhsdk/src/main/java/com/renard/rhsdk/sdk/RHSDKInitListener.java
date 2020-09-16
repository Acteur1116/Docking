package com.renard.rhsdk.sdk;

/**
 * Created by Riven_rabbit on 2020/9/15
 *
 * @author suyanan
 */
public interface RHSDKInitListener {
    /**
     * 初始化结果
     * @param code
     * @param msg
     */
    public void onInitResult(int code, String msg);


    /**
     * 平台登录回调
     */
    public void onLoginResult(int code, String userID, String token);

    /**
     * 游戏中通过SDK切换到新账号的回调，游戏收到该回调，需要引导用户重新登录，重新加载该新用户对应的角色数据
     */
    public void onSwitchAccount(String userID, String token);

    /**
     * 用户登出回调（需要收到该回调需要返回游戏登录界面，并调用login接口，打开SDK登录界面）
     */
    public void onLogout();

    /**
     * 支付结果回调
     * @param code
     * @param msg
     */
    public void onPayResult(int code, String msg);
}
