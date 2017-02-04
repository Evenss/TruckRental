package com.computer.hdu.truckrental.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.computer.hdu.truckrental.domain.Driver;
import com.computer.hdu.truckrental.domain.Order;

/**
 * Created by Even on 2017/2/3.
 */

public class OrderDao {
    private MyDBHelper myDBHelper;
    private String tag = "OrderDao.class";
    private UserDao userDao;
    private DriverDao driverDao;

    //init
    public OrderDao(Context context){
        myDBHelper = new MyDBHelper(context);
        userDao = new UserDao(context);
        driverDao = new DriverDao(context);
    }

    //add
    public boolean addOrder(Order order){
        //valid check
        //检查可以设置错误码，用来返回，方便给出提示语
        //状态检验
        if(order.getOrder_state()<0||4<order.getOrder_state()){

        }
        //评分检验
        if(order.getOrder_score()<1||100<order.getOrder_score()){

        }
        //回城检验
        if(order.getOrder_back()<0||1<order.getOrder_back()){

        }
        //搬运检查
        if(order.getOrder_carry()<0||1<order.getOrder_carry()){

        }
        Driver driver = driverDao.findDriverById(order.getFk_driver_id());
        //跟车人数检验
        switch (driver.getDriver_car_type()){
            case 0://小型面包车
                if(order.getOrder_followers()!=0){

                }
            case 1://中型面包车
                if (order.getOrder_followers()!=0){

                }
            case 2://小型货车
                if(order.getOrder_followers()<0||2<order.getOrder_followers()){

                }
            case 3://中型货车
                if(order.getOrder_followers()<0||3<order.getOrder_followers()){

                }
        }

        //把String类型的日期转换成datetime类型的

        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()){
            database.execSQL("insert into orders(" +
                    "order_number," +
                    "fk_user_id," +
                    "fk_driver_id," +
                    "order_departure," +
                    "order_destination," +
                    "order_remarks," +
                    "order_distance," +
                    "order_price," +
                    "order_state," +
                    "order_score," +
                    "order_date," +
                    "order_back," +
                    "order_carry," +
                    "order_followers)",
                    new Object[]{
                            order.getOrder_number(),
                            order.getFk_user_id(),
                            order.getFk_driver_id(),
                            order.getOrder_departure(),
                            order.getOrder_destination(),
                            order.getOrder_remarks(),
                            order.getOrder_destination(),
                            order.getOrder_price(),
                            order.getOrder_state(),
                            order.getOrder_score(),
                            order.getOrder_date(),//这里需要转换
                            order.getOrder_back(),
                            order.getOrder_carry(),
                            order.getOrder_followers()
                    });
            database.close();
        }
        return true;
    }

    //find
    public Order findOrderById(Integer order_id){
        Order order;
        order = null;
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()){
            Cursor cursor = database.rawQuery("select * from orders where order_id=?",
                    new String[]{order_id.toString()});
            if(cursor.moveToFirst()){
                String order_number=
                        cursor.getString(cursor.getColumnIndex("order_number"));
                Integer fk_user_id= cursor.getInt(cursor.getColumnIndex("fk_user_id"));
                Integer fk_driver_id= cursor.getInt(cursor.getColumnIndex("fk_driver_id"));
                String order_departure=
                        cursor.getString(cursor.getColumnIndex("order_departure"));
                String order_destination=
                        cursor.getString(cursor.getColumnIndex("order_destination"));
                String order_remarks=
                        cursor.getString(cursor.getColumnIndex("order_remarks"));
                float order_distance=
                        cursor.getFloat(cursor.getColumnIndex("order_distance"));
                float order_price= cursor.getFloat(cursor.getColumnIndex("order_price"));
                Integer order_state= cursor.getInt(cursor.getColumnIndex("order_state"));
                Integer order_score= cursor.getInt(cursor.getColumnIndex("order_score"));
                String order_date= cursor.getString(cursor.getColumnIndex("order_date"));
                Integer order_back= cursor.getInt(cursor.getColumnIndex("order_back"));
                Integer order_carry= cursor.getInt(cursor.getColumnIndex("order_carry"));
                Integer order_followers=
                        cursor.getInt(cursor.getColumnIndex("order_followers"));

                order.setOrder_number(order_number);
                order.setFk_driver_id(fk_driver_id);
                order.setFk_user_id(fk_user_id);
                order.setOrder_departure(order_departure);
                order.setOrder_destination(order_destination);
                order.setOrder_remarks(order_remarks);
                order.setOrder_distance(order_distance);
                order.setOrder_price(order_price);
                order.setOrder_state(order_state);
                order.setOrder_score(order_score);
                order.setOrder_date(order_date);
                order.setOrder_back(order_back);
                order.setOrder_carry(order_carry);
                order.setOrder_followers(order_followers);
            }
            database.close();
        }
        return order;
    }

    //update
    //update state
    public void updateOrderState(Integer order_id,Integer state){
        if(state<0 || 4<state){//0已接单，1为未接单，2用户取消，3司机取消，4已完成
            //错误处理
            return;
        }
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()) {
            database.execSQL("update orders set order_state=? where order_id=?",
                    new Object[]{state, order_id});
            database.close();
        }
    }

    //delete
    public void deleteOrder(Integer order_id){
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        if(database.isOpen()){
            database.execSQL("delete from orders where order_id = ?",
                    new String[]{order_id.toString()});
            database.close();
        }
    }
}
