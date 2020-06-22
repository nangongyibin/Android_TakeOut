package com.ngyb.takeout.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ngyb.mvpbase.BaseMvpActivity;
import com.ngyb.takeout.R;
import com.ngyb.takeout.contract.MainContract;
import com.ngyb.takeout.fragment.HomeFragment;
import com.ngyb.takeout.fragment.MoreFragment;
import com.ngyb.takeout.fragment.OrderFragment;
import com.ngyb.takeout.fragment.UserFragment;
import com.ngyb.takeout.presenter.MainPresenter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseMvpActivity<MainPresenter> implements MainContract.View, View.OnClickListener {

    private FrameLayout mainFragmentContainer;
    private LinearLayout mainBottomSwitcherContainer;
    private List<Fragment> fragmentList;
    //是否需要检测后台定位权限，设置为true时，如果用户没有给予后台定位权限会弹窗提示
    private boolean needCheckBackLocation = false;
    //如果设置了target > 28，需要增加这个权限，否则不会弹出"始终允许"这个选择框
    private static String BACK_LOCATION_PERMISSION = "android.permission.ACCESS_BACKGROUND_LOCATION";
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            BACK_LOCATION_PERMISSION
    };
    private static final int PERMISSON_REQUESTCODE = 0;
    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        initView();
        initPremission();
        initFragment();
        initListener();
        initDefault();
    }

    private void initPremission() {
        if(Build.VERSION.SDK_INT > 28
                && getApplicationContext().getApplicationInfo().targetSdkVersion > 28) {
            needPermissions = new String[] {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    BACK_LOCATION_PERMISSION
            };
            needCheckBackLocation = true;
        }
//        ActivityCompat.requestPermissions(this, str, 0);
    }

    /**
     * 默认选中第0个条目
     */
    private void initDefault() {
        View viewFirst = mainBottomSwitcherContainer.getChildAt(0);
        onClick(viewFirst);
    }

    private void initListener() {
        int childCount = mainBottomSwitcherContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAtView = mainBottomSwitcherContainer.getChildAt(i);
            childAtView.setOnClickListener(this);
        }
    }

    private void initFragment() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new OrderFragment());
        fragmentList.add(new UserFragment());
        fragmentList.add(new MoreFragment());
    }

    private void initView() {
        mainFragmentContainer = findViewById(R.id.main_fragment_container);
        mainBottomSwitcherContainer = findViewById(R.id.main_bottom_switcher_container);
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
        //v底部现行布局的索引位置
        int indexOfChild = mainBottomSwitcherContainer.indexOfChild(v);
        //点击哪个帧布局，这个帧布局中的图片就需要变为蓝色
        changeUI(indexOfChild);
        //顶部的Fragment页面内容需要切换
        changeFragment(indexOfChild);
    }

    private void changeFragment(int indexOfChild) {
        //需要切换到哪个fragment显示
        Fragment fragment = fragmentList.get(indexOfChild);
        //替换帧布局内部内容
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, fragment).commit();
    }

    private void changeUI(int indexOfChild) {
        //蓝色-->不可用
        int childCount = mainBottomSwitcherContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childFrameLayout = mainBottomSwitcherContainer.getChildAt(i);
            //循环遍历到了选中的索引位置
            if (i == indexOfChild) {
                //点钟的帧布局，内部的图片和文字都需要变蓝色
                setEnable(childFrameLayout, false);
            } else {
                //未点中的帧布局，内部的图片和文字都需要变黑色
                setEnable(childFrameLayout, true);
            }
        }
    }

    /**
     * @param childFrameLayout 帧布局
     * @param b                是否可用
     *                         让帧布局内部的控件和自身都是（可用）不可用的状态
     */
    private void setEnable(View childFrameLayout, boolean b) {
        childFrameLayout.setEnabled(b);
        if (childFrameLayout instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) childFrameLayout).getChildCount(); i++) {
                View childAt = ((ViewGroup) childFrameLayout).getChildAt(i);
                childAt.setEnabled(b);
            }
        }
    }

    /*************************************** 权限检查******************************************************/
    @Override
    protected void dealOnResume() {
        try{
            super.dealOnResume();
            if (Build.VERSION.SDK_INT >= 23) {
                if (isNeedCheck) {
                    checkPermissions(needPermissions);
                }
            }
        }catch(Throwable e){
            e.printStackTrace();
        }
    }

    /**
     * @param
     * @since 2.5.0
     */
    @TargetApi(23)
    private void checkPermissions(String... permissions) {
        try{
            if (Build.VERSION.SDK_INT >= 23 && getApplicationInfo().targetSdkVersion >= 23) {
                List<String> needRequestPermissonList = findDeniedPermissions(permissions);
                if (null != needRequestPermissonList
                        && needRequestPermissonList.size() > 0) {
                    try {
                        String[] array = needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]);
                        Method method = getClass().getMethod("requestPermissions", new Class[]{String[].class, int.class});
                        method.invoke(this, array, 0);
                    } catch (Throwable e) {

                    }
                }
            }

        }catch(Throwable e){
            e.printStackTrace();
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    @TargetApi(23)
    private List<String> findDeniedPermissions(String[] permissions) {
        try{
            List<String> needRequestPermissonList = new ArrayList<String>();
            if (Build.VERSION.SDK_INT >= 23 && getApplicationInfo().targetSdkVersion >= 23) {
                for (String perm : permissions) {
                    if (checkMySelfPermission(perm) != PackageManager.PERMISSION_GRANTED
                            || shouldShowMyRequestPermissionRationale(perm)) {
                        if(!needCheckBackLocation
                                && BACK_LOCATION_PERMISSION.equals(perm)) {
                            continue;
                        }
                        needRequestPermissonList.add(perm);
                    }
                }
            }
            return needRequestPermissonList;
        }catch(Throwable e){
            e.printStackTrace();
        }
        return null;
    }

    private int checkMySelfPermission(String perm) {
        try {
            Method method = getClass().getMethod("checkSelfPermission", new Class[]{String.class});
            Integer permissionInt = (Integer) method.invoke(this, perm);
            return permissionInt;
        } catch (Throwable e) {
        }
        return -1;
    }

    private boolean shouldShowMyRequestPermissionRationale(String perm) {
        try {
            Method method = getClass().getMethod("shouldShowRequestPermissionRationale", new Class[]{String.class});
            Boolean permissionInt = (Boolean) method.invoke(this, perm);
            return permissionInt;
        } catch (Throwable e) {
        }
        return false;
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        try{
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }catch(Throwable e){
            e.printStackTrace();
        }
        return true;
    }

    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        try{
            if (Build.VERSION.SDK_INT >= 23) {
                if (requestCode == PERMISSON_REQUESTCODE) {
                    if (!verifyPermissions(paramArrayOfInt)) {
                        showMissingPermissionDialog();
                        isNeedCheck = false;
                    }
                }
            }
        }catch(Throwable e){
            e.printStackTrace();
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("当前应用缺少必要权限。\\n\\n请点击\\\"设置\\\"-\\\"权限\\\"-打开所需权限");
            // 拒绝, 退出应用
            builder.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try{
                                finish();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    });
            builder.setPositiveButton("设置",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                startAppSettings();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    });
            builder.setCancelable(false);

            builder.show();
        }catch(Throwable e){
            e.printStackTrace();
        }
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        try{
            Intent intent = new Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
