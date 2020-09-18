package com.renard.rhsdk.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.renard.rhsdk.analytics.RHSubmit;
import com.renard.rhsdk.insterface.ActivityCallback;
import com.renard.rhsdk.insterface.RHSDKListener;
import com.renard.rhsdk.log.Log;
import com.renard.rhsdk.plugin.KSUser;
import com.renard.rhsdk.plugin.PluginFactory;
import com.renard.rhsdk.user.RHSDKFlag;
import com.renard.rhsdk.util.EncryptUtils;
import com.renard.rhsdk.util.HttpUtils;
import com.renard.rhsdk.verify.RHToken;
import com.renard.rhsdk.verify.RHVerify;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Riven_rabbit on 2020/9/15
 *
 * @author suyanan
 */
public class RHSDKManager {
    private static final String DEFAULT_PKG_NAME = "com.ssy185.sdk";
    private static final String APP_PROXY_NAME = "SSY185_APPLICATION_PROXY_NAME";
    private static final String APP_GAME_NAME = "SSY185_Game_Application";
    public static RHSDKManager instance;

    private Application application;
    private Activity context;
    private Handler mainThreadHandler;

    private RHSDKParams developInfo;
    private Bundle metaData;

    private List<RHSDKListener> listeners;

    private List<ActivityCallback> activityCallbacks;

//    private List<IApplicationListener> applicationListeners;

    private String sdkUserID = null;
    private RHToken tokenData = null;

    public RHSDKParams getSDKParams(){
        return developInfo;
    }

    public Bundle getMetaData() {
        return metaData;
    }

    public RHSDKManager() {
        mainThreadHandler = new Handler(Looper.getMainLooper());
        listeners = new ArrayList<RHSDKListener>();
        activityCallbacks = new ArrayList<ActivityCallback>(1);
//        applicationListeners = new ArrayList<ApplicationListener>(2);
    }
    public static RHSDKManager getInstance(){
        if (instance==null){
            instance=new RHSDKManager();
        }
        return instance;
    }

    /**
     * 获取子渠道号
     * @return
     */
    public int getSubChannel(){

        if(this.developInfo == null || !this.developInfo.contains("SSY185_Sub_Channel")){
            return 0;
        }else{
            return this.developInfo.getInt("SSY185_Sub_Channel");
        }
    }

    /**
     * 获取当前SDK对应的渠道号
     * @return
     */
    public int getCurrChannel(){

        if(this.developInfo == null || !this.developInfo.contains("RH_ChannelID")){
            return 0;
        }else{
            return this.developInfo.getInt("RH_ChannelID");
        }

    }

    public int getAppID(){
        if(this.developInfo == null || !this.developInfo.contains("RH_AppID")){
            return 0;
        }

        return this.developInfo.getInt("RH_AppID");
    }

    public String getAppKey(){
        if(this.developInfo == null || !this.developInfo.contains("RH_AppKey")){
            return "";
        }

        return this.developInfo.getString("RH_AppKey");
    }

    public String getPayPrivateKey(){
        if(this.developInfo == null || !this.developInfo.contains("SSY185_PAY_PRIVATEKEY")){
            return "";
        }

        return this.developInfo.getString("SSY185_PAY_PRIVATEKEY");
    }

    //是否走登录验证
    public boolean isAuth(){

        return getAuthURL() != null;
    }

    //是否客户端下单
    public boolean isGetOrder(){

        return getOrderURL() != null;
    }

    public String getOrderURL(){
        if(this.developInfo == null){
            return null;
        }

        if(this.developInfo.contains("SSY185_ORDER_URL")){

            return this.developInfo.getString("SSY185_ORDER_URL");
        }

        String baseUrl = getSDKServerURL();
        if(baseUrl == null){
            return null;
        }

        return baseUrl + "/pay/getOrderID";
    }

    public String getAuthURL(){
        if(this.developInfo == null ){
            return null;
        }

        if(this.developInfo.contains("SSY185_AUTH_URL")){

            return this.developInfo.getString("SSY185_AUTH_URL");
        }

        String baseUrl = getSDKServerURL();
        if(baseUrl == null){
            return null;
        }

        return baseUrl + "/user/getToken";

    }

    public String getAnalyticsURL(){
        if(this.developInfo == null){
            return null;
        }

        if(this.developInfo.contains("SSY185_ANALYTICS_URL")){

            return this.developInfo.getString("SSY185_ANALYTICS_URL");
        }

        String baseUrl = getSDKServerURL();
        if(baseUrl == null){
            return null;
        }

        return baseUrl + "/user";

    }

