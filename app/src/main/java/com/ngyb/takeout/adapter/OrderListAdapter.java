package com.ngyb.takeout.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngyb.takeout.R;
import com.ngyb.takeout.constant.Constant;
import com.ngyb.takeout.net.bean.OrderBean;
import com.ngyb.takeout.observable.OrderObservable;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import retrofit2.http.POST;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/14 21:24
 */
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> implements Observer {
    private List<OrderBean> data;
    private static final String TAG = "OrderListAdapter";

    public OrderListAdapter() {
        OrderObservable.getInstance().addObserver(this);
    }

    public void setData(List<OrderBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //1.布局转换成view
        //2.view放到viewholder里面去
        //3.返回viewholder
        View view = View.inflate(viewGroup.getContext(), R.layout.item_order_item, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.e(TAG, "onBindViewHolder: "+i );
        //把viewholder中的控件找出来赋值
        OrderBean orderBean = data.get(i);
        //设置商家名称
        if (orderBean.getSellerBean() != null) {
            viewHolder.tvOrderItemSellerName.setText(orderBean.getSellerBean().getName());
        }
        //设置订单状态
        //服务器返回一个type,此type对应一个状态，需要将状态显示出来，10-->未支付 50-->已送达
        //根据type获取订单状态字符串
        viewHolder.tvOrderItemType.setText(getTypeString(orderBean.getType()));
        viewHolder.setPosition(i);
    }

    private String getTypeString(String type) {
        String strType = "";
        switch (type) {
            case OrderObservable.ORDERTYPE_UNPAYMENT://10
                strType = "未支付";
                break;
            case OrderObservable.ORDERTYPE_SUBMIT://20
                strType = "已提交";
                break;
            case OrderObservable.ORDERTYPE_RECEIVEORDER://30
                strType = "商家接单";
                break;
            case OrderObservable.ORDERTYPE_DISTRIBUTION://40
                strType = "配送中";
                break;
            case OrderObservable.ORDERTYPE_SERVED://50
                strType = "已送达";
                break;
            case OrderObservable.ORDERTYPE_CANCELLEDORDER://60
                strType = "取消订单";
                break;
        }
        return strType;
    }

    /**
     * 看懂观察者模式源码
     * observable类
     * observer接口
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (data != null && data.size() > 0) {
            Log.e(TAG, "getItemCount: " + data.size());
            return data.size();
        }
        return 0;
    }

    /**
     * 通过update去更新页面中的某一个条目的订单状态
     * arg就是更新界面的数据，此数据在notifyObservers中提供
     * arg即为在MyReceiver界面接收到的服务器传递过来的json转换成的集合
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        HashMap<String, String> hashMap = (HashMap<String, String>) arg;
        //获取修改哪个订单
        String orderInfo = hashMap.get(Constant.OrderInfo);
        //修改成什么状态
        String type = hashMap.get(Constant.TYPE);
        //通过orderInfo找到需要修改的订单对象，然后将其type的值改为服务器提供的type值
        int changePosition = -1;
        for (int i = 0; i < data.size(); i++) {
            OrderBean orderBean = data.get(i);
            if (orderBean != null && orderBean.getId() != null && orderInfo.equals(orderBean.getId())) {
                String orderId = orderBean.getId();
                data.get(i).setType(type);
                changePosition = i;
                break;
            }
        }
        if (changePosition != -1) {
            notifyItemChanged(changePosition);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivOrderItemSellerLogo;
        private final TextView tvOrderItemSellerName;
        private final TextView tvOrderItemType;
        private final TextView tvOrderItemTime;
        private final TextView tvOrderItemFoods;
        private final TextView tvOrderItemMoney;
        private final TextView tvOrderItemMultiFunction;
        private int position;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            ivOrderItemSellerLogo = itemView.findViewById(R.id.iv_order_item_seller_logo);
            tvOrderItemSellerName = itemView.findViewById(R.id.tv_order_item_seller_name);
            tvOrderItemType = itemView.findViewById(R.id.tv_order_item_type);
            tvOrderItemTime = itemView.findViewById(R.id.tv_order_item_time);
            tvOrderItemFoods = itemView.findViewById(R.id.tv_order_item_foods);
            tvOrderItemMoney = itemView.findViewById(R.id.tv_order_item_money);
            tvOrderItemMultiFunction = itemView.findViewById(R.id.tv_order_item_multi_function);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(itemView.getContext(), OrderDetailActivity.class);
//                    intent.putExtra(Constant.ORDERID, data.get(position).getId());
//                    intent.putExtra(Constant.TYPE, data.get(position).getType());
//                    itemView.getContext().startActivity(intent);
                }
            });
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}
