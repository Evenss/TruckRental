package com.hdu.truckrental.driver;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hdu.truckrental.R;
import com.hdu.truckrental.dao.OrderDao;
import com.hdu.truckrental.domain.Order;

/**
 * Created by Even on 2017/3/16.
 * 司机订单细节展示
 */

public class DriverOrderDetailsShowActivity extends Activity {

    private static final String TAG = "ShowDetailsOrder";
    private static final int RECEIVED = 0;

    private OrderDao orderDao;
    private Order order;
    private int orderId;

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
        orderId = getIntent().getIntExtra("order_id",-1);
        orderDao = new OrderDao(this);
        if(orderId != 0){
            order = orderDao.findOrderById(orderId);

            if(order.getOrder_back() == 1){
                ShowBackTextView.setText(R.string.back);
            }
            if(order.getOrder_carry() == 1){
                ShowCarryTextView.setText(R.string.carry);
            }
            ShowStartDateTextView.setText(order.getOrder_start_date());
            ShowDistanceTextView.setText(order.getOrder_distance()/1000.0+"公里");
            ShowFollowersTextView.setText("跟车人数："+order.getOrder_followers());
            showPriceTextView.setText(order.getOrder_price()+"元");
            ShowDepartureTextView.setText(order.getOrder_departure());
            ShowDestinationTextView.setText(order.getOrder_destination());
            ShowRemarksTextView.setText("备注："+order.getOrder_remarks());
        }
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
                Intent intent = new Intent(DriverOrderDetailsShowActivity.this,
                        DriverPriceDetailsShowActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_take_order:
                //抢单成功，修改订单状态，加入司机id
                SharedPreferences pref = getSharedPreferences("driver",MODE_PRIVATE);
                orderDao.updateFkDriverId(orderId,pref.getInt("id",-1));
                orderDao.updateOrderState(orderId,RECEIVED);
                Toast.makeText(this,"抢单成功！",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
