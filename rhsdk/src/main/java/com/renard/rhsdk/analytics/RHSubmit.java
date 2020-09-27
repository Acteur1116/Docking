package com.renard.rhsdk.analytics;

import android.app.Activity;
import android.os.AsyncTask;

import com.renard.rhsdk.log.Log;
import com.renard.rhsdk.sdk.RHSDKManager;
import com.renard.rhsdk.user.UserExtraData;
import com.renard.rhsdk.util.MobileUtil;
import com.renard.rhsdk.verify.RHToken;

/**
 * Created by Riven_rabbit on 2020/9/16
 * 该类主要用于上报统计用户
 * @author suyanan
 */
public class RHSubmit {
    private static RHSubmit instance;

    private RHSubmit() {
    }

    public static RHSubmit getInstance(){
        if (instance==null){
            instance=new RHSubmit();
        }
        return instance;
    }
    /**
     * 初始化
     * 统计功能，上报设备信息
     */
    public void init(final Activity activity){
        try{
            final MobileDevice device = MobileManager.getInstance().collectDeviceInfo(activity,
                    RHSDKManager.getInstance().getAppID(), RHSDKManager.getInstance().getCurrChannel(),
                    RHSDKManager.getInstance().getSubChannel());
            if(device == null){
                Log.e("RHSDK", "collect device info failed");
                return;
            }

            SubmitTask task = new SubmitTask(new SubmitListener() {

                @Override
                public void run() {
                    MobileManager.getInstance().submitDeviceInfo(activity,
                            RHSDKManager.getInstance().getAnalyticsURL(),
                            RHSDKManager.getInstance().getAppKey(), device);
                }
            });
            task.execute();
        }catch(Exception e){
            Log.e("RHSDK", "submit device info failed.\n"+e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * 上报用户信息到SDKServer，供数据统计使用
     * @param context
     * @param user
     */
    public void submitUserInfo(final Activity context, final UserExtraData user){

        try{

            RHToken token = RHSDKManager.getInstance().getUToken();
            if(token == null){
                android.util.Log.e("RHSYSDK", "utoken is null. submit user info failed.");
                return;
            }

            final UserLog log = new UserLog();
            boolean sendable = false;
            switch(user.getDataType()){
                case UserExtraData.TYPE_CREATE_ROLE:
                    log.setOpType(UserLog.OP_CREATE_ROLE);
                    sendable = true;
                    break;
                case UserExtraData.TYPE_ENTER_GAME:
                    sendable = true;
                    log.setOpType(UserLog.OP_ENTER_GAME);
                    break;
                case UserExtraData.TYPE_LEVEL_UP:
                    sendable = true;
                    log.setOpType(UserLog.OP_LEVEL_UP);
                    break;
                case UserExtraData.TYPE_EXIT_GAME:
                    sendable = true;
                    log.setOpType(UserLog.OP_EXIT);
                    break;
            }

            if(sendable){
                log.setUserID(token.getUserID());
                log.setAppID(RHSDKManager.getInstance().getAppID());
                log.setChannelID(RHSDKManager.getInstance().getCurrChannel());
                log.setServerID(user.getServerID()+"");
                log.setServerName(user.getServerName());
                log.setRoleID(user.getRoleID());
                log.setRoleName(user.getRoleName());
                log.setRoleLevel(user.getRoleLevel());
                log.setDeviceID(MobileUtil.getDeviceID(context));


                SubmitTask task = new SubmitTask(new SubmitListener() {

                    @Override
                    public void run() {
                        MobileManager.getInstance().submitUserInfo( RHSDKManager.getInstance().getAnalyticsURL(), RHSDKManager.getInstance().getAppKey(), log);
                    }
                });
                task.execute();
            }

        }catch(Exception e){
            android.util.Log.e("RHSDK", "submit user info failed.\n"+e.getMessage());
            e.printStackTrace();
        }

    }

    class SubmitTask extends AsyncTask<Void, Void, Void> {

        private SubmitListener runListener;

        public SubmitTask(SubmitListener run){
            this.runListener = run;
        }

        @Override
        protected Void doInBackground(Void... arg) {

            if(this.runListener != null){
                this.runListener.run();
            }

            return null;
        }

    }

    interface SubmitListener {
        void run();
    }
}
