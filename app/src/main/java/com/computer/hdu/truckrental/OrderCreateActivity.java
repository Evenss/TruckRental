package com.computer.hdu.truckrental;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.computer.hdu.truckrental.dao.OrderDao;
import com.computer.hdu.truckrental.domain.Order;

/**
 * Created by Even on 2017/2/8.
 */

public class OrderCreateActivity extends Activity {

    private OrderDao orderDao;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_create_order);

        Button mOrderCreateBtn = (Button) findViewById(R.id.order_create_btn);
        mOrderCreateBtn.setOnClickListener(new View.OnClickListener() {
            //获取当前日期
            String startDate = "0";
            @Override
            public void onClick(View v) {
                if(OrderCreate(startDate)<0){
                    ErrorShow();
                }
            }
        });

        Button mOrderCreateAdvancedBtn = (Button) findViewById(R.id.order_create_advanced_btn);
        mOrderCreateAdvancedBtn.setOnClickListener(new View.OnClickListener() {
            //先跳转到另外一个日期选择界面

            //得到预约日期
            String startDate;
            @Override
            public void onClick(View v) {
                if(OrderCreate(startDate)<0){
                    ErrorShow();
                }
            }
        });
    }

    //匹配错误码并展示错误
    private void ErrorShow(){

    }

    //将价格计算单独提出来，方便以后优化计算
    private float getOrderPrice(float distance,String car_type){
        return 0;
    }
    //创建订单
    private Integer OrderCreate(String startDate){
        //get info from page
        RadioGroup mOrderCarTypeRg = (RadioGroup) findViewById(R.id.order_car_type);
        RadioButton mOrderCarTypeRb = (RadioButton)
                findViewById(mOrderCarTypeRg.getCheckedRadioButtonId());
        Button mOrderSelectDeparture = (Button) findViewById(R.id.order_select_departure);
        Button mOrderSelectDestination = (Button) findViewById(R.id.order_select_destination);
        CheckBox mOrderBackCb = (CheckBox) findViewById(R.id.order_back);
        CheckBox mOrderCarryCb = (CheckBox) findViewById(R.id.order_carry);
        Spinner mOrderFollowers = (Spinner) findViewById(R.id.order_followers);
        EditText mOrderRemarksEd = (EditText) findViewById(R.id.order_remarks);

        //info to order class
        Order order = new Order();
        String dateTime = "";//需要计算
        //订单号
        String orderNumber = "";//需要计算得到，邮编（6位）+用户id(最多10位)+时间（最多14位）
        order.setOrder_number(orderNumber);
        //用户id
        Integer fkUserId = 0;//上一个活动传到这个活动中,这里最好设置为私有成员
        order.setFk_user_id(fkUserId);
        //出发地
        order.setOrder_departure(mOrderSelectDeparture.getText().toString());
        //目的地
        order.setOrder_destination(mOrderSelectDestination.getText().toString());
        //备注
        order.setOrder_remarks(mOrderRemarksEd.getText().toString());
        //距离
        float orderDistance = 0;//上个活动传到这里，同上
        order.setOrder_distance(orderDistance);
        //车型
        switch (mOrderCarTypeRb.getText().toString()){
            case "小面包车":
                order.setOrder_car_type(1);
                break;
            case "中面包车":
                order.setOrder_car_type(2);
                break;
            case "小货车":
                order.setOrder_car_type(3);
                break;
            case "中货车":
                order.setOrder_car_type(4);
                break;
        }
        //价格
        float orderPrice = getOrderPrice(orderDistance,mOrderCarTypeRb.getText().toString());
        order.setOrder_price(orderPrice);
        //状态
        order.setOrder_state(1);
        //订单生成时间
        order.setOrder_date(dateTime);
        //回程
        if(mOrderBackCb.isChecked()){
            order.setOrder_back(1);
        }else{
            order.setOrder_back(0);
        }
        //搬运
        if(mOrderCarryCb.isChecked()){
            order.setOrder_carry(1);
        }else{
            order.setOrder_carry(0);
        }
        //跟车人数
        //这里前端页面只能用string，但string不能转换为int，数据库里面是int类型，这里需要优化
        switch (mOrderFollowers.getSelectedItem().toString()){
            case "1":
                order.setOrder_followers(1);
                break;
            case "2":
                order.setOrder_followers(2);
                break;
            case "3":
                order.setOrder_followers(3);
                break;
            default:
                order.setOrder_followers(0);
        }
        //运货日期
        order.setOrder_start_date(startDate);//这里的string日期转换为datetime日期应该写在dao里面

        //info to db
        Integer state = 0;
        state = orderDao.addOrder(order);
        return state;
    }
}
