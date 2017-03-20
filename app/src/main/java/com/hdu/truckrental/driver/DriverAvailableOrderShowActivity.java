package com.hdu.truckrental.driver;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.hdu.truckrental.R;
import com.hdu.truckrental.adapter.MyAdapter;
import com.hdu.truckrental.dao.OrderDao;
import com.hdu.truckrental.domain.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Even on 2017/3/16.
 * 抢单页面
 */

public class DriverAvailableOrderShowActivity extends AppCompatActivity {
    private OrderDao orderDao;
    private Order order;
    private List<Order> orderList;

    //侧滑菜单
    private Toolbar driverToolbar;
    private DrawerLayout driverDrawerLayout;
    private ActionBarDrawerToggle driverDrawerToggle;
    private ListView driverLeftMenu;
    private String[] leftList = {"我的信息", "密码修改"};
    private ArrayAdapter leftAdapter;
    //switch
    private Switch mySwitch;
    //下拉刷新
    private static final int REFRESH_COMPLETE = 0X110;//????????????????????????
    private SwipeRefreshLayout driverSwipeLayout;
    //订单详情
    private MyAdapter myAdapter;
    private ListView myListView;
    private List<Order> totalList = new ArrayList<>();

    //分页处理
    private int totalNum,pageNum;
    private int currentPage = 1;
    private int pageSize = 10;
    private boolean isDivPage;
    private static final String TAG = "DriverMainActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_available_order_show);
        initView();

        //标题栏
        driverToolbar.setTitle("driverToolbar");
        driverToolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        this.setSupportActionBar(driverToolbar);
        //导航栏
        getSupportActionBar().setHomeButtonEnabled(true);//设置返回键可用
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true); //创建返回键，并实现打开关/闭监听
        driverDrawerToggle = new ActionBarDrawerToggle(this,driverDrawerLayout,driverToolbar,
                R.string.drawer_open,R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        driverDrawerToggle.syncState();
        driverDrawerLayout.addDrawerListener(driverDrawerToggle);
        //设置菜单列表
        leftAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, leftList);
        driverLeftMenu.setAdapter(leftAdapter);

        /**
         * switch点击事件???????????????????????
         */
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    //put_info_list();
                    myAdapter.notifyDataSetChanged();
                } else {
                    currentPage = 1;
                    totalList.clear();
                    myAdapter.notifyDataSetChanged();
                }
            }
        });

        /**
         * 下拉刷新
         */
        driverSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 1500);//????????????????????
            }
        });
        driverSwipeLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        driverLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i)
                {
                    case 0:
                        Intent intent = new Intent(DriverAvailableOrderShowActivity.this,
                                DriverInfoShowActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(DriverAvailableOrderShowActivity.this,
                                DriverChangePwdActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        /**
         * listView点击事件
         */
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ListView listView = (ListView) parent;
                order = (Order) listView.getItemAtPosition(position);
                int order_id = order.getOrder_id();
                Intent intent = new Intent(DriverAvailableOrderShowActivity.this,
                        DriverOrderDetailsShowActivity.class);
                intent.putExtra("order_id",order_id);
                startActivity(intent);
            }
        });
    }

    private void initView(){
        driverToolbar = (Toolbar) findViewById(R.id.toolbar_driver);
        driverLeftMenu = (ListView) findViewById(R.id.driver_left_menu);
        driverDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout_driver);
        mySwitch = (Switch) findViewById(R.id.switch1);

        myListView = (ListView) findViewById(R.id.main_driver_list);
        driverSwipeLayout = (SwipeRefreshLayout)findViewById(R.id.SwipeRefreshLayout_driver);
        myAdapter = new MyAdapter(this, totalList);
        myListView.setAdapter(myAdapter);

    }

    public void show_orders(View view){
        Intent intent;
        switch (view.getId())
        {
            case R.id.driver_orders_btn:
                intent = new Intent(DriverAvailableOrderShowActivity.this,
                        DriverRunningOrdersShowActivity.class);
                startActivity(intent);
                break;
            case R.id.driver_record_btn:
                intent = new Intent(DriverAvailableOrderShowActivity.this,
                        DriverAllOrdersShowActivity.class);
                startActivity(intent);
                break;
        }
    }

/*    private void put_info_Bundle(Bundle bundle, Order order){
        bundle.putString("运货时间", order.getOrder_start_date());
        bundle.putString("下单时间", order.getOrder_date());
        bundle.putString("订单号", order.getOrder_number());
        bundle.putString("出发地址", order.getOrder_departure());
        bundle.putString("目的地址", order.getOrder_destination());
        bundle.putString("备注", order.getOrder_remarks());
        bundle.putFloat("路程数", order.getOrder_distance());
        bundle.putFloat("金额", order.getOrder_price());
        bundle.putInt("是否回程", order.getOrder_back());
        bundle.putInt("是否搬运", order.getOrder_carry());
        bundle.putInt("跟车人数", order.getOrder_followers());
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
        totalList.add(order1);
    }*/
    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case REFRESH_COMPLETE:
                    orderDao = new OrderDao(DriverAvailableOrderShowActivity.this);
                    orderList = orderDao.findAllOrder();
                    for(Order order:orderList){
                        totalList.add(order);
                    }
                    myAdapter.notifyDataSetChanged();
                    driverSwipeLayout.setRefreshing(false);
                    break;
            }
        }
    };
}
