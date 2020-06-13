package com.ngyb.takeout.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngyb.takeout.R;
import com.ngyb.takeout.activity.AddAddressActivity;
import com.ngyb.takeout.activity.AddressListActivity;
import com.ngyb.takeout.constant.Constant;
import com.ngyb.takeout.dao.ReceiptAddressDao;
import com.ngyb.takeout.dao.bean.ReceiptAddressBean;

import java.util.List;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/7 19:53
 */
public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.ViewHolder> {
    private Context context;
    private List<ReceiptAddressBean> data;
    private String[] addressLabels;
    private int[] bgLabels;
    private ReceiptAddressDao receiptAddressDao;
    private static final String TAG = "AddressListAdapter";

    public AddressListAdapter(Context context, List<ReceiptAddressBean> data) {
        this.context = context;
        this.data = data;
        addressLabels = new String[]{"家", "公司", "学校"};
        bgLabels = new int[]{Color.parseColor("#fc7251"), Color.parseColor("#468ade"), Color.parseColor("#02c14b")};
        receiptAddressDao = new ReceiptAddressDao(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.item_receipt_address, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ReceiptAddressBean receiptAddressBean = data.get(i);
        //设置名称
        viewHolder.tvName.setText(receiptAddressBean.getName());
        viewHolder.tvSex.setText(receiptAddressBean.getSex());
        //有2个号码可以显示
        if (!TextUtils.isEmpty(receiptAddressBean.getPhone()) && !TextUtils.isEmpty(receiptAddressBean.getPhoneOther())) {
            viewHolder.tvPhone.setText(receiptAddressBean.getPhone() + "," + receiptAddressBean.getPhoneOther());
        }
        //有一个号码可以显示
        if (!TextUtils.isEmpty(receiptAddressBean.getPhone()) && TextUtils.isEmpty(receiptAddressBean.getPhoneOther())) {
            viewHolder.tvPhone.setText(receiptAddressBean.getPhone());
        }
        //显示地址是公司,还是学校,还是家
        viewHolder.tvLabel.setText(receiptAddressBean.getLabel());
        //获取label后需要获取数组的索引位置,通过索引位置获取tvLabel控件的背景色
        if (receiptAddressBean.getLabel() != null && !receiptAddressBean.getLabel().equals("")) {
            int index = getIndex(receiptAddressBean.getLabel());
            viewHolder.tvLabel.setBackgroundColor(bgLabels[index]);
        }

        //显示初步地址和详细地址的合并结果
        viewHolder.tvAddress.setText(receiptAddressBean.getReceiptAddress() + receiptAddressBean.getDetailAddress());
        viewHolder.setPosition(i);
        //判断receiptAddressBean对象中的isSelect的值是否为1,决定前面√是否出现
        if (receiptAddressBean.getIsSelect() == 1) {
            //显示√
            viewHolder.cb.setVisibility(View.VISIBLE);
            viewHolder.cb.setChecked(true);
        } else {
            //隐藏√
            viewHolder.cb.setVisibility(View.GONE);
            viewHolder.cb.setChecked(false);
        }
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

    @Override
    public int getItemCount() {
        Log.e(TAG, "getItemCount: " + data.size());
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox cb;
        private final TextView tvName;
        private final TextView tvSex;
        private final TextView tvPhone;
        private final TextView tvLabel;
        private final TextView tvAddress;
        private final ImageView ivEdit;
        private int position;

        public ViewHolder(@NonNull View view) {
            super(view);
            cb = view.findViewById(R.id.cb);
            tvName = view.findViewById(R.id.tv_name);
            tvSex = view.findViewById(R.id.tv_sex);
            tvPhone = view.findViewById(R.id.tv_phone);
            tvLabel = view.findViewById(R.id.tv_label);
            tvAddress = view.findViewById(R.id.tv_address);
            ivEdit = view.findViewById(R.id.iv_edit);

            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReceiptAddressBean receiptAddressBean = data.get(position);
                    Intent intent = new Intent(context, AddAddressActivity.class);
                    intent.putExtra(Constant.RECEIPTADDRESS, receiptAddressBean);
                    context.startActivity(intent);
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //循环遍历data地址集合
                    for (int i = 0; i < data.size(); i++) {
                        ReceiptAddressBean receiptAddressBean = data.get(i);
                        if (i == position) {
                            //找到了选中的条目,要将选中条目对象isselect值修改为1
                            receiptAddressBean.setIsSelect(1);
                        } else {
                            //其余的条目都是没有选中的,要将未选中条目对象isselect值修改为0
                            receiptAddressBean.setIsSelect(0);
                        }
                        //更新数据值数据库中
                        receiptAddressDao.update(receiptAddressBean);
                    }
                    notifyDataSetChanged();
                    ((AddressListActivity) context).finish();
                }
            });
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}
