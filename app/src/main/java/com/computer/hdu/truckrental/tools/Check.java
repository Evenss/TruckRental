package com.computer.hdu.truckrental.tools;

import com.computer.hdu.truckrental.domain.Driver;
import com.computer.hdu.truckrental.domain.Order;
import com.computer.hdu.truckrental.domain.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hanjianhao on 17/2/5.
 * 检验工具类
 * 对于范围的检验 本来想写一个函数 （参数为 检验数 最小值 最大值） 但后来发现代码并没有减少 反而出栈入栈增加 降低效率
 */

public class Check {

    /*
     * 检验规则
     * 手机号检验规则
     * 姓名检验规则
     * 车牌号检验规则
     * 行驶证号检验规则
     */
    private static String phoneRule = "^[1][358]\\d{9}$";
    private static String nameRule = "^[\\u4E00-\\u9FA5\\uF900-\\uFA20]{2,4}$";
    private static String licensePlateRule = "^[\\u5180\\u8c6b\\u4e91\\u8fbd\\u9ed1\\u6e58" +
            "\\u7696\\u9c81\\u65b0\\u82cf\\u6d59\\u8d63\\u9102\\u6842\\u7518\\u664b\\u8499" +
            "\\u9655\\u5409\\u95fd\\u8d35\\u7ca4\\u5ddd\\u85cf\\u743c\\u5b81\\u6e1d\\u4eac" +
            "\\u6d25\\u6caa][0-9A-Z]{6}$";
    private static String licenseRule = "^[0-9]{17}[0-9Xx]$";

    /*
     * 检验数据所返回的错误
     */
    public static final int PHONE_ERROR = -1;                           //手机号错误
    public static final int NAME_ERROR = -2;                            //姓名错误
    public static final int LICENSE_PLATE_ERROR = -3;                   //车牌号错误
    public static final int LICENSE_ERROR = -4;                         //行驶证号错误（也就是身份证）
    public static final int PASSWORD_ERROR = -5;                        //密码错误
    public static final int EMAIL_ERROR = -6;                           //邮箱错误
    public static final int USER_LEVEL_ERROR = -7;                      //用户等级错误
    public static final int ORDER_STATE_ERROR = -8;                     //订单状态错误
    public static final int ORDER_SCORE_ERROR = -9;                     //订单评分错误
    public static final int ORDER_BACK_ERROR = -10;                     //订单回程错误
    public static final int ORDER_CARRY_ERROR = -11;                    //
    public static final int ORDER_FOLLOWERS_ERROR = -12;                //订单跟车人数错误
    public static final int DRIVER_CAR_TYPE_ERROR = -13;                //司机车型错误
    public static final int DRIVER_LEVEL_ERROR = -14;                   //司机等级错误
    public static final int DRIVER_SCORE_ERROR = -15;                   //司机评分错误
    public static final int DRIVER_STATE_ERROR = -16;                   //司机状态错误

    public static final int SUCCEED = 0;                                //检验成功返回的数值

    public static final int OUT_OF_CONDITION = 1;                       //超出条件范围错误

    //手机号检验
    public static int checkPhone(String phone){
        Pattern pattern = Pattern.compile(phoneRule);
        Matcher matcher = pattern.matcher(phone);
        if(matcher.find()){
            return SUCCEED;
        }else{
            return PHONE_ERROR;
        }
    }

