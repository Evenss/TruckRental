package com.hdu.truckrental.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hdu.truckrental.R;
import com.hdu.truckrental.dao.UserDao;
import com.hdu.truckrental.domain.Order;
import com.hdu.truckrental.domain.User;

import java.util.List;

import static com.hdu.truckrental.R.id.order_state_view;
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
            holder.tv_departure = (TextView) convertView.findViewById(R.id.departure_view);
            holder.tv_destination = (TextView) convertView.findViewById(R.id.destination_view);
            holder.tv_start_date = (TextView) convertView.findViewById(R.id.order_start_date_view);
            holder.tv_user_level = (TextView) convertView.findViewById(R.id.user_level_view);
            holder.tv_order_state = (TextView) convertView.findViewById(order_state_view);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_start_date.setText(mOrder.getOrder_start_date());
        holder.tv_departure.setText(mOrder.getOrder_departure());
        holder.tv_destination.setText(mOrder.getOrder_destination());
        holder.tv_user_level.setText("用户等级:"+user.getUser_level());
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
        TextView tv_departure;
        TextView tv_destination;
        TextView tv_start_date;
        TextView tv_user_level;
        TextView tv_order_state;
        ImageButton view_btn;
    }
}
