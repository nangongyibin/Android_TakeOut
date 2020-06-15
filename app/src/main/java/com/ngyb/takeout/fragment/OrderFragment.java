package com.ngyb.takeout.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ngyb.mvpbase.BaseMvpFragment;
import com.ngyb.takeout.R;
import com.ngyb.takeout.adapter.OrderListAdapter;
import com.ngyb.takeout.app.MyApplication;
import com.ngyb.takeout.contract.OrderFContract;
import com.ngyb.takeout.presenter.OrderFPresenter;
import com.uber.autodispose.AutoDisposeConverter;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/14 21:04
 */
public class OrderFragment extends BaseMvpFragment<OrderFPresenter> implements OrderFContract.View {

    private RecyclerView rvOrderList;
    private SwipeRefreshLayout srlOrder;
    private OrderFPresenter orderFPresenter;
    private OrderListAdapter orderListAdapter;

    @Override
    protected void init(View view) {
        initView(view);
        initClass();
    }

    private void initClass() {
        orderFPresenter = new OrderFPresenter();
        orderFPresenter.attachView(this);
    }

    private void initView(View view) {
        rvOrderList = view.findViewById(R.id.rv_order_list);
        srlOrder = view.findViewById(R.id.srl_order);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order;
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
    protected void dealOnActivityCreated(Bundle savedInstanceState) {
        initAdapter();
        getData();
        super.dealOnActivityCreated(savedInstanceState);
    }

    private void getData() {
        orderFPresenter.getOrderData(orderListAdapter, MyApplication.userId);
    }

    private void initAdapter() {
        orderListAdapter = new OrderListAdapter();
        rvOrderList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvOrderList.setAdapter(orderListAdapter);
    }
}
