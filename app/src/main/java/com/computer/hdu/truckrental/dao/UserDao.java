package com.computer.hdu.truckrental.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.computer.hdu.truckrental.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Even on 2017/2/1.
 */

public class UserDao {
    private MyDBHelper myDBHelper;
    private String tag = "UserDao.class";

    //init
    public UserDao(Context context){
        myDBHelper = new MyDBHelper(context);
/*        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        myDBHelper.onUpgrade(database,3,4);*/
    }

    //add
    public boolean addUser(User user){
        //电话号码正则检验
        if(false){
            return false;
        }
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()){
            database.execSQL("insert into users (user_phone,user_level) values(?,?)",
                    new Object[]{user.getUser_phone(),user.getUser_level()});
            database.close();
        }
        return true;
    }

    //find
    public User findUserById(Integer user_id){
        User user;
        user = null;
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()){
            Cursor cursor = database.rawQuery("select * from users where user_id=?",
                  new String[]{user_id.toString()});
            if(cursor.moveToFirst()){
                String phone = cursor.getString(cursor.getColumnIndex("user_phone"));
                int level = cursor.getInt(cursor.getColumnIndex("user_level"));
                user = new User(user_id,phone,level);
            }
            database.close();
        }
        return user;
    }

    //find all
    public List<User> findUserAll(){
        List<User> userList = new ArrayList<User>();
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()){
            Cursor cursor = database.rawQuery("select * from users",null);
            while(cursor.moveToNext()){
                Integer id = cursor.getInt(cursor.getColumnIndex("user_id"));
                String phone = cursor.getString(cursor.getColumnIndex("user_phone"));
                int level = cursor.getInt(cursor.getColumnIndex("user_level"));
                User user = new User(id,phone,level);
                userList.add(user);
            }
            database.close();
        }
        return userList;
    }

    //update level
    public void updateUserLevel(Integer user_id,int level){
        if(level<0 || 5<level){
            //信用等级不合法并处理它
            return ;
        }
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()){
            database.execSQL("update users set user_level=? where user_id=?",
                    new Object[]{level,user_id});
            database.close();
        }
    }

    //delete
    public void deleteUser(Integer user_id){
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()){
            database.execSQL("delete from users where user_id = ?",
                    new String[]{user_id.toString()});
            database.close();
        }
    }
}
