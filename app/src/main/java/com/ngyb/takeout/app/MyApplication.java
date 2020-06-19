package com.ngyb.takeout.app;

import android.app.Application;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.ngyb.takeout.dao.bean.UserInfoBean;
import com.ngyb.takeout.dao.db.DBHelper;

import java.sql.SQLException;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/5/17 15:52
 */
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    public static int statusBarHeight = 0;
    public static int userId = -1;//目前没有用户登录的

    @Override
    public void onCreate() {
        super.onCreate();
        //获取status_bar_height资源得ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            Log.e(TAG, "onCreate: " + statusBarHeight);
        }
        // 主要是添加下面这句代码
        MultiDex.install(this);
        try {
            //当前手机是否有登录用户,如果有登录用户,则需要将userid重新赋值
            //找到一个isLogin字段,如果此字段中有为1的情况,则有登录用户
            DBHelper dbHelper = new DBHelper(this);
            Dao<UserInfoBean,Integer> dao = dbHelper.getDao(UserInfoBean.class);
            List<UserInfoBean> userInfoBeans = dao.queryBuilder().where().eq("isLogin", 1).query();
            if (userInfoBeans!=null && userInfoBeans.size()>0){
                UserInfoBean userInfoBean = userInfoBeans.get(0);
                userId = userInfoBean.get_id();
                Log.e(TAG, "onCreate: "+userId );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
