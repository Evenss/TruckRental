package com.hdu.truckrental.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hdu.truckrental.R;
import com.hdu.truckrental.dao.UserDao;
import com.hdu.truckrental.domain.Order;

import java.util.List;

import static com.hdu.truckrental.tools.Tool.getDisplayTime;
import static com.hdu.truckrental.tools.Tool.isAdvancedDate;

/**
 * Created by hanjianhao on 17/2/14.
 * Altered by Even
 */

public class RunningOrderAdapter extends MyAdapter {

    private static final String TAG = "RunningOrderAdapter";
    //这里返回码用作回调函数的判断，但是并不能使用回调函数，必须是Activity才能使用
    private static final int CALL_REQUEST_CODE = 123;

    public RunningOrderAdapter(Activity context, List<Order> orderList) {
        super(context, orderList);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        mOrder = mOrderList.get(position);
        userDao = new UserDao(context);
        user = userDao.findUserById(mOrder.getFk_user_id());
        ViewHolder holder;
        if (convertView == null){
            convertView =
                    LayoutInflater.from(context).inflate(R.layout.listview_running_orders,null);
            holder = new RunningOrderAdapter.ViewHolder();
            holder.tv_departure_city =
                    (TextView) convertView.findViewById(R.id.running_departure_city_view);
            holder.tv_departure_address =
                    (TextView) convertView.findViewById(R.id.running_departure_addr_view);
            holder.tv_destination_city =
                    (TextView) convertView.findViewById(R.id.running_destination_city_view);
            holder.tv_destination_address =
                    (TextView) convertView.findViewById(R.id.running_destination_addr_view);
            holder.tv_start_date =
                    (TextView) convertView.findViewById(R.id.running_order_start_date_view);
            holder.tv_user_level =
                    (RatingBar) convertView.findViewById(R.id.running_user_level_view);
            holder.tv_order_state =
                    (TextView) convertView.findViewById(R.id.running_order_state_view);
            holder.view_btn=  (ImageButton) convertView.findViewById(R.id.running_phone_btn);
            convertView.setTag(holder);
        }else {
            holder = (RunningOrderAdapter.ViewHolder) convertView.getTag();
        }

        mDepartureAddr = mOrder.getOrder_departure().split(":");
        mDestinationAddr = mOrder.getOrder_destination().split(":");
        holder.tv_start_date.setText(getDisplayTime(mOrder.getOrder_start_date()));
        holder.tv_departure_city.setText(" "+mDepartureAddr[0]);
        holder.tv_departure_address.setText(mDepartureAddr[1]);
        holder.tv_destination_city.setText(" "+mDestinationAddr[0]);
        holder.tv_destination_address.setText(mDestinationAddr[1]);
        holder.tv_user_level.setRating(user.getUser_level());
        //设置图标
        if(!isAdvancedDate(mOrder.getOrder_start_date(),mOrder.getOrder_date())){
            holder.tv_order_state.setText(R.string.prompt_immediate_order);
            int redColor = ContextCompat.getColor(context,R.color.red);
            holder.tv_order_state.setTextColor(redColor);
        }else {
            holder.tv_order_state.setText(R.string.prompt_appointment_order);
            Drawable photo = ContextCompat.getDrawable(context,R.drawable.order_state_advanced);
            photo.setBounds(0, 0, photo.getMinimumWidth(), photo.getMinimumHeight());
            holder.tv_order_state.setCompoundDrawables(null,photo,null,null);
            int greenColor = ContextCompat.getColor(context,R.color.green);
            holder.tv_order_state.setTextColor(greenColor);
        }

        holder.view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final android.view.View view) {
                //危险权限，需动态申请
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"  + user.getUser_phone()));
                if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ActivityCompat.requestPermissions(context,
                                new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST_CODE);
                    }
                }else{
                    context.startActivity(intent);
                }
            }
        });
        return convertView;
    }
}
