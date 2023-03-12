package com.example.learnandroid.sqlite;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.Nullable;
/**
 * @Auther jian xian si qi
 * @Date 2023/3/12 9:13
 */
public class SqliteUtils extends SQLiteOpenHelper {
    // 单例模式
    private static SqliteUtils mInstance;

    // 提供对外提供的函数。
    public static synchronized SqliteUtils getInstance(Context context){
        if (mInstance == null){
            mInstance = new SqliteUtils(context,"itholmes.db",null,1);
        }
        return mInstance;
    }

    /**
     * 通过构造函数，将数据库名称 ， 数据库版本号等定义出来。因此，必须要声明一个构造函数。
     * @param context context就是环境，在此环境下进行创建数据库。
     * @param name 数据库名称
     * @param factory 工厂
     * @param version 版本 数据库升级后需要不断加1操作！
     */
    public SqliteUtils(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 数据库初始化用的:
     *      创建表 表数据初始化 数据库第一次创建的时候调用。第二次发现有该表了就不会重复创建了！
     *      此函数只会执行一次。 因此，添加了新的数据库或者修改了数据库，必须要对应项目目录将databases目录下的db等文件删除。
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 创建表：person表。

        // 主键： _id(标准) 或者 id(不标准) 必须唯一，也可以添加自动增长 。
        String sql = "create table persons(" +
                "_id integer primary key autoincrement," +
                "_date" +
                "name text" +
                ")";

        // 执行sql
        sqLiteDatabase.execSQL(sql);
    }

    // 数据库升级用的
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
