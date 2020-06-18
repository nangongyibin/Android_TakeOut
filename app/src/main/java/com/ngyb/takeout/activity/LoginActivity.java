package com.ngyb.takeout.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ngyb.mvpbase.BaseMvpActivity;
import com.ngyb.takeout.R;
import com.ngyb.takeout.contract.LoginContract;
import com.ngyb.takeout.presenter.LoginPresenter;

import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/17 22:06
 */
public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements LoginContract.View, View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private ImageView ivUserBack;
    private TextView ivUserPasswordLogin;
    private EditText etUserPhone;
    private TextView tvUserCode;
    private EditText etUserPsd;
    private EditText etUserCode;
    private TextView login;
    private int time = 60;
    public static final int KEEP_TIME_DESC = 100;//时间递减状态码
    public static final int RESET_TIME = 101;//时间重置，从60s开始重新计算

    EventHandler eh = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            // TODO 此处不可直接处理UI线程，处理后续操作需传到主线程中操作
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            handler.sendMessage(msg);
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                if (msg.arg2 == SMSSDK.RESULT_COMPLETE) {
                    Log.e(TAG, "handleMessage: 短信下发成功");
                    tvUserCode.setEnabled(false);
                    new Thread() {
                        @Override
                        public void run() {
                            while (time > 0) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                handler.sendEmptyMessage(KEEP_TIME_DESC);
                            }
                            handler.sendEmptyMessage(RESET_TIME);
                            super.run();
                        }
                    }.start();

                } else {
                    Log.e(TAG, "handleMessage: 短信下发失败");
                }
            }
            if (msg.arg1 == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                if (msg.arg2 == SMSSDK.RESULT_COMPLETE) {
                    Log.e(TAG, "handleMessage: 短信验证成功");
                    login();
                } else {
                    Log.e(TAG, "handleMessage: 短信验证失败");
                }
            }
            switch (msg.what) {
                case KEEP_TIME_DESC:
                    //时间递减1s
                    time--;
                    tvUserCode.setText("稍后再发（" + time + ")");
                    break;
                case RESET_TIME:
                    //当前倒计时结束，则让控件在此可用
                    tvUserCode.setEnabled(true);
                    tvUserCode.setText("重新发送");
                    time = 60;
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private LoginPresenter loginPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {
        initView();
        initSms();
        initClass();
        initListener();
    }

    private void initClass() {
        loginPresenter = new LoginPresenter();
        loginPresenter.attachView(this);
    }

    private void initListener() {
        tvUserCode.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    private void initSms() {
        //注册一个事件回调监听，用于处理SMSSDK接口请求的结果
        SMSSDK.registerEventHandler(eh);
    }

    private void initView() {
        ivUserBack = findViewById(R.id.iv_user_back);
        ivUserPasswordLogin = findViewById(R.id.iv_user_password_login);
        etUserPhone = findViewById(R.id.et_user_phone);
        tvUserCode = findViewById(R.id.tv_user_code);
        etUserPsd = findViewById(R.id.et_user_psd);
        etUserCode = findViewById(R.id.et_user_code);
        login = findViewById(R.id.login);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_user_code:
                sendCode();
                break;
            case R.id.login:
                checkCode();
                break;
        }
    }

    private void checkCode() {
        String phone = etUserPhone.getText().toString().trim();
        String psd = etUserPsd.getText().toString().trim();
        String code = etUserCode.getText().toString().trim();
        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(psd) && !TextUtils.isEmpty(code)) {
            //短信验证码校验
            SMSSDK.submitVerificationCode("86", phone, code);
        }
    }

    private void login() {
        String phone = etUserPhone.getText().toString().trim();
        String psd = etUserPsd.getText().toString().trim();
        if (!TextUtils.isEmpty(phone)&& !TextUtils.isEmpty(psd)){
            loginPresenter.getLoginData(phone,psd,3);
        }
    }

    private void sendCode() {
        //判断是否有输入电话号码，电话号码合法性
        String phone = etUserPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            //给指定手机号下发短信验证码
            // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
            SMSSDK.getVerificationCode("86", phone, new OnSendMessageHandler() {
                @Override
                public boolean onSendMessage(String s, String s1) {
                    return false;
                }
            });
        } else {
            Toast.makeText(context, "手机号码为空", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void dealOnDestroy() {
        super.dealOnDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }
}
