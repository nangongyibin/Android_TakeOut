package com.ngyb.takeout.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngyb.takeout.R;
import com.ngyb.takeout.activity.BusinessActivity;
import com.ngyb.takeout.app.MyApplication;
import com.ngyb.takeout.net.bean.GoodsInfoBean;
import com.ngyb.takeout.constant.Constant;
import com.ngyb.takeout.fragment.GoodsFragment;
import com.ngyb.takeout.utils.CountPriceFormater;
import com.ngyb.utils.LogUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/6 11:35
 */
public class GoodsAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private static final String TAG = "GoodsAdapter";
    private GoodsFragment goodsFragment;
    private Context context;
    private ArrayList<GoodsInfoBean> goodsInfoBeans;

    public GoodsAdapter(GoodsFragment goodsFragment, Context context) {
        this.goodsFragment = goodsFragment;
        this.context = context;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        //返回头的view对象
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_type_header, null);
        }
        GoodsInfoBean goodsInfoBean = getItem(position);
        //给每一个商品设置分类名称头
        ((TextView) convertView).setText(goodsInfoBean.getTypeName());
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //返回值情况有几种,就有几个头
        return goodsInfoBeans.get(position).getTypeId();//0到goodsInfoBeans.size()-1
    }

    @Override
    public int getCount() {
        if (goodsInfoBeans != null && goodsInfoBeans.size() > 0) {
            return goodsInfoBeans.size();
        }
        return 0;
    }

    @Override
    public GoodsInfoBean getItem(int position) {
        return goodsInfoBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_goods, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GoodsInfoBean goodsInfoBean = getItem(position);
        //设置商品名称
        viewHolder.tvName.setText(goodsInfoBean.getName());
        //设置商品另一个名称
        viewHolder.tvZuCheng.setText(goodsInfoBean.getForm());
        //月售
        viewHolder.tvYueShaoShouNum.setText("月售"+goodsInfoBean.getMonthSaleNum()+"份");
        //设置金额
        String strNewPrice = CountPriceFormater.format(goodsInfoBean.getNewPrice());
        String strOldPrice = CountPriceFormater.format(goodsInfoBean.getOldPrice());
        viewHolder.tvNewPrice.setText(strNewPrice);
        viewHolder.tvOldPrice.setText(strOldPrice);
        viewHolder.tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        //设置图片
        Log.e(TAG, position+"getView: "+goodsInfoBean.getIcon() );
        Picasso.with(parent.getContext()).load(Constant.BASEURL + goodsInfoBean.getIcon()).into(viewHolder.ivIcon);
        //显示选中商品的数量
        if (goodsInfoBean.getCount() > 0) {
            viewHolder.tvCount.setVisibility(View.VISIBLE);
            viewHolder.tvCount.setText(goodsInfoBean.getCount() + "");
            viewHolder.ibMinus.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvCount.setVisibility(View.GONE);
            viewHolder.ibMinus.setVisibility(View.GONE);
        }
        viewHolder.setPosition(position);
        return convertView;
    }

    public void setData(ArrayList<GoodsInfoBean> goodsInfoBeans) {
        this.goodsInfoBeans = goodsInfoBeans;
        notifyDataSetChanged();
    }

    /**
     * @return 商品列表集合
     */
    public ArrayList<GoodsInfoBean> getData() {
        return goodsInfoBeans;
    }

    class ViewHolder implements View.OnClickListener {

        private final ImageView ivIcon;
        private final TextView tvName;
        private final TextView tvZuCheng;
        private final TextView tvYueShaoShouNum;
        private final TextView tvNewPrice;
        private final TextView tvOldPrice;
        private final ImageButton ibMinus;
        private final TextView tvCount;
        private final ImageButton ibAdd;
        private int operation = Constant.ADD;
        private int position;

        public ViewHolder(View view) {
            ivIcon = view.findViewById(R.id.iv_icon);
            tvName = view.findViewById(R.id.tv_name);
            tvZuCheng = view.findViewById(R.id.tv_zucheng);
            tvYueShaoShouNum = view.findViewById(R.id.tv_yueshaoshounum);
            tvNewPrice = view.findViewById(R.id.tv_newprice);
            tvOldPrice = view.findViewById(R.id.tv_oldprice);
            ibMinus = view.findViewById(R.id.ib_minus);
            tvCount = view.findViewById(R.id.tv_count);
            ibAdd = view.findViewById(R.id.ib_add);

            ibAdd.setOnClickListener(this);
            ibMinus.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_add:
                    //点击完+号,则控件不可用
                    v.setEnabled(false);
                    addGoods(v);
                    operation = Constant.ADD;
                    break;
                case R.id.ib_minus:
                    deleteGoods();
                    operation = Constant.DELETE;
                    break;
            }
            //如果对商品的数量进行了增加或者减少,则需要告知分类数量进行变更
            //修改分类数量(说明分类的数量增加还是减少,那个分类数量需要变化)
            int typeId = goodsInfoBeans.get(position).getTypeId();
            //通知商品分类数量进行变化
            goodsFragment.goodsTypeAdapter.refreshUI(typeId, operation);
            //更改购物车中商品数量和总金额
            ((BusinessActivity) context).businessPresenter.getShopCartCountAndPrice();

        }

        /**
         * 减少一件商品
         */
        private void deleteGoods() {
            //选中条目对象count的值为0的时候,才有如下动画
            if (goodsInfoBeans.get(position).getCount() == 0) {
                //旋转
                RotateAnimation rotateAnimation = new RotateAnimation(720, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                //透明度变化
                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.5f);
                //平移(x轴,正值--->0)
                TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 2.0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
                AnimationSet animationSet = new AnimationSet(false);
                animationSet.addAnimation(rotateAnimation);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(translateAnimation);
                animationSet.setDuration(300);
                ibMinus.startAnimation(animationSet);
                //减号一出去的动画执行完成后,需要将括号和数量在此隐藏
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ibMinus.setVisibility(View.GONE);
                        tvCount.setVisibility(View.GONE);
                        //数量归0操作
                        goodsInfoBeans.get(position).setCount(0);
                        //因为数量和减号控件已经被隐藏了等同于刷新数据适配器效果
                        //所以数据适配器没必要刷新
                        ((BusinessActivity) context).businessPresenter.getShopCartCountAndPrice();

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            } else {
                //数量减少
                //用户点击+号添加一件商品,需要刷新数据适配器更新商品数量
                int deleteCount = goodsInfoBeans.get(position).getCount() - 1;
                goodsInfoBeans.get(position).setCount(deleteCount);
                notifyDataSetChanged();
            }
        }

        /**
         * @param v 添加一件商品
         */
        private void addGoods(View v) {
            //选中条目对象count的值为0的时候,才有如下动画
            if (goodsInfoBeans.get(position).getCount() == 0) {
                //旋转
                RotateAnimation rotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                //透明度变化
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
                //平移(x轴,正值--->0)
                TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 2.0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
                AnimationSet animationSet = new AnimationSet(false);
                animationSet.addAnimation(rotateAnimation);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(translateAnimation);
                animationSet.setDuration(300);
                ibMinus.startAnimation(animationSet);
                //减号和数量控件必须显示,才可以让动画效果可见
                ibMinus.setVisibility(View.VISIBLE);
                tvCount.setVisibility(View.VISIBLE);
            }
            //用户点击+号添加一件商品,需要刷新数据适配器更新商品数量
            int addCount = goodsInfoBeans.get(position).getCount() + 1;
            goodsInfoBeans.get(position).setCount(addCount);
            notifyDataSetChanged();
            //点击加号时抛物线动画效果
            addFlyImage(v);
        }

        private void addFlyImage(View v) {
            //用户飞行小球
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.mipmap.button_add);
            //制定飞行小球开始位置
            int[] viewLocation = new int[2];
            v.getLocationInWindow(viewLocation);
            //让imageview和view的所在位置重合
            imageView.setX(viewLocation[0]);
            imageView.setY(viewLocation[1] - MyApplication.statusBarHeight);
            //并且将小球需要添加在帧布局的页面中
            ((BusinessActivity) context).addFlyImageView(imageView, v.getWidth(), v.getHeight());
            //获取飞行起点和终点的坐标
            int[] desLocation = ((BusinessActivity) context).getShopCartLocation();
            int[] sourceLocation = new int[2];
            imageView.getLocationInWindow(sourceLocation);
            //飞
            fly(imageView, v, sourceLocation, desLocation);
        }

        private void fly(final ImageView imageView, final View v, int[] sourceLocation, int[] desLocation) {
            //x轴起飞位置和终点位置
            int startX = sourceLocation[0];
            int endX = desLocation[0];
            //y轴起飞位置和终点位置
            int startY = sourceLocation[1];
            int endY = desLocation[1];
            //管理x轴飞行,匀速
            LogUtils.doLog(TAG, "开始位置:" + startX + "====" + startY);
            LogUtils.doLog(TAG, "结束位置:" + endX + "====" + endY);
            TranslateAnimation translateAnimationX = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, endX - startX, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
            translateAnimationX.setInterpolator(new LinearInterpolator());
            //管理y轴飞行
            TranslateAnimation translateAnimationY = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, endY - startY);
            translateAnimationY.setInterpolator(new AccelerateInterpolator());
            //将2个动画合并
            AnimationSet animationSet = new AnimationSet(false);
            animationSet.addAnimation(translateAnimationX);
            animationSet.addAnimation(translateAnimationY);
            animationSet.setDuration(500);
            imageView.startAnimation(animationSet);
            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    imageView.setVisibility(View.GONE);
                    //飞行动画结束控件可用
                    v.setEnabled(true);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}
