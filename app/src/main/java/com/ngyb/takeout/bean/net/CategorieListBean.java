package com.ngyb.takeout.bean.net;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/5/15 09:40
 */
public class CategorieListBean {
    /**
     * id : 1
     * name : 美食
     * pic : /imgs/category/1.png
     */

    private int id;
    private String name;
    private String pic;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
