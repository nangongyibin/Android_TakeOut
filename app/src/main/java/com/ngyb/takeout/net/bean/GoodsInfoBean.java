package com.ngyb.takeout.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/6 12:26
 */
public class GoodsInfoBean implements Serializable {
    /**
     * bargainPrice : true
     * form : 肉末烧汁茄子+千叶豆腐+小食+时蔬+含粗粮米饭)
     * icon : /imgs/goods/caiping_taocan.webp
     * id : 1001
     * monthSaleNum : 53
     * name : 肉末烧汁茄子+千叶豆腐套餐(含粗粮米饭)
     * new : false
     * newPrice : 13.9
     * oldPrice : 30
     */

    private boolean bargainPrice;
    private int id;
    private String form;
    private String icon;
    private int monthSaleNum;
    private String name;
    private boolean isNew;//服务器字段传错了,用了关键字
    private float newPrice;
    private float oldPrice;
    private int count; //商品选择的数量
    private int typeId; //用于指定此商品所受的商品类型id
    private int sellerId; //商品所在商铺的id
    private String typeName;//类型名称

    public boolean isBargainPrice() {
        return bargainPrice;
    }

    public void setBargainPrice(boolean bargainPrice) {
        this.bargainPrice = bargainPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getMonthSaleNum() {
        return monthSaleNum;
    }

    public void setMonthSaleNum(int monthSaleNum) {
        this.monthSaleNum = monthSaleNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public float getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(float newPrice) {
        this.newPrice = newPrice;
    }

    public float getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(float oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
