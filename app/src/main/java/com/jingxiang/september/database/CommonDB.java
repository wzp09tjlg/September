package com.jingxiang.september.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jingxiang.september.util.LogUtil;

/**
 * Created by wu on 2016/10/8.
 * 通用的数据库工具类
 * 1.SQL的关键字使用大些字母来实现,表属性使用小写字母来实现
 * 2.在创建表时,一定记得在创建表字段时注明 含义及创建的时间和版本,方便以后对数据库的维护
 */
public class CommonDB extends SQLiteOpenHelper {
    /** Data */
    private Context mContext;

    /** FinalData */
    private static final String  DB_NAME = "CommonDB.db";//创建数据库的名字
    public static int  VERSION = 3;

    public static final String TABLE_NEW = "channel_news";
    public static final String TABLE_VIDEO = "channel_video";

    private final String CREATE_TABLE_NEW = "create table if not exists " + TABLE_NEW +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " id INTEGER, " +
            " name TEXT, " +
            " orderId INTEGER, " +
            " selected INTEGER)";

    private final String CREATE_TABLE_VIDEO = "create table if not exists " + TABLE_VIDEO +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " id INTEGER, " +
            " name TEXT, " +
            " orderId INTEGER, " +
            " selected INTEGER)";
    /**************************************/
    public CommonDB(Context context){
        super(context, DB_NAME, null, VERSION);
        this.mContext = context;
        LogUtil.i("CommonDB construct");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//第一次获取数据对象实例的时候执行onCreate方法
        LogUtil.i("CommonDB onCreate");
        db.execSQL(CREATE_TABLE_NEW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//在数据库升级过程中，第一次获取数据库实例的时候执行onUpdate方法
        LogUtil.i("CommonDB onUpdate");
        switch (oldVersion){
            case 1: //在升级高版本的时候，由低版本数据库号1到高版本数据库号N时 执行操作
                db.execSQL(CREATE_TABLE_VIDEO);
                break;
            case 2:
                LogUtil.i("CommonDB onUpdate");
                break;
        }
    }
}
