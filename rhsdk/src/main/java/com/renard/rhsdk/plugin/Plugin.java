package com.renard.rhsdk.plugin;

/**
 * Created by Riven_rabbit on 2020/9/17
 *
 * @author suyanan
 */
public interface Plugin {

    /**
     * 是否支持某个接口
     * @param methodName
     * @return
     */
    public boolean isSupportMethod(String methodName);
}
