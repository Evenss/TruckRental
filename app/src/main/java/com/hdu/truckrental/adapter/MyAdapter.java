package com.hdu.truckrental.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hdu.truckrental.R;
import com.hdu.truckrental.domain.Order;

import java.util.List;


/**
 * Created by hanjianhao on 17/2/14.
 */

public class MyAdapter extends BaseAdapter {

    private Context context;
    private List<Order> mOrder;
    private static final String TAG = "MyAdapter";

    public MyAdapter(Context context, List<Order> order) {
        this.context = context;
        mOrder = order;
    }

    @Override
    public int getCount() {
        return mOrder.size();
    }

    @Override
    public Object getItem(int position) {
        return mOrder.get(position);
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
        ViewHolder holder = null;
        if (convertView == null){
            convertView =
                    LayoutInflater.from(context).inflate(R.layout.listview_available_order,null);
            holder = new ViewHolder();
            holder.tv_departure_destination =
                    (TextView) convertView.findViewById(R.id.departure_destination_view);
            holder.tv_start_date = (TextView) convertView.findViewById(R.id.order_start_date_view);
            holder.tv_user_level = (TextView) convertView.findViewById(R.id.user_level_view);
            holder.tv_order_state = (TextView) convertView.findViewById(R.id.order_state_view);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_departure_destination.setText(mOrder.get(position).getOrder_departure()
                + "--->" + mOrder.get(position).getOrder_destination());
        holder.tv_start_date.setText(mOrder.get(position).getOrder_start_date());
        holder.tv_user_level.setText("用户id:"+mOrder.get(position).getFk_user_id()+"");
        if( mOrder.get(position).getOrder_start_date().equals(mOrder.get(position).getOrder_date()) ){
            holder.tv_order_state.setText(R.string.prompt_immediate_order);
            //这边有版本兼容性问题，故使用过时api
            holder.tv_order_state.setTextColor(context.getResources().getColor(R.color.red));
            //holder.tv_order_state.setCompoundDrawables(null,context.getResources().getDrawable(R.drawable.order_state1),null,null);
        }else {
            holder.tv_order_state.setText(R.string.prompt_appointment_order);
            holder.tv_order_state.setTextColor(context.getResources().getColor(R.color.green));
            //holder.tv_order_state.setCompoundDrawables(null,context.getResources().getDrawable(R.drawable.order_state2,null),null,null);
        }

        return convertView;
    }

    static class ViewHolder{
        TextView tv_departure_destination,tv_start_date,tv_user_level;
        TextView tv_order_state;
    }
}
