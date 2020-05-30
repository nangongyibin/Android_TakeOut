package com.ngyb.takeout.net;

import com.ngyb.takeout.bean.net.ResponseInfoBean;

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
}
