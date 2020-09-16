package com.renard.rhsdk.plugin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Xml;

import com.renard.rhsdk.log.Log;
import com.renard.rhsdk.sdk.RHSDKManager;
import com.renard.rhsdk.sdk.RHSDKParams;
import com.renard.rhsdk.util.RHSDKTools;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Riven_rabbit on 2020/9/15
 *
 * @author suyanan
 */
@SuppressLint("UseSparseArrays")
public class PluginFactory {
    private static PluginFactory instance;

    private Map<Integer, String> supportedPlugins;

    private PluginFactory(){
        supportedPlugins = new HashMap<Integer, String>();
    }

    public static PluginFactory getInstance(){
        if(instance == null){
            instance = new PluginFactory();
        }

        return instance;
    }

    private boolean isSupportPlugin(int type){

        return supportedPlugins.containsKey(type);
    }

    private String getPluginName(int type){
        if(supportedPlugins.containsKey(type)){
            return supportedPlugins.get(type);
        }
        return null;
    }

    public Bundle getMetaData(Context context)
    {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);

            if (appInfo != null && appInfo.metaData != null)
            {
                return appInfo.metaData;
            }
        } catch (PackageManager.NameNotFoundException e) {
        }

        return new Bundle();
    }

    public RHSDKParams getSDKParams(Context context){
        Map<String, String> configs = RHSDKTools.getAssetPropConfig(context, "ssy185_developer.properties");
        return new RHSDKParams(configs);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object initPlugin(int type){
        Class localClass = null;

        try {

            if(!isSupportPlugin(type)){

                if(type == UserPlugin.PLUGIN_TYPE || type == PayPlugin.PLUGIN_TYPE){
                    Log.e("RHSDK", "The config of the RHSDK is not support plugin type:"+type);
                }else{
                    Log.w("RHSDK", "The config of the RHSDK is not support plugin type:"+type);
                }

                return null;
            }

            String pluginName = getPluginName(type);

            localClass = Class.forName(pluginName);

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        try {
            return localClass.getDeclaredConstructor(new Class[]{Activity.class}).newInstance(new Object[]{RHSDKManager.getInstance().getContext()});
        } catch (Exception e) {

            try {
                //以默认构造函数再次尝试实例化
                return localClass.getDeclaredConstructor().newInstance();

            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return null;
    }


    public void loadPluginInfo(Context context){
        String xmlPlugins = RHSDKTools.getAssetConfigs(context, "ssy185_plugin.xml");

        if (xmlPlugins == null)
        {
            Log.e("RHSDK", "fail to load ssy185_plugin.xml");
            return;
        }

        XmlPullParser parser = Xml.newPullParser();

        try {
            parser.setInput(new StringReader(xmlPlugins));

            int eventType = parser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){

                switch(eventType){
                    case XmlPullParser.START_TAG:
                        String tag = parser.getName();
                        if("plugin".equals(tag)){
                            String name = parser.getAttributeValue(0);
                            int type = Integer.parseInt(parser.getAttributeValue(1));
                            this.supportedPlugins.put(type, name);
                            Log.d("RHSDK", "Curr Supported Plugin: "+type+"; name:"+name);
                        }
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
