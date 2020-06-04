package com.ngyb.takeout.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.ngyb.takeout.R;
import com.ngyb.takeout.bean.net.CategorieListBean;
import com.ngyb.takeout.bean.net.HomeInfoBean;
import com.ngyb.takeout.bean.net.PromotionListBean;
import com.ngyb.takeout.bean.net.SellerBean;
import com.ngyb.takeout.constant.Constant;
import com.ngyb.utils.LogUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/5/8 21:49
 */
public class HomeAdapter extends RecyclerView.Adapter {
    private static final String TAG = "HomeAdapter";
    public static final int ITEM_HEADER = 0;//索引为0 的时候，头条目状态码
    public static final int ITEM_SELLER = 1;//商家条目状态码
    public static final int ITEM_DIV = 2;//分割线条目状态码
    private Context context;
    private HomeInfoBean homeInfoBean;

    public HomeAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //不同的条目布局效果不一样，则会有不同的ViewHolder对象存储控件
        if (i == ITEM_HEADER) {
            //将布局文件转换成view对象
            View view = View.inflate(context, R.layout.item_title, null);
            //创建viewholder对象
            HeaderViewHolder headerViewHolder = new HeaderViewHolder(view);
            return headerViewHolder;
        } else if (i == ITEM_DIV) {
            View view = View.inflate(context, R.layout.item_division, null);
            DivViewHolder divViewHolder = new DivViewHolder(view);
            return divViewHolder;
        } else {
            //商家viewholder对象创建
            View view = View.inflate(context, R.layout.item_seller, null);
            SellerViewHolder sellerViewHolder = new SellerViewHolder(view);
            return sellerViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof HeaderViewHolder) {
            //因为头轮播图的数据，在HeaderViewHolder构造方法中已经设置过了，所以在此处无代码
        } else if (viewHolder instanceof DivViewHolder) {
            //设置分割线数据
            setDivViewHolderData(viewHolder, i - 1);
            Log.e(TAG, "onBindViewHolder: ");
        } else {
            //设置普通商家条目的数据
            setSellerViewHolderData(viewHolder, i - 1);
            ((SellerViewHolder) viewHolder).setPosition(i - 1);
        }
    }

    private void setSellerViewHolderData(RecyclerView.ViewHolder viewHolder, int i) {
        SellerBean seller = homeInfoBean.getBody().get(i).getSeller();
        //设置商家名称
        ((SellerViewHolder) viewHolder).tvTitle.setText(seller.getName());
        //评分，星星个数
        if (!TextUtils.isEmpty(seller.getScore())) {
            float score = Float.parseFloat(seller.getScore());
            ((SellerViewHolder) viewHolder).ratingBar.setRating(score);
        }
        if (!TextUtils.isEmpty(seller.getSendPrice()) && !TextUtils.isEmpty(seller.getDeliveryFee())) {
//            Y0起送/配送费Y19
            String str = "¥" + seller.getSendPrice() + "起送/配送费¥" + seller.getDeliveryFee();
            ((SellerViewHolder) viewHolder).send.setText(str);
        }
        if (!TextUtils.isEmpty(seller.getPic())) {
            Picasso
                    .with(context)
                    .load(Constant.BASEURL + seller.getPic())
                    .error(R.mipmap.item_kfc)
                    .into(((SellerViewHolder) viewHolder).pic);
        }
    }

    private void setDivViewHolderData(RecyclerView.ViewHolder viewHolder, int i) {
        Log.e(TAG, "setDivViewHolderData: ");
        List<String> recommendInfos = homeInfoBean.getBody().get(i).getRecommendInfos();
        ((DivViewHolder) viewHolder).tv1.setText(recommendInfos.get(0));
        ((DivViewHolder) viewHolder).tv2.setText(recommendInfos.get(1));
        ((DivViewHolder) viewHolder).tv3.setText(recommendInfos.get(2));
        ((DivViewHolder) viewHolder).tv4.setText(recommendInfos.get(3));
        ((DivViewHolder) viewHolder).tv5.setText(recommendInfos.get(4));
        ((DivViewHolder) viewHolder).tv6.setText(recommendInfos.get(5));
    }

    @Override
    public int getItemCount() {
        if (homeInfoBean != null && homeInfoBean.getBody() != null && homeInfoBean.getBody().size() > 0) {
            //当前列表的条目数据 = 商品列表个数 +1 （顶部轮播图头条木）
            return homeInfoBean.getBody().size() + 1;
        }
        return 0;
    }

    /**
     * @param homeInfoBean 首页请求网络获取数据
     *                     head object 用户填充包含轮播图的头
     *                     body list 用户填充商铺列表
     */
    public void setData(HomeInfoBean homeInfoBean) {
        this.homeInfoBean = homeInfoBean;
        //在获取数据后，需要告知适配器按照传递的数据进行刷新
        notifyDataSetChanged();
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder implements BaseSliderView.OnSliderClickListener {

        private final SliderLayout slider;
        private final TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8;
        private final ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            slider = itemView.findViewById(R.id.slider);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
            tv4 = itemView.findViewById(R.id.tv4);
            tv5 = itemView.findViewById(R.id.tv5);
            tv6 = itemView.findViewById(R.id.tv6);
            tv7 = itemView.findViewById(R.id.tv7);
            tv8 = itemView.findViewById(R.id.tv8);
            iv1 = itemView.findViewById(R.id.iv1);
            iv2 = itemView.findViewById(R.id.iv2);
            iv3 = itemView.findViewById(R.id.iv3);
            iv4 = itemView.findViewById(R.id.iv4);
            iv5 = itemView.findViewById(R.id.iv5);
            iv6 = itemView.findViewById(R.id.iv6);
            iv7 = itemView.findViewById(R.id.iv7);
            iv8 = itemView.findViewById(R.id.iv8);
            //结果中包含三个对象，3个对象中既有图片描述，图片的链接地址
            List<PromotionListBean> promotionList = homeInfoBean.getHead().getPromotionList();
            for (int i = 0; i < promotionList.size(); i++) {
                LogUtils.doLog(TAG,Constant.BASEURL + promotionList.get(i).getPic());
                TextSliderView textSliderView = new TextSliderView(context);
                textSliderView.description(promotionList.get(i).getInfo())//描述文本
                        .image(Constant.BASEURL + promotionList.get(i).getPic())//展示图片内容
                        .setScaleType(BaseSliderView.ScaleType.Fit)//前景填充背景方式
                        .setOnSliderClickListener(this);
                //将包含了图片和描述了文本内容的控件添加到容器内
                slider.addSlider(textSliderView);
            }
            //SliderLayout指定切换内部图片的动画效果
            slider.setPresetTransformer(SliderLayout.Transformer.Tablet);
            //指示器的所在位置
            slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            //描述内容出现的动画
            slider.setCustomAnimation(new DescriptionAnimation());
            //切换页面间隔时长
            slider.setDuration(4000);
            List<CategorieListBean> categorieList = homeInfoBean.getHead().getCategorieList();
            for (int i = 0; i < categorieList.size(); i++) {
                CategorieListBean categorieListBean = categorieList.get(i);
                if (categorieListBean.getId() == 1) {
                    tv1.setText(categorieListBean.getName());
                    Picasso
                            .with(context)
                            .load(Constant.BASEURL + categorieListBean.getPic())
                            .into(iv1);
                }
                if (categorieListBean.getId() == 2) {
                    tv2.setText(categorieListBean.getName());
                    Picasso
                            .with(context)
                            .load(Constant.BASEURL + categorieListBean.getPic())
                            .into(iv2);
                }
                if (categorieListBean.getId() == 3) {
                    tv3.setText(categorieListBean.getName());
                    Picasso
                            .with(context)
                            .load(Constant.BASEURL + categorieListBean.getPic())
                            .into(iv3);
                }
                if (categorieListBean.getId() == 4) {
                    tv4.setText(categorieListBean.getName());
                    Picasso
                            .with(context)
                            .load(Constant.BASEURL + categorieListBean.getPic())
                            .into(iv4);
                }
                if (categorieListBean.getId() == 5) {
                    tv5.setText(categorieListBean.getName());
                    Picasso
                            .with(context)
                            .load(Constant.BASEURL + categorieListBean.getPic())
                            .into(iv5);
                }
                if (categorieListBean.getId() == 6) {
                    tv6.setText(categorieListBean.getName());
                    Picasso
                            .with(context)
                            .load(Constant.BASEURL + categorieListBean.getPic())
                            .into(iv6);
                }
                if (categorieListBean.getId() == 7) {
                    tv7.setText(categorieListBean.getName());
                    Picasso
                            .with(context)
                            .load(Constant.BASEURL + categorieListBean.getPic())
                            .into(iv7);
                }
                if (categorieListBean.getId() == 8) {
                    tv8.setText(categorieListBean.getName());
                    Picasso
                            .with(context)
                            .load(Constant.BASEURL + categorieListBean.getPic())
                            .into(iv8);
                }
            }
        }

        @Override
        public void onSliderClick(BaseSliderView slider) {

        }
    }

    class DivViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvDivisionTitle;
        private final TextView tv1, tv2, tv3, tv4, tv5, tv6;

        public DivViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDivisionTitle = itemView.findViewById(R.id.tv_division_title);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
            tv4 = itemView.findViewById(R.id.tv4);
            tv5 = itemView.findViewById(R.id.tv5);
            tv6 = itemView.findViewById(R.id.tv6);
        }
    }

    class SellerViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvCount;
        private final TextView tvTitle;
        private final RatingBar ratingBar;
        private int position;
        private final TextView send;
        private final ImageView pic;

        public SellerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvTitle = itemView.findViewById(R.id.tv_title);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            send = itemView.findViewById(R.id.send);
            pic = itemView.findViewById(R.id.pic);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //position点钟条目索引位置 TODO
                    //通过索引从服务器返回的商品列表集合中，找到指定的商家，让后一个页面加载此商家的数据
