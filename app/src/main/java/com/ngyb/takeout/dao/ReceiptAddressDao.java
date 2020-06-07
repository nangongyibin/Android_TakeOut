package com.ngyb.takeout.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.ngyb.takeout.activity.ConfirmOrderActivity;
import com.ngyb.takeout.dao.bean.ReceiptAddressBean;
import com.ngyb.takeout.dao.db.DBHelper;

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
                ReceiptAddressBean receiptAddressBean = addressBeans.get(0);
                return receiptAddressBean;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