    //获取ssysdkserver跟地址
    public String getSDKServerURL(){
        if(this.developInfo == null || !this.developInfo.contains("SSY185_SERVER_URL")){
            return "http://dev.185sy.com";
        }

        String url = this.developInfo.getString("SSY185_SERVER_URL");
        if(url == null || url.trim().length() == 0){
            return "http://dev.185sy.com";
        }

        while(url.endsWith("/")){
            url = url.substring(0, url.length()-1);
        }
        return url;
    }

    //是否使用ssysdkserver统计功能
    public boolean isUseSDKAnalytics(){
        if(this.developInfo == null || !this.developInfo.contains("SSY185_ANALYTICS")){
            return true;
        }
        String use = this.developInfo.getString("SSY185_ANALYTICS");
        return "true".equalsIgnoreCase(use);
    }

    //当前渠道SDK是否需要显示闪屏
    public boolean isSDKShowSplash(){
        if(this.developInfo == null || !this.developInfo.contains("SSY185_SDK_SHOW_SPLASH")){
            return false;
        }

        String show = this.developInfo.getString("SSY185_SDK_SHOW_SPLASH");
        return "true".equalsIgnoreCase(show);
    }

    //获取当前渠道SDK的版本号
    public String getSDKVersionCode(){
        if(this.developInfo == null || !this.developInfo.contains("SSY185_SDK_VERSION_CODE")){
            return "1";
        }

        return this.developInfo.getString("SSY185_SDK_VERSION_CODE");
    }

    //获取提示消息
    public String getMsg(){
        if(this.developInfo == null || !this.developInfo.contains("msg")){
            return "";
        }

        return this.developInfo.getString("msg");
    }

    public void setSDKListener(RHSDKListener listener){
        if(!listeners.contains(listener) && listener != null){
            this.listeners.add(listener);
        }
    }

    public void setActivityCallback(ActivityCallback callback){
        //this.activityCallback = callback;
        if(!this.activityCallbacks.contains(callback) && callback != null){
            this.activityCallbacks.add(callback);
        }

    }

    public Application getApplication(){

        return this.application;
    }

    public String getSDKUserID(){
        return this.sdkUserID;
    }

    public RHToken getUToken(){
        return this.tokenData;
    }

    /**
     * called from onCreate method of Application
     * @param application
     */
    public void onAppCreate(Application application){
        this.application = application;
//        for(IApplicationListener lis : applicationListeners){
//            lis.onProxyCreate();
//        }
    }

    /**
     * called from attachBaseContext method of Application
     * @param application
     * @param context
     */
    public void onAppAttachBaseContext(Application application, Context context){
        this.application = application;

//        MultiDex.install(application);
        Log.init(context);

//        applicationListeners.clear();

        PluginFactory.getInstance().loadPluginInfo(context);
        developInfo = PluginFactory.getInstance().getSDKParams(context);
        metaData = PluginFactory.getInstance().getMetaData(context);

        startInitTask();

        if(metaData.containsKey(APP_PROXY_NAME)){
            String proxyAppNames = metaData.getString(APP_PROXY_NAME);
            String[] proxyApps = proxyAppNames.split(",");
            for(String proxy : proxyApps){
                if(!TextUtils.isEmpty(proxy)){
                    Log.d("RHSDK", "add a new application listener:"+proxy);
//                    IApplicationListener listener = newApplicationInstance(application, proxy);
//                    if(listener != null){
//                        applicationListeners.add(listener);
//                    }
                }
            }
        }

        if(metaData.containsKey(APP_GAME_NAME)){
            String gameAppName = metaData.getString(APP_GAME_NAME);
//            IApplicationListener listener = newApplicationInstance(application, gameAppName);
//            if(listener != null){
//                Log.e("RHSDK", "add a game application listener:"+gameAppName);
//                applicationListeners.add(listener);
//            }

        }

//        for(IApplicationListener lis : applicationListeners){
//            lis.onProxyAttachBaseContext(context);
//        }
    }

    /**
     * called from onConfigurationChanged method of Application
     * @param application
     * @param newConfig
     */
    public void onAppConfigurationChanged(Application application, Configuration newConfig){
//        for(IApplicationListener lis : applicationListeners){
//            lis.onProxyConfigurationChanged(newConfig);
//        }
    }

    public void onTerminate(){
//        for(IApplicationListener lis : applicationListeners){
//            lis.onProxyTerminate();
//        }
        Log.destory();
    }

