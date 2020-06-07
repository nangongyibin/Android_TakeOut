package com.ngyb.takeout.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;

import com.ngyb.mvpbase.BaseMvpFragment;
import com.ngyb.takeout.R;
import com.ngyb.takeout.adapter.GoodsAdapter;
import com.ngyb.takeout.adapter.GoodsTypeAdapter;
import com.ngyb.takeout.net.bean.GoodsInfoBean;
import com.ngyb.takeout.net.bean.GoodsTypeInfoBean;
import com.ngyb.takeout.net.bean.SellerBean;
import com.ngyb.takeout.constant.Constant;
import com.ngyb.takeout.contract.GoodsFContract;
import com.ngyb.takeout.presenter.GoodsFPresenter;
import com.ngyb.utils.LogUtils;
import com.uber.autodispose.AutoDisposeConverter;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/6 10:26
 */
public class GoodsFragment extends BaseMvpFragment<GoodsFPresenter> implements GoodsFContract.View {
    private static final String TAG = "GoodsFragment";
    private SellerBean seller;
    private RecyclerView rvGoodsType;
    private StickyListHeadersListView slhlv;
    public GoodsTypeAdapter goodsTypeAdapter;
    public GoodsAdapter goodsAdapter;

    @Override
    protected void init(View view) {
        initView(view);
    }

    private void initView(View view) {
        rvGoodsType = view.findViewById(R.id.rv_goods_type);
        slhlv = view.findViewById(R.id.slhlv);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods;
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
    public <T> AutoDisposeConverter<T> bindAutoDispose() {
        return null;
    }

    @Override
    protected void dealOnCreate(Bundle savedInstanceState) {
        initIntent();
        super.dealOnCreate(savedInstanceState);
    }

    private void initIntent() {
        Bundle bundle = getArguments();
        //获取数据适配器中通过bundle传递过来的数据
        seller = (SellerBean) bundle.getSerializable(Constant.KEY);
    }

    @Override
    protected void dealOnActivityCreated(Bundle savedInstanceState) {
        goodsTypeAdapter = new GoodsTypeAdapter(this, mContext);
        rvGoodsType.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rvGoodsType.setAdapter(goodsTypeAdapter);
        goodsAdapter = new GoodsAdapter(this, mContext);
        slhlv.setAdapter(goodsAdapter);
        slhlv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //firstVisibleItem listview列表的第一个条目的索引值
                //商品列表集合
                ArrayList<GoodsInfoBean> goodsInfoBeans = goodsAdapter.getData();
                //商品分类集合
                List<GoodsTypeInfoBean> goodsTypeInfoBeans = goodsTypeAdapter.getData();
                if (goodsInfoBeans != null && goodsTypeInfoBeans != null) {
                    //可以通过firstVisibleItem找到右侧列表第一个显示条目的对象,goodsInfoBeans--->typeid
                    int typeId = goodsInfoBeans.get(firstVisibleItem).getTypeId();
                    //找到目前选中的分类索引位置的对象的id和typeid比对,如果不一致,则在后续的循环中找到那个一致的条目为止
                    int id = goodsTypeInfoBeans.get(goodsTypeAdapter.currentPosition).getId();
                    if (typeId != id) {
                        //通过typeid找到商品分类中的id和typeId一致的那个条目,位置i
                        for (int i = 0; i < goodsTypeInfoBeans.size(); i++) {
                            GoodsTypeInfoBean goodsTypeInfoBean = goodsTypeInfoBeans.get(i);
                            if (goodsTypeInfoBean.getId() == typeId) {
                                LogUtils.doLog(TAG,"条目:"+goodsTypeInfoBean.getId()+"==="+typeId);
                                //让左侧商品分类选中i的这个条目
                                goodsTypeAdapter.currentPosition = i;
                                //刷新商品分类的数据适配器
                                goodsTypeAdapter.notifyDataSetChanged();
                                //因为i可能在屏幕之外,所以需要让左侧的商品分类滚动到i的位置,让用户可以看见
                                rvGoodsType.smoothScrollToPosition(i);
                                break;
                            }
                        }
                    }
                }
            }
        });
        //发送网络请求获取商品列表和分类数据
        GoodsFPresenter goodsFPresenter = new GoodsFPresenter(goodsTypeAdapter, goodsAdapter, seller);
        LogUtils.doLog(TAG, seller.getId() + "");
        goodsFPresenter.getGoodsData(seller.getId());
        super.dealOnActivityCreated(savedInstanceState);
    }

    @Override
    public void refreshGoodsAdapterUI(int id) {
        //通过此id在商品列表集合中遍历,找到id和typeid一致的那个条目,此条目记录为i
        ArrayList<GoodsInfoBean> goodsInfoBeans = goodsAdapter.getData();
        if (goodsInfoBeans != null) {
            for (int i = 0; i < goodsInfoBeans.size(); i++) {
                GoodsInfoBean goodsInfoBean = goodsInfoBeans.get(i);
                if (goodsInfoBean.getTypeId() == id) {
                    //让listview切换到i的位置显示
                    slhlv.setSelection(i);
                    break;
                }
            }
        }
    }
}
