package com.ngyb.takeout.bean.net;

import java.util.List;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/5/15 09:38
 */
public class HeadBean {
    private List<CategorieListBean> categorieList;
    private List<PromotionListBean> promotionList;

    public List<CategorieListBean> getCategorieList() {
        return categorieList;
    }

    public void setCategorieList(List<CategorieListBean> categorieList) {
        this.categorieList = categorieList;
    }

    public List<PromotionListBean> getPromotionList() {
        return promotionList;
    }

    public void setPromotionList(List<PromotionListBean> promotionList) {
        this.promotionList = promotionList;
    }
}
