package com.ngyb.takeout.activity;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ngyb.mvpbase.BaseMvpActivity;
import com.ngyb.takeout.R;
import com.ngyb.takeout.contract.MainContract;
import com.ngyb.takeout.fragment.HomeFragment;
import com.ngyb.takeout.fragment.MoreFragment;
import com.ngyb.takeout.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseMvpActivity<MainPresenter> implements MainContract.View, View.OnClickListener {

    private FrameLayout mainFragmentContainer;
    private LinearLayout mainBottomSwitcherContainer;
    private List<Fragment> fragmentList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        initView();
        initFragment();
        initListener();
        initDefault();
    }

    /**
     * 默认选中第0个条目
     */
    private void initDefault() {
        View viewFirst = mainBottomSwitcherContainer.getChildAt(0);
        onClick(viewFirst);
    }

    private void initListener() {
        int childCount = mainBottomSwitcherContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAtView = mainBottomSwitcherContainer.getChildAt(i);
            childAtView.setOnClickListener(this);
        }
    }

    private void initFragment() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new HomeFragment());
        fragmentList.add(new HomeFragment());
//        fragmentList.add(new OrderFragment());
//        fragmentList.add(new UserFragment());
        fragmentList.add(new MoreFragment());
    }

    private void initView() {
        mainFragmentContainer = findViewById(R.id.main_fragment_container);
        mainBottomSwitcherContainer = findViewById(R.id.main_bottom_switcher_container);
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
    public void onClick(View v) {
        //v底部现行布局的索引位置
        int indexOfChild = mainBottomSwitcherContainer.indexOfChild(v);
        //点击哪个帧布局，这个帧布局中的图片就需要变为蓝色
        changeUI(indexOfChild);
        //顶部的Fragment页面内容需要切换
        changeFragment(indexOfChild);
    }

    private void changeFragment(int indexOfChild) {
        //需要切换到哪个fragment显示
        Fragment fragment = fragmentList.get(indexOfChild);
        //替换帧布局内部内容
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, fragment).commit();
    }

    private void changeUI(int indexOfChild) {
        //蓝色-->不可用
        int childCount = mainBottomSwitcherContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childFrameLayout = mainBottomSwitcherContainer.getChildAt(i);
            //循环遍历到了选中的索引位置
            if (i == indexOfChild) {
                //点钟的帧布局，内部的图片和文字都需要变蓝色
                setEnable(childFrameLayout, false);
            } else {
                //未点中的帧布局，内部的图片和文字都需要变黑色
                setEnable(childFrameLayout, true);
            }
        }
    }

    /**
     * @param childFrameLayout 帧布局
     * @param b                是否可用
     *                         让帧布局内部的控件和自身都是（可用）不可用的状态
     */
    private void setEnable(View childFrameLayout, boolean b) {
        childFrameLayout.setEnabled(b);
        if (childFrameLayout instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) childFrameLayout).getChildCount(); i++) {
                View childAt = ((ViewGroup) childFrameLayout).getChildAt(i);
                childAt.setEnabled(b);
            }
        }
    }
}
