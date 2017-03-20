package com.hdu.truckrental.driver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hdu.truckrental.R;

/**
 * Created by Even on 2017/3/16.
 * 司机订单细节展示
 */

public class DriverOrderDetailsShowActivity extends Activity {

    private static final String TAG = "ShowDetailsOrder";

    private TextView showPriceTextView;
    private TextView ShowStartDateTextView;
    private TextView ShowDistanceTextView;
    private TextView ShowRemarksTextView;
    private TextView ShowDepartureTextView;
    private TextView ShowDestinationTextView;
    private TextView ShowCarryTextView;
    private TextView ShowBackTextView;
    private TextView ShowFollowersTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_order_details_show);
        initView();
        Bundle bundle = getIntent().getExtras();

        if(bundle.getInt("是否回程") == 1){
            ShowBackTextView.setText(R.string.back);
        }
        if(bundle.getInt("是否搬运") == 1){
            ShowCarryTextView.setText(R.string.carry);
        }

        ShowStartDateTextView.setText(bundle.getString("运货时间"));
        ShowDistanceTextView.setText(""+bundle.getFloat("路程数")+"公里");
        ShowFollowersTextView.setText("跟车人数："+Integer.toString(bundle.getInt("跟车人数")));
        showPriceTextView.setText(""+bundle.getFloat("金额")+"元");
        ShowDepartureTextView.setText(bundle.getString("出发地址"));
        ShowDestinationTextView.setText(bundle.getString("目的地址"));
        ShowRemarksTextView.setText("备注："+bundle.getString("备注"));

    }
    private void initView(){
        showPriceTextView = (TextView)findViewById(R.id.details_price);
        ShowStartDateTextView = (TextView)findViewById(R.id.details_start_date);
        ShowDistanceTextView = (TextView)findViewById(R.id.details_distance);
        ShowRemarksTextView = (TextView)findViewById(R.id.details_remarks);
        ShowDepartureTextView = (TextView)findViewById(R.id.details_departure);
        ShowDestinationTextView = (TextView)findViewById(R.id.details_destination);
        ShowCarryTextView = (TextView)findViewById(R.id.details_carry);
        ShowBackTextView = (TextView)findViewById(R.id.details_back);
        ShowFollowersTextView = (TextView)findViewById(R.id.details_followers);
    }

    public void btn_operate(View view){
        switch (view.getId())
        {
            case R.id.btn_price_details:
                Intent intent = new Intent(DriverOrderDetailsShowActivity.this,DriverPriceDetailsShowActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_take_order:
                //跳转订单进行页面
                intent = new Intent(DriverOrderDetailsShowActivity.this,DriverPriceDetailsShowActivity.class);
                startActivity(intent);
                break;
        }
    }
}
