package com.ngyb.takeout.bean.net;

import java.util.List;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/6 12:24
 */
public class GoodsTypeInfoBean {
    /**
     * id : 101
     * info : (不与其它活动同享)13.9元特价套餐!!|13.9特价套餐!!(每单仅限2份)
     * list : [{"bargainPrice":true,"form":"肉末烧汁茄子+千叶豆腐+小食+时蔬+含粗粮米饭)","icon":"/imgs/goods/caiping_taocan.webp","id":1001,"monthSaleNum":53,"name":"肉末烧汁茄子+千叶豆腐套餐(含粗粮米饭)","new":false,"newPrice":13.9,"oldPrice":30},{"bargainPrice":true,"form":"肉末烧汁茄子+榄菜肉末四季豆+小食+时蔬+含粗粮米饭)","icon":"/imgs/goods/caiping_taocan.webp","id":1002,"monthSaleNum":37,"name":"肉末烧汁茄子+四季豆套餐(含粗粮米饭)","new":false,"newPrice":13.9,"oldPrice":30},{"bargainPrice":true,"form":"手撕杏鲍菇+千叶豆腐+小食+时蔬+含粗粮米饭)","icon":"/imgs/goods/caiping_taocan.webp","id":1003,"monthSaleNum":27,"name":"手撕杏鲍菇+千叶豆腐套餐(含粗粮米饭)","new":false,"newPrice":13.9,"oldPrice":30},{"bargainPrice":true,"form":"肉末烧汁茄子+杏鲍菇+小食+时蔬+含粗粮米饭)","icon":"/imgs/goods/caiping_taocan.webp","id":1004,"monthSaleNum":24,"name":"肉末烧汁茄子+杏鲍菇套餐(含粗粮米饭)","new":false,"newPrice":13.9,"oldPrice":30},{"bargainPrice":true,"form":"榄菜肉末四季豆+千叶豆腐+小食+时蔬+含粗粮米饭)","icon":"/imgs/goods/caiping_taocan.webp","id":1005,"monthSaleNum":53,"name":"榄菜肉末四季豆+千叶豆腐套餐(含粗粮米饭)","new":false,"newPrice":13.9,"oldPrice":30},{"bargainPrice":true,"form":"榄菜肉末四季豆+手撕杏鲍菇+小食+时蔬+含粗粮米饭)","icon":"/imgs/goods/caiping_taocan.webp","id":1006,"monthSaleNum":53,"name":"榄菜肉末四季豆+手撕杏鲍菇套餐(含粗粮米饭)","new":false,"newPrice":13.9,"oldPrice":30}]
     * name : 13.9特价套餐
     */

    private int id;//商品分类id
    private int count;//选择此类商品的总数量
    private String info;//商品分类详细信息
    private String name;//商品分类名称
    private List<GoodsInfoBean> list;//商品信息的集合

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GoodsInfoBean> getList() {
        return list;
    }

    public void setList(List<GoodsInfoBean> list) {
        this.list = list;
    }
}
