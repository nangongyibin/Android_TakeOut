package com.ngyb.takeout.presenter;

import android.util.Log;

import com.ngyb.mvpbase.BasePresenter;
import com.ngyb.mvpbase.BaseView;
import com.ngyb.takeout.net.bean.ResponseInfoBean;
import com.ngyb.takeout.constant.Constant;
import com.ngyb.takeout.net.ResponseInfoApi;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/5/17 16:54
 */
public abstract class BaseLocalPresenter<T extends BaseView> extends BasePresenter<T> {
    public ResponseInfoApi responseInfoApi;
    private HashMap<String, String> errorMap = new HashMap<>();
    private static final String TAG = "BaseLocalPresenter";

    public BaseLocalPresenter() {
        //简历一个错误状态码错误内容的索引表
        errorMap.put("1", "数据没有更新");
        errorMap.put("2", "请求链接地址无效");
        errorMap.put("3", "请求参数异常");
        //创建retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                //约定需要解析的数据格式
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constant.BASEURL)
                .build();
        responseInfoApi = retrofit.create(ResponseInfoApi.class);
    }

    public class CallBackAdapter implements Callback<ResponseInfoBean> {

        @Override
        public void onResponse(Call<ResponseInfoBean> call, Response<ResponseInfoBean> response) {
            Log.e(TAG, "onResponse: ");
            ResponseInfoBean responseInfo = response.body();
            if (responseInfo != null) {
                String code = responseInfo.getCode();
                if (code != null || !code.equals("")) {
                    if (code.equals("0")) {
                        //有意义，则需要进行解析展示
                        parseJson(responseInfo.getData());
                    } else {
                        String errorMessage = errorMap.get(code);
                        //处理错误
                        onFailure(call, new RuntimeException(errorMessage));
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseInfoBean> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t.getLocalizedMessage().toString());
            if (t instanceof RuntimeException) {
                //请求服务器成功，状态码不为0的问题
                showErrorMessage(t.getMessage());
            }
            showErrorMessage("服务器忙，请稍后重试");
        }
    }

    /**
     * @param data 需要解析的json,此json因为结构不同，所以交由子类实现
     */
    protected abstract void parseJson(String data);

    /**
     * @param message 具体错误的原因，此方法交由子类处理
     */
    protected abstract void showErrorMessage(String message);
}

