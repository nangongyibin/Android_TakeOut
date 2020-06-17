package com.ngyb.takeout.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ngyb.mvpbase.BaseMvpActivity;
import com.ngyb.takeout.R;
import com.ngyb.takeout.adapter.AddressListAdapter;
import com.ngyb.takeout.app.MyApplication;
import com.ngyb.takeout.contract.AddressListContract;
import com.ngyb.takeout.dao.ReceiptAddressDao;
import com.ngyb.takeout.dao.bean.ReceiptAddressBean;
import com.ngyb.takeout.presenter.AddressListPresenter;

import java.util.List;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/7 16:44
 */
public class AddressListActivity extends BaseMvpActivity<AddressListPresenter> implements AddressListContract.View, View.OnClickListener {
    private ImageButton ibBack;
    private TextView tvTitle;
    private RecyclerView rvReceiptAddress;
    private TextView tvAddAddress;
    private ReceiptAddressDao receiptAddressDao;
    private static final String TAG = "AddressListActivity";

    @Override
    public int getLayoutId() {
        return R.layout.activity_address_list;
    }

    @Override
    public void init() {
        initView();
        initClass();
        initData();
        initListener();
    }

    private void initData() {
        //查询数据库中登录用户的所有地址列表展示
        List<ReceiptAddressBean> receiptAddressBeanList = receiptAddressDao.queryUserAddress(MyApplication.userId);
        Log.e(TAG, "initData: " + (receiptAddressBeanList == null) + "====" + (receiptAddressBeanList.size()));
        if (receiptAddressBeanList != null && receiptAddressBeanList.size() > 0) {
            //receiptAddressBeanList是登录用户的地址
            AddressListAdapter addressListAdapter = new AddressListAdapter(this, receiptAddressBeanList);
            rvReceiptAddress.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            rvReceiptAddress.setAdapter(addressListAdapter);
        }
    }

    private void initClass() {
        receiptAddressDao = new ReceiptAddressDao(this);
    }

    private void initListener() {
        tvAddAddress.setOnClickListener(this);
    }

    private void initView() {
        ibBack = findViewById(R.id.ib_back);
        tvTitle = findViewById(R.id.tv_title);
        rvReceiptAddress = findViewById(R.id.rv_receipt_address);
        tvAddAddress = findViewById(R.id.tv_add_address);
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
        switch (v.getId()) {
            case R.id.tv_add_address:
                Intent intent = new Intent(this, AddAddressActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void dealOnResume() {
        initClass();
        initData();
        super.dealOnResume();
    }
}
