package com.ngyb.takeout.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ngyb.mvpbase.BaseFragment;
import com.ngyb.takeout.bean.net.SellerBean;
import com.ngyb.takeout.constant.Constant;
import com.ngyb.takeout.fragment.GoodsFragment;
import com.ngyb.takeout.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/4 22:50
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private SellerBean seller;
    private String[] title;
    private List<Fragment> fragmentList = new ArrayList<>();

    public MyFragmentPagerAdapter(FragmentManager fm, SellerBean seller) {
        super(fm);
        this.seller = seller;
        title = new String[]{"商品", "评价", "商家"};
    }

    @Override
    public Fragment getItem(int i) {
        //根据索引返回不同的fragment对象，现在选项卡有3个，fragment界面就有3个
        BaseFragment baseFragment = null;
        switch (i) {
            case 0:
                baseFragment = new GoodsFragment();
                break;
            case 1:
                //TODO
                baseFragment = new HomeFragment();
//                baseFragment = new SuggestFragment();
                break;
            case 2:
                //TODO
                baseFragment = new HomeFragment();
//                baseFragment = new SellerFragment();
                break;
        }
        //seller需要存储在bundle进行传递
        Bundle bundle = new Bundle();
        //将seller存储在bundle中
        bundle.putSerializable(Constant.KEY, seller);
        //将邮包传递给每一个fragment
        baseFragment.setArguments(bundle);
        if (!fragmentList.contains(baseFragment)) {
            fragmentList.add(baseFragment);
        }
        return baseFragment;
    }

    /**
     * @return 获取GoodsFragment方法
     */
    public GoodsFragment getGoodsFragment() {
        if (fragmentList != null && fragmentList.size() > 0) {
            return (GoodsFragment) fragmentList.get(0);
        }
        return null;
    }

    /**
     * @return 告知viewpager一共几个fragment界面
     */
    @Override
    public int getCount() {
        return title.length;
    }

    /**
     * @param position 给viewpager绑定tab设置标题文本内容方法
     * @return
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}

