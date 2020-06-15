package com.ngyb.takeout.observable;

import java.util.Observable;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/14 21:51
 */
public class OrderObservable extends Observable {
    //订单状态 10未支付 20已提交订单 30商家接单 40配送中，等待送达 50已送达 60取消订单
    public static final String ORDERTYPE_UNPAYMENT = "10";
    public static final String ORDERTYPE_SUBMIT = "20";
    public static final String ORDERTYPE_RECEIVEORDER = "30";
    public static final String ORDERTYPE_DISTRIBUTION = "40";
    public static final String ORDERTYPE_SERVED = "50";
    public static final String ORDERTYPE_CANCELLEDORDER = "60";
    //骑手状态 43接单  46取餐 48送餐
    public static final String ORDERTYPE_DISTRIBUTION_RIDER_RECEIVE = "43";
    public static final String ORDERTYPE_DISTRIBUTION_RIDER_TANK_MEAL = "46";
    public static final String ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL = "48";
    private static OrderObservable orderObservable = null;

    /**
     * 单例，确保没有线程安全问题
     */
    private OrderObservable() {
    }

    /**
     * @return 提供一个返回对象的方法
     */
    public synchronized static OrderObservable getInstance() {
        if (orderObservable == null) {
            orderObservable = new OrderObservable();
        }
        return orderObservable;
    }

    public void changeUI(Object obj) {
        //让change值由默认的false变成true
        this.setChanged();
        //循环遍历Observable集合，然后获取集合中的每一个对象，调用update方法
        notifyObservers(obj);
    }
}
