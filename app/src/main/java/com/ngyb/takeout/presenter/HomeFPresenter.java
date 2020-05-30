package com.ngyb.takeout.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.ngyb.takeout.adapter.HomeAdapter;
import com.ngyb.takeout.bean.net.HomeInfoBean;
import com.ngyb.takeout.bean.net.ResponseInfoBean;
import com.ngyb.takeout.contract.HomeFContract;
import com.ngyb.utils.LogUtils;

import retrofit2.Call;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/5/17 16:52
 */
public class HomeFPresenter extends BaseLocalPresenter<HomeFContract.View> implements HomeFContract.Presenter {
    private HomeAdapter homeAdapter;
    private static final String TAG = "HomeFPresenter";

    public HomeFPresenter(HomeAdapter homeAdapter) {
        this.homeAdapter = homeAdapter;
    }

    @Override
    public void getHomeData() {
        Call<ResponseInfoBean> homeInfo = responseInfoApi.getHomeInfo();
        homeInfo.enqueue(new CallBackAdapter());
    }

    @Override
    protected void parseJson(String data) {
        LogUtils.doLog(TAG, data);
        Gson gson = new Gson();
        HomeInfoBean homeInfoBean = gson.fromJson(data, HomeInfoBean.class);
        //数据需要放置在数据适配器中列表展示
        homeAdapter.setData(homeInfoBean);
    }

    @Override
    protected void showErrorMessage(String message) {

    }
}
