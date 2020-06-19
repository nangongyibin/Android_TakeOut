package com.ngyb.takeout.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.ngyb.mvpbase.BaseMvpActivity;
import com.ngyb.takeout.R;
import com.ngyb.takeout.constant.Constant;
import com.ngyb.takeout.contract.OrderDetailContract;
import com.ngyb.takeout.observable.OrderObservable;
import com.ngyb.takeout.presenter.OrderDetailPresenter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/16 20:12
 */
public class OrderDetailActivity extends BaseMvpActivity<OrderDetailPresenter> implements OrderDetailContract.View, Observer {
    private static final String TAG = "OrderDetailActivity";
    private ImageView ivOrderDetailBack;
    private TextView tvSellerName;
    private TextView tvOrderDetailTime;
    private MapView map;
    private LinearLayout llOrderDetailTypeContainer;
    private LinearLayout llOrderDetailTypePointContainer;
    private String orderId, type;
    private AMap aMap;
    private int index = -1;
    private LatLng latLngBuyter;
    private LatLng latLngSeller;
    private ArrayList<LatLng> riderPosList;
    private LatLng riderLatLng;
    private Marker riderMarker;
    private LatLng currentPos;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void init() {
        initView();
        initIntent();
        initMap();
        initObservable();
        getIndex(type);
        changeUI();
    }

    /**
     * 根据index的索引位置，更新文字和颜色
     */
    private void changeUI() {
        if (index != -1) {
            //将所有的文字和图片都设置为黑色和灰色
            for (int i = 0; i < 4; i++) {
                ((TextView) llOrderDetailTypeContainer.getChildAt(i)).setTextColor(Color.BLACK);
                ((ImageView) llOrderDetailTypePointContainer.getChildAt(i)).setImageResource(R.mipmap.order_time_node_normal);
            }
            ((TextView) llOrderDetailTypeContainer.getChildAt(index)).setTextColor(Color.BLUE);
            ((ImageView) llOrderDetailTypePointContainer.getChildAt(index)).setImageResource(R.mipmap.order_time_node_disabled);
        }
    }

    /**
     * @param type 根据前一个页面传递过来的type决定需要变蓝点和文字的索引位置
     */
    private void getIndex(String type) {
        switch (type) {
            case OrderObservable.ORDERTYPE_SUBMIT:
                index = 0;
                break;
            case OrderObservable.ORDERTYPE_RECEIVEORDER:
                index = 1;
                break;
            case OrderObservable.ORDERTYPE_DISTRIBUTION:
                index = 2;
                break;
            case OrderObservable.ORDERTYPE_SERVED:
                index = 3;
                break;
        }
    }

    /**
     * 在此界面中注册观察者过程
     * 注册广播接收者
     */
    private void initObservable() {
        OrderObservable.getInstance().addObserver(this);
    }

    private void initMap() {
        if (aMap == null) {
            //初始化地图控制器对象
            aMap = map.getMap();
        }
    }

    private void initIntent() {
        Intent intent = getIntent();
        orderId = intent.getStringExtra(Constant.ORDERID);
        type = intent.getStringExtra(Constant.TYPE);
    }

    @Override
    protected void dealOnCreate(Bundle savedInstanceState) {
        super.dealOnCreate(savedInstanceState);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        if (map == null) {
            map = findViewById(R.id.map);
        }
        map.onSaveInstanceState(savedInstanceState);
    }

