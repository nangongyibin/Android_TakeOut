package com.ngyb.takeout.net.bean;

import java.util.List;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/5/15 09:36
 */
public class HomeInfoBean {

    /**
     * body : [{"recommendInfos":[],"seller":{"activityList":[],"deliveryFee":"5","distance":"","ensure":"","id":1,"invoice":"","name":"南宫燚滨外卖项目","pic":"/imgs/category/1.png","recentVisit":"","sale":"","score":"5","sendPrice":"10","time":""},"type":0},{"recommendInfos":[],"seller":{"activityList":[],"deliveryFee":"5","distance":"","ensure":"","id":2,"invoice":"","name":"南宫燚滨第2家分店","pic":"","recentVisit":"","sale":"","score":"","sendPrice":"10","time":""},"type":0},{"recommendInfos":[],"seller":{"activityList":[],"deliveryFee":"5","distance":"","ensure":"","id":3,"invoice":"","name":"南宫燚滨第3家分店","pic":"","recentVisit":"","sale":"","score":"","sendPrice":"10","time":""},"type":0},{"recommendInfos":[],"seller":{"activityList":[],"deliveryFee":"5","distance":"","ensure":"","id":4,"invoice":"","name":"南宫燚滨第4家分店","pic":"","recentVisit":"","sale":"","score":"","sendPrice":"10","time":""},"type":0},{"recommendInfos":[],"seller":{"activityList":[],"deliveryFee":"5","distance":"","ensure":"","id":5,"invoice":"","name":"南宫燚滨第5家分店","pic":"","recentVisit":"","sale":"","score":"","sendPrice":"10","time":""},"type":0},{"recommendInfos":[],"seller":{"activityList":[],"deliveryFee":"5","distance":"","ensure":"","id":6,"invoice":"","name":"南宫燚滨第6家分店","pic":"","recentVisit":"","sale":"","score":"","sendPrice":"10","time":""},"type":0},{"recommendInfos":[],"seller":{"activityList":[],"deliveryFee":"5","distance":"","ensure":"","id":7,"invoice":"","name":"南宫燚滨第7家分店","pic":"","recentVisit":"","sale":"","score":"","sendPrice":"10","time":""},"type":0},{"recommendInfos":[],"seller":{"activityList":[],"deliveryFee":"5","distance":"","ensure":"","id":8,"invoice":"","name":"南宫燚滨第8家分店","pic":"","recentVisit":"","sale":"","score":"","sendPrice":"10","time":""},"type":0},{"recommendInfos":[],"seller":{"activityList":[],"deliveryFee":"5","distance":"","ensure":"","id":9,"invoice":"","name":"南宫燚滨第9家分店","pic":"","recentVisit":"","sale":"","score":"","sendPrice":"10","time":""},"type":0},{"recommendInfos":["黄焖鸡","米线","南宫燚滨","南宫燚滨","酷丁鱼","博学谷"],"seller":null,"type":1},{"recommendInfos":[],"seller":{"activityList":[],"deliveryFee":"5","distance":"","ensure":"","id":10,"invoice":"","name":"南宫燚滨第10家分店","pic":"","recentVisit":"","sale":"","score":"","sendPrice":"10","time":""},"type":0},{"recommendInfos":[],"seller":{"activityList":[],"deliveryFee":"5","distance":"","ensure":"","id":11,"invoice":"","name":"南宫燚滨第11家分店","pic":"","recentVisit":"","sale":"","score":"","sendPrice":"10","time":""},"type":0},{"recommendInfos":[],"seller":{"activityList":[],"deliveryFee":"5","distance":"","ensure":"","id":12,"invoice":"","name":"南宫燚滨第12家分店","pic":"","recentVisit":"","sale":"","score":"","sendPrice":"10","time":""},"type":0},{"recommendInfos":[],"seller":{"activityList":[],"deliveryFee":"5","distance":"","ensure":"","id":13,"invoice":"","name":"南宫燚滨第13家分店","pic":"","recentVisit":"","sale":"","score":"","sendPrice":"10","time":""},"type":0},{"recommendInfos":[],"seller":{"activityList":[],"deliveryFee":"5","distance":"","ensure":"","id":14,"invoice":"","name":"南宫燚滨第14家分店","pic":"","recentVisit":"","sale":"","score":"","sendPrice":"10","time":""},"type":0},{"recommendInfos":[],"seller":{"activityList":[],"deliveryFee":"5","distance":"","ensure":"","id":15,"invoice":"","name":"南宫燚滨第15家分店","pic":"","recentVisit":"","sale":"","score":"","sendPrice":"10","time":""},"type":0},{"recommendInfos":[],"seller":{"activityList":[],"deliveryFee":"5","distance":"","ensure":"","id":16,"invoice":"","name":"南宫燚滨第16家分店","pic":"","recentVisit":"","sale":"","score":"","sendPrice":"10","time":""},"type":0},{"recommendInfos":[],"seller":{"activityList":[],"deliveryFee":"5","distance":"","ensure":"","id":17,"invoice":"","name":"南宫燚滨第17家分店","pic":"","recentVisit":"","sale":"","score":"","sendPrice":"10","time":""},"type":0},{"recommendInfos":[],"seller":{"activityList":[],"deliveryFee":"5","distance":"","ensure":"","id":18,"invoice":"","name":"南宫燚滨第18家分店","pic":"","recentVisit":"","sale":"","score":"","sendPrice":"10","time":""},"type":0},{"recommendInfos":[],"seller":{"activityList":[],"deliveryFee":"5","distance":"","ensure":"","id":19,"invoice":"","name":"南宫燚滨第19家分店","pic":"","recentVisit":"","sale":"","score":"","sendPrice":"10","time":""},"type":0}]
     * head : {"categorieList":[{"id":1,"name":"美食","pic":"/imgs/category/1.png"},{"id":2,"name":"甜品饮料","pic":"/imgs/category/2.png"},{"id":3,"name":"商店超市","pic":"/imgs/category/3.png"},{"id":4,"name":"早餐","pic":"/imgs/category/4.png"},{"id":5,"name":"果蔬","pic":"/imgs/category/5.png"},{"id":6,"name":"新店","pic":"/imgs/category/6.png"},{"id":7,"name":"下午茶","pic":"/imgs/category/7.png"},{"id":8,"name":"麻辣烫","pic":"/imgs/category/8.png"}],"promotionList":[{"id":1,"info":"promotion info...","pic":"/imgs/promotion/1.jpg"},{"id":2,"info":"promotion info...","pic":"/imgs/promotion/2.jpg"},{"id":3,"info":"promotion info...","pic":"/imgs/promotion/3.jpg"}]}
     */

    private HeadBean head;
    private List<BodyBean> body;

    public HeadBean getHead() {
        return head;
    }

    public void setHead(HeadBean head) {
        this.head = head;
    }

    public List<BodyBean> getBody() {
        return body;
    }

    public void setBody(List<BodyBean> body) {
        this.body = body;
    }
}
