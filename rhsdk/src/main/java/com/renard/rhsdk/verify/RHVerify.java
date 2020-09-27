package com.renard.rhsdk.verify;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.renard.rhsdk.log.Log;
import com.renard.rhsdk.pay.PayParams;
import com.renard.rhsdk.pay.RHOrder;
import com.renard.rhsdk.plugin.RHPay;
import com.renard.rhsdk.sdk.RHSDKManager;
import com.renard.rhsdk.util.EncryptUtils;
import com.renard.rhsdk.util.HttpUtils;
import com.renard.rhsdk.util.MobileUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Riven_rabbit on 2020/9/16
 *
 * @author suyanan
 */
public class RHVerify {
    /***
     * 去ssysdkserver进行SDK的登录认证，同时获取ssysdkserver返回的token，userID,sdkUserID等信息
     * @param result
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static RHToken auth(String result){

        try{
            Map<String, String> params = new HashMap<String, String>();
            params.put("appID", RHSDKManager.getInstance().getAppID()+"");
            params.put("channelID", "" + RHSDKManager.getInstance().getCurrChannel());
            params.put("subChannelID", "" + RHSDKManager.getInstance().getSubChannel());	//PS:不加入签名
            params.put("extension", result);
            params.put("sdkVersionCode", RHSDKManager.getInstance().getSDKVersionCode());
            params.put("deviceID", MobileUtil.getDeviceID(RHSDKManager.getInstance().getContext()));

            StringBuilder sb = new StringBuilder();
            sb.append("appID=").append(RHSDKManager.getInstance().getAppID()+"")
                    .append("channelID=").append(RHSDKManager.getInstance().getCurrChannel())
                    .append("extension=").append(result).append(RHSDKManager.getInstance().getAppKey());

            String sign = EncryptUtils.md5(sb.toString()).toLowerCase();

            params.put("sign", sign);

            String authResult = HttpUtils.httpGet(RHSDKManager.getInstance().getAuthURL(), params);

            Log.d("RHSDK", "The sign is " + sign + " The auth result is "+authResult);

            return parseAuthResult(authResult);

        }catch(Exception e){
            Log.e("RHSDK", "ssysdkserver auth exception.", e);
            e.printStackTrace();
        }

        return new RHToken();

    }

    /***
     * 访问ssysdkserver验证sid的合法性，同时获取ssysdkserver返回的token，userID,sdkUserID信息
     * 这里仅仅是测试，正式环境下，请通过游戏服务器来获取订单号，不要放在客户端操作
     * @param result
     * @return
     */
    public static RHOrder getOrder(PayParams data){

        try{

            RHToken tokenInfo = RHSDKManager.getInstance().getUToken();
            if(tokenInfo == null){
                Log.e("RHSDK", "The user not logined. the token is null");
                return null;
            }

            String packageName = RHPay.getInstance().isSupportSw("sw")?RHSDKManager.getInstance().getApplication().getPackageName():"";
            Log.d("RHSDK", "sw: "+ RHPay.getInstance().isSupportSw("sw")+" PackageName: "+packageName);

            Map<String, String> params = new HashMap<String, String>();
            params.put("userID", ""+tokenInfo.getUserID());
            params.put("productID", data.getProductId());
            params.put("productName", data.getProductName());
            params.put("productDesc", data.getProductDesc());
            params.put("money", ""+Math.round(data.getPrice()*100));
            params.put("origMoney", ""+Math.round(data.getOrigPrice()*100));
            params.put("roleID", ""+data.getRoleId());
            params.put("roleName", data.getRoleName());
            params.put("roleLevel", data.getRoleLevel()+"");
            params.put("serverID", data.getServerId());
            params.put("serverName", data.getServerName());
            params.put("extension", data.getExtension());
            params.put("notifyUrl", data.getPayNotifyUrl());
            params.put("appID", RHSDKManager.getInstance().getAppID()+"");
            params.put("channelID", RHSDKManager.getInstance().getCurrChannel()+"");
            params.put("packageName", packageName);

            params.put("signType", "md5");
            String sign = generateSign(tokenInfo, data, packageName);
            params.put("sign", sign);

            String orderResult = HttpUtils.httpPost(RHSDKManager.getInstance().getOrderURL(), params);

            Log.d("RHSDK", "The order result is "+orderResult);

            return parseOrderResult(orderResult);

        }catch(Exception e){
            e.printStackTrace();
        }

        return null;

    }

