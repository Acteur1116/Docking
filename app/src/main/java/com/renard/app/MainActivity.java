package com.renard.app;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.renard.rhsdk.RHSDK;
import com.renard.rhsdk.sdk.RHSDKCode;
import com.renard.rhsdk.sdk.RHSDKInitListener;

public class MainActivity extends Activity {
    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        RHSDK.getInstance().init(this, new RHSDKInitListener() {
            @Override
            public void onInitResult(int code, String msg) {
                if (code== RHSDKCode.CODE_INIT_SUCCESS){
                    Log.d("初始化成功","+++++++++++++++++++++");
                    RHSDK.getInstance().login(context);
                }
            }

            @Override
            public void onLoginResult(int code, String userID, String token) {

            }

            @Override
            public void onSwitchAccount(String userID, String token) {

            }

            @Override
            public void onLogout() {

            }

            @Override
            public void onPayResult(int code, String msg) {

            }
        });
    }
}
