package com.renard.rhsdk.imple;

import android.widget.Toast;

import com.renard.rhsdk.RHSDK;
import com.renard.rhsdk.pay.PayParams;
import com.renard.rhsdk.pay.RHOrder;
import com.renard.rhsdk.plugin.SWPlugin;
import com.renard.rhsdk.sdk.RHSDKManager;

/**
 * Created by Riven_rabbit on 2020/9/22
 *
 * @author suyanan
 */
public class SimpleDefaultSW implements SWPlugin {
    @Override
    public boolean isSupportMethod(String methodName) {
        return false;
    }

    @Override
    public void sw(PayParams data, RHOrder rhOrder) {
        Toast.makeText(RHSDKManager.getInstance().getContext(), "支付渠道异常", Toast.LENGTH_LONG).show();
    }
}
