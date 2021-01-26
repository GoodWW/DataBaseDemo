package com.zrw.example.databasedemo;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //如何自动创建数据库
        //如何自动创建数据库表
        //如何让用户在使用的时候非常方便
        //将user对象里面的 类名 属性 转换成 创建数据库表 的  SQL 语句
        //create table user(id integer,name text, password text); 这样就完成创建表名的的语句

    }

    public void insertObject(View view) {
    }
}