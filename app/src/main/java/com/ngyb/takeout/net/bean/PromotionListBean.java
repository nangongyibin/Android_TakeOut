package com.ngyb.takeout.net.bean;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/5/15 09:40
 */
public class PromotionListBean {
    /**
     * id : 1
     * info : promotion info...
     * pic : /imgs/promotion/1.jpg
     */

    private int id;
    private String info;
    private String pic;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
