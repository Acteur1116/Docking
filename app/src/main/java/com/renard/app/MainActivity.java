package com.renard.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

import com.renard.rhsdk.RHSDK;
import com.renard.rhsdk.log.Log;
import com.renard.rhsdk.sdk.RHSDKCode;
import com.renard.rhsdk.sdk.RHSDKInitListener;

import static com.renard.rhsdk.RHSDK.*;

public class MainActivity extends AppCompatActivity {
    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        init();
    }

    private void init() {
        getInstance().init(this, new RHSDKInitListener() {
            @Override
            public void onInitResult(int code, String msg) {
                if (code== RHSDKCode.CODE_INIT_SUCCESS){
                    Log.d("初始化成功","+++++++++++++++++++++");
                    getInstance().login(context);
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
