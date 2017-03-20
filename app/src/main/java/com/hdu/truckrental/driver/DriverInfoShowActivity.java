package com.hdu.truckrental.driver;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hdu.truckrental.R;
import com.hdu.truckrental.dao.DriverDao;
import com.hdu.truckrental.domain.Driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Even on 2017/3/16.
 * 司机信息展示
 */

public class DriverInfoShowActivity extends AppCompatActivity {

    private static final int SMALL_VAN = 1;
    private static final int VAN = 2;
    private static final int SMALL_TRUCK = 3;
    private static final int TRUCK = 4;

    private DriverDao driverDao;
    private Driver driver = new Driver();

    private ListView driverInfoListView;
    private ArrayList<Map<String,Object>> driverInfoList = new ArrayList<Map<String, Object>>();
    private SimpleAdapter driverInfoAdapter;
    private String[] driverInfoTitle =
            {"我的姓名","手机号码","驾驶车型","所在城市","车牌号","驾驶证号","目前等级"};
    private String[] driverInfoContent = {"","","","","","",""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        driverInfoListView = new ListView(this);

        SharedPreferences pref = getSharedPreferences("driver",MODE_PRIVATE);
        driverDao = new DriverDao(this);
        driver = driverDao.findDriverById(pref.getInt("id",-1));
        putInfoContent(driver,driverInfoContent);

        for(int i=0;i<driverInfoTitle.length;i++){
            Map<String,Object> item = new HashMap<String, Object>();
            item.put("标题", driverInfoTitle[i]);
            item.put("内容", driverInfoContent[i]);
            driverInfoList.add(item);
        }

        driverInfoAdapter = new SimpleAdapter(this, driverInfoList, R.layout.listview_driver_info,
                new String[]{"标题","内容"}, new int[]{R.id.driver_info_title, R.id.driver_info_content});
        driverInfoListView.setAdapter(driverInfoAdapter);
        setContentView(driverInfoListView);
    }

    private void putInfoContent(Driver driver, String[] driverInfoContent){
        driverInfoContent[0] = driver.getDriver_name();
        driverInfoContent[1] = driver.getDriver_phone();
        switch (driver.getDriver_car_type()){
            case SMALL_VAN:
                driverInfoContent[2] = "小面包车";
                break;
            case VAN:
                driverInfoContent[2] = "中面包车";
                break;
            case SMALL_TRUCK:
                driverInfoContent[2] = "小货车";
                break;
            case TRUCK:
                driverInfoContent[2] = "中货车";
                break;
        }
        driverInfoContent[3] = driver.getDriver_city();
        driverInfoContent[4] = driver.getDriver_license_plate();
        driverInfoContent[5] = driver.getDriver_license();
        driverInfoContent[6] = driver.getDriver_level().toString();
    }
}
