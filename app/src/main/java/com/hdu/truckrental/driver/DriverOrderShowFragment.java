package com.hdu.truckrental.driver;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hdu.truckrental.R;
import com.hdu.truckrental.adapter.MyAdapter;
import com.hdu.truckrental.dao.DriverDao;
import com.hdu.truckrental.dao.OrderDao;
import com.hdu.truckrental.domain.Driver;
import com.hdu.truckrental.domain.Order;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.hdu.truckrental.driver.DriverOrderDetailsShowActivity.UNRECEIVED;

/**
 * Created by Even on 2017/3/31.
 */

public class DriverOrderShowFragment extends Fragment {

    private OrderDao orderDao;
    private Order order;
    private List<Order> orderList;
    private DriverDao driverDao;
    private Driver driver;
    private List<Order>  totalList = new ArrayList<>();
    //订单详情
    private MyAdapter myAdapter;
    private ListView myListView;
    //下拉刷新
    private SwipeRefreshLayout driverSwipeLayout;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_driver_order_show,container,false);
        initView();
        /**
         * 下拉刷新
         */
        driverSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                put_info_list();
            }
        });
        driverSwipeLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        /**
         * listView点击事件
         */
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ListView listView = (ListView) parent;
                order = (Order) listView.getItemAtPosition(position);
                int order_id = order.getOrder_id();
                Intent intent = new Intent(getActivity(),
                        DriverOrderDetailsShowActivity.class);
                intent.putExtra("order_id",order_id);
                startActivity(intent);
            }
        });
        return view;

    }

    @Override
    public void onResume() {
        put_info_list();
        super.onResume();
    }

    void initView(){
        myListView = (ListView) view.findViewById(R.id.main_driver_list);
        driverSwipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.SwipeRefreshLayout_driver);
    }

    private void put_info_list(){
        totalList.clear();
        orderDao = new OrderDao(getActivity());
        orderList = orderDao.findAllOrder();
        SharedPreferences pref = getActivity().getSharedPreferences("driver",MODE_PRIVATE);
        driverDao = new DriverDao(getActivity());
        driver = driverDao.findDriverById(pref.getInt("id",-1));
        for(Order order:orderList){
            //展示符合相同车型的订单
            if(order.getOrder_car_type() == driver.getDriver_car_type() &&
                    order.getOrder_state() == UNRECEIVED){
                totalList.add(order);
            }
        }
        driverSwipeLayout.setRefreshing(false);
        if(totalList.size() == 0){
            Snackbar.make(getActivity().getWindow().getDecorView(),
                    "暂无订单",Snackbar.LENGTH_SHORT).show();
        }
        myAdapter = new MyAdapter(getActivity(), totalList);
        myListView.setAdapter(myAdapter);
    }
}
