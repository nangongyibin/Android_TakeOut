package com.ngyb.takeout.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.ngyb.takeout.activity.ConfirmOrderActivity;
import com.ngyb.takeout.app.MyApplication;
import com.ngyb.takeout.dao.bean.ReceiptAddressBean;
import com.ngyb.takeout.dao.db.DBHelper;
import com.ngyb.utils.LogUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/7 15:22
 */
public class ReceiptAddressDao {
    private DBHelper dbHelper;
    private Dao<ReceiptAddressBean, Integer> dao;
    private static final String TAG = "ReceiptAddressDao";

    public ReceiptAddressDao(Context context) {
        dbHelper = new DBHelper(context);
        dao = dbHelper.getDao(ReceiptAddressBean.class);
    }

    /**
     * @param uid
     * @return 查询当前用户登录的默认送货地址
     */
    public ReceiptAddressBean queryUserSelectAddress(int uid) {
        try {
            List<ReceiptAddressBean> addressBeans = dao.queryBuilder().where().eq("uid", uid).and().eq("isSelect", 1).query();
            if (addressBeans != null && addressBeans.size() > 0) {
                return addressBeans.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param userId
     * @return 查询当前登录用户的所有地址
     */
    public List<ReceiptAddressBean> queryUserAddress(int userId) {
        try {
            if (userId ==-1){
                userId =0;
            }
            return dao.queryBuilder().where().eq("uid", userId).query();
        } catch (SQLException e) {
            Log.e(TAG, "queryUserAddress: "+e.getLocalizedMessage().toString() );
            e.printStackTrace();
        }
        return null;
    }

    public void update(ReceiptAddressBean receiptAddressBean) {
        try {
            dao.update(receiptAddressBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void create(ReceiptAddressBean receiptAddressBean) {
        try {
            int i = dao.create(receiptAddressBean);
            LogUtils.doLog(TAG,receiptAddressBean);
            Log.e(TAG, "create: "+i +"===="+ MyApplication.userId);
        } catch (SQLException e) {
            Log.e(TAG, "create: "+e.getLocalizedMessage() );
            e.printStackTrace();
        }
    }

    public void delete(ReceiptAddressBean receiptAddressBean) {
        try {
            dao.delete(receiptAddressBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
