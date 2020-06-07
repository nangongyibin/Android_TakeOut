package com.ngyb.takeout.presenter;

import android.content.Context;

import com.ngyb.mvpbase.BasePresenter;
import com.ngyb.takeout.activity.BusinessActivity;
import com.ngyb.takeout.adapter.GoodsAdapter;
import com.ngyb.takeout.adapter.GoodsTypeAdapter;
import com.ngyb.takeout.net.bean.GoodsInfoBean;
import com.ngyb.takeout.net.bean.GoodsTypeInfoBean;
import com.ngyb.takeout.contract.BusinessContract;
import com.ngyb.takeout.fragment.GoodsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/4 22:10
 */
public class BusinessPresenter extends BasePresenter<BusinessContract.View> implements BusinessContract.Presenter {
    private Context context;

    public BusinessPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void getShopCartCountAndPrice() {
        int totalCount = 0;
        float totalPrice = 0.0f;
        //获取右侧的商品列表集合数据,判断每一个对象中的count的值是否大于0,如果大于0,则需要记录下来
        GoodsFragment goodsFragment = ((BusinessActivity) context).myFragmentPagerAdapter.getGoodsFragment();
        if (goodsFragment != null) {
            GoodsAdapter goodsAdapter = goodsFragment.goodsAdapter;
            //goodsAdapter数据取出来
            ArrayList<GoodsInfoBean> goodsInfoBeans = goodsAdapter.getData();
            for (int i = 0; i < goodsInfoBeans.size(); i++) {
                GoodsInfoBean goodsInfoBean = goodsInfoBeans.get(i);
                if (goodsInfoBean.getCount() > 0) {
                    //总数量累加
                    totalCount += goodsInfoBean.getCount();
                    //总金额累加
                    totalPrice += goodsInfoBean.getNewPrice() * goodsInfoBean.getCount();
                }
            }
            ((BusinessActivity) context).refreshShopCartData(totalCount, totalPrice);
        }
    }

    @Override
    public ArrayList<GoodsInfoBean> getShopCartList() {
        ArrayList<GoodsInfoBean> shopCartList = new ArrayList<>();
        GoodsFragment goodsFragment = ((BusinessActivity) context).myFragmentPagerAdapter.getGoodsFragment();
        if (goodsFragment != null) {
            GoodsAdapter goodsAdapter = goodsFragment.goodsAdapter;
            //goodsAdapter数据取出来
            ArrayList<GoodsInfoBean> goodsInfoBeans = goodsAdapter.getData();
            //判断goodsInfoBeans中每一个对象的count值是否大于0,如果大于0,则此对象需要添加在购物车集合中
            for (int i = 0; i < goodsInfoBeans.size(); i++) {
                GoodsInfoBean goodsInfoBean = goodsInfoBeans.get(i);
                if (goodsInfoBean.getCount() > 0) {
                    shopCartList.add(goodsInfoBean);
                }
            }
            return shopCartList;
        }
        return null;
    }

    @Override
    public void clearAll() {
        //清空商品列表
        clearGoodsAdapter();
        //清空商品分类
        clearGoodsTypeAdapter();
        //清空购物车
        clearShopCart();
        //让对话框隐藏
        ((BusinessActivity) context).hiddenDialog();
        //将购物车总数量和总金额都设置为0
        ((BusinessActivity) context).refreshShopCartData(0, 0.0f);
    }

    private void clearShopCart() {
        //因为现在的操作是清空购物车,购物车的集合就没有存在的必要
        List<GoodsInfoBean> shopCartList = getShopCartList();
        shopCartList.clear();
    }

    private void clearGoodsTypeAdapter() {
        GoodsFragment goodsFragment = ((BusinessActivity) context).myFragmentPagerAdapter.getGoodsFragment();
        if (goodsFragment != null) {
            GoodsTypeAdapter goodsTypeAdapter = goodsFragment.goodsTypeAdapter;
            List<GoodsTypeInfoBean> goodsTypeInfoBeans = goodsTypeAdapter.getData();
            for (int i = 0; i < goodsTypeInfoBeans.size(); i++) {
                GoodsTypeInfoBean goodsTypeInfoBean = goodsTypeInfoBeans.get(i);
                if (goodsTypeInfoBean.getCount() > 0) {
                    goodsTypeInfoBean.setCount(0);
                }
            }
            goodsTypeAdapter.notifyDataSetChanged();
        }
    }

    private void clearGoodsAdapter() {
        GoodsFragment goodsFragment = ((BusinessActivity) context).myFragmentPagerAdapter.getGoodsFragment();
        if (goodsFragment != null) {
            GoodsAdapter goodsAdapter = goodsFragment.goodsAdapter;
            //goodsAdapter数据获取出来
            ArrayList<GoodsInfoBean> goodsInfoBeans = goodsAdapter.getData();
            //判断goodsInfoBeans中每一个对象的count值是否大于0,如果大于0,则此对象需要添加到购物车集合中
            for (int i = 0; i < goodsInfoBeans.size(); i++) {
                GoodsInfoBean goodsInfoBean = goodsInfoBeans.get(i);
                if (goodsInfoBean.getCount() > 0) {
                    goodsInfoBean.setCount(0);
                }
            }
            goodsAdapter.notifyDataSetChanged();
        }
    }
}