    private void initView() {
        ivOrderDetailBack = findViewById(R.id.iv_order_detail_back);
        tvSellerName = findViewById(R.id.tv_seller_name);
        tvOrderDetailTime = findViewById(R.id.tv_order_detail_time);
        map = findViewById(R.id.map);
        llOrderDetailTypeContainer = findViewById(R.id.ll_order_detail_type_container);
        llOrderDetailTypePointContainer = findViewById(R.id.ll_order_detail_type_point_container);
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
    public void update(Observable o, Object arg) {
        HashMap<String, String> hashMap = (HashMap<String, String>) arg;
        String orderInfo = hashMap.get(Constant.OrderInfo);
        String type = hashMap.get(Constant.TYPE);
        Log.e(TAG, "update: " + type);
        //通过type获取新的选中蓝色条目的索引
        getIndex(type);
        changeUI();
        switch (type) {
            case OrderObservable.ORDERTYPE_RECEIVEORDER:
                //商家已接单，需要显示地图
                initAMap();
                break;
            case OrderObservable.ORDERTYPE_DISTRIBUTION:
                //商家已经接单
                initRider(hashMap);//骑手
                break;
            case OrderObservable.ORDERTYPE_DISTRIBUTION_RIDER_TANK_MEAL:
            case OrderObservable.ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL:
                changeRider(hashMap, type);
                break;
        }
    }

    /**
     * 骑手取餐和送餐行走轨迹绘制方法
     *
     * @param hashMap
     * @param type
     */
    private void changeRider(HashMap<String, String> hashMap, String type) {
        //获取骑手在行走过程中经纬度坐标
        String lat = hashMap.get(Constant.LAT);
        String lng = hashMap.get(Constant.LNG);
        double dlat = Double.parseDouble(lat);
        double dlng = Double.parseDouble(lng);
        currentPos = new LatLng(dlat, dlng);
        //记录目前骑手的经纬度坐标在一个有序的集合中
        riderPosList.add(currentPos);
        //时刻保证骑手在地图的中心位置
        //设置骑手的所在位置
        riderMarker.setPosition(currentPos);
        //地图定位焦点
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(currentPos));
        String info = "";
        DecimalFormat format = new DecimalFormat(".00");
        switch (type) {
            case OrderObservable.ORDERTYPE_DISTRIBUTION_RIDER_TANK_MEAL://取餐
                //取餐，距离卖家的距离
                //calculateLineDistance骑手现在所在的经纬度，商家现在所在的经纬度
                float ds = AMapUtils.calculateLineDistance(currentPos, latLngSeller);
                info = "距离商家" + format.format(ds) + "米";
                break;
            case OrderObservable.ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL:
                //送餐，距离买家的距离
                //参数一 骑手当前所在的位置
                //参数二 买家所在的位置
                //计算得出2个经纬度的直线距离db
                float db = AMapUtils.calculateLineDistance(currentPos, latLngBuyter);
                //给db保留小数点后的2位显示
                info = "距离买家" + format.format(db) + "米";
                break;
        }
        //要将计算出来的距离放置在骑手描述文本上
        riderMarker.setSnippet(info);
        //将设置的信息在界面中显示
        riderMarker.showInfoWindow();
        //参数1 当前所在的位置
        //参数二 历史的点，前一个点
        changeRider(currentPos, riderPosList.get(riderPosList.size() - 1));
    }

    private void changeRider(LatLng currentPos, LatLng pos) {
        //绘制线绿色，宽度为2像素
        aMap.addPolyline(new PolylineOptions().add(pos, currentPos).width(2).color(Color.GREEN));
    }

    private void initRider(HashMap<String, String> hashMap) {
        riderPosList = new ArrayList<>();
        //将接单的快递小哥经纬获取，并且将显示在地图上
        String lat = hashMap.get(Constant.LAT);
        String lng = hashMap.get(Constant.LNG);
        double dlat = Double.parseDouble(lat);
        double dlng = Double.parseDouble(lng);
        riderLatLng = new LatLng(dlat, dlng);
        //修改地图的缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        //创建一个在地图上需要显示的点的对象
        riderMarker = aMap.addMarker(new MarkerOptions().position(riderLatLng));
        //给要显示的点准备图片
        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.order_rider_icon));
        //将图片添加在点上
        riderMarker.setIcon(descriptor);
        //骑手接单描述内容
        riderMarker.setSnippet("骑手已接单");
        //显示骑手
        riderMarker.showInfoWindow();
        //将快递小哥接单额位置显示在地图的中心
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(riderLatLng));
        //在集合中添加骑手的开始点
        riderPosList.add(riderLatLng);
    }

    private void initAMap() {
        //显示地图
        map.setVisibility(View.VISIBLE);
        //修改地图的缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
        //显示买卖双方，在地图上展示经纬度坐标
        //添加买家master
        latLngBuyter = new LatLng(40.100519, 116.365828);
        //给买家在地图上显示的时候提供一张图片
        MarkerOptions markerOptions = new MarkerOptions();
        Marker marker = aMap.addMarker(markerOptions.position(latLngBuyter));
        //解析资源文件中的图片资源
        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.order_buyer_icon));
        //将图片资源放置在地图点的创建对象上
        marker.setIcon(descriptor);
        //将地图的中心指向某一个经纬坐标点，指定地图的中心点为买家
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLngBuyter));
        //添加卖家marker
        latLngSeller = new LatLng(40.060244, 116.343513);
        //创建一个在地图上需要现实的点的对象
        Marker marker1 = aMap.addMarker(new MarkerOptions().position(latLngSeller));
        //给要显示的点准备图片
        BitmapDescriptor descriptor1 = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.order_seller_icon));
        //将图片添加到点上
        marker1.setIcon(descriptor1);
    }

    @Override
    protected void dealOnDestroy() {
        //注销观察者
        OrderObservable.getInstance().deleteObserver(this);
        map.onDestroy();
        super.dealOnDestroy();
    }

    @Override
    protected void dealOnResume() {
        super.dealOnResume();
        //在activity执行onresume时执行mapview。onresume（），重新绘制加载地图
        map.onResume();
    }

    @Override
    protected void dealOnPause() {
        super.dealOnPause();
        //在activity执行onPause时执行mapview.onPause，暂停地图的绘制
        map.onPause();
    }

    @Override
    protected void dealOnSaveInstanceState(Bundle savedInstanceState) {
        super.dealOnSaveInstanceState(savedInstanceState);
        //在activity执行onSaveInstanceState时执行mapview.onSaveInstanceState(savedInstanceState)，保存地图当前的状态
        map.onSaveInstanceState(savedInstanceState);
    }
}
