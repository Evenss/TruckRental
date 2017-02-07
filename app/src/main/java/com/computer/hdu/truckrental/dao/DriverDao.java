package com.computer.hdu.truckrental.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.computer.hdu.truckrental.domain.Driver;

import java.util.ArrayList;
import java.util.List;

import static com.computer.hdu.truckrental.tools.Check.checkDriver;
import static com.computer.hdu.truckrental.tools.Check.checkDriverCarType;
import static com.computer.hdu.truckrental.tools.Check.checkDriverLevel;
import static com.computer.hdu.truckrental.tools.Check.checkDriverScore;
import static com.computer.hdu.truckrental.tools.Check.checkDriverState;
import static com.computer.hdu.truckrental.tools.Encrypt.getEncryption;

/**
 * Created by Even on 2017/2/3.
 */

public class DriverDao {
    private MyDBHelper myDBHelper;
    private String tag = "DriverDao";

    //init
    public DriverDao(Context context){
        myDBHelper = new MyDBHelper(context);
/*        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        myDBHelper.onUpgrade(database,4,5);*/
    }

    //test
    public void showDriver(Driver driver){
        Log.d(tag,driver.toString());
    }

    //add
    public Integer addDriver(Driver driver){

        //valid check
        Integer state = checkDriver(driver);

        //pwd encrypt
        String pwdEncrypted = getEncryption(driver.getDriver_pwd());
        driver.setDriver_pwd(pwdEncrypted);

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
                            driver.getDriver_pwd(),
                            driver.getDriver_car_type(),
                            driver.getDriver_city(),
                            driver.getDriver_license_plate(),
                            driver.getDriver_license(),
                            driver.getDriver_level(),//5
                            driver.getDriver_score(),//100
                            driver.getDriver_state()//0
                    });
            database.close();
        }
        return state;
    }

    //get driver
    private Driver getDriver(Cursor cursor,Integer driver_id){
        Driver driver = new Driver();
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

        showDriver(driver);
        return driver;
    }

    //find by id
    public Driver findDriverById(Integer driver_id){
        Driver driver;
        driver = null;
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()){
            Cursor cursor = database.rawQuery("select * from drivers where driver_id=?",
                    new String[]{driver_id.toString()});
            if(cursor.moveToFirst()){
                driver = getDriver(cursor,driver_id);
            }
            database.close();
        }
        return driver;
    }

    //find by car type
    public List<Driver> findDriverListByCarType(Integer car_type){
        List<Driver> driverList = new ArrayList<Driver>();
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()){
            Cursor cursor = database.rawQuery("select * from drivers where driver_car_type=?",
                    new String[]{car_type.toString()});
            while(cursor.moveToNext()){
                Integer driver_id = cursor.getInt(cursor.getColumnIndex("driver_id"));
                driverList.add(getDriver(cursor,driver_id));
            }
            database.close();
        }
        return driverList;
    }

    //update
    private void updateDriver(String sql, Object[] attribute){
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()) {
            database.execSQL(sql, attribute);
            database.close();
        }
    }
    //update car_type
    public Integer updateDriverCarType(Integer driver_id,Integer car_type){
        Integer state = checkDriverCarType(car_type);
        String carTypeSql = "update drivers set driver_car_type=? where driver_id=?";
        Object[] attribute = new Object[]{car_type,driver_id};
        updateDriver(carTypeSql,attribute);
        return state;
    }
    //update level
    public Integer updateDriverLevel(Integer driver_id,Integer level){
        Integer state = checkDriverLevel(level);
        String levelSql = "update drivers set driver_level=? where driver_id=?";
        Object[] attribute = new Object[]{level,driver_id};
        updateDriver(levelSql,attribute);
        return state;
    }
    //update score
    public Integer updateDriverScore(Integer driver_id,Integer score){
        Integer state = checkDriverScore(score);
        String ScoreSql = "update drivers set driver_score=? where driver_id=?";
        Object[] attribute = new Object[]{score, driver_id};
        updateDriver(ScoreSql,attribute);
        return score;
    }
    //update state
    public Integer updateDriverState(Integer driver_id,Integer driver_state){
        Integer state = checkDriverState(driver_state);
        String StateSql = "update drivers set driver_state=? where driver_id=?";
        Object[] attribute = new Object[]{driver_state, driver_id};
        updateDriver(StateSql,attribute);
        return state;
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
