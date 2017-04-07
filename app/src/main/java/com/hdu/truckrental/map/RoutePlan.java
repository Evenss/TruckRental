package com.hdu.truckrental.map;

import android.app.Activity;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

/**
 * Created by Even on 2017/3/7.
 * 封装地图路径类，返回距离
 */

public class RoutePlan extends Activity implements OnGetRoutePlanResultListener {
    protected RouteLine route = null;
    protected RoutePlanSearch mSearch = null;
    protected DrivingRouteResult mDrivingRouteResult = null;
    protected boolean isReturn = false;
    protected static Integer distance = -1;

    //开启新线程计算路径
    public void getDistance(final LatLng start, final LatLng end, final DistanceCallBack listener){
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(RoutePlan.this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //设置起点终点信息
                    PlanNode stNode = PlanNode.withLocation(start);
                    PlanNode enNode = PlanNode.withLocation(end);

                    mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
                    while(!isReturn);
                    Log.d("RouteTread",distance.toString());
                    listener.onDataReceiveSuccess(distance);
                }catch (Exception e){
                    if(listener != null){
                        listener.onDataReceiveFailed(e);
                    }
                }
            }
        }).start();
    }

    //驾车路线
    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            if (result.getRouteLines().size() >= 1 ) {
                mDrivingRouteResult = result;
                route = result.getRouteLines().get(0);
                distance = route.getDistance();
                isReturn = true;
                Log.d("RouteCalculation!",distance.toString());
            }else {
                Log.d("route result", "结果数<0" );
                return;
            }
        }
    }

    //步行路线
    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }
    //公交路线
    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }
    //轨道交通
    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }
    //室内路线
    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }
    //骑行路线
    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }
}
