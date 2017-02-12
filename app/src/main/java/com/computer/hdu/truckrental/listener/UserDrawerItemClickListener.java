package com.computer.hdu.truckrental.listener;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Even on 2017/2/12.
 */

public class UserDrawerItemClickListener implements ListView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectItem(position);
    }


    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        switch (position){//这里写左侧导航栏的业务逻辑
            case 0:
                Log.d("hello","订单记录");
                break;
            case 1:
                Log.d("hello", "我的司机");
                break;
        }
    }

}
