package com.computer.hdu.truckrental;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Even on 2017/1/24.
 */

public class DriverRegisterActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register_driver);

        Button mDriverRule = (Button) findViewById(R.id.driver_rule);
        mDriverRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //展示注册司机条款界面
                Intent intent = new Intent(DriverRegisterActivity.this,RuleDriverShowActivity.class);
                startActivity(intent);
            }
        });

        Button mRegisterDriverInfo = (Button) findViewById(R.id.register_driverInfo);
        mRegisterDriverInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注册司机信息到服务器
                registerDriver();
            }
        });
    }

    private void registerDriver(){
        //这里写注册司机的具体业务逻辑
        Toast.makeText(getApplicationContext(),"还没写呢T^T",Toast.LENGTH_SHORT).show();
    }
}
