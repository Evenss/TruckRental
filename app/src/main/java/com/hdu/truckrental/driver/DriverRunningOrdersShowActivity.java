package com.hdu.truckrental.driver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hdu.truckrental.R;
import com.hdu.truckrental.adapter.RunningOrderAdapter;
import com.hdu.truckrental.domain.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yjt on 2017/2/11.
 */

public class DriverRunningOrdersShowActivity extends AppCompatActivity{
    private static final String TAG = "DriverRunningOrdersShow";
    private static final int REFRESH_COMPLETE = 0X110;
    private ListView runningOrderLv;
    private SwipeRefreshLayout runningOrderSrl;
    private List<Order> runningOrderList = new ArrayList<>();
    private RunningOrderAdapter runningOrderAdapter;
    private int totalNum,pageNum;
    private int pageSize = 15;
    private int currentPage = 1;
    private boolean isDivPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_running_orders_show);
        runningOrderLv = (ListView) findViewById(R.id.running_orders_ListView);
        //running_orders_srl = (SwipeRefreshLayout) findViewById(R.id.running_orders_SwipeRefreshLayout);

        put_info_list();
        runningOrderAdapter = new RunningOrderAdapter(this,runningOrderList);
        runningOrderLv.setAdapter(runningOrderAdapter);

       /* //下拉刷新
        running_orders_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 1500);
            }
        });
        running_orders_srl.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        */

        //item点击事件
        runningOrderLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ListView listView = (ListView) parent;
                Order order = (Order) listView.getItemAtPosition(position);

                Intent intent = new Intent(DriverRunningOrdersShowActivity.this, DriverRunningOrdersDetailsShowActivity.class);
                startActivity(intent);
            }
        });

        //需要从数据库获得符合条件订单条数
        /*totalNum = DbManager.getDataCount(db,Constant.TABLE_NAME);
        pageNum = (int) Math.ceil(totalNum/(double)pageSize);
        if(currentPage == 1){
            //添加数据
            totalList = DbManager.getListByCurrentPage(db,Constant.TABLE_NAME,currentPage,pageSize);
        }*/

        /*myListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if(isDivPage && AbsListView.OnScrollListener.SCROLL_STATE_IDLE == scrollState){
                    if (currentPage < pageNum){
                        currentPage++;
                        //数据添加
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                isDivPage = ((firstVisibleItem+visibleItemCount) == totalItemCount);
            }
        });*/
    }

    private void put_info_list(){
        Order order1 = new Order();
        order1.setOrder_start_date("2017/2/10");
        order1.setFk_user_id(1);
        order1.setOrder_departure("兰溪");
        order1.setOrder_destination("金华");
        order1.setOrder_date("2017/2/10");
        order1.setOrder_number("111");
        order1.setOrder_remarks("无");
        order1.setOrder_distance(12);
        order1.setOrder_price(54);
        order1.setOrder_back(1);
        order1.setOrder_carry(1);
        order1.setOrder_followers(2);
        runningOrderList.add(order1);
    }
}
