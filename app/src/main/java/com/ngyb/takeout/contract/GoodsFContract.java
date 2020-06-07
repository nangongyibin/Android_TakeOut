package com.ngyb.takeout.contract;

import com.ngyb.mvpbase.BaseView;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/6 10:30
 */
public interface GoodsFContract {
    interface Model {
    }

    interface View extends BaseView {
        void refreshGoodsAdapterUI(int id);
    }

    interface Presenter {
        void getGoodsData(int id);
    }
}
