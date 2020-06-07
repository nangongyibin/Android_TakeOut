package com.ngyb.takeout.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ngyb.takeout.R;
import com.ngyb.takeout.net.bean.GoodsTypeInfoBean;
import com.ngyb.takeout.constant.Constant;
import com.ngyb.takeout.fragment.GoodsFragment;

import java.util.List;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/6 10:59
 */
public class GoodsTypeAdapter extends RecyclerView.Adapter<GoodsTypeAdapter.ViewHolder> {
    private GoodsFragment goodsFragment;
    private Context context;
    private List<GoodsTypeInfoBean> goodsTypeInfoBeans;
    //提供一个变量,用户记录选中的分类条目索引
    public int currentPosition = 0;

    public GoodsTypeAdapter(GoodsFragment goodsFragment, Context context) {
        this.goodsFragment = goodsFragment;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context, R.layout.item_type, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        GoodsTypeInfoBean goodsTypeInfoBean = goodsTypeInfoBeans.get(i);
        //设置分类标题
        viewHolder.type.setText(goodsTypeInfoBean.getName());
        //设置分类数量,气泡显示隐藏
        int count = goodsTypeInfoBean.getCount();
        if (count == 0) {
            //隐藏气泡
            viewHolder.tvCount.setVisibility(View.GONE);
        } else {
            //显示气泡,并显示数量
            viewHolder.tvCount.setVisibility(View.VISIBLE);
            viewHolder.tvCount.setText(count + "");
        }
        if (currentPosition == i) {
            //将viewholder中的布局文件转换成的view对象背景设置成白色
            viewHolder.itemView.setBackgroundColor(Color.WHITE);
            //显示分类的textview颜色红
            viewHolder.type.setTextColor(Color.RED);
        } else {
            //将viewholder中的布局文件转换成的view对象背景设置成灰色
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
            //显示分类的textview颜色黑
            viewHolder.type.setTextColor(Color.BLACK);
        }
        viewHolder.setPosition(i);
    }

    @Override
    public int getItemCount() {
        if (goodsTypeInfoBeans != null && goodsTypeInfoBeans.size() > 0) {
            return goodsTypeInfoBeans.size();
        }
        return 0;
    }

    public void setData(List<GoodsTypeInfoBean> goodsTypeInfoBeans) {
        this.goodsTypeInfoBeans = goodsTypeInfoBeans;
        notifyDataSetChanged();
    }

    public void refreshUI(int typeId, int operation) {
        if (goodsTypeInfoBeans != null) {
            for (int i = 0; i < goodsTypeInfoBeans.size(); i++) {
                if (goodsTypeInfoBeans.get(i).getId() == typeId) {
                    //找到了需要修改分类对象
                    switch (operation) {
                        case Constant.ADD:
                            int addCount = goodsTypeInfoBeans.get(i).getCount() + 1;
                            goodsTypeInfoBeans.get(i).setCount(addCount);
                            break;
                        case Constant.DELETE:
                            if (goodsTypeInfoBeans.get(i).getCount() > 0) {
                                int deleteCount = goodsTypeInfoBeans.get(i).getCount() - 1;
                                goodsTypeInfoBeans.get(i).setCount(deleteCount);
                            }
                            break;
                    }
                    break;
                }
            }
            //刷新分类数据适配器方法
            notifyDataSetChanged();
        }
    }

    public List<GoodsTypeInfoBean> getData() {
        return goodsTypeInfoBeans;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvCount;
        private final TextView type;
        private int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCount = itemView.findViewById(R.id.tvCount);
            type = itemView.findViewById(R.id.type);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPosition = position;
                    notifyDataSetChanged();
                    //根据选中条目,切换右侧商品列表显示的数据
                    //找到点钟条目的分类id
                    int id = goodsTypeInfoBeans.get(position).getId();
                    //后续的2段代码逻辑和商品列表有关,商品列表的数据适配器在GoodsFragment,所以将后续逻辑放在了GoodsFragment中编写
                    goodsFragment.refreshGoodsAdapterUI(id);
                    //在右侧商品列表集合中,循环遍历,直到找到typeid和id一致的那个条目为止
                    //让右侧的列表,切换到指定条目显示
                }
            });
        }

        public void setPosition(int i) {
            position = i;
        }
    }
}
