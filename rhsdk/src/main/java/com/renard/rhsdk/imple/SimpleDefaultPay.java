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
public class SimpleDefaultPay implements PayPlugin {
    @Override
    public boolean isSupportMethod(String methodName) {
        return true;
    }

    @Override
    public void pay(PayParams data) {
        Toast.makeText(RHSDKManager.getInstance().getContext(), "调用[支付接口]接口成功，PayParams中的参数，除了extension，其他的请都赋值，最后还需要经过打包工具来打出最终的渠道包", Toast.LENGTH_LONG).show();
    }
}
