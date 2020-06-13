package com.ngyb.takeout.activity;

import com.ngyb.mvpbase.BaseMvpActivity;
import com.ngyb.takeout.contract.PayOnlineContract;
import com.ngyb.takeout.presenter.PayOnlinePresenter;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/13 20:17
 */
public class PayOnlineActivity extends BaseMvpActivity<PayOnlinePresenter> implements PayOnlineContract.View {
    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void init() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onSuccess() {

    }
}
