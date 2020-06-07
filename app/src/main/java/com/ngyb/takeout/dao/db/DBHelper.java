package com.ngyb.takeout.dao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.ngyb.takeout.dao.bean.ReceiptAddressBean;
import com.ngyb.takeout.dao.bean.UserInfoBean;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2020/6/7 15:30
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {
    //hashmap用户存储多个dao对象
    private HashMap<String, Dao> daoHashMap = new HashMap<>();

    public DBHelper(Context context) {
        super(context, "takeout.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            //userinfo--->t_user表
            TableUtils.createTable(connectionSource, UserInfoBean.class);
            TableUtils.createTable(connectionSource, ReceiptAddressBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    /**
     * dao可以对表进行crud,一个dao只能操作一张表中的数据
     *
     * @param clazz 类字节码文件
     * @return dao对象
     */
    public Dao getDao(Class clazz) {
        Dao dao = daoHashMap.get(clazz.getSimpleName());
        if (dao == null) {
            try {
                dao = super.getDao(clazz);
                daoHashMap.put(clazz.getSimpleName(), dao);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dao;
    }

    @Override
    public void close() {
        //清空daoHashMap
        for (String key : daoHashMap.keySet()) {
            Dao dao = daoHashMap.get(key);
            dao = null;
        }
        super.close();
    }
}
