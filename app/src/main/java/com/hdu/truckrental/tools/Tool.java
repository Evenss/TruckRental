package com.hdu.truckrental.tools;

import java.text.SimpleDateFormat;
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
}
