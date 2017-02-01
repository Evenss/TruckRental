package com.computer.hdu.truckrental.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Even on 2017/2/1.
 */

public class UserDao {
    private MyDBHelper myDBHelper;
    private String tag = "UserDao.class";

    //init
    public UserDao(Context context){
        myDBHelper = new MyDBHelper(context);
    }

    //add
    public void addUser(String user_phone,int user_level){
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()){
            database.execSQL("insert into users (user_phone,user_level) values(?,?)",
                    new Object[]{user_phone,user_level});
            database.close();
        }
    }

    //find

    //find all

    //delete

    //update
}
