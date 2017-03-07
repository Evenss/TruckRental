package com.hdu.truckrental.map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.hdu.truckrental.R;
import com.mapapi.PoiOverlay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Even on 2017/3/6.
 * 地图搜索定位，确定出发地或目的地
 */

public class MapLocationActivity extends Activity implements
        OnGetPoiSearchResultListener,OnGetSuggestionResultListener {

    //位置实体类
    private LocationBean locationBean = new LocationBean();

    //视图
    private LocationClient mLocationClient;
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    //搜索及建议
    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    private List<String> suggest;

    //自动定位相关
    private double myLat = 0.0;//自动定位的位置
    private double myLng = 0.0;
    public MapLocationActivity.MyLocationListener myLocationListener = new MyLocationListener();
    private boolean isFirstLocation = true;

    //搜索窗口及按钮
    private EditText mCity = null;
    private AutoCompleteTextView mAddress = null;
    private Button mSearch = null;
    private Button mConfirm = null;

    //提示窗口
    private ArrayAdapter<String> mSugAdapter = null;
    private int loadIndex = 0;//加载的页面数量

    //logo
    private BitmapDescriptor icon =
            BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
    private BitmapDescriptor mkLocation =
            BitmapDescriptorFactory.fromResource(R.drawable.btn_order_location);

    //??
    private InfoWindow mInfoWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_map);

        //初始化输入框及按钮
        mCity = (EditText) findViewById(R.id.city);
        mAddress = (AutoCompleteTextView) findViewById(R.id.address);
        mSearch = (Button) findViewById(R.id.search);
        mConfirm = (Button) findViewById(R.id.confirm);

        //初始化地图
        mMapView = (MapView) findViewById(R.id.map_view);
        mBaiduMap = mMapView.getMap();

        //隐藏比例尺和缩放控件
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);

        //初始化搜索模块
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        //初始化建议搜索模块
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        //设置适配器
        mSugAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line);
        mAddress.setAdapter(mSugAdapter);
        mAddress.setThreshold(1);//出现提示的最小字符

        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        //定位初始化
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myLocationListener);
        initLocation();
        mLocationClient.start();

        //输入的关键字变化时，动态更新建议列表
        mAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()<=0){
                    return;
                }
                //使用建议搜索服务获取建议列表
                mSuggestionSearch
                        .requestSuggestion((new SuggestionSearchOption())
                                .keyword(s.toString()).city(mCity.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //搜索Button监听事件
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchButtonProcess(v);
            }
        });

        //确认Button监听事件
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmButtonProcess();
            }
        });

    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // 资源回收
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        // 退出时销毁定位
        mLocationClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    //定位配置初始化
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");
        //请求间隔
        option.setScanSpan(1000);
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setLocationNotify(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，定位SDK内部是一个独立进程SERVICE，，设置是否在stop的时候杀死这个进程，默认杀死
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
    }

    /**
     * 响应搜索按钮点击事件
     */
    public void searchButtonProcess(View v){
        //关闭定位
        mBaiduMap.setMyLocationEnabled(false);
        String city = mCity.getText().toString();
        String address = mAddress.getText().toString();
        mPoiSearch.searchInCity(new PoiCitySearchOption()
                .city(city).keyword(address).pageNum(loadIndex));
    }

    /**
     * 响应确认按钮点击事件,将信息保存在文件中
     */
    public void confirmButtonProcess(){
        Log.d("return_data",locationBean.toString());
        SharedPreferences.Editor editor = getSharedPreferences("location",MODE_PRIVATE).edit();
        editor.putString("city",locationBean.getCity());
        editor.putString("address",locationBean.getAddress());
        editor.putFloat("lat",(float)locationBean.getLat());
        editor.putFloat("lng",(float)locationBean.getLng());
        editor.apply();
        setResult(RESULT_OK);
        finish();
    }

    /**
     * 自定义位置监听器
     */
    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bd) {
            //map view 销毁后不再处理新接受的位置
            if(bd == null || mMapView ==null){
                return;
            }
            //获取地理位置信息
            MyLocationData locationData = new MyLocationData.Builder()
                    .accuracy(bd.getRadius())
                    .direction(100)//?
                    .latitude(bd.getLatitude())
                    .longitude(bd.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(locationData);
            //显示在图层上
            if(isFirstLocation){
                isFirstLocation = false;
                //保存地理位置到实体类中
                setLocation(bd.getCity(),bd.getStreet(),bd.getLongitude(),bd.getLatitude());
                LatLng latLng = new LatLng(bd.getLatitude(),bd.getLongitude());
                //保存当前位置并展示
                myLat = bd.getLatitude();
                myLng = bd.getLongitude();
                OverlayOptions options = new MarkerOptions().position(latLng).icon(icon);
                mBaiduMap.addOverlay(options);

                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(latLng).zoom(15);//缩放级别
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                //展示目前自动定位的位置
                mCity.setText(locationBean.getCity());
                mAddress.setText(locationBean.getAddress());
            }
        }
    }

    /**
     * POI搜索监听事件
     */
    //获取POI搜索结果
    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if(poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND){
            Toast.makeText(MapLocationActivity.this, "未找到结果", Toast.LENGTH_LONG).show();
            return;
        }
        //在市区内找到相关地点
        if(poiResult.error == SearchResult.ERRORNO.NO_ERROR){
            mBaiduMap.clear();
            PoiOverlay overlay = new MapLocationActivity.MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(poiResult);
            overlay.addToMap();
            overlay.zoomToSpan();//????

            double lat = overlay.getPoiResult().getAllPoi().get(0).location.latitude;
            double lng = overlay.getPoiResult().getAllPoi().get(0).location.longitude;
            //保存地理位置到实体类中
            setLocation(mCity.getText().toString(), mAddress.getText().toString(),lng,lat);
        }
        //当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
        if(poiResult.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD){
            String strInfo = "在";
            for (CityInfo cityInfo : poiResult.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(MapLocationActivity.this, strInfo, Toast.LENGTH_LONG).show();
        }
    }

    //获POI详情搜索结果，得到searchPoiDetail返回的搜索结果
    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        if(poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR){
            Toast.makeText(MapLocationActivity.this,"抱歉，未找到结果",Toast.LENGTH_SHORT).show();
        }else{
            String info = poiDetailResult.getName()+": "+poiDetailResult.getAddress();
            Toast.makeText(MapLocationActivity.this,info,Toast.LENGTH_LONG).show();
        }
    }

    //返回室内搜索结果
    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    /**
     * POI搜索建议监听事件
     */
    @Override
    public void onGetSuggestionResult(SuggestionResult result) {
        if(result == null || result.getAllSuggestions() == null){
            return;
        }
        suggest = new ArrayList<>();
        for(SuggestionResult.SuggestionInfo info : result.getAllSuggestions()){
            if(info.key != null){
                suggest.add(info.key);
            }
        }
        mSugAdapter = new ArrayAdapter<String>
                (MapLocationActivity.this,android.R.layout.simple_dropdown_item_1line,suggest);
        mAddress.setAdapter(mSugAdapter);
        mSugAdapter.notifyDataSetChanged();

    }

    /**
     * 自定义覆盖物类
     */
    private class MyPoiOverlay extends PoiOverlay {
        public MyPoiOverlay(BaiduMap baiduMap){
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poiInfo = getPoiResult().getAllPoi().get(index);//得到所有相关的poi
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(poiInfo.uid));//??
            return true;
        }
    }

    /**
     * 保存地理位置到位置实体类中
     */
    private void setLocation(String city,String address,double lng,double lat){
        locationBean.setCity(city);
        locationBean.setAddress(address);
        locationBean.setLng(lng);
        locationBean.setLat(lat);
    }

}
