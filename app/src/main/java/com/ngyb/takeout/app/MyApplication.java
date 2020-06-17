package com.ngyb.takeout.app;

import android.app.Application;
import android.support.multidex.MultiDex;
import android.util.Log;

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
    }
}
