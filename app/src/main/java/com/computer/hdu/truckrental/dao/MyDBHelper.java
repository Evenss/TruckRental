package com.computer.hdu.truckrental.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Even on 2017/2/1.
 * For creating tables
 */

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TruckRental.db";
    private static final int DATABASE_VERSION = 1;

    private static final String create_users = "create table if not exists users";
    private static final String users_attributes =
            "(user_id integer primary key autoincrement not null," +
            " user_phone char(11) not null," +
            " user_level tinyint not null)";

    public MyDBHelper(Context context){
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_users + users_attributes);
    }

    //更新数据库版本时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
