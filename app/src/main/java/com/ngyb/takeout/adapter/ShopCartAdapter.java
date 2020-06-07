package com.ngyb.takeout.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ngyb.takeout.R;
import com.ngyb.takeout.activity.BusinessActivity;
import com.ngyb.takeout.bean.net.GoodsInfoBean;
import com.ngyb.takeout.bean.net.GoodsTypeInfoBean;
import com.ngyb.takeout.constant.Constant;
import com.ngyb.takeout.fragment.GoodsFragment;
import com.ngyb.takeout.utils.CountPriceFormater;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/4 22:52
 */
public class ShopCartAdapter extends RecyclerView.Adapter<ShopCartAdapter.ViewHolder> {
    private Context context;
    private List<GoodsInfoBean> goodsInfoBeans;

    public ShopCartAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<GoodsInfoBean> goodsInfoBeans) {
        this.goodsInfoBeans = goodsInfoBeans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.item_cart, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        GoodsInfoBean goodsInfoBean = goodsInfoBeans.get(i);
        viewHolder.tvName.setText(goodsInfoBean.getName());
        viewHolder.tvCount.setText(goodsInfoBean.getCount() + "");
        float price = goodsInfoBean.getCount() * goodsInfoBean.getNewPrice();
        viewHolder.tvTypeAllPrice.setText(CountPriceFormater.format(price));
        viewHolder.setPosition(i);
    }

    @Override
    public int getItemCount() {
        if (goodsInfoBeans != null && goodsInfoBeans.size() > 0) {
            return goodsInfoBeans.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tvName;
        private final TextView tvTypeAllPrice;
        private final ImageButton ibMinus;
        private final TextView tvCount;
        private final ImageButton ibAdd;
        private final LinearLayout ll;
        private int position;
        private int operation = Constant.ADD;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
            tvTypeAllPrice = view.findViewById(R.id.tv_type_all_price);
            ibMinus = view.findViewById(R.id.ib_minus);
            tvCount = view.findViewById(R.id.tv_count);
            ibAdd = view.findViewById(R.id.ib_add);
            ll = view.findViewById(R.id.ll);
            ibAdd.setOnClickListener(this);
            ibMinus.setOnClickListener(this);
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_add:
                    operation = Constant.ADD;
                    //商品里汇总的某件商品数量需要变化
                    refreshGoodsAdapterData(operation);
                    //商品分类数量需要进行变化
                    refreshGodsTypeAdapter(operation);
                    //购物车列表中的数据需要进行变化
                    notifyDataSetChanged();
                    //更新购物车中的商品总金额和总数量,在businessPresenter中计算购物车数据的方法
                    ((BusinessActivity) context).businessPresenter.getShopCartCountAndPrice();
                    break;
                case R.id.ib_minus:
                    operation = Constant.DELETE;
                    //商品里汇总的某件商品数量需要变化
                    refreshGoodsAdapterData(operation);
                    //商品分类数量需要进行变化
                    refreshGodsTypeAdapter(operation);
                    //如果此商品是减掉了最后一件商品,则此条目需要从购物车列表中移除
                    if (goodsInfoBeans.get(position).getCount() ==0){
                        goodsInfoBeans.remove(position);
                    }
                    //购物车列表中的数据需要进行变化
                    notifyDataSetChanged();
                    //如果删除的是所有商品的最后一件,则需要将对话框隐藏
                    if (goodsInfoBeans.size() ==0){
                        hiddenBottomSheetLayout();
                    }
                    //更新购物车中的商品总金额和总数量,在businessPresenter中计算购物车数据的方法
                    ((BusinessActivity) context).businessPresenter.getShopCartCountAndPrice();
                    break;
            }
        }

        /**
         * 让context隐藏对话框
         */
        private void hiddenBottomSheetLayout() {
            ((BusinessActivity)context).hiddenDialog();
        }

        /**
         * 增加或者减少商品分类选中的数量
         *
         * @param operation 操作符 增加或者减少
         */
        private void refreshGodsTypeAdapter(int operation) {
            //获取购物车中需要修改数量商品分类的typeId
            int typeId = goodsInfoBeans.get(position).getTypeId();
            //商品分类集合
            GoodsFragment goodsFragment = ((BusinessActivity) context).myFragmentPagerAdapter.getGoodsFragment();
            if (goodsFragment != null) {
                List<GoodsTypeInfoBean> goodsTypeInfoBeans = goodsFragment.goodsTypeAdapter.getData();
                for (int i = 0; i < goodsTypeInfoBeans.size(); i++) {
                    GoodsTypeInfoBean goodsTypeInfoBean = goodsTypeInfoBeans.get(i);
                    if (goodsTypeInfoBean.getId() == typeId) {
                        switch (operation) {
                            case Constant.ADD:
                                int addCount = goodsTypeInfoBean.getCount() + 1;
                                goodsTypeInfoBean.setCount(addCount);
                                break;
                            case Constant.DELETE:
                                if (goodsTypeInfoBean.getCount() > 0) {
                                    int deleteCount = goodsTypeInfoBean.getCount() - 1;
                                    goodsTypeInfoBean.setCount(deleteCount);
                                }
                                break;
                        }
                    }
                }
                goodsFragment.goodsTypeAdapter.notifyDataSetChanged();
            }
        }

        private void refreshGoodsAdapterData(int operation) {
            //获取需要更改商品数量对象的商品唯一性id
            int id = goodsInfoBeans.get(position).getId();
            //针对id和operation对商品列表中的商品数量进行变更
            GoodsFragment goodsFragment = ((BusinessActivity) context).myFragmentPagerAdapter.getGoodsFragment();
            if (goodsFragment != null) {
                ArrayList<GoodsInfoBean> goodsInfoBeans = goodsFragment.goodsAdapter.getData();
                for (int i = 0; i < goodsInfoBeans.size(); i++) {
                    GoodsInfoBean goodsInfoBean = goodsInfoBeans.get(i);
                    if (goodsInfoBean.getId() == id) {
                        switch (operation) {
                            case Constant.ADD:
                                int addCount = goodsInfoBean.getCount() + 1;
                                goodsInfoBean.setCount(addCount);
                                break;
                            case Constant.DELETE:
                                if (goodsInfoBean.getCount() > 0) {
                                    int deleteCount = goodsInfoBean.getCount() - 1;
                                    goodsInfoBean.setCount(deleteCount);
                                }
                                break;
                        }
                    }
                }
                goodsFragment.goodsAdapter.notifyDataSetChanged();
            }
        }
    }
}

