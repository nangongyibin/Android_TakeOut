package com.ngyb.takeout.net;

import com.ngyb.takeout.net.bean.ResponseInfoBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/5/10 17:00
 */
public interface ResponseInfoApi {
    @GET("HomeServlet/home")
    Call<ResponseInfoBean> getHomeInfo();

    @GET("BusinessServlet/business")
    Call<ResponseInfoBean> getGoodsInfo(@Query("sellerId") long sellerId);

    @GET("OrderServlet/order")
    Call<ResponseInfoBean> getOrderInfo(@Query("userId") int userId);

    @GET("UserLoginServlet/login")
    Call<ResponseInfoBean> getLoginInfo(@Query("username") String username, @Query("password") String password, @Query("phone") String phone, @Query("type") int type);
}
