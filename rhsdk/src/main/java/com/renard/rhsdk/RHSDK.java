package com.renard.rhsdk;

import android.app.Activity;

import com.renard.rhsdk.insterface.RHSDKListener;
import com.renard.rhsdk.log.Log;
import com.renard.rhsdk.sdk.RHSDKInitListener;
import com.renard.rhsdk.sdk.RHSDKManager;
import com.renard.rhsdk.sdk.RHSDKCode;
import com.renard.rhsdk.verify.RHToken;

/**
 * Created by Riven_rabbit on 2020/9/15
 *
 * @author suyanan
 */
public class RHSDK {
    public static RHSDK instance;

    public RHSDK() {
    }

    public static RHSDK getInstance(){
        if (instance==null){
            instance=new RHSDK();
        }
        return instance;
    }


    /**
     * SDK初始化，需要在游戏启动Activity的onCreate中调用
     * @param context
     * @param callback
     */
    public void init(Activity context, final RHSDKInitListener callback){

        Log.d("RHSDK", "Init SDK start");

        if(callback == null){
            Log.d("RHSDK", "RHSDKInitListener must be not null.");
            return;
        }

        try{

            RHSDKManager.getInstance().setSDKListener(new RHSDKListener() {

                @Override
                public void onResult(final int code, final String msg) {
                    Log.d("RHSDK", "onResult.code:"+code+";msg:"+msg);

                    RHSDKManager.getInstance().runOnMainThread(new Runnable() {

                        @Override
                        public void run() {
                            switch(code){
                                case RHSDKCode.CODE_INIT_SUCCESS:
                                    callback.onInitResult(RHSDKCode.CODE_INIT_SUCCESS, msg);
                                    break;
                                case RHSDKCode.CODE_INIT_FAIL:
                                    callback.onInitResult(RHSDKCode.CODE_INIT_FAIL, msg);
                                    break;
                                case RHSDKCode.CODE_LOGIN_FAIL:
                                    callback.onLoginResult(RHSDKCode.CODE_LOGIN_FAIL, null, null);
                                    break;
                                case RHSDKCode.CODE_PAY_SUCCESS:
                                    callback.onPayResult(RHSDKCode.CODE_PAY_SUCCESS, msg);
                                    break;
                                case RHSDKCode.CODE_PAY_FAIL:
                                    callback.onPayResult(RHSDKCode.CODE_PAY_FAIL, msg);
                                    break;
                                case RHSDKCode.CODE_PAY_CANCEL:
                                    callback.onPayResult(RHSDKCode.CODE_PAY_CANCEL, msg);
                                    break;
                                case RHSDKCode.CODE_PAY_UNKNOWN:
                                    callback.onPayResult(RHSDKCode.CODE_PAY_UNKNOWN, msg);
                                    break;
                                case RHSDKCode.CODE_PAYING:
                                    callback.onPayResult(RHSDKCode.CODE_PAYING, msg);
                                    break;
                            }
                        }
                    });

                }

                @Override
                public void onLogout() {
                    RHSDKManager.getInstance().runOnMainThread(new Runnable() {

                        @Override
                        public void run() {
                            callback.onLogout();
                        }
                    });
                }

                @Override
                public void onSwitchAccount() {
                    RHSDKManager.getInstance().runOnMainThread(new Runnable() {

                        @Override
                        public void run() {
                            callback.onLogout();
                        }
                    });
                }

                @Override
                public void onLoginResult(String data) {
                    Log.d("RHSDK", "SDK 登录成功,不用做处理，在onAuthResult中处理登录成功, 参数如下:");
                    Log.d("RHSDK", data);
                    isSwitchAccount = false;
                }

                @Override
                public void onSwitchAccount(String data) {
                    Log.d("RHSDK", "SDK 切换帐号并登录成功,不用做处理，在onAuthResult中处理登录成功, 参数如下:");
                    Log.d("RHSDK", data);
                    isSwitchAccount = true;
                }



                @Override
                public void onAuthResult(final RHToken authResult) {
                    RHSDKManager.getInstance().runOnMainThread(new Runnable() {

                        @Override
                        public void run() {

                            if(isSwitchAccount){
                                if(authResult.isSuc()){
                                    Log.d("RHSDK", "Callback onSwitchAccount. userID: "+authResult.getUserID()+" token: "+authResult.getToken());
                                    callback.onSwitchAccount(authResult.getUserID(), authResult.getToken());
                                }else{
                                    Log.e("RHSDK", "switch account auth failed.");
                                }
                            }else{

                                if(!authResult.isSuc()){
                                    callback.onLoginResult(RHSDKCode.CODE_LOGIN_FAIL, null, null);
                                    return;
                                }
                                Log.d("RHSDK", "Callback onLoginResult. userID: "+authResult.getUserID()+" token: "+authResult.getToken());
                                callback.onLoginResult(RHSDKCode.CODE_LOGIN_SUCCESS, authResult.getUserID(), authResult.getToken());
                            }
                        }
                    });
                }
            });

            RHSDKManager.getInstance().init(context);
            RHSDKManager.getInstance().onCreate();

        }catch(Exception e){
            callback.onInitResult(RHSDKCode.CODE_INIT_FAIL, e.getMessage());
            Log.e("RHSDK", "init failed.", e);
            e.printStackTrace();
        }
    }
}
