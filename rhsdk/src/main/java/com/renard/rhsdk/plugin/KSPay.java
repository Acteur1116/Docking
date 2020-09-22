package com.renard.rhsdk.plugin;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import com.renard.rhsdk.imple.SimpleDefaultPay;
import com.renard.rhsdk.imple.SimpleDefaultSW;
import com.renard.rhsdk.log.Log;
import com.renard.rhsdk.imple.ClosePay;
import com.renard.rhsdk.pay.PayParams;
import com.renard.rhsdk.pay.RHOrder;
import com.renard.rhsdk.pay.SW;
import com.renard.rhsdk.sdk.RHSDKManager;
import com.renard.rhsdk.user.RHSDKFlag;
import com.renard.rhsdk.util.RHSDKTools;
import com.renard.rhsdk.verify.RHVerify;

/**
 * Created by Riven_rabbit on 2020/9/22
 *
 * @author suyanan
 */
public class KSPay {
    private static KSPay instance;

    private PayPlugin payPlugin;
    private SWPlugin swPlugin;

    private KSPay(){

    }

    public static KSPay getInstance(){
        if(instance == null){
            instance = new KSPay();
        }
        return instance;
    }

    public void init(){

        int sdkFlag = RHSDKManager.getInstance().getSdkFlag();
        if(sdkFlag== RHSDKFlag.OPEN){
            this.payPlugin = (PayPlugin)PluginFactory.getInstance().initPlugin(PayPlugin.PLUGIN_TYPE);
        }else if(sdkFlag==RHSDKFlag.SWITCH){
            this.payPlugin = (PayPlugin)PluginFactory.getInstance().initPlugin(Constants.PLUGIN_TYPE_PAY_SW);
            if(this.payPlugin == null){
                this.payPlugin = (PayPlugin)PluginFactory.getInstance().initPlugin(PayPlugin.PLUGIN_TYPE);
            }
        }else{
            this.payPlugin = new ClosePay();
        }
        if(this.payPlugin == null){
            this.payPlugin = new SimpleDefaultPay();
        }

        this.swPlugin = (SWPlugin)PluginFactory.getInstance().initPlugin(SWPlugin.PLUGIN_TYPE);
        if(this.swPlugin == null){
            this.swPlugin = new SimpleDefaultSW();
        }
    }

    public boolean isSupport(String method){
        if(this.payPlugin == null){
            return false;
        }

        return this.payPlugin.isSupportMethod(method);
    }

    public boolean isSupportSw(String method){
        if(this.swPlugin == null){
            return false;
        }

        return this.swPlugin.isSupportMethod(method);
    }

    /***
     * 支付接口（弹出支付界面）
     * @param data
     */
    public void pay(PayParams data){
        if(this.payPlugin == null){
            return;
        }

        Log.d("SuperSYSDK", "****PayParams Print Begin****");
        Log.d("SuperSYSDK", "productId="+data.getProductId());
        Log.d("SuperSYSDK", "productName="+data.getProductName());
        Log.d("SuperSYSDK", "productDesc="+data.getProductDesc());
        Log.d("SuperSYSDK", "price="+data.getPrice());
        Log.d("SuperSYSDK", "origPrice="+data.getOrigPrice());
        Log.d("SuperSYSDK", "coinNum="+data.getCoinNum());
        Log.d("SuperSYSDK", "serverId="+data.getServerId());
        Log.d("SuperSYSDK", "serverName="+data.getServerName());
        Log.d("SuperSYSDK", "roleId="+data.getRoleId());
        Log.d("SuperSYSDK", "roleName="+data.getRoleName());
        Log.d("SuperSYSDK", "roleLevel="+data.getRoleLevel());
        Log.d("SuperSYSDK", "vip="+data.getVip());
        //Log.d("SuperSYSDK", "orderID="+data.getOrderID());
        Log.d("SuperSYSDK", "extension="+data.getExtension());
        Log.d("SuperSYSDK", "****PayParams Print End****");

        if(RHSDKManager.getInstance().isGetOrder()){

            startOrderTask(data);
        }else{
            this.payPlugin.pay(data);
        }

    }

    //默认的AsyncTask的执行顺序可能会有些影响，导致队列中的任务并不能被及时执行
    private void startOrderTask(PayParams data){
        GetOrderTask authTask = new GetOrderTask(data);
        if (Build.VERSION.SDK_INT >= 11) //Build.VERSION_CODES.HONEYCOMB
        {
            authTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else
        {
            authTask.execute();
        }
    }

    class GetOrderTask extends AsyncTask<Void, Void, RHOrder>{

        private PayParams data;

        private ProgressDialog processTip;

        public GetOrderTask(PayParams data){
            this.data = data;
        }

        protected void onPreExecute(){
            processTip = RHSDKTools.showProgressTip(RHSDKManager.getInstance().getContext(), "正在启动支付，请稍后...");
        }

        @Override
        protected RHOrder doInBackground(Void... args) {
            Log.d("SuperSYSDK", "begin to get order id from ssysdkserver...");
            RHOrder result = RHVerify.getOrder(data);

            return result;
        }

        protected void onPostExecute(RHOrder order){

            RHSDKTools.hideProgressTip(processTip);

            if(order == null){
                Log.e("SuperSYSDK", "get order from ssysdkserver failed.");
                Toast.makeText(RHSDKManager.getInstance().getContext(), "获取订单号失败", Toast.LENGTH_SHORT).show();
                return;
            }

            data.setOrderID(order.getOrder());
            data.setExtension(order.getExtension());

            if(order.getSw() == SW.SW_PLUGIN){
                swPlugin.sw(data, order);
            }else{
                payPlugin.pay(data);
            }
        }

    }
}
