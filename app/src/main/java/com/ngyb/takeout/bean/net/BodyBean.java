package com.ngyb.takeout.bean.net;

import java.util.List;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/5/15 09:42
 */
public class BodyBean {
    /**
     * recommendInfos : []
     * seller : {"activityList":[],"deliveryFee":"5","distance":"","ensure":"","id":1,"invoice":"","name":"南宫燚滨外卖项目","pic":"/imgs/category/1.png","recentVisit":"","sale":"","score":"5","sendPrice":"10","time":""}
     * type : 0
     */

    private SellerBean seller;
    private int type;
    private List<String> recommendInfos;

    public SellerBean getSeller() {
        return seller;
    }

    public void setSeller(SellerBean seller) {
        this.seller = seller;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getRecommendInfos() {
        return recommendInfos;
    }

    public void setRecommendInfos(List<String> recommendInfos) {
        this.recommendInfos = recommendInfos;
    }
}
