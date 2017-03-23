package com.hdu.truckrental.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hanjianhao on 17/2/9.
 * 存储DateTime时  只需要字符串的格式符合以上格式即可 所以这边不在写String转DateTime类型的函数
 */

public class Tool {

    //得到当前的时间 格式为年-月-日 时:分:秒
    public static String getCurrentTime(){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());

        return date;
    }

    //得到输入时间的两小时之后的时间 格式为月-日 时:分
    //如果出错 返回输入时间
    public static String getStartTime(String time){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Calendar calendar = Calendar.getInstance();
            Date date = simpleDateFormat.parse(time);
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, 2);
            calendar.add(Calendar.MONTH, 1);
            String startTime = calendar.get(Calendar.MONTH) + "-" +
                    calendar.get(Calendar.DAY_OF_MONTH) + " " +
                    calendar.get(Calendar.HOUR) + ":" +
                    calendar.get(Calendar.MINUTE);
            return startTime;

        }catch (ParseException p){
            p.printStackTrace();
        }

        return time;
    }

    //判断是否为及时订单（判断两个时间差是否为2小时）
    //请注意参数顺序 MM-dd hh:mm和yyyy-MM-dd HH:mm:ss
    public static boolean isAdvancedDate(String startTime, String createTime){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd hh:mm");
        try{
            Calendar calendar = Calendar.getInstance();
            Date date = simpleDateFormat.parse(startTime);
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, -2);
            calendar.add(Calendar.MONTH, 1);

            String[] dateDivide = createTime.split("-");
            String[] strDivide = dateDivide[2].split(" ");
            String[] timeDivide = strDivide[1].split(":");

            int createMonth = Integer.parseInt(dateDivide[1]);
            int createDay = Integer.parseInt(strDivide[0]);
            int createHour = Integer.parseInt(timeDivide[0]);
            int createMinute = Integer.parseInt(timeDivide[1]);

            int startMonth = calendar.get(Calendar.MONTH);
            int startDay = calendar.get(Calendar.DAY_OF_MONTH);
            int startHour = calendar.get(Calendar.HOUR_OF_DAY);
            int startMinute = calendar.get(Calendar.MINUTE);

            if (createMonth == startMonth && createDay == startDay
                    && createHour == startHour && createMinute == startMinute){
                return true;
            }else{
                return false;
            }

        }catch (ParseException p){
            p.printStackTrace();
        }
        return false;
    }


    //生成订单编号
    public static String getOrderNumber(String currentTime, String userId){

        String orderNumber;

        String[] dateDivide = currentTime.split("-");
        String[] strDivide = dateDivide[2].split(" ");
        String[] timeDivide = strDivide[1].split(":");

        String year = dateDivide[0];
        String month = dateDivide[1];
        String day = strDivide[0];
        String hour = timeDivide[0];
        String minute = timeDivide[1];
        String second = timeDivide[2];

        orderNumber = userId + year + month + day + hour + minute + second;

        return orderNumber;

    }

    public static void main(String[] arg){
        String createDateStr = "2017-02-10 20:11:34";
        String startDateStr = "02-10 22:11";

        System.out.println(isAdvancedDate(startDateStr, createDateStr));
    }




}

