package com.ngyb.takeout.fragment;

import android.view.View;
import android.widget.TextView;

import com.ngyb.mvpbase.BaseMvpFragment;
import com.ngyb.takeout.contract.SellerFContract;
import com.ngyb.takeout.presenter.SellerFPresenter;
import com.uber.autodispose.AutoDisposeConverter;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/7 18:07
 */
public class SellerFragment extends BaseMvpFragment<SellerFPresenter> implements SellerFContract.View {
    @Override
    protected void init(View view) {

    }

    @Override
    protected int getLayoutId() {
        return 0;
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

    @Override
    protected View getOnCreateView() {
        TextView textView = new TextView(context);
        //class类的字节码文件(类的名称),BaseFragment子类的名称在textview中展示
        textView.setText(this.getClass().getSimpleName());
        return textView;
    }
}
