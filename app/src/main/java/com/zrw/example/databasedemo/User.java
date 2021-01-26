package com.zrw.example.databasedemo;

import com.zrw.example.databasedemo.annotation.DbField;
import com.zrw.example.databasedemo.annotation.DbTable;

/**
 * 作者：张人文
 * 时间：2021/1/26 22:51
 * 邮箱：479696877@QQ.COM
 * 描述：
 */
//得到User对应的表名

@DbTable("tb_user")
public class User {

    //得到user对象对应的列名  得到这些东西 最好的办法就是  注解
    @DbField("u_id")
    private Integer id;
    private String name;
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
