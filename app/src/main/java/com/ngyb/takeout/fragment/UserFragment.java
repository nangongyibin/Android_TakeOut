package com.ngyb.takeout.fragment;

import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.ngyb.mvpbase.BaseMvpFragment;
import com.ngyb.takeout.R;
import com.ngyb.takeout.activity.LoginActivity;
import com.ngyb.takeout.app.MyApplication;
import com.ngyb.takeout.contract.UserFContract;
import com.ngyb.takeout.dao.bean.UserInfoBean;
import com.ngyb.takeout.dao.db.DBHelper;
import com.ngyb.takeout.presenter.UserFPresenter;
import com.uber.autodispose.AutoDisposeConverter;

import java.sql.SQLException;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/17 21:07
 */
public class UserFragment extends BaseMvpFragment<UserFPresenter> implements UserFContract.View, View.OnClickListener {

    private ImageView ivUserSetting;
    private ImageView ivUserNotice;
    private ImageView login;
    private TextView username;
    private TextView phone;
    private LinearLayout llUserInfo;
    private ImageView ivAddress;

    @Override
    protected void init(View view) {
        initView(view);
        initListener();
    }

    private void initListener() {
        login.setOnClickListener(this);
    }

    private void initView(View view) {
        ivUserSetting = view.findViewById(R.id.iv_user_setting);
        ivUserNotice = view.findViewById(R.id.iv_user_notice);
        login = view.findViewById(R.id.login);
        username = view.findViewById(R.id.username);
        phone = view.findViewById(R.id.phone);
        llUserInfo = view.findViewById(R.id.ll_userinfo);
        ivAddress = view.findViewById(R.id.iv_address);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user;
    }

    @Override
    protected void dealOnResume() {
        //如何判断现在有没有登录用户
        if (MyApplication.userId != -1) {
            //有用户处于登录状态，获取登录用户的信息，展示
            login.setVisibility(View.GONE);
            llUserInfo.setVisibility(View.VISIBLE);
            try {
                DBHelper dbHelper = new DBHelper(context);
                Dao<UserInfoBean, Integer> dao = dbHelper.getDao(UserInfoBean.class);
                UserInfoBean userInfoBean = dao.queryForId(MyApplication.userId);
                if (userInfoBean != null) {
                    username.setText(userInfoBean.getName());
                    phone.setText(userInfoBean.getPhone());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            //用户处于非登录状态
            login.setVisibility(View.VISIBLE);
            llUserInfo.setVisibility(View.GONE);
        }
        super.dealOnResume();
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
