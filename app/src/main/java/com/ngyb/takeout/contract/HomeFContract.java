package com.ngyb.takeout.contract;

import com.ngyb.mvpbase.BaseView;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/5/17 16:52
 */
public interface HomeFContract {
    interface Model {
    }

    interface View extends BaseView {
    }

    interface Presenter {
        void getHomeData();
    }
}
