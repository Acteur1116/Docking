package com.renard.rhsdk.pay;

/**
 * Created by Riven_rabbit on 2020/9/16
 *
 * @author suyanan
 */
public class PayParams {
    private String productId;//商品id
    private String productName;//商品名
    private String productDesc;//商品详情
    private float price; //实际价格
    private float origPrice; //原价 废弃字段
    private int ratio;	//兑换比例，暂时无用
    private int buyNum;//购买数量
    private int coinNum;//金币数量
    private String serverId;//服务器id
    private String serverName;//服务器名
    private String roleId;//角色id
    private String roleName;//角色名
    private int roleLevel;//角色等级
    private String payNotifyUrl;//支付回调url
    private String vip;//vip
    private String orderID;//订单id
    private String extension;//透传数据

    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }
    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public float getOrigPrice() {
        return origPrice;
    }
    public void setOrigPrice(float origPrice) {
        this.origPrice = origPrice;
    }
    public int getBuyNum() {
        return buyNum;
    }
    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }
    public int getCoinNum() {
        return coinNum;
    }
    public void setCoinNum(int coinNum) {
        this.coinNum = coinNum;
    }
    public String getServerId() {
        return serverId;
    }
    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
    public String getRoleId() {
        return roleId;
    }
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public int getRoleLevel() {
        return roleLevel;
    }
    public void setRoleLevel(int roleLevel) {
        this.roleLevel = roleLevel;
    }
    public String getExtension() {
        return extension;
    }
    public void setExtension(String extension) {
        this.extension = extension;
    }
    public int getRatio() {
        return ratio;
    }
    public void setRatio(int ratio) {
        this.ratio = ratio;
    }
    public String getServerName() {
        return serverName;
    }
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    public String getVip() {
        return vip;
    }
    public void setVip(String vip) {
        this.vip = vip;
    }
    public String getPayNotifyUrl() {
        return payNotifyUrl;
    }
    public void setPayNotifyUrl(String payNotifyUrl) {
        this.payNotifyUrl = payNotifyUrl;
    }
    public String getOrderID() {
        return orderID;
    }
    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
}
