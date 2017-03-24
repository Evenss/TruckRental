package com.hdu.truckrental.driver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hdu.truckrental.R;

/**
 * Created by Even on 2017/3/22.
 * 正在运行订单的细节展示
 */

public class DriverRunningOrdersDetailsShowActivity extends DriverOrderDetailsShowActivity{
    private static final String TAG = "DriverRunningOrdersData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_driver_running_orders_details_show);
        showPriceTextView = (TextView)findViewById(R.id.running_details_price);
        ShowStartDateTextView = (TextView)findViewById(R.id.running_details_start_date);
        ShowDistanceTextView = (TextView)findViewById(R.id.running_details_distance);
        ShowRemarksTextView = (TextView)findViewById(R.id.running_details_remarks);
        ShowDepartureTextView = (TextView)findViewById(R.id.running_details_departure);
        ShowDestinationTextView = (TextView)findViewById(R.id.running_details_destination);
        ShowCarryTextView = (TextView)findViewById(R.id.running_details_carry);
        ShowBackTextView = (TextView)findViewById(R.id.running_details_back);
        ShowFollowersTextView= (TextView)findViewById(R.id.running_details_followers);
    }

    @Override
    public void btn_operate(View view) {
        Intent intent;
        switch (view.getId())
        {
            case R.id.btn_price_details:
                intent = new Intent(DriverRunningOrdersDetailsShowActivity.this,
                        DriverPriceDetailsShowActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_navigate:
                //跳转导航页面
                intent = new Intent(DriverRunningOrdersDetailsShowActivity.this,
                        DriverPriceDetailsShowActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_cancel_order:
                orderDao.updateFkDriverId(orderId,0);
                orderDao.updateOrderState(orderId,UNFINISHED);
                Toast.makeText(this,"取消订单成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
