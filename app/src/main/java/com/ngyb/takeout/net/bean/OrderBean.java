package com.ngyb.takeout.net.bean;

import com.ngyb.takeout.net.bean.GoodsInfoBean;
import com.ngyb.takeout.net.bean.SellerBean;

import java.util.List;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/14 21:37
 */
public class OrderBean {
    private String id;
    private String type;
    private SellerBean sellerBean;
    private RiderBean riderBean;
    private List<GoodsInfoBean> goodsInfoBeans;
    private DistributionBean distributionBean;
    private OrderDetailBean orderDetailBean;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SellerBean getSellerBean() {
        return sellerBean;
    }

    public void setSellerBean(SellerBean sellerBean) {
        this.sellerBean = sellerBean;
    }

    public RiderBean getRiderBean() {
        return riderBean;
    }

    public void setRiderBean(RiderBean riderBean) {
        this.riderBean = riderBean;
    }

    public List<GoodsInfoBean> getGoodsInfoBeans() {
        return goodsInfoBeans;
    }

    public void setGoodsInfoBeans(List<GoodsInfoBean> goodsInfoBeans) {
        this.goodsInfoBeans = goodsInfoBeans;
    }

    public DistributionBean getDistributionBean() {
        return distributionBean;
    }

    public void setDistributionBean(DistributionBean distributionBean) {
        this.distributionBean = distributionBean;
    }

    public OrderDetailBean getOrderDetailBean() {
        return orderDetailBean;
    }

    public void setOrderDetailBean(OrderDetailBean orderDetailBean) {
        this.orderDetailBean = orderDetailBean;
    }
}
