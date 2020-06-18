package com.ngyb.takeout.presenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.dao.Dao;
import com.ngyb.mvpbase.BasePresenter;
import com.ngyb.takeout.activity.LoginActivity;
import com.ngyb.takeout.app.MyApplication;
import com.ngyb.takeout.contract.LoginContract;
import com.ngyb.takeout.dao.bean.UserInfoBean;
import com.ngyb.takeout.dao.db.DBHelper;
import com.ngyb.takeout.net.bean.ResponseInfoBean;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;

import retrofit2.Call;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/17 22:07
 */
public class LoginPresenter extends BaseLocalPresenter<LoginContract.View> implements LoginContract.Presenter {
    private Context context;

    public LoginPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void getLoginData(String phone, String psd, int i) {
        Call<ResponseInfoBean> loginInfo = responseInfoApi.getLoginInfo(phone, psd, phone, 3);
        loginInfo.enqueue(new CallBackAdapter());
    }

    @Override
    protected void parseJson(String data) {
        Gson gson = new Gson();
        //当前登录用户的信息在userinfobean中
        UserInfoBean userInfoBean = gson.fromJson(data, UserInfoBean.class);
        MyApplication.userId = userInfoBean.get_id();
        //获取dao对象
        DBHelper dbHelper = new DBHelper(context);
        Dao<UserInfoBean, Integer> dao = dbHelper.getDao(UserInfoBean.class);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        AndroidDatabaseConnection connection = new AndroidDatabaseConnection(db, true);
        try {
            //将数据库表中的所有的islogin字段设置为0
            //设置事务回滚点
            Savepoint savePoint = connection.setSavePoint("START");
            //让ormlite不要自己管理实务，有我们自己的代码管理事务
            connection.setAutoCommit(false);
            List<UserInfoBean> userInfoBeans = dao.queryForAll();
            for (UserInfoBean infoBean : userInfoBeans) {
                infoBean.setIsLogin(0);
                //数据有没有更新到数据库中
                dao.update(infoBean);
            }
            UserInfoBean bean = dao.queryForId(userInfoBean.get_id());
            if (bean != null) {
                //是老用户，则将老用户已有的数据 0修改为1即可
                bean.setIsLogin(1);
                dao.update(bean);
            } else {
                //如果现在登录的是新用户，新用户在数据库中没有记录数据，则需要插入一条isLogin为1的数据
                userInfoBean.setIsLogin(1);
                dao.create(userInfoBean);
            }
            connection.commit(savePoint);
            //登录完成后，用户徐亚看到部分登录信息，将当前的登录页面finish
            ((LoginActivity) context).finish();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void showErrorMessage(String message) {

    }
}
