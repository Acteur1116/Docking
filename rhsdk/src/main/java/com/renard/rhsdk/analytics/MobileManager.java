package com.renard.rhsdk.analytics;

import android.app.Activity;

import com.renard.rhsdk.log.Log;
import com.renard.rhsdk.sdk.RHSDKManager;
import com.renard.rhsdk.util.EncryptUtils;
import com.renard.rhsdk.util.HttpUtils;
import com.renard.rhsdk.util.MobileUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Riven_rabbit on 2020/9/16
 *
 * @author suyanan
 */
public class MobileManager {
    private static MobileManager instance;

    private MobileManager() {
    }

    public static MobileManager getInstance(){
        if (instance==null){
            instance=new MobileManager();
        }
        return instance;
    }

    public void submitUserInfo(String url, String appKey, UserLog log){
        try{

            Log.d("RHSDK", "begin submit user info to ssysdkserver:"+log.getOpType());

            Map<String, String> params = new HashMap<String, String>();
            params.put("userID", log.getUserID()+"");
            params.put("appID", log.getAppID()+"");
            params.put("channelID", log.getChannelID()+"");
            params.put("serverID", log.getServerID());
            params.put("serverName", log.getServerName());
            params.put("roleID", log.getRoleID());
            params.put("roleName", log.getRoleName());
            params.put("roleLevel", log.getRoleLevel());
            params.put("deviceID", log.getDeviceID());
            params.put("opType", log.getOpType()+"");

            StringBuilder sb = new StringBuilder();
            sb.append("appID=").append(log.getAppID())
                    .append("channelID=").append(log.getChannelID())
                    .append("deviceID=").append(log.getDeviceID())
                    .append("opType=").append(log.getOpType())
                    .append("roleID=").append(log.getRoleID())
                    .append("roleLevel=").append(log.getRoleLevel())
                    .append("roleName=").append(log.getRoleName())
                    .append("serverID=").append(log.getServerID())
                    .append("serverName=").append(log.getServerName())
                    .append("userID=").append(log.getUserID())
                    .append(appKey);

            String sign = EncryptUtils.md5(sb.toString()).toLowerCase();

            params.put("sign", sign);

            url = url + "/addUserLog";

            String result = HttpUtils.httpGet(url, params);

            Log.d("RHSDK", "The sign is " + sign + " The result is "+result);

            if(result != null && result.trim().length() > 0){

                JSONObject jsonObj = new JSONObject(result);
                int state = jsonObj.getInt("state");

                if(state != 1){
                    Log.d("RHSDK", "submit user info failed. the state is "+ state);
                }else{
                    Log.d("RHSDK", "submit user info success");
                }
            }

        }catch(Exception e){
            Log.e("RHSDK", "submit user info failed.\n"+e.getMessage());
            e.printStackTrace();
        }
    }

    public void submitDeviceInfo(Activity context, String url, String appKey, MobileDevice device){

        try{

            Log.d("RHSDK", "begin submit device info to ssysdkserver");

            Map<String, String> params = new HashMap<String, String>();
            params.put("appID", device.getAppID()+"");
            params.put("deviceID", device.getDeviceID());
            params.put("mac", device.getMac());
            params.put("deviceType", device.getDeviceType());
            params.put("deviceOS", device.getDeviceOS()+"");
            params.put("deviceDpi", device.getDeviceDpi());
            params.put("channelID", device.getChannelID()+"");
            params.put("subChannelID", device.getSubChannelID() == null ? "0" : device.getSubChannelID()+""); 		//PS:不加入签名

            StringBuilder sb = new StringBuilder();
            sb.append("appID=").append(device.getAppID()+"")
                    .append("channelID=").append(device.getChannelID())
                    .append("deviceDpi=").append(device.getDeviceDpi())
                    .append("deviceID=").append(device.getDeviceID())
                    .append("deviceOS=").append(device.getDeviceOS())
                    .append("deviceType=").append(device.getDeviceType())
                    .append("mac=").append(device.getMac())
                    .append(appKey);

            String sign = EncryptUtils.md5(sb.toString()).toLowerCase();

            params.put("sign", sign);

            url = url + "/addDevice";

            String result = HttpUtils.httpGet(url, params);

            Log.d("RHSDK", "The sign is " + sign + " The result is "+result);

            if(result != null && result.trim().length() > 0){

                JSONObject jsonObj = new JSONObject(result);
                int state = jsonObj.getInt("state");

                if(state != 1){
                    Log.d("RHSDK", "submit device info failed. the state is "+ state);
                }else{
                    Log.d("RHSDK", "submit device info success");
                }
            }


        }catch(Exception e){
            e.printStackTrace();
            Log.e("RHSDK", "submit device info failed.\n"+e.getMessage());
        }
    }

    /**
     * 手机用户设备信息
     * @param context
     * @param appID
     * @return
     */
    public MobileDevice collectDeviceInfo(Activity context, Integer appID, Integer channelID, Integer subChannelID){
        try{

            MobileDevice device = new MobileDevice();
            device.setAppID(appID);
            device.setChannelID(channelID);
            Log.d("RHSDK", "subChannelID:"+ RHSDKManager.getInstance().getSubChannel());
            device.setSubChannelID(RHSDKManager.getInstance().getSubChannel());
            device.setDeviceID(MobileUtil.getDeviceID(context));
            device.setMac(MobileUtil.getMacAddress(context));
            device.setDeviceType(android.os.Build.MODEL);
            device.setDeviceOS(1);
            device.setDeviceDpi(MobileUtil.getScreenDpi(context));

            return device;

        }catch(Exception e){
            e.printStackTrace();
            Log.e("RHSDK", e.getMessage());
        }

        return null;
    }
}
