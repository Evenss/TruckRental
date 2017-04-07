package com.hdu.truckrental.driver;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hdu.truckrental.R;
import com.hdu.truckrental.adapter.RunningOrderAdapter;
import com.hdu.truckrental.dao.OrderDao;
import com.hdu.truckrental.domain.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yjt on 2017/2/11.
 */

public class DriverRunningOrdersShowFragment extends Fragment{
    private static final String TAG = "DriverRunningOrdersShow";
    private static final int RUNNING = 1;

    private ListView runningOrderLv;
    private RunningOrderAdapter runningOrderAdapter;
    private View view;

    private List<Order> mOrderList = new ArrayList<>();;
    private List<Order> allOrderList;
    private OrderDao orderDao;

    private int totalNum,pageNum;
    private int pageSize = 15;
    private int currentPage = 1;
    private boolean isDivPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_driver_running_orders_show,container,false);
        return view;
    }

    @Override
    public void onResume() {
        runningOrderLv = (ListView) view.findViewById(R.id.running_orders_ListView);
        put_info_list();
        runningOrderAdapter = new RunningOrderAdapter(getActivity(),mOrderList);
        runningOrderLv.setAdapter(runningOrderAdapter);
        if(mOrderList.size() == 0){
            Toast toast = Toast.makeText(getActivity(), "无正在运行订单",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }

        //item点击事件
        runningOrderLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ListView listView = (ListView) parent;
                Order order = (Order) listView.getItemAtPosition(position);
                int order_id = order.getOrder_id();
                Intent intent = new Intent(getActivity().getApplication(),
                        DriverRunningOrdersDetailsShowActivity.class);
                intent.putExtra("order_id",order_id);
                startActivity(intent);
            }
        });
        super.onResume();
    }

    protected void put_info_list(){
        orderDao = new OrderDao(getActivity());
        allOrderList = orderDao.findAllOrder();
        mOrderList.clear();
        for(Order order:allOrderList){
            if(order.getOrder_state() == RUNNING){
                mOrderList.add(order);
            }
        }
    }
}
