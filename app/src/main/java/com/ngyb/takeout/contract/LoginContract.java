package com.ngyb.takeout.contract;

import com.ngyb.mvpbase.BaseView;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/17 22:07
 */
public interface LoginContract {
    interface Model {
    }

    interface View extends BaseView {
    }

    interface Presenter {
        void getLoginData(String phone, String psd, int i);
    }
}
