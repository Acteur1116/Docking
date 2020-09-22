package com.renard.rhsdk.imple;

import android.widget.Toast;

import com.renard.rhsdk.pay.PayParams;
import com.renard.rhsdk.plugin.PayPlugin;
import com.renard.rhsdk.sdk.RHSDKManager;

/**
 * Created by Riven_rabbit on 2020/9/22
 *
 * @author suyanan
 */
public class ClosePay implements PayPlugin {
    @Override
    public boolean isSupportMethod(String methodName) {
        return true;
    }

    @Override
    public void pay(PayParams data) {
        String msg = RHSDKManager.getInstance().getMsg();
        if(msg==null||msg.length()==0){
            return;
        }
        Toast.makeText(RHSDKManager.getInstance().getContext(), msg, Toast.LENGTH_LONG).show();
    }
}
