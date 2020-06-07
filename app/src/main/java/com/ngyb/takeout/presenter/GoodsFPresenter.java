package com.ngyb.takeout.presenter;

import com.google.gson.Gson;
import com.ngyb.takeout.adapter.GoodsAdapter;
import com.ngyb.takeout.adapter.GoodsTypeAdapter;
import com.ngyb.takeout.net.bean.BusinessInfoBean;
import com.ngyb.takeout.net.bean.GoodsInfoBean;
import com.ngyb.takeout.net.bean.GoodsTypeInfoBean;
import com.ngyb.takeout.net.bean.ResponseInfoBean;
import com.ngyb.takeout.net.bean.SellerBean;
import com.ngyb.takeout.contract.GoodsFContract;
import com.ngyb.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/6 10:30
 */
public class GoodsFPresenter extends BaseLocalPresenter<GoodsFContract.View> implements GoodsFContract.Presenter {
    private static final String TAG = "GoodsFPresenter";
    private GoodsTypeAdapter goodsTypeAdapter;
    private GoodsAdapter goodsAdapter;
    private SellerBean seller;
    private final Gson gson;
    private List<GoodsTypeInfoBean> goodsTypeInfoBeans;
    private ArrayList<GoodsInfoBean> allGoodsInfoList;

    public GoodsFPresenter(GoodsTypeAdapter goodsTypeAdapter, GoodsAdapter goodsAdapter, SellerBean seller) {
        this.goodsTypeAdapter = goodsTypeAdapter;
        this.goodsAdapter = goodsAdapter;
        this.seller = seller;
        gson = new Gson();
    }

    /**
     * @param id 触发请求商品数据方法
     */
    @Override
    public void getGoodsData(int id) {
        Call<ResponseInfoBean> goodsInfo = responseInfoApi.getGoodsInfo(id);
        goodsInfo.enqueue(new CallBackAdapter());
    }

    @Override
    protected void parseJson(String data) {
        LogUtils.doLog(TAG, data);
        BusinessInfoBean businessInfoBean = gson.fromJson(data, BusinessInfoBean.class);
        //1.填充分类的数据适配器列表
        //2.获取商品分类的数据集合
        goodsTypeInfoBeans = businessInfoBean.getList();
        goodsTypeAdapter.setData(goodsTypeInfoBeans);
        //将分布在每一个分类类中的List里面的对象进行合并,然后填充商品列表数据适配器
        initGoodsInfoList();
        //用所有商品所在的大的集合填充商品列表数据适配器
        goodsAdapter.setData(allGoodsInfoList);
    }

    private void initGoodsInfoList() {
        allGoodsInfoList = new ArrayList<>();
        for (int i = 0; i < goodsTypeInfoBeans.size(); i++) {
            //每一个商品分类的bean对象
            GoodsTypeInfoBean goodsTypeInfoBean = goodsTypeInfoBeans.get(i);
            //此分类下的所有商品集合
            List<GoodsInfoBean> goodsInfoBeans = goodsTypeInfoBean.getList();
            for (int j = 0; j < goodsInfoBeans.size(); j++) {
                //获取每一个商品对象
                GoodsInfoBean goodsInfoBean = goodsInfoBeans.get(j);
                //指定此商品分类id
                goodsInfoBean.setTypeId(goodsTypeInfoBean.getId());
                //指定此商品分类名称
                goodsInfoBean.setTypeName(goodsTypeInfoBean.getName());
                //指定此商品所属的商家
                goodsInfoBean.setSellerId(seller.getId());
                //商品数量
                //将所有的商品bean对象添加到一个大的集合中
                allGoodsInfoList.add(goodsInfoBean);
            }
        }
    }

    @Override
    protected void showErrorMessage(String message) {

    }
}
