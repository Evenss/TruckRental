package com.hdu.truckrental.driver;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hdu.truckrental.R;
import com.hdu.truckrental.adapter.MyAdapter;
import com.hdu.truckrental.dao.OrderDao;
import com.hdu.truckrental.domain.Order;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Even on 2017/3/16.
 * 司机所有订单展示
 */

public class DriverAllOrdersShowFragment extends Fragment {
    private ListView allOrdersListView;
    private MyAdapter allOrdersAdapter;
    private View view;

    private List<Order> mOrderList = new ArrayList<>();
    private OrderDao orderDao;
    private int driverId;

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
        view = inflater.inflate(R.layout.fragment_driver_all_orders_show,container,false);
        return view;

    }

    @Override
    public void onResume() {
        allOrdersListView = (ListView) view.findViewById(R.id.all_orders_ListView);
        put_info_list();
        allOrdersAdapter = new MyAdapter(getActivity(),mOrderList);
        allOrdersListView.setAdapter(allOrdersAdapter);
        if(mOrderList.size() == 0){
            Toast toast = Toast.makeText(getActivity(), "无订单记录",Toast.LENGTH_LONG);
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
                Intent intent = new Intent(getActivity(),
                        DriverAllOrdersDetailsShowActivity.class);
                intent.putExtra("order_id",order_id);
                startActivity(intent);
            }
        });
        super.onResume();
    }
    private void put_info_list(){
        orderDao = new OrderDao(getActivity());
        SharedPreferences pref = getActivity().getSharedPreferences("driver",MODE_PRIVATE);
        driverId = pref.getInt("id",-1);
        if(driverId != -1){
            mOrderList.clear();
            mOrderList = orderDao.findOrderByDriverId(driverId);
        }
    }
}
