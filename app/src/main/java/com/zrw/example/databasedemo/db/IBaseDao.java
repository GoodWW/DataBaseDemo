package com.zrw.example.databasedemo.db;

/**
 * 作者：张人文
 * 时间：2021/1/26 23:18
 * 邮箱：479696877@QQ.COM
 * 描述：
 */
public interface IBaseDao<T> {

    long insert(T entity);
}
