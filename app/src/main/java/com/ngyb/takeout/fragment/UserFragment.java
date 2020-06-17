package com.ngyb.takeout.fragment;

import android.view.View;

import com.ngyb.mvpbase.BaseMvpFragment;
import com.ngyb.takeout.R;
import com.ngyb.takeout.contract.UserFContract;
import com.ngyb.takeout.presenter.UserFPresenter;
import com.uber.autodispose.AutoDisposeConverter;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/17 21:07
 */
public class UserFragment extends BaseMvpFragment<UserFPresenter> implements UserFContract.View {
    @Override
    protected void init(View view) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user;
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

    @Override
    public <T> AutoDisposeConverter<T> bindAutoDispose() {
        return null;
    }
}
