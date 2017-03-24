package com.hdu.truckrental.driver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hdu.truckrental.R;
import com.hdu.truckrental.adapter.RunningOrderAdapter;
import com.hdu.truckrental.dao.OrderDao;
import com.hdu.truckrental.domain.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yjt on 2017/2/11.
 */

public class DriverRunningOrdersShowActivity extends AppCompatActivity{
    private static final String TAG = "DriverRunningOrdersShow";
    private static final int REFRESH_COMPLETE = 0X110;
    private static final int RECEIVED = 0;

    private ListView runningOrderLv;
    private RunningOrderAdapter runningOrderAdapter;

    private List<Order> runningOrderList = new ArrayList<>();;
    private List<Order> orderList;
    private OrderDao orderDao;

    private int totalNum,pageNum;
    private int pageSize = 15;
    private int currentPage = 1;
    private boolean isDivPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_running_orders_show);
        runningOrderLv = (ListView) findViewById(R.id.running_orders_ListView);

        put_info_list();
        runningOrderAdapter = new RunningOrderAdapter(this,runningOrderList);
        runningOrderLv.setAdapter(runningOrderAdapter);


        //item点击事件
        runningOrderLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ListView listView = (ListView) parent;
                Order order = (Order) listView.getItemAtPosition(position);

                Intent intent = new Intent(DriverRunningOrdersShowActivity.this,
                        DriverRunningOrdersDetailsShowActivity.class);
                startActivity(intent);
            }
        });
    }

    private void put_info_list(){
        orderDao = new OrderDao(DriverRunningOrdersShowActivity.this);
        orderList = orderDao.findAllOrder();
        for(Order order:orderList){
            if(order.getOrder_state() == RECEIVED){
                runningOrderList.add(order);
            }
        }
    }
}
