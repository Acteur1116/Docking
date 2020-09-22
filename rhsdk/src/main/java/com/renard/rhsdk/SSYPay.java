package com.renard.rhsdk;

import android.app.Activity;

import com.renard.rhsdk.pay.PayParams;
import com.renard.rhsdk.plugin.PayPlugin;

/**
 * Created by Riven_rabbit on 2020/9/22
 *
 * @author suyanan
 */
public class SSYPay implements PayPlugin {

    public SSYPay(Activity activity) {
    }

    @Override
    public boolean isSupportMethod(String arg0) {
        return true;
    }

    @Override
    public void pay(PayParams arg0) {
//		SYSdk.getInstance().pay(arg0);
    }
}