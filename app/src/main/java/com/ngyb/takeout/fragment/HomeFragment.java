package com.ngyb.takeout.fragment;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ngyb.mvpbase.BaseMvpFragment;
import com.ngyb.takeout.R;
import com.ngyb.takeout.adapter.HomeAdapter;
import com.ngyb.takeout.contract.HomeFContract;
import com.ngyb.takeout.presenter.HomeFPresenter;
import com.uber.autodispose.AutoDisposeConverter;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/5/17 16:50
 */
public class HomeFragment extends BaseMvpFragment<HomeFPresenter> implements HomeFContract.View {
    private static final String TAG = "HomeFragment";
    private RecyclerView rvHome;
    private TextView homeTvAddress;
    private LinearLayout llTitleSearch;
    private LinearLayout llTitleContainer;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    int sumY = 0;
    private HomeAdapter homeAdapter;
    private HomeFPresenter homeFPresenter;

    @Override
    protected void init(View view) {
        initView(view);
    }

    @Override
    protected void dealOnCreate(Bundle savedInstanceState) {
        sumY = 0;
        super.dealOnCreate(savedInstanceState);
    }

    private void initView(View view) {
        rvHome = view.findViewById(R.id.rv_home);
        homeTvAddress = view.findViewById(R.id.home_tv_address);
        llTitleSearch = view.findViewById(R.id.ll_title_search);
        llTitleContainer = view.findViewById(R.id.ll_title_container);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onActivityCreated: ");
        initAdapter();
        initClass();
        initListener();
        super.onActivityCreated(savedInstanceState);
    }

    private void initListener() {
        rvHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                //滚动过程中发生变化
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                //滚动中，需要将每一次滚动的额距离进行累加，累加结果存储在sumY中
                sumY+=dy;
                //修改此空间中的背景颜色
                int bgColor = 0X553190E8;
                if (sumY ==0){
                    //没有移动时，色值就是初始色值
                    bgColor = 0X553190E8;
                }else if (sumY >=300){
                    //如果移动达到300个像素，则色值和状态栏一样的色值
                    bgColor = 0XFF3190E8;
                }else{
                    //0-300随着移动距离的增加，颜色进行变化
                    bgColor = (int) argbEvaluator.evaluate(sumY/300.0f,0X553190E8,0XFF3190E8);
                }
                //将管理好的色值设置给顶部的title作为背景色
                llTitleContainer.setBackgroundColor(bgColor);
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void initClass() {
        homeFPresenter = new HomeFPresenter(homeAdapter);
        homeFPresenter.getHomeData();
    }

    private void initAdapter() {
        homeAdapter = new HomeAdapter(mContext);
        rvHome.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rvHome.setAdapter(homeAdapter);
    }
}
