package com.ngyb.takeout.contract;

import com.ngyb.mvpbase.BaseView;
import com.ngyb.takeout.adapter.OrderListAdapter;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/14 21:12
 */
public interface OrderFContract {
    interface Model {
    }

    interface View extends BaseView {
    }

    interface Presenter {
        void getOrderData(OrderListAdapter orderListAdapter, int userId);
    }
}
