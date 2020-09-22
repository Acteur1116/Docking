package com.renard.rhsdk.plugin;

import com.renard.rhsdk.pay.RHOrder;
import com.renard.rhsdk.pay.PayParams;

/**
 * Created by Riven_rabbit on 2020/9/22
 *
 * @author suyanan
 */
public interface SWPlugin extends Plugin{
    public static final int PLUGIN_TYPE = Constants.PLUGIN_TYPE_SW;

    /***
     * 调用支付界面
     * @param data
     */
    public void sw(PayParams data, RHOrder rhOrder);
}
