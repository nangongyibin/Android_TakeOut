package com.ngyb.takeout.presenter;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ngyb.mvpbase.BasePresenter;
import com.ngyb.takeout.activity.PayOnlineActivity;
import com.ngyb.takeout.constant.Constant;
import com.ngyb.takeout.contract.PayOnlineContract;
import com.ngyb.takeout.utils.OrderInfoUtil2_0;
import com.ngyb.takeout.utils.PayResult;
import com.ngyb.takeout.utils.SignUtils;

import java.util.Map;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/13 20:20
 */
public class PayOnlinePresenter extends BasePresenter<PayOnlineContract.View> implements PayOnlineContract.Presenter {
    private static final int SDK_PAY_FLAG = 100;
    private PayOnlineActivity payOnlineActivity;

    public PayOnlinePresenter(PayOnlineActivity payOnlineActivity) {
        this.payOnlineActivity = payOnlineActivity;
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /*
                    对于支付结果，请商户依赖服务端的异步通知结果，同步通知结果，仅作为支付结果的通知
                     */
                    String resultInfo = payResult.getResult();//同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9999")) {
                        //该笔订单是否真实支付成功，需要依赖服务端的异步通知
                        Toast.makeText(payOnlineActivity, "支付成功", Toast.LENGTH_SHORT).show();
                        /*
                        给公司的服务器发送请求，告知服务器客户端是支付成功了，服务器你也改一下支付状态吧
                        好的，我的状态改过了，你给用户显示这个支付成功的结果吧。
                         */
                    } else {
                        //该笔订单真实的支付结果，需要依赖服务端的异步通知
                        Toast.makeText(payOnlineActivity, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 支付宝支付业务
     */
    @Override
    public void pay() {
        /*
        这里只是为了方便直接向商户展示支付宝的整个支付流程；所以demo中加签过程直接放在客户端完成；
        真实APP里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
        防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险。
        orderInfo的获取必须来自服务端
         */
        //维护需要上传的参数键值对
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(Constant.APPID, true);
        //将键值对转换成key=value&key=value型式
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        //生成签名过程
        String sign = OrderInfoUtil2_0.getSign(params, Constant.RSA2_PRIVATE, true);
        //订单加签过程
        final String orderInfo = orderParam + "&" + sign;
        //客户端代码从此开始
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                //子线程中执行的代码
                //创建一个支付的任务
                PayTask alipay = new PayTask(payOnlineActivity);
                //触发一个支付的请求网络
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message message = handler.obtainMessage();
                message.what = SDK_PAY_FLAG;
                message.obj = result;
                handler.sendMessage(message);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
