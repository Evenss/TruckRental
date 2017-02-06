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

    private static final String create_users = "create table if not exists users" +
            "(user_id integer primary key autoincrement not null," +
            " user_phone string not null unique," +
            " user_level integer not null)";
    private static final String create_drivers = "create table if not exists drivers" +
            "(driver_id integer primary key autoincrement not null," +
            " driver_name string not null," +
            " driver_phone string not null unique," +
            " driver_pwd string not null," +
            " driver_car_type integer not null," +
            " driver_city string not null," +
            " driver_license_plate string not null unique," +
            " driver_license string not null unique," +
            " driver_level integer not null," +
            " driver_score integer not null," +
            " driver_state integer not null)";
    private static final String create_orders = "create table if not exists orders" +
            "(order_id integer primary key autoincrement not null," +
            " order_number string not null unique," +
            " fk_user_id integer not null," +
            " fk_driver_id integer not null," +
            " order_departure string not null," +
            " order_destination string not null," +
            " order_remarks string," +
            " order_distance float not null," +
            " order_price float not null," +
            " order_state integer not null," +
            " order_score integer," +
            " order_date datetime not null," +
            " order_back integer not null," +
            " order_carry integer not null," +
            " order_followers integer not null," +
            " order_car_type integer not null," +
            " order_start_date datetime not null," +
            " foreign key(fk_user_id) references users(user_id)," +
            " foreign key(fk_driver_id) references drivers(diver_id))";

    public MyDBHelper(Context context){
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_users);
        db.execSQL(create_drivers);
        db.execSQL(create_orders);
    }

    //更新数据库版本时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists users");
        db.execSQL("drop table if exists drivers");
        db.execSQL("drop table if exists orders");
        onCreate(db);
    }
}