//                    SellerBean seller = homeInfoBean.getBody().get(position).getSeller();
//                    //此seller会作为区分不同商家对象
//                    Intent intent = new Intent(context, BusinessActivity.class);
//                    intent.putExtra("seller", seller);
//                    context.startActivity(intent);

                }
            });
        }

        public void setPosition(int i) {
            this.position = i;
        }
    }

    /**
     * 3中条目类型展示
     *
     * @param position 列表索引值
     * @return 根据列表的索引值判断此索引位置需要显示的条目类型是那种（头、分割线、商家）
     */
    @Override
    public int getItemViewType(int position) {
//        LogUtils.doLog(TAG, "位置：" + position);
        if (position == 0) {
//            LogUtils.doLog(TAG, "头");
            //返回头的条目类型状态码
            return ITEM_HEADER;
        } else if (homeInfoBean.getBody().get(position - 1).getType() == 1) {
//            LogUtils.doLog(TAG, "分隔符");
//            LogUtils.doLog(TAG, homeInfoBean.getBody().get(position - 1).getType() + "");
            //返回分割线条目状态码
            return ITEM_DIV;
        } else {
//            LogUtils.doLog(TAG, "商家");
//            LogUtils.doLog(TAG, homeInfoBean.getBody().get(position - 1).getType() + "");
//            LogUtils.doLog(TAG, homeInfoBean.getBody().get(position - 1).getType() + "");
            //返回商家条目状态码
            return ITEM_SELLER;
        }
    }
}
