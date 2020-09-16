package com.renard.rhsdk.verify;

import org.json.JSONObject;

/**
 * Created by Riven_rabbit on 2020/9/15
 *
 * @author suyanan
 */
public class RHToken {
    private boolean suc;
    private String userID;
    private String sdkUserID;
    private String username;
    private String sdkUsername;
    private String token;
    private String extension;

    public RHToken(){
        this.suc = false;
    }

    public RHToken(String userID, String sdkUserID, String username, String sdkUsername, String token, String extension){
        this.userID = userID;
        this.sdkUserID = sdkUserID;
        this.username = username;
        this.sdkUsername = sdkUsername;
        this.token = token;
        this.extension = extension;
        this.suc = true;
    }

    public String toString(){

        JSONObject json = new JSONObject();
        try{
            json.put("suc", suc);
            json.put("userID", userID);
            json.put("sdkUserID", sdkUserID);
            json.put("username", username);
            json.put("sdkUsername", sdkUsername);
            json.put("token", token);
            json.put("extension", extension);
        }catch(Exception e){
            e.printStackTrace();
        }
        return json.toString();

    }

    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getSdkUserID() {
        return sdkUserID;
    }
    public void setSdkUserID(String sdkUserID) {
        this.sdkUserID = sdkUserID;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public boolean isSuc() {
        return suc;
    }

    public void setSuc(boolean suc) {
        this.suc = suc;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSdkUsername() {
        return sdkUsername;
    }

    public void setSdkUsername(String sdkUsername) {
        this.sdkUsername = sdkUsername;
    }
}
