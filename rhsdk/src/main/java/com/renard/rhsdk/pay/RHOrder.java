package com.renard.rhsdk.pay;

/**
 * Created by Riven_rabbit on 2020/9/22
 *
 * @author suyanan
 */
public class RHOrder {
    private String order;
    private String extension;

    private int sw = SW.SW_CHANNEL;
    private String url;
    private String key;

    public RHOrder(String order, String ext){
        this.order = order;
        this.extension = ext;
    }

    public String getOrder() {
        return order;
    }
    public void setOrder(String order) {
        this.order = order;
    }
    public String getExtension() {
        return extension;
    }
    public void setExtension(String extension) {
        this.extension = extension;
    }

    public int getSw() {
        return sw;
    }
    public void setSw(int sw) {
        this.sw = sw;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
