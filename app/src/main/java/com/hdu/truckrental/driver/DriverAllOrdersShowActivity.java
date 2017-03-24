package com.hdu.truckrental.driver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hdu.truckrental.R;
import com.hdu.truckrental.adapter.MyAdapter;
import com.hdu.truckrental.dao.OrderDao;
import com.hdu.truckrental.domain.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Even on 2017/3/16.
 * 司机所有订单展示
 */

public class DriverAllOrdersShowActivity extends AppCompatActivity {
    private ListView allOrdersListView;
    private MyAdapter allOrdersAdapter;

    private List<Order> mOrderList = new ArrayList<>();
    private OrderDao orderDao;
    private int driverId;

    private int totalNum,pageNum;
    private int pageSize = 15;
    private int currentPage = 1;
    private boolean isDivPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_all_orders_show);
        allOrdersListView = (ListView) findViewById(R.id.all_orders_ListView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        put_info_list();
        allOrdersAdapter = new MyAdapter(this,mOrderList);
        allOrdersListView.setAdapter(allOrdersAdapter);
        if(mOrderList.size() == 0){
            Toast toast = Toast.makeText(this, "无订单记录",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }

        //item点击事件
        allOrdersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ListView listView = (ListView) parent;
                Order order = (Order) listView.getItemAtPosition(position);
                int order_id = order.getOrder_id();
                Intent intent = new Intent(DriverAllOrdersShowActivity.this,
                        DriverRunningOrdersDetailsShowActivity.class);
                intent.putExtra("order_id",order_id);
                startActivity(intent);
            }
        });
    }

    private void put_info_list(){
        orderDao = new OrderDao(DriverAllOrdersShowActivity.this);
        SharedPreferences pref = getSharedPreferences("driver",MODE_PRIVATE);
        driverId = pref.getInt("id",-1);
        if(driverId != -1){
            mOrderList.clear();
            mOrderList = orderDao.findOrderByDriverId(driverId);
        }
    }
}
