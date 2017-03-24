package com.hdu.truckrental.driver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hdu.truckrental.R;

/**
 * Created by Even on 2017/3/24.
 */

public class DriverAllOrdersDetailsShowActivity extends DriverOrderDetailsShowActivity{
    private TextView ShowStatementTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setState();
    }

    private void setState(){
        switch (order.getOrder_state()){
            case RUNNING:
                ShowStatementTextView.setText(R.string.prompt_order_running);
                break;
            case USER_CANCEL:
                ShowStatementTextView.setText(R.string.prompt_order_user_cancel);
                break;
            case DRIVER_CANCEL:
                ShowStatementTextView.setText(R.string.prompt_order_driver_cancel);
                break;
            case FINISHED:
                ShowStatementTextView.setText(R.string.prompt_order_finished);
                break;
        }
    }
    @Override
    protected void initView() {
        setContentView(R.layout.activity_driver_all_orders_details_show);
        showPriceTextView = (TextView) findViewById(R.id.all_details_price);
        ShowStartDateTextView = (TextView) findViewById(R.id.all_details_start_date);
        ShowDistanceTextView = (TextView) findViewById(R.id.all_details_distance);
        ShowRemarksTextView = (TextView) findViewById(R.id.all_details_remarks);
        ShowStatementTextView = (TextView)findViewById(R.id.all_order_state);
        ShowDepartureTextView = (TextView) findViewById(R.id.all_details_departure);
        ShowDestinationTextView = (TextView) findViewById(R.id.all_details_destination);
        ShowCarryTextView = (TextView) findViewById(R.id.all_details_carry);
        ShowBackTextView = (TextView) findViewById(R.id.all_details_back);
        ShowFollowersTextView = (TextView) findViewById(R.id.all_details_followers);
    }

    @Override
    protected void btn_operate(View view) {
        Intent intent;
        switch (view.getId())
        {
            case R.id.btn_all_price_details:
                intent = new Intent(DriverAllOrdersDetailsShowActivity.this,
                        DriverPriceDetailsShowActivity.class);
                startActivity(intent);
                break;
        }
    }
}
