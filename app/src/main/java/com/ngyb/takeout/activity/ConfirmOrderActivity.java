package com.ngyb.takeout.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ngyb.mvpbase.BaseMvpActivity;
import com.ngyb.takeout.R;
import com.ngyb.takeout.app.MyApplication;
import com.ngyb.takeout.constant.Constant;
import com.ngyb.takeout.contract.ConfirmOrderContract;
import com.ngyb.takeout.dao.ReceiptAddressDao;
import com.ngyb.takeout.dao.bean.ReceiptAddressBean;
import com.ngyb.takeout.net.bean.GoodsInfoBean;
import com.ngyb.takeout.presenter.ConfirmOrderPresenter;
import com.ngyb.takeout.utils.CountPriceFormater;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/7 08:29
 */
public class ConfirmOrderActivity extends BaseMvpActivity<ConfirmOrderPresenter> implements ConfirmOrderContract.View, View.OnClickListener {

    private ImageButton ibBack;
    private TextView tvLogin;
    private ImageView ivLocation;
    private TextView tvHintSelectReceiptAddress;
    private TextView tvName;
    private TextView tvSex;
    private TextView tvPhone;
    private TextView tvLabel;
    private LinearLayout llReceiptAddress;
    private ImageView ivArrow;
    private RelativeLayout rlLocation;
    private ImageView ivIcon;
    private TextView tvSellerName;
    private LinearLayout llSelectGoods;
    private TextView tvDeliveryFee;
    private TextView tvCountPrice;
    private TextView tvSubmit;
    private String[] addressLabels;
    private int[] bgLabels;
    private ReceiptAddressDao receiptAddressDao;
    private ArrayList<GoodsInfoBean> shopCartList;
    private float deliveryFee;
    private TextView tvAddress;

    @Override
    public int getLayoutId() {
        return R.layout.activity_confirm_order;
    }

    @Override
    public void init() {
        initView();
        initClass();
        initIntent();
        initData();
        initListener();
    }

    private void initListener() {
        rlLocation.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    private void initIntent() {
        //获取前一个页面在集合中存储的商品
        shopCartList = (ArrayList<GoodsInfoBean>) getIntent().getSerializableExtra(Constant.SHOPCARTLIST);
        //运费
        deliveryFee = getIntent().getFloatExtra(Constant.DELIVERFEE, 0.0f);
    }

    private void initClass() {
        receiptAddressDao = new ReceiptAddressDao(this);
    }

    private void initData() {
        addressLabels = new String[]{"家", "公司", "学校"};
        bgLabels = new int[]{Color.parseColor("#fc7251"), Color.parseColor("#468ade"), Color.parseColor("#02c14b")};

        tvDeliveryFee.setText(CountPriceFormater.format(deliveryFee));
        //llSelectGoods容器中添加购买商品的列表
        //清空线性布局中的所有控件
        llSelectGoods.removeAllViews();
        float totalPrice = 0.0f;

        for (int i = 0; i < shopCartList.size(); i++) {
            GoodsInfoBean goodsInfoBean = shopCartList.get(i);
            //购买商品数量
            int count = goodsInfoBean.getCount();
            //购买商品金额
            float price = count * goodsInfoBean.getNewPrice();
            totalPrice += price;
            //单个条目布局文件转换成view对象
            View view = View.inflate(this, R.layout.item_confirm_order_goods, null);
            TextView tvName = view.findViewById(R.id.tv_name);
            TextView tvCount = view.findViewById(R.id.tv_count);
            TextView tvPrice = view.findViewById(R.id.tv_price);
            tvName.setText(goodsInfoBean.getName());
            tvCount.setText(CountPriceFormater.format(price));
            tvPrice.setText(count + "");
            llSelectGoods.addView(view);
        }
        totalPrice += deliveryFee;
        tvCountPrice.setText("待支付:" + CountPriceFormater.format(totalPrice));
    }

    private void initView() {
        ibBack = findViewById(R.id.ib_back);
        tvLogin = findViewById(R.id.tv_login);
        ivLocation = findViewById(R.id.iv_location);
        tvHintSelectReceiptAddress = findViewById(R.id.tv_hint_select_receipt_address);
        tvName = findViewById(R.id.tv_name);
        tvSex = findViewById(R.id.tv_sex);
        tvPhone = findViewById(R.id.tv_phone);
        tvLabel = findViewById(R.id.tv_label);
        tvAddress = findViewById(R.id.tv_address);
        llReceiptAddress = findViewById(R.id.ll_receipt_address);
        ivArrow = findViewById(R.id.iv_arrow);
        rlLocation = findViewById(R.id.rl_location);
        ivIcon = findViewById(R.id.iv_icon);
        tvSellerName = findViewById(R.id.tv_seller_name);
        llSelectGoods = findViewById(R.id.ll_select_goods);
        tvDeliveryFee = findViewById(R.id.tv_deliveryFee);
        tvCountPrice = findViewById(R.id.tv_CountPrice);
        tvSubmit = findViewById(R.id.tvSubmit);
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
            case R.id.rl_location:
                Intent intent = new Intent(this, AddressListActivity.class);
                startActivity(intent);
                break;
            case R.id.tvSubmit:
                Intent intent1 = new Intent(this, PayOnlineActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    protected void dealOnResume() {
        //查询登录用户的选中送货地址
        ReceiptAddressBean receiptAddressBean = receiptAddressDao.queryUserSelectAddress(MyApplication.userId);
        if (receiptAddressBean != null) {
            //设置名称
            tvName.setText(receiptAddressBean.getName());
            tvSex.setText(receiptAddressBean.getSex());
            //有2个号码可以显示
            if (!TextUtils.isEmpty(receiptAddressBean.getPhone()) && !TextUtils.isEmpty(receiptAddressBean.getPhoneOther())) {
                tvPhone.setText(receiptAddressBean.getPhone() + "," + receiptAddressBean.getPhoneOther());
            }
            if (!TextUtils.isEmpty(receiptAddressBean.getPhone()) && TextUtils.isEmpty(receiptAddressBean.getPhoneOther())) {
                tvPhone.setText(receiptAddressBean.getPhone());
            }
            //显示地址是公司还是学校,还是家
            tvLabel.setText(receiptAddressBean.getLabel());
            //获取label后需要获取数组的索引位置,通过索引位置获取tvLabel控件的背景色
            int index = getIndex(receiptAddressBean.getLabel());
            tvLabel.setBackgroundColor(bgLabels[index]);
            //显示初略地址和详细地址的合并结果
            tvAddress.setText(receiptAddressBean.getDetailAddress() + receiptAddressBean.getDetailAddress());
        }
        super.dealOnResume();
    }

    private int getIndex(String label) {
        int index = -1;
        for (int i = 0; i < addressLabels.length; i++) {
            if (addressLabels[i].equals(label)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
