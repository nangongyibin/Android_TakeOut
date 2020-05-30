package com.ngyb.takeout.bean.net;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/5/15 09:43
 */
public class SellerBean implements Serializable {
    /**
     * activityList : []
     * deliveryFee : 5
     * distance :
     * ensure :
     * id : 1
     * invoice :
     * name : 南宫燚滨外卖项目
     * pic : /imgs/category/1.png
     * recentVisit :
     * sale :
     * score : 5
     * sendPrice : 10
     * time :
     */

    private String deliveryFee;
    private String distance;
    private String ensure;
    private int id;
    private String invoice;
    private String name;
    private String pic;
    private String recentVisit;
    private String sale;
    private String score;
    private String sendPrice;
    private String time;
    private List<?> activityList;

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(String deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getEnsure() {
        return ensure;
    }

    public void setEnsure(String ensure) {
        this.ensure = ensure;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getRecentVisit() {
        return recentVisit;
    }

    public void setRecentVisit(String recentVisit) {
        this.recentVisit = recentVisit;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getSendPrice() {
        return sendPrice;
    }

    public void setSendPrice(String sendPrice) {
        this.sendPrice = sendPrice;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<?> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<?> activityList) {
        this.activityList = activityList;
    }
}
