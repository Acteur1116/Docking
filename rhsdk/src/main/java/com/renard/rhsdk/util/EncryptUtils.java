package com.renard.rhsdk.util;

import java.security.MessageDigest;

/**
 * Created by Riven_rabbit on 2020/9/16
 *
 * @author suyanan
 */
public class EncryptUtils {
    public static String md5(String txt){

        return encrypt(txt, "md5");
    }

    public static String sha(String txt){

        return encrypt(txt, "sha");
    }

    private static String encrypt(String txt, String algorithName){

        if(txt == null || txt.trim().length() == 0){
            return null;
        }

        if(algorithName == null || algorithName.trim().length() == 0){
            algorithName = "MD5";
        }

        String result = null;

        try{
            MessageDigest m = MessageDigest.getInstance(algorithName);
            m.reset();
            m.update(txt.getBytes("UTF-8"));
            byte[] bts = m.digest();

            result = hex(bts);
        }catch (Exception e){
            e.printStackTrace();
        }


        return result;
    }

    private static String hex(byte[] bts){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<bts.length; i++){
            sb.append(Integer.toHexString((bts[i] & 0xFF) | 0x100).substring(1,3));
        }

        return sb.toString();
    }
}
