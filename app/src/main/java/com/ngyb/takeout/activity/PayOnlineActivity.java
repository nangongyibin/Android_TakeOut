package com.ngyb.takeout.activity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ngyb.mvpbase.BaseMvpActivity;
import com.ngyb.takeout.R;
import com.ngyb.takeout.contract.PayOnlineContract;
import com.ngyb.takeout.presenter.PayOnlinePresenter;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/13 20:17
 */
public class PayOnlineActivity extends BaseMvpActivity<PayOnlinePresenter> implements PayOnlineContract.View, View.OnClickListener {

    private ImageButton ibBack;
    private TextView tvResidualTime;
    private TextView tvOrderName;
    private TextView tv;
    private TextView tvOrderDetail;
    private ImageView ivTriangle;
    private RelativeLayout llOrderToggle;
    private TextView tvReceiptConnectInfo;
    private TextView tvReceiptAddressInfo;
    private LinearLayout llGoods;
    private LinearLayout llOrderDetail;
    private TextView tvPayMoney;
    private ImageView ivPayAlipay;
    private CheckBox cbPayAlipay;
    private TextView tvSelectorOtherPayment;
    private LinearLayout llHintInfo;
    private ImageView ivPayWechat;
    private CheckBox cbPayWechat;
    private ImageView ivPayQq;
    private CheckBox cbPayQq;
    private ImageView ivPayFenqile;
    private CheckBox cbPayFenqile;
    private LinearLayout llOtherPayment;
    private Button btConfirmPay;
    private PayOnlinePresenter payOnlinePresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_online;
    }

    @Override
    public void init() {
        initView();
        initClass();
        initListener();
    }

    private void initClass() {
        payOnlinePresenter = new PayOnlinePresenter(this);
        payOnlinePresenter.attachView(this);
    }

    private void initListener() {
        btConfirmPay.setOnClickListener(this);
    }

    private void initView() {
        ibBack = findViewById(R.id.ib_back);
        tvResidualTime = findViewById(R.id.tv_residualTime);
        tvOrderName = findViewById(R.id.tv_order_name);
        tv = findViewById(R.id.tv);
        tvOrderDetail = findViewById(R.id.tv_order_detail);
        ivTriangle = findViewById(R.id.iv_triangle);
        llOrderToggle = findViewById(R.id.ll_order_toggle);
        tvReceiptConnectInfo = findViewById(R.id.tv_receipt_connect_info);
        tvReceiptAddressInfo = findViewById(R.id.tv_Receipt_address_info);
        llGoods = findViewById(R.id.ll_goods);
        llOrderDetail = findViewById(R.id.ll_order_detail);
        tvPayMoney = findViewById(R.id.tv_pay_money);
        ivPayAlipay = findViewById(R.id.iv_pay_alipay);
        cbPayAlipay = findViewById(R.id.cb_pay_alipay);
        tvSelectorOtherPayment = findViewById(R.id.tv_selector_other_payment);
        llHintInfo = findViewById(R.id.ll_hint_info);
        ivPayWechat = findViewById(R.id.iv_pay_wechat);
        cbPayWechat = findViewById(R.id.cb_pay_wechat);
        ivPayQq = findViewById(R.id.iv_pay_qq);
        cbPayQq = findViewById(R.id.cb_pay_qq);
        ivPayFenqile = findViewById(R.id.iv_pay_fenqile);
        cbPayFenqile = findViewById(R.id.cb_pay_fenqile);
        llOtherPayment = findViewById(R.id.ll_other_payment);
        btConfirmPay = findViewById(R.id.bt_confirm_pay);
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
            case R.id.bt_confirm_pay:
                //调用支付宝进行支付
                payOnlinePresenter.pay();
                break;
        }
    }
}
