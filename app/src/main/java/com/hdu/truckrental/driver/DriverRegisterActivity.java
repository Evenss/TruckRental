package com.hdu.truckrental.driver;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hdu.truckrental.R;
import com.hdu.truckrental.dao.DriverDao;
import com.hdu.truckrental.domain.Driver;

import static com.hdu.truckrental.tools.Check.DRIVER_CAR_TYPE_ERROR;
import static com.hdu.truckrental.tools.Check.DRIVER_DUPLICATE_ERROR;
import static com.hdu.truckrental.tools.Check.LICENSE_ERROR;
import static com.hdu.truckrental.tools.Check.LICENSE_PLATE_ERROR;
import static com.hdu.truckrental.tools.Check.NAME_ERROR;
import static com.hdu.truckrental.tools.Check.PASSWORD_ERROR;
import static com.hdu.truckrental.tools.Check.PHONE_ERROR;

/**
 * Created by Even on 2017/1/24.
 * 司机注册
 */

public class DriverRegisterActivity extends AppCompatActivity {

    private Button mDriverRuleBtn;
    private Button mRegisterDriverInfo;
    private TextView mDriverNameTv;
    private TextView mDriverPhoneTv;
    private EditText mDriverPassWordEt;
    private RadioGroup mDriverCarTypeRg;
    private RadioButton mDriverCarTypeRb;
    private TextView mDriverCityTv;
    private TextView mDriverLicensePlateTv;
    private TextView mDriverLicenseTv;
    private Toolbar mToolbarDriverRegister;

    private int state;
    private static final String TAG = "DriverRegister.class";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register_driver);

        mDriverRuleBtn = (Button) findViewById(R.id.driver_rule_btn);
        mDriverRuleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //展示注册司机条款界面
                Intent intent = new Intent(DriverRegisterActivity.this, DriverRegisterRuleShowActivity.class);
                startActivity(intent);
            }
        });

        mRegisterDriverInfo = (Button) findViewById(R.id.register_driverInfo_btn);
        mRegisterDriverInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注册司机信息到服务器
                state = registerDriver();
                if(state < 0){
                    ErrorShow(state);
                }else{
                    Toast.makeText(DriverRegisterActivity.this,"注册成功！正在等待审核...",
                            Toast.LENGTH_SHORT).show();
                    //跳转到司机登录界面
                    Intent intent = new Intent(DriverRegisterActivity.this, DriverLoginActivity.class);
                    startActivity(intent);//暂时先跳转到用户登录界面
                    finish();
                }

            }
        });
        mToolbarDriverRegister = (Toolbar)findViewById(R.id.toolbar_driver_register);
        setSupportActionBar(mToolbarDriverRegister);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbarDriverRegister.setNavigationIcon(R.drawable.nav_return);
        mToolbarDriverRegister.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void ErrorShow(Integer state){
        String errorMessage = "";
        switch (state){
            case NAME_ERROR:
                errorMessage = "姓名不合法(暂只支持中国地区)";
                break;
            case PHONE_ERROR:
                errorMessage = "电话输入不合法";
                break;
            case PASSWORD_ERROR:
                errorMessage = "密码太短";
                break;
            case DRIVER_CAR_TYPE_ERROR:
                errorMessage = "请选择货车类型";
                break;
            case LICENSE_PLATE_ERROR:
                errorMessage = "车牌号输入不合法";
                break;
            case LICENSE_ERROR:
                errorMessage = "驾照号输入不合法";
                break;
            case DRIVER_DUPLICATE_ERROR:
                errorMessage = "该电话已被注册";
                break;
        }
        AlertDialog.Builder errorBuilder = new AlertDialog.Builder(this);
        errorBuilder.setTitle("错误提示");
        errorBuilder.setMessage(errorMessage);
        errorBuilder.setPositiveButton("确定",null);
        errorBuilder.show();
    }
    private int registerDriver(){
        //get info
        mDriverNameTv = (TextView) findViewById(R.id.driver_name);
        mDriverPhoneTv = (TextView) findViewById(R.id.driver_phone);
        mDriverPassWordEt = (EditText) findViewById(R.id.driver_password);
        mDriverCarTypeRg = (RadioGroup) findViewById(R.id.driver_car_type);
        mDriverCarTypeRb = (RadioButton) findViewById(mDriverCarTypeRg.getCheckedRadioButtonId());
        mDriverCityTv = (TextView) findViewById(R.id.driver_city);//这里需要修改，省市区三级联动！
        mDriverLicensePlateTv = (TextView) findViewById(R.id.driver_license_plate);
        mDriverLicenseTv = (TextView) findViewById(R.id.driver_license);

        //info to driver class
        Driver driver = new Driver();
        driver.setDriver_name(mDriverNameTv.getText().toString());
        driver.setDriver_phone(mDriverPhoneTv.getText().toString());
        driver.setDriver_pwd(mDriverPassWordEt.getText().toString());
        if(mDriverCarTypeRb != null){
            switch (mDriverCarTypeRb.getText().toString()){
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
        }else{
            driver.setDriver_car_type(-1);
        }

        driver.setDriver_city(mDriverCityTv.getText().toString());
        driver.setDriver_license_plate(mDriverLicensePlateTv.getText().toString());
        driver.setDriver_license(mDriverLicenseTv.getText().toString());
        driver.setDriver_level(5);//初始信用等级为5
        driver.setDriver_score(100);//初始评分为100
        driver.setDriver_state(0);//状态为审核中

        //info to db
        DriverDao driverDao = new DriverDao(DriverRegisterActivity.this);
        state = driverDao.addDriver(driver);
        return state;
    }
}
