package com.hdu.truckrental.map;

/**
 * Created by Even on 2017/3/6.
 * 异步线程返回值接口类
 */

public interface DistanceCallBack {
    void onDataReceiveSuccess(Integer distance);
    void onDataReceiveFailed(Exception e);
}
