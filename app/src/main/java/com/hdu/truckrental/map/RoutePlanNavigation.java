package com.hdu.truckrental.map;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.hdu.truckrental.R;
import com.mapapi.DrivingRouteOverlay;
import com.mapapi.OverlayManager;

/**
 * Created by Even on 2017/4/3.
 */

public class RoutePlanNavigation extends RoutePlan{
    private BaiduMap mBaiduMap;
    private MapView mMapView;
    private OverlayManager routeOverlay = null;

    private LatLng stLocation;
    private LatLng enLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);
        //初始化地图
        mMapView = (MapView) findViewById(R.id.map_view);
        mBaiduMap = mMapView.getMap();
        SharedPreferences pref = getSharedPreferences("start_loc",MODE_PRIVATE);
        stLocation = new LatLng(pref.getFloat("lng",0), pref.getFloat("lat",0));
        pref = getSharedPreferences("end_loc",MODE_PRIVATE);
        enLocation = new LatLng(pref.getFloat("lng",0), pref.getFloat("lat",0));
        getRoutePlan(stLocation, enLocation);
    }

    private void getRoutePlan(LatLng start, LatLng end){
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(RoutePlanNavigation.this);

        PlanNode stNode = PlanNode.withLocation(start);
        PlanNode enNode = PlanNode.withLocation(end);
        mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
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

                route = result.getRouteLines().get(0);
                DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
                routeOverlay = overlay;
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }else {
                Log.d("route result", "结果数<0" );
                return;
            }
        }
    }
}
