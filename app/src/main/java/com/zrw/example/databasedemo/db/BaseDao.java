package com.zrw.example.databasedemo.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * 作者：张人文
 * 时间：2021/1/26 23:22
 * 邮箱：479696877@QQ.COM
 * 描述：
 */
public class BaseDao implements IBaseDao {
    //持有数据库操作的引用
    private SQLiteDatabase sqLiteDatabase;

    @Override
    public long insert(Object entity) {
        return 0;
    }
}
