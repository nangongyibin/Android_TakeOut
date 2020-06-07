package com.ngyb.takeout.contract;

import com.ngyb.mvpbase.BaseView;
import com.ngyb.takeout.bean.net.GoodsInfoBean;

import java.util.ArrayList;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/4 22:10
 */
public interface BusinessContract {
    interface Model {
    }

    interface View extends BaseView {
    }

    interface Presenter {
        void getShopCartCountAndPrice();

        ArrayList<GoodsInfoBean> getShopCartList();

        void clearAll();
    }
}
