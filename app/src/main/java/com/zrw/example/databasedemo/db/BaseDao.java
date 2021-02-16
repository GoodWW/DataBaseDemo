package com.zrw.example.databasedemo.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zrw.example.databasedemo.annotation.DbField;
import com.zrw.example.databasedemo.annotation.DbTable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * 作者：张人文
 * 时间：2021/1/26 23:22
 * 邮箱：479696877@QQ.COM
 * 描述：
 */
public class BaseDao<T> implements IBaseDao<T> {

    //标识 用来标识是否已经初始化
    private boolean isInit = false;
    //定义一个缓存空间 （key  字段名   value  成员变量）
    private HashMap<String, Field> catchMap;


    //持有数据库操作的引用
    private SQLiteDatabase sqLiteDatabase;
    //需要一个表名
    private String tableName;
    //操作数据库多对应的java类型
    private Class<T> entityClass;

    //有了   //持有数据库操作的引用
    // private SQLiteDatabase sqLiteDatabas
    // //需要一个表名
    // private String tableName;
    // //操作数据库多对应的java类型
    // private Class<T> entityClass;
    //这些参数后就可以进行对数据库的创建  不应爱暴怒给外界
    //这个方法完成基础的表创建  就可以进行数据的插入操作了
    protected boolean init(SQLiteDatabase sqLiteDatabase, Class<T> entityClass) {
        this.sqLiteDatabase = sqLiteDatabase;
        this.entityClass = entityClass;

        if (!isInit) {
            //根据传入的class进行数据表创建  本例子中  对应的是  user对象
            //第一步  就是得到表名   根据  DbTable  和 DbField  的注解  得到  表名和列名
            DbTable dt = entityClass.getAnnotation(DbTable.class);//查找传进来的 对象里面有没有 DBTable 的 注解
            if (null != dt && !"".equals(dt.value())) {//得到表名  相当于  user对象中的 tb_user
                tableName = dt.value();//有注解
            } else {
                tableName = entityClass.getName();//没有注解
            }

            if (!sqLiteDatabase.isOpen()) {//如果 sqLiteDatabase 不是打开的   就返回
                return false;
            }

            //第二步  得到表名以后  就来拼接  创建数据表的 SQL语句  封装成一个方法
            String createTable = getCreateTableSql();
            sqLiteDatabase.execSQL(createTable);//执行语句 将 对象转化成  数据库表
            catchMap = new HashMap<>();//执行完SQL语句以后就对缓存 hasmap   进行初始化
            initCacheMap();
            isInit = true;
        }
        return isInit;
    }

    private void initCacheMap() {
        //取得所有 的 列名  意思是从第一条数据开始 取 0 条数据   就是取出 数据表 的 表结构
        String sql = "select * from " + tableName + " limit 1,0";

        //通过游标的方式对数据进行获取
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        //所有的列名  就  等于
        String[] columnNames = cursor.getColumnNames();
        //所有的成员变量  就  等于
        Field[] columnFields = entityClass.getDeclaredFields();

        //然后将  列名 和  成员变量    进行  遍历   存到  catchMap  缓存空间中
        //在循环之前 先把 字段访问权限 打开
        for (Field field : columnFields) {
            field.setAccessible(true);
        }

        for (String columnName : columnNames) {
            Field columnField = null;
            for (Field field : columnFields) {
                String fieldName = null;
                if (null != field.getAnnotation(DbField.class) && !"".equals(Objects.requireNonNull(field.getAnnotation(DbField.class)).value())) {
                    fieldName = Objects.requireNonNull(field.getAnnotation(DbField.class)).value();
                } else {
                    fieldName = field.getName();
                }
                if (columnName.equals(fieldName)) {
                    columnField = field;
                    break;
                }
            }
            if (columnField != null) {
                catchMap.put(columnName, columnField);
            }
        }

    }

    //将 entityClass 中对应的字段封装成 SQL 语句
    private String getCreateTableSql() {
        StringBuffer stringBuffer = new StringBuffer();
        //拼接前面固有的  和表名
        stringBuffer.append("create table if not exists ");
        stringBuffer.append(tableName + "(");

        //拼接列名和类型  通过反射得到所有的成员变量
        Field[] fields = entityClass.getDeclaredFields();

        //对成员变量进行遍历得到每个成员变量的类型
        for (Field field : fields) {
            Class type = field.getType();//得到成员变量的类型

            //这个注解同样有两种情况  一种有注解   一种么有注解
            DbField dbField = field.getAnnotation(DbField.class);
            if (null != dbField && !"".equals(dbField.value())) {//有注解  就用注解的名字作为列名
                if (type == String.class) {
                    stringBuffer.append(dbField.value() + "TEXT,");
                } else if (type == Integer.class) {
                    stringBuffer.append(dbField.value() + "INTEGER,");
                } else if (type == Long.class) {
                    stringBuffer.append(dbField.value() + "BIGINT,");
                } else if (type == Double.class) {
                    stringBuffer.append(dbField.value() + "DOUBLE,");
                } else if (type == byte[].class) {
                    stringBuffer.append(dbField.value() + "BLOB,");
                } else {
                    //不支持的类型
                    continue;
                }
            } else {//没有注解就用  成员名字作为列名
                if (type == String.class) {
                    stringBuffer.append(field.getName() + "TEXT,");
                } else if (type == Integer.class) {
                    stringBuffer.append(field.getName() + "INTEGER,");
                } else if (type == Long.class) {
                    stringBuffer.append(field.getName() + "BIGINT,");
                } else if (type == Double.class) {
                    stringBuffer.append(field.getName() + "DOUBLE,");
                } else if (type == byte[].class) {
                    stringBuffer.append(field.getName() + "BLOB,");
                } else {
                    //不支持的类型
                    continue;
                }
            }
        }
        if (stringBuffer.charAt(stringBuffer.length() - 1) == ',') {//删除最后的 ，号
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }

        stringBuffer.append(")");//加上最后 的 括号

        return stringBuffer.toString();
    }

    @Override
    public long insert(T entity) {
        //将传过来的 entity 对象 转换成  contentvalues

        Map<String, String> map = getValue(entity);

        //写一个方法得到 ContentValues 对象
        ContentValues values = getContentValues(map);

        //执行插入语句需要这样的语法
        sqLiteDatabase.insert(tableName, null, values);
        return 0;
    }

    private Map<String, String> getValue(T entity) {
        HashMap<String, String> map = new HashMap<>();
        //得到所有的 entity 成员变量
        Iterator<Field> fieldIterator = catchMap.values().iterator();
        return null;
    }
}
