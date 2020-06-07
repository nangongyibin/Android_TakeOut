package com.ngyb.takeout.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.ngyb.mvpbase.BaseMvpActivity;
import com.ngyb.takeout.R;
import com.ngyb.takeout.adapter.MyFragmentPagerAdapter;
import com.ngyb.takeout.adapter.ShopCartAdapter;
import com.ngyb.takeout.bean.net.GoodsInfoBean;
import com.ngyb.takeout.bean.net.SellerBean;
import com.ngyb.takeout.constant.Constant;
import com.ngyb.takeout.contract.BusinessContract;
import com.ngyb.takeout.presenter.BusinessPresenter;
import com.ngyb.takeout.utils.CountPriceFormater;
import com.ngyb.utils.LogUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/4 22:09
 */
public class BusinessActivity extends BaseMvpActivity<BusinessPresenter> implements BusinessContract.View, View.OnClickListener {
    private static final String TAG = "BusinessActivity";
    private ImageButton ibBack;
    private TextView tvTitle;
    private ImageButton ibMenu;
    private TabLayout tab;
    private ViewPager vp;
    private BottomSheetLayout bottomSheetLayout;
    private ImageView imgCart;
    private TextView tvSelectNum;
    private TextView tvCountPrice;
    private TextView tvDeliveryFee;
    private TextView tvSendPrice;
    private TextView tvSumbit;
    private LinearLayout bottom;
    private FrameLayout flContainer;
    private SellerBean seller;
    public BusinessPresenter businessPresenter;
    public MyFragmentPagerAdapter myFragmentPagerAdapter;
    private float floatDeliveryFee;
    private float floatSendPrivice;
    private View sheetView;
    private ShopCartAdapter shopCartAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bussiness;
    }

    @Override
    public void init() {
        initView();
        initClass();
        initIntent();
        initTab();
        initData();
        initListener();
    }

    private void initListener() {
        tvSumbit.setOnClickListener(this);
        bottom.setOnClickListener(this);
    }

    private void initData() {
        floatDeliveryFee = Float.parseFloat(seller.getDeliveryFee());
        String strDeliverFee = CountPriceFormater.format(floatDeliveryFee);
        //配送费
        tvDeliveryFee.setText(Constant.PSF + strDeliverFee);
        floatSendPrivice = Float.parseFloat(seller.getSendPrice());
        String strSendPrice = CountPriceFormater.format(floatSendPrivice);
        //起送价
        tvSendPrice.setText(Constant.QS + strSendPrice);
    }

    private void initTab() {
        //给veiwpager设置数据适配器，FragmentPagerAdapter
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), seller);
        vp.setAdapter(myFragmentPagerAdapter);
        //tablayout和设置完数据适配器的viewpager绑定
        tab.setupWithViewPager(vp);
    }

    private void initClass() {
        businessPresenter = new BusinessPresenter(this);
        businessPresenter.attachView(this);
    }

    private void initIntent() {
        //接收前一个页面传递过来的数据
        seller = (SellerBean) getIntent().getSerializableExtra(Constant.KEY);
        LogUtils.doLog(TAG, seller.getId() + "");
    }

    private void initView() {
        ibBack = findViewById(R.id.ib_back);
        tvTitle = findViewById(R.id.tv_title);
        ibMenu = findViewById(R.id.ib_menu);
        tab = findViewById(R.id.tabs);
        vp = findViewById(R.id.vp);
        bottomSheetLayout = findViewById(R.id.bottomSheetLayout);
        imgCart = findViewById(R.id.imgCart);
        tvSelectNum = findViewById(R.id.tvSelectNum);
        tvCountPrice = findViewById(R.id.tvCountPrice);
        tvDeliveryFee = findViewById(R.id.tvDeliveryFee);
        tvSendPrice = findViewById(R.id.TvSendPrice);
        tvSumbit = findViewById(R.id.tvSumbit);
        bottom = findViewById(R.id.bottom);
        flContainer = findViewById(R.id.fl_Container);
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
            case R.id.tvSumbit:
//                Intent intent = new Intent(this, ConfirmOrderActivity.class);
//                ArrayList<GoodsInfoBean> shopCartList = businessPresenter.getShopCartList();
//                //购买商品的集合
//                intent.putExtra("shopcartList", shopCartList);
//                //运费数量
//                intent.putExtra("deliverFee", floatDeliveryFee);
//                startActivity(intent);
                break;
            case R.id.bottom:
                if (sheetView == null) {
                    //将布局文件转换成sheetView供使用
                    sheetView = onCreateView();
                }
                //判断弹出还是隐藏对话框
                if (bottomSheetLayout.isSheetShowing()) {
                    bottomSheetLayout.dismissSheet();
                } else {
                    //重新获取加入购物车的数据集合
                    List<GoodsInfoBean> shopCartListNew = businessPresenter.getShopCartList();
                    if (shopCartListNew != null && shopCartListNew.size() > 0) {
                        bottomSheetLayout.showWithSheetView(sheetView);
                        //将刚刚获取的数据，更新至数据适配器中
                        shopCartAdapter.setData(shopCartListNew);
                    }
                }
                break;
        }
    }

    /**
     * @param imageView 需要添加的imageview
     * @param width     添加的imageview宽度
     * @param height    添加的imageview高度
     */
    public void addFlyImageView(ImageView imageView, int width, int height) {
        flContainer.addView(imageView, width, height);
    }

    /**
     * @return 获取购物车所在屏幕的位置
     */
    public int[] getShopCartLocation() {
        //获取购物车的控件
        int[] shopCartLocation = new int[2];
        imgCart.getLocationInWindow(shopCartLocation);
        return shopCartLocation;
    }

    /**
     * @param totalCount  购物车需要显示的总数量
     * @param totalPrices 购物车需要显示的总金额
     */
    public void refreshShopCartData(int totalCount, float totalPrices) {
        //如果totalCount的值大于0，则需要显示气泡和购物车中总金额
        if (totalCount > 0) {
            tvSelectNum.setVisibility(View.VISIBLE);
            tvSelectNum.setText(totalCount + "");
            String strTotalPrice = CountPriceFormater.format(totalPrices);
            tvCountPrice.setText(strTotalPrice);
        } else {
            tvSelectNum.setVisibility(View.GONE);
            tvCountPrice.setText(CountPriceFormater.format(0.0f));
        }
        //判断去结算按钮是否出现，起送金额是否出现
        if (totalPrices > floatSendPrivice) {
            //购买金额>起送价，显示区结算按钮
            tvSumbit.setVisibility(View.VISIBLE);
            tvSendPrice.setVisibility(View.GONE);
        } else {
            //购买金额<起送价，显示起送价，提示用户还没买够
            tvSumbit.setVisibility(View.GONE);
            tvSendPrice.setVisibility(View.VISIBLE);
        }
    }

    private View onCreateView() {
        View view = View.inflate(this, R.layout.cart_list, null);
        TextView tvClear = view.findViewById(R.id.tvClear);
        RecyclerView rvCart = view.findViewById(R.id.rvCart);
        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        shopCartAdapter = new ShopCartAdapter(this);
        rvCart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvCart.setAdapter(shopCartAdapter);
        List<GoodsInfoBean> shopCartList = businessPresenter.getShopCartList();
        shopCartAdapter.setData(shopCartList);
        return view;
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否清空？");
        builder.setPositiveButton("清空", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                businessPresenter.clearAll();
            }
        });
        builder.setNegativeButton("稍后", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 隐藏BusinessActivity对话框
     */
    public void hiddenDialog() {
        if (bottomSheetLayout.isSheetShowing()) {
            bottomSheetLayout.dismissSheet();
        }
    }
}
