package com.renard.rhsdk.plugin;

import com.renard.rhsdk.pay.PayParams;

/**
 * Created by Riven_rabbit on 2020/9/16
 *
 * @author suyanan
 */
public interface PayPlugin {
    public static final int PLUGIN_TYPE = Constants.PLUGIN_TYPE_PAY;

    /***
     * 调用支付界面
     * @param data
     */
    public void pay(PayParams data);
}
