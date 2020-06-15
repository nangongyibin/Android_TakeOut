package com.ngyb.takeout.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ngyb.mvpbase.BasePresenter;
import com.ngyb.takeout.adapter.OrderListAdapter;
import com.ngyb.takeout.contract.OrderFContract;
import com.ngyb.takeout.net.bean.OrderBean;
import com.ngyb.takeout.net.bean.ResponseInfoBean;
import com.ngyb.utils.LogUtils;

import java.util.List;

import retrofit2.Call;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/14 21:12
 */
public class OrderFPresenter extends BaseLocalPresenter<OrderFContract.View> implements OrderFContract.Presenter {
    private OrderListAdapter orderListAdapter;
    private static final String TAG = "OrderFPresenter";

    @Override
    public void getOrderData(OrderListAdapter orderListAdapter, int userId) {
        this.orderListAdapter = orderListAdapter;
        Call<ResponseInfoBean> orderInfo = responseInfoApi.getOrderInfo(userId);
        orderInfo.enqueue(new CallBackAdapter());
    }

    @Override
    protected void parseJson(String data) {
        LogUtils.doLog(TAG, data);
        Gson gson = new Gson();
        List<OrderBean> list = gson.fromJson(data, new TypeToken<List<OrderBean>>() {
        }.getType());
        orderListAdapter.setData(list);
    }

    @Override
    protected void showErrorMessage(String message) {

    }
}
