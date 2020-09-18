package com.renard.rhsdk.util;

/**
 * Created by Riven_rabbit on 2020/9/16
 *
 * @author suyanan
 */
public class Arrays {
    public static boolean contain(Object[] array, Object element){
        if(array == null || array.length == 0){
            return false;
        }

        for(Object ele : array){
            if(ele.equals(element)){
                return true;
            }
        }

        return false;
    }
}
