package com.hdu.truckrental.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hdu.truckrental.R;
import com.hdu.truckrental.dao.UserDao;
import com.hdu.truckrental.domain.Order;
import com.hdu.truckrental.domain.User;

import java.util.List;

import static com.hdu.truckrental.R.id.order_state_view;
import static com.hdu.truckrental.tools.Tool.getDisplayTime;
import static com.hdu.truckrental.tools.Tool.isAdvancedDate;


/**
 * Created by hanjianhao on 17/2/14.
 * Altered by Even
 */

public class MyAdapter extends BaseAdapter {

    protected Activity context;
    protected Order mOrder;
    protected UserDao userDao;
    protected User user;
    protected List<Order> mOrderList;
    protected String [] mDepartureAddr;
    protected String [] mDestinationAddr;
    private static final String TAG = "MyAdapter";

    public MyAdapter(Activity context, List<Order> orderList) {
        this.context = context;
        mOrderList = orderList;
    }

    @Override
    public int getCount() {
        return mOrderList.size();
    }

    @Override
    public Object getItem(int position) {
        return mOrderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        mOrder = mOrderList.get(position);
        userDao = new UserDao(context);
        user = userDao.findUserById(mOrder.getFk_user_id());
        ViewHolder holder;
        if (convertView == null){
            convertView =
                    LayoutInflater.from(context).inflate(R.layout.listview_order,null);
            holder = new ViewHolder();
            holder.tv_departure_city = (TextView) convertView.findViewById(R.id.departure_city_view);
            holder.tv_departure_address =
                    (TextView) convertView.findViewById(R.id.departure_address_view);
            holder.tv_destination_city =
                    (TextView) convertView.findViewById(R.id.destination_city_view);
            holder.tv_destination_address =
                    (TextView) convertView.findViewById(R.id.destination_address_view);
            holder.tv_start_date = (TextView) convertView.findViewById(R.id.order_start_date_view);
            holder.tv_user_level = (RatingBar) convertView.findViewById(R.id.user_level_view);
            holder.tv_order_state = (TextView) convertView.findViewById(order_state_view);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
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

        return convertView;
    }

    static class ViewHolder{
        TextView tv_departure_city;
        TextView tv_departure_address;
        TextView tv_destination_city;
        TextView tv_destination_address;
        TextView tv_start_date;
        RatingBar tv_user_level;
        TextView tv_order_state;
        ImageButton view_btn;
    }
}
