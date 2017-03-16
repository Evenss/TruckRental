package com.hdu.truckrental.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hdu.truckrental.domain.User;
import com.hdu.truckrental.tools.Check;

import java.util.ArrayList;
import java.util.List;

import static com.hdu.truckrental.tools.Check.USER_DUPLICATE_ERROR;

/**
 * Created by Even on 2017/2/1.
 */

public class UserDao {
    private MyDBHelper myDBHelper;
    private String tag = "UserDao.class";

    //init
    public UserDao(Context context){
        myDBHelper = new MyDBHelper(context);/*
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        myDBHelper.onUpgrade(database,4,5);*/
    }

    //add
    public Integer addUser(User user){
        //输入合法性验证
        Integer state = Check.checkUser(user);
        if(state != Check.SUCCEED){
            return state;
        }
        if(isSamePhone(user.getUser_phone())){
            return USER_DUPLICATE_ERROR;
        }
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()){
            database.execSQL("insert into users (user_phone,user_level) values(?,?)",
                    new Object[]{user.getUser_phone(),user.getUser_level()});
            database.close();
        }
        return state;
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

    //find user by phone
    public User findUserByPhone(String phone){
        List<User> userList ;
        userList = findUserAll();
        for(User user: userList){
            if(user.getUser_phone().equals(phone)){
                return user;
            }
        }
        return null;
    }

    // same phone judge
    private boolean isSamePhone(String phone){
        List<User> userList ;
        userList = findUserAll();
        for(User user: userList){
            if(user.getUser_phone().equals(phone)){
                return true;
            }
        }
        return false;
    }

    //update level
    public Integer updateUserLevel(Integer user_id,int level){
        Integer state = Check.checkUserLevel(level);
        if(state != Check.SUCCEED){
            return state;
        }
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()){
            database.execSQL("update users set user_level=? where user_id=?",
                    new Object[]{level,user_id});
            database.close();
        }
        return state;
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
