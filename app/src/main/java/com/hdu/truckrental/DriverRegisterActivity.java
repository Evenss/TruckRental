package com.hdu.truckrental;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hdu.truckrental.dao.DriverDao;
import com.hdu.truckrental.domain.Driver;

/**
 * Created by Even on 2017/1/24.
 */

public class DriverRegisterActivity extends Activity {

    private String tag = "DriverRegister.class";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register_driver);

        Button mDriverRuleBtn = (Button) findViewById(R.id.driver_rule_btn);
        mDriverRuleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //展示注册司机条款界面
                Intent intent = new Intent(DriverRegisterActivity.this, com.hdu.truckrental.RuleDriverShowActivity.class);
                startActivity(intent);
            }
        });

        Button mRegisterDriverInfo = (Button) findViewById(R.id.register_driverInfo_btn);
        mRegisterDriverInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注册司机信息到服务器
                registerDriver();
                //跳转到司机登录界面
                Intent intent = new Intent(DriverRegisterActivity.this, com.hdu.truckrental.LoginActivity.class);
                startActivity(intent);//暂时先跳转到用户登录界面
            }
        });
    }

    private void registerDriver(){
        //get info
        TextView mDriverNameTv = (TextView) findViewById(R.id.driver_name);
        TextView mDriverPhoneTv = (TextView) findViewById(R.id.driver_phone);
        EditText mDriverPassWordEt = (EditText) findViewById(R.id.driver_password);
        RadioGroup mDriverCarTypeRg = (RadioGroup) findViewById(R.id.driver_car_type);
        RadioButton mDriverCarTypeRb =
                (RadioButton) findViewById(mDriverCarTypeRg.getCheckedRadioButtonId());
        TextView mDriverCityTv = (TextView) findViewById(R.id.driver_city);//这里需要修改，省市区三级联动！
        TextView mDriverLicensePlateTv = (TextView) findViewById(R.id.driver_license_plate);
        TextView mDriverLicenseTv = (TextView) findViewById(R.id.driver_license);

        //info to driver class
        Driver driver = new Driver();
        driver.setDriver_name(mDriverNameTv.getText().toString());
        driver.setDriver_phone(mDriverPhoneTv.getText().toString());
        driver.setDriver_pwd(mDriverPassWordEt.getText().toString());
        switch (mDriverCarTypeRb.getText().toString()){//这里想法可以优化一下
            case "小面包车":
                driver.setDriver_car_type(1);
                break;
            case "中面包车":
                driver.setDriver_car_type(2);
                break;
            case "小货车":
                driver.setDriver_car_type(3);
                break;
            case "中货车":
                driver.setDriver_car_type(4);
                break;
        }
        driver.setDriver_city(mDriverCityTv.getText().toString());
        driver.setDriver_license_plate(mDriverLicensePlateTv.getText().toString());
        driver.setDriver_license(mDriverLicenseTv.getText().toString());
        driver.setDriver_level(5);//初始信用等级为5
        driver.setDriver_score(100);//初始评分为100
        driver.setDriver_state(0);//状态为审核中

        Log.d(tag,driver.toString());

        //info to db
        DriverDao driverDao = new DriverDao(getApplicationContext());
        driverDao.addDriver(driver);

        Toast.makeText(getApplicationContext(),"注册成功！正在等待审核...",Toast.LENGTH_SHORT).show();
    }
}