    private static String generateSign(RHToken token, PayParams data, String packageName) throws UnsupportedEncodingException {

        StringBuilder sb = new StringBuilder();
        sb.append("userID=").append(token.getUserID()).append("&")
                .append("productID=").append(data.getProductId() == null ? "" : data.getProductId()).append("&")
                .append("productName=").append(data.getProductName() == null ? "" : data.getProductName()).append("&")
                .append("productDesc=").append(data.getProductDesc() == null ? "" : data.getProductDesc()).append("&")
                .append("money=").append(Math.round(data.getPrice()*100)).append("&")
                .append("roleID=").append(data.getRoleId() == null ? "" : data.getRoleId()).append("&")
                .append("roleName=").append(data.getRoleName() == null ? "" : data.getRoleName()).append("&")
                .append("roleLevel=").append(data.getRoleLevel()).append("&")
                .append("serverID=").append(data.getServerId() == null ? "" : data.getServerId()).append("&")
                .append("serverName=").append(data.getServerName() == null ? "" : data.getServerName()).append("&")
                .append("extension=").append(data.getExtension() == null ? "" : data.getExtension());

        //这里是游戏服务器自己的支付回调地址，可以在下单的时候， 传给ssysdkserver。
        //ssysdkserver 支付成功之后， 会优先回调这个地址。 如果不传， 则需要在ssysdkserver后台游戏管理中配置游戏服务器的支付回调地址
        //如果传notifyUrl，则notifyUrl参与签名
        if(!TextUtils.isEmpty(data.getPayNotifyUrl())){
            sb.append("&notifyUrl=").append(data.getPayNotifyUrl());
        }
        //如果包名不为空，参与签名
        if(!TextUtils.isEmpty(packageName)){
            sb.append("&packageName=").append(packageName);
        }

        sb.append(RHSDKManager.getInstance().getAppKey());

        String encoded = URLEncoder.encode(sb.toString(), "UTF-8");	//url encode

        Log.d("RHSDK", "The encoded getOrderID sign is "+encoded);

        //这里用md5方式生成sign
        String sign = EncryptUtils.md5(encoded).toLowerCase();

        //如果签名方式是RSA，走下面方式
        //String privateKey = RHSDK.getInstance().getPayPrivateKey();
        //String sign = RSAUtils.sign(encoded, privateKey, "UTF-8", "SHA1withRSA");

        Log.d("RHSDK", "The getOrderID sign is "+sign);

        return sign;

    }


    private static RHToken parseAuthResult(String authResult){

        if(authResult == null || TextUtils.isEmpty(authResult)){

            return new RHToken();
        }

        try {
            JSONObject jsonObj = new JSONObject(authResult);
            int state = jsonObj.getInt("state");

            if(state != 1){
                Log.d("RHSDK", "auth failed. the state is "+ state);
                return new RHToken();
            }

            JSONObject jsonData = jsonObj.getJSONObject("data");

            return new RHToken(jsonData.getString("userID")
                    , jsonData.getString("sdkUserID")
                    , jsonData.getString("username")
                    , jsonData.getString("sdkUserName")
                    , jsonData.getString("token")
                    , jsonData.getString("extension"));

        } catch (JSONException e) {

            e.printStackTrace();
        }

        return new RHToken();
    }

    private static RHOrder parseOrderResult(String orderResult){

        try {
            JSONObject jsonObj = new JSONObject(orderResult);
            int state = jsonObj.getInt("state");

            if(state != 1){
                Log.d("RHSDK", "get order failed. the state is "+ state);
                return null;
            }

            JSONObject jsonData = jsonObj.getJSONObject("data");
            RHOrder rhOrder = new RHOrder(jsonData.getString("orderID"), jsonData.getString("extension"));
            if(jsonData.has("sw")){
                rhOrder.setSw(jsonData.getInt("sw"));
            }
            if(jsonData.has("url")){
                rhOrder.setUrl(jsonData.getString("url"));
            }
            if(jsonData.has("key")){
                rhOrder.setKey(jsonData.getString("key"));
            }

            return rhOrder;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
