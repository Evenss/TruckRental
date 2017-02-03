package com.computer.hdu.truckrental.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.computer.hdu.truckrental.domain.Driver;

/**
 * Created by Even on 2017/2/3.
 */

public class DriverDao {
    private MyDBHelper myDBHelper;
    private String tag = "DriverDao";

    //init
    public DriverDao(Context context){
        myDBHelper = new MyDBHelper(context);
    }

    //add
    public boolean addDriver(Driver driver){

        //valid check
        //电话号码正则检验
        if(true){
        }
        //车型检验
        if(driver.getDriver_car_type()<1 || 4<driver.getDriver_car_type()){
            return false;
        }
        //信用等级合法检验
        if(driver.getDriver_level()<1 || 5<driver.getDriver_level()){
            return false;
        }
        //评分合法检验
        if(driver.getDriver_score()<1 || 100<driver.getDriver_score()){
            return false;
        }
        //状态检验
        if(driver.getDriver_state()<0 || 3<driver.getDriver_state()){
            return false;
        }

        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()){
            database.execSQL("insert into drivers (" +
                    "driver_name," +
                    "driver_phone," +
                    "driver_pwd," +
                    "driver_car_type," +
                    "driver_city," +
                    "driver_license_plate," +
                    "driver_license," +
                    "driver_level," +
                    "driver_score," +
                    "driver_state) " +
                    "values(?,?,?,?,?,?,?,?,?,?)",
                    new Object[]{//有一些初始值的，放到创建Driver中，这里不直接写初始值
                            driver.getDriver_name(),
                            driver.getDriver_phone(),
                            driver.getDriver_pwd(),//要加密处理密码
                            driver.getDriver_car_type(),
                            driver.getDriver_city(),
                            driver.getDriver_license_plate(),
                            driver.getDriver_license(),
                            driver.getDriver_level(),//5
                            driver.getDriver_score(),//
                            driver.getDriver_state()//0
                    });
            database.close();
        }
        return true;
    }

    //find
    public Driver findDriverById(Integer driver_id){
        Driver driver;
        driver = null;
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()){
            Cursor cursor = database.rawQuery("select * from drivers where driver_id=?",
                    new String[]{driver_id.toString()});
            if(cursor.moveToFirst()){
                String driver_name = cursor.getString(cursor.getColumnIndex("driver_name"));
                String driver_phone = cursor.getString(cursor.getColumnIndex("driver_phone"));
                String driver_pwd = cursor.getString(cursor.getColumnIndex("driver_pwd"));
                Integer driver_car_type = cursor.getInt(cursor.getColumnIndex("driver_car_type"));
                String driver_city = cursor.getString(cursor.getColumnIndex("driver_city"));
                String driver_license_plate =
                        cursor.getString(cursor.getColumnIndex("driver_license_plate"));
                String driver_license = cursor.getString(cursor.getColumnIndex("driver_license"));
                Integer driver_level = cursor.getInt(cursor.getColumnIndex("driver_level"));
                Integer driver_score = cursor.getInt(cursor.getColumnIndex("driver_score"));
                Integer driver_state = cursor.getInt(cursor.getColumnIndex("driver_state"));

                driver.setDriver_id(driver_id);
                driver.setDriver_name(driver_name);
                driver.setDriver_phone(driver_phone);
                driver.setDriver_pwd(driver_pwd);
                driver.setDriver_car_type(driver_car_type);
                driver.setDriver_city(driver_city);
                driver.setDriver_license_plate(driver_license_plate);
                driver.setDriver_license(driver_license);
                driver.setDriver_level(driver_level);
                driver.setDriver_score(driver_score);
                driver.setDriver_state(driver_state);
            }
            database.close();
        }
        return driver;
    }

    //下面四个更新可以提取出来，但是合法检查不好处理，这里可以再想想其他方法
    //update car_type
    public void updateDriverCarType(Integer driver_id,Integer car_type){
        if(car_type<1 || 4<car_type){
            //错误处理
            return;
        }
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()) {
            database.execSQL("update drivers set driver_car_type=? where driver_id=?",
                    new Object[]{car_type, driver_id});
            database.close();
        }
    }
    //update level
    public void updateDriverLevel(Integer driver_id,Integer level){
        if(level<1 || 5<level){
            //错误处理
            return;
        }
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()) {
            database.execSQL("update drivers set driver_level=? where driver_id=?",
                    new Object[]{level, driver_id});
            database.close();
        }
    }
    //update score
    public void updateDriverScore(Integer driver_id,Integer score){
        if(score<0 || 100<score){
            //错误处理
            return;
        }
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()) {
            database.execSQL("update drivers set driver_score=? where driver_id=?",
                    new Object[]{score, driver_id});
            database.close();
        }
    }
    //update state
    public void updateDriverState(Integer driver_id,Integer state){
        if(state<0 || 3<state){
            //错误处理
            return;
        }
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()) {
            database.execSQL("update drivers set driver_state=? where driver_id=?",
                    new Object[]{state, driver_id});
            database.close();
        }
    }

    //delete
    public void deleteDriver(Integer driver_id){
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()){
            database.execSQL("delete from drivers where driver_id = ?",
                    new String[]{driver_id.toString()});
            database.close();
        }
    }
}