    @SuppressWarnings("rawtypes")
//    private IApplicationListener newApplicationInstance(Application application, String proxyAppName){
//
//        if(proxyAppName == null || SDKTools.isNullOrEmpty(proxyAppName)){
//            return null;
//        }
//
//        if(proxyAppName.startsWith(".")){
//            proxyAppName = DEFAULT_PKG_NAME + proxyAppName;
//        }
//
//        try {
//            Class clazz = Class.forName(proxyAppName);
//            return (IApplicationListener)clazz.newInstance();
//
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//
//        return null;
//    }

    /***
     * 游戏调用抽象层的时候，需要在Activity的onCreate方法中调用该方法
     * @param context
     */
    public void init(Activity context){
        this.context = context;
        try{
            if(isUseSDKAnalytics()){
                RHSubmit.getInstance().init(context);
            }

//            SSY185Push.getInstance().init();
            KSUser.getInstance().init();
//            SSY185Pay.getInstance().init();
//            SSY185Share.getInstance().init();
//            SSY185Analytics.getInstance().init();
//            SSY185Download.getInstance().init();
//            SSY185Gear.getInstance().init();

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private int getSdkFlagFromServer(){
        //请求服务器动态参数
        try{
            Log.d("RHSDK", "begin init from ssysdkserver");

            int appID = this.getAppID();
            int channelID = this.getCurrChannel();
            String packageName = RHSDKManager.getInstance().getApplication().getPackageName();

            Map<String, String> params = new HashMap<String, String>();
            params.put("appID", appID+"");
            params.put("channelID", channelID+"");
            params.put("packageName", packageName);

            StringBuilder sb = new StringBuilder();
            sb.append("appID=").append(appID)
                    .append("channelID=").append(channelID)
                    .append("packageName=").append(packageName)
                    .append(this.getAppKey());

            String sign = EncryptUtils.md5(sb.toString()).toLowerCase();

            params.put("sign", sign);

            String url = this.getSDKServerURL() + "/user/init";
            String result = HttpUtils.httpPost(url, params);

            Log.d("RHSDK", "The sign is " + sign + " The result is "+result);
            if(result != null && result.trim().length() > 0){

                JSONObject jsonObj = new JSONObject(result);
                int state = jsonObj.getInt("state");
                if(state == 1){
                    int sdkFlag = jsonObj.getInt("sdkFlag");
                    this.developInfo.put("SdkFlag", sdkFlag+"");
                    if(sdkFlag == RHSDKFlag.SWITCH){
                        this.developInfo.put("RH_ChannelID", jsonObj.getString("RH_ChannelID"));
                        this.developInfo.put("AppID", jsonObj.getString("AppID"));
                        this.developInfo.put("ClientKey", jsonObj.getString("ClientKey"));
                        this.developInfo.put("msg", jsonObj.has("msg")?jsonObj.getString("msg"):"");
                    }else{
                        this.developInfo.put("msg", jsonObj.has("msg")?jsonObj.getString("msg"):"");
                    }
                }else{
                    this.developInfo.put("SdkFlag", RHSDKFlag.OPEN+"");
                }
            }else{
                this.developInfo.put("SdkFlag", RHSDKFlag.OPEN+"");
            }

        }catch(Exception e){
            this.developInfo.put("SdkFlag", RHSDKFlag.OPEN+"");
            e.printStackTrace();
            Log.e("RHSDK", "init failed.\n"+e.getMessage());
        }

        return this.developInfo.getInt("SdkFlag");
    }

    public int getSdkFlag(){
        if(this.developInfo == null){
            return RHSDKFlag.OPEN;
        }

        //由于初始化异步请求服务器，这里等待服务器返回
        int i=0;
        while(!this.developInfo.contains("SdkFlag")){
            i += 100;
            if(i > 5000){//5秒超时
                return RHSDKFlag.OPEN;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return this.developInfo.getInt("SdkFlag");
    }

    public void runOnMainThread(Runnable runnable){
        if(mainThreadHandler != null){
            mainThreadHandler.post(runnable);
            return;
        }

        if(context != null){
            context.runOnUiThread(runnable);
        }
    }

    public void setContext(Activity context){
        this.context = context;
    }

    public Activity getContext(){
        return this.context;
    }

    public void onResult(int code, String msg){
        for(RHSDKListener listener : listeners){
            listener.onResult(code, msg);
        }
    }

    public void onLoginResult(String result){

        for(RHSDKListener listener : listeners){
            listener.onLoginResult(result);
        }

        if(isAuth()){
//			AuthTask authTask = new AuthTask();
//			authTask.execute(result);
            startAuthTask(result);
        }

    }

    public void onSwitchAccount(){
        for(RHSDKListener listener : listeners){
            listener.onSwitchAccount();
        }
    }

    public void onSwitchAccount(String result){
        for(RHSDKListener listener : listeners){
            listener.onSwitchAccount(result);
        }

        if(isAuth()){
//			AuthTask authTask = new AuthTask();
//			authTask.execute(result);
            startAuthTask(result);
        }
    }

    public void onLogout(){
        for(RHSDKListener listener : listeners){
            listener.onLogout();
        }
    }

    private void onAuthResult(RHToken token){


        if(token.isSuc()){
            this.sdkUserID = token.getSdkUserID();
            this.tokenData = token;
        }

        for(RHSDKListener listener : listeners){
            listener.onAuthResult(token);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(this.activityCallbacks != null){
            for(ActivityCallback callback : this.activityCallbacks){
                callback.onActivityResult(requestCode, resultCode, data);
            }

        }
    }

    public void onBackPressed(){
        if(this.activityCallbacks != null){
            for(ActivityCallback callback : this.activityCallbacks){
                callback.onBackPressed();
            }

        }
    }

    public void onCreate(){
        if(this.activityCallbacks != null){
            for(ActivityCallback callback : this.activityCallbacks){
                callback.onCreate();
            }

        }
    }

    public void onStart(){
        if(this.activityCallbacks != null){
            for(ActivityCallback callback : this.activityCallbacks){
                callback.onStart();
            }
        }
    }

    public void onPause() {
        if(this.activityCallbacks != null){
            for(ActivityCallback callback : this.activityCallbacks){
                callback.onPause();
            }

        }
    }


    public void onResume() {

        if(this.activityCallbacks != null){
            for(ActivityCallback callback : this.activityCallbacks){
                callback.onResume();
            }

        }

    }


    public void onNewIntent(Intent newIntent) {
        if(this.activityCallbacks != null){
            for(ActivityCallback callback : this.activityCallbacks){
                callback.onNewIntent(newIntent);
            }

        }

    }

    public void onStop() {
        if(this.activityCallbacks != null){
            for(ActivityCallback callback : this.activityCallbacks){
                callback.onStop();
            }

        }

    }


    public void onDestroy() {
        if(this.activityCallbacks != null){
            for(ActivityCallback callback : this.activityCallbacks){
                callback.onDestroy();
            }

        }

    }


    public void onRestart() {
        if(this.activityCallbacks != null){
            for(ActivityCallback callback : this.activityCallbacks){
                callback.onRestart();
            }

        }

    }

    public void onConfigurationChanged(Configuration newConfig){
        if(this.activityCallbacks != null){
            for(ActivityCallback callback : this.activityCallbacks){
                callback.onConfigurationChanged(newConfig);
            }
        }
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults){
        if(this.activityCallbacks != null){
            for(ActivityCallback callback : this.activityCallbacks){
                callback.onRequestPermissionResult(requestCode, permissions, grantResults);
            }
        }
    }

    //默认的AsyncTask的执行顺序可能会有些影响，导致队列中的任务并不能被及时执行
    private void startAuthTask(String result){
        AuthTask authTask = new AuthTask();
        if (Build.VERSION.SDK_INT >= 11) //Build.VERSION_CODES.HONEYCOMB
        {
            authTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, result);
        }
        else
        {
            authTask.execute(result);
        }
    }

    //默认的AsyncTask的执行顺序可能会有些影响，导致队列中的任务并不能被及时执行
    private void startInitTask(){
        InitTask initTask = new InitTask();
        if (Build.VERSION.SDK_INT >= 11) //Build.VERSION_CODES.HONEYCOMB
        {
            initTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else
        {
            initTask.execute();
        }
    }

    class AuthTask extends AsyncTask<String, Void, RHToken>{


        @Override
        protected RHToken doInBackground(String... args) {

            String result = args[0];
            Log.d("RHSDK", "begin to auth...");
            RHToken token = RHVerify.auth(result);

            return token;
        }

        protected void onPostExecute(RHToken token){

            onAuthResult(token);
        }

    }

    class InitTask extends AsyncTask<Void, Void, Integer>{


        @Override
        protected Integer doInBackground(Void... args) {

            return getSdkFlagFromServer();
        }

        protected void onPostExecute(Integer sdkFlag){

            //do nothing
        }

    }
}