    //姓名检验
    public static int checkName(String name){
        Pattern pattern = Pattern.compile(nameRule);
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()){
            return SUCCEED;
        }else{
            return NAME_ERROR;
        }
    }

    //车牌号检验
    public static int checkLicensePlate(String licensePlate){
        Pattern pattern = Pattern.compile(licensePlateRule);
        Matcher matcher = pattern.matcher(licensePlate);
        if (matcher.find()){
            return SUCCEED;
        }else{
            return LICENSE_PLATE_ERROR;
        }
    }

    //行驶证检验
    public static int checkLicense(String license){
        Pattern pattern = Pattern.compile(licenseRule);
        Matcher matcher = pattern.matcher(license);
        if (matcher.find()){
            return SUCCEED;
        }else{
            return LICENSE_ERROR;
        }
    }

    //密码检验
    public static int checkPassword(String password){

        if (password.length() < 4){
            return PASSWORD_ERROR;
        }else {
            return SUCCEED;
        }
    }

    //email检验
    public static int checkEmail(String email){
        if (email.contains("@")){
            return SUCCEED;
        }else{
            return EMAIL_ERROR;
        }
    }

    //用户等级检验
    public static int checkUserLevel(int level){
        if (level < 1 || level > 5){
            return USER_LEVEL_ERROR;
        }else{
            return SUCCEED;
        }
    }

    public static int checkOrderState(int state){
        if (state < 0 || state > 4){
            return ORDER_STATE_ERROR;
        }else{
            return SUCCEED;
        }
    }

    public static int checkOrderScore(int score){
        if (score < 1 || score > 100){
            return ORDER_SCORE_ERROR;
        }else{
            return SUCCEED;
        }
    }

    public static int checkOrderBack(int back){
        if (back != 0 && back != 1){
            return ORDER_BACK_ERROR;
        }else{
            return SUCCEED;
        }
    }

    public static int checkOrderCarry(int carry){
        if (carry != 0 && carry != 1){
            return ORDER_CARRY_ERROR;
        }else{
            return SUCCEED;
        }
    }

    public static int checkOrderFollowers(int carType, int followers){

        int state = SUCCEED;

        switch(carType){
            case 0:if (followers != 0)
                state = ORDER_FOLLOWERS_ERROR;
                break;
            case 1:if (followers != 0)
                state = ORDER_FOLLOWERS_ERROR;
                break;
            case 2:if (followers < 0 || followers > 2)
                state = ORDER_FOLLOWERS_ERROR;
                break;
            case 3:if (followers < 0 || followers > 3)
                state = ORDER_FOLLOWERS_ERROR;
                break;
            default:state = OUT_OF_CONDITION;
                break;
        }

        return state;
    }

    public static int checkDriverCarType(int carType){
        if (carType < 1 || carType > 4){
            return DRIVER_CAR_TYPE_ERROR;
        }else{
            return SUCCEED;
        }
    }

    public static int checkDriverLevel(int level){
        if (level < 1 || level > 5){
            return DRIVER_LEVEL_ERROR;
        }else{
            return SUCCEED;
        }
    }

    public static int checkDriverScore(int score){
        if (score < 1 || score > 100){
            return DRIVER_SCORE_ERROR;
        }else{
            return SUCCEED;
        }
    }

    public static int checkDriverState(int state){
        if (state < 0 || state > 3){
            return DRIVER_STATE_ERROR;
        }else{
            return SUCCEED;
        }
    }

    //用户检验（综合用户所有属性的检验，并返回相应的错误代码）
    public static int checkUser(User user){

        int state = SUCCEED;

        if ((state = checkPhone(user.getUser_phone().toString())) != SUCCEED){
            //do nothing
        }else if ((state = checkUserLevel(user.getUser_level().intValue())) != SUCCEED){
            //do nothing
        }else{
            state = OUT_OF_CONDITION;
        }

        return state;
    }

    //订单检验 （综合订单一些属性的检验，并返回相应的错误代码）
    public static int checkOrder(Order order, int carType){

        int state = SUCCEED;

        if ((state = checkOrderState(order.getOrder_state().intValue())) != SUCCEED){
            //do nothing
        }else if ((state = checkOrderScore(order.getOrder_score().intValue())) != SUCCEED){
            //do nothing
        }else if ((state = checkOrderBack(order.getOrder_back().intValue())) != SUCCEED){
            //do nothing
        }else if ((state = checkOrderCarry(order.getOrder_carry().intValue())) != SUCCEED){
            //do nothing
        }else if ((state = checkOrderFollowers(carType, order.getOrder_followers().intValue())) != SUCCEED){
            //do nothing
        }else{
            state = OUT_OF_CONDITION;
        }

        return state;
    }

    //司机检验 （综合一些司机属性的检验，并返回相应的错误代码）
    public static int checkDriver(Driver driver){
        int state = SUCCEED;

        if ((state = checkDriverCarType(driver.getDriver_car_type().intValue())) != SUCCEED){
            //do nothing
        }else if ((state = checkDriverLevel(driver.getDriver_level().intValue())) != SUCCEED){
            //do nothing
        }else if ((state = checkDriverScore(driver.getDriver_score().intValue())) != SUCCEED){
            //do nothing
        }else if ((state = checkDriverState(driver.getDriver_state().intValue())) != SUCCEED){
            //do nothing
        }else{
            state = OUT_OF_CONDITION;
        }

        return state;
    }
}
