package com.computer.hdu.truckrental.dao;

import android.content.Context;

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
        return true;
    }
}
