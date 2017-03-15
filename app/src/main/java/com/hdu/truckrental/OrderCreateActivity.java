package com.hdu.truckrental;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.baidu.mapapi.model.LatLng;
import com.hdu.truckrental.dao.OrderDao;
import com.hdu.truckrental.domain.Order;
import com.hdu.truckrental.listener.UserDrawerItemClickListener;
import com.hdu.truckrental.map.DistanceCallBack;
import com.hdu.truckrental.map.LocationBean;
import com.hdu.truckrental.map.MapLocationActivity;
import com.hdu.truckrental.map.RoutePlan;

import static com.hdu.truckrental.tools.Check.ORDER_FOLLOWERS_ERROR;
import static com.hdu.truckrental.tools.Tool.getCurrentTime;
import static com.hdu.truckrental.tools.Tool.getOrderNumber;

/**
 * Created by Even on 2017/2/8.
 */

public class OrderCreateActivity extends AppCompatActivity implements View.OnClickListener{

    private OrderDao orderDao;
    private Integer state;//添加订单后返回的状态码
    private ActionBarDrawerToggle mUserDrawerToggle;
    private Toolbar userToolbar;
    private DrawerLayout mUserDrawerLayout;
    private ArrayAdapter arrayAdapter;
    private String[] userList = {"订单记录", "我的司机"};
    private ListView userLeftMenu;

    private Button mOrderSelectDeparture;
    private Button mOrderSelectDestination;
    private Button mOrderCreateBtn;
    private Button mOrderCreateAdvancedBtn;

    private LocationBean stLocation;
    private LocationBean enLocation;

    RoutePlan routePlan;
    private Integer mDistance;

    private static final int DEPARTURE_CODE = 1;
    private static final int DESTINATION_CODE = 2;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_create_order);

        //设置toolbar标题
        userToolbar = (Toolbar) findViewById(R.id.toolbar_user);
        userToolbar.setTitle("");
        this.setSupportActionBar(userToolbar);
/*        getSupportActionBar().setHomeButtonEnabled(false);//决定左上角的图标是否可以点击
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false); // 给左上角图标加上一个返回的图标*/

        mUserDrawerLayout = (DrawerLayout) findViewById(R.id.dl_user_left);

        //实现左边导航打开关闭的监听
        mUserDrawerToggle =
                new ActionBarDrawerToggle(this,mUserDrawerLayout,userToolbar,R.string.drawer_open,
                        R.string.drawer_close){
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
        mUserDrawerToggle.syncState();//什么作用？
        mUserDrawerLayout.addDrawerListener(mUserDrawerToggle);
        //设置菜单列表
        userLeftMenu = (ListView) findViewById(R.id.user_left_menu);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, userList);
        userLeftMenu.setAdapter(arrayAdapter);
        userLeftMenu.setOnItemClickListener(new UserDrawerItemClickListener());

        //出发地选择
        mOrderSelectDeparture = (Button) findViewById(R.id.order_select_departure);
        mOrderSelectDeparture.setOnClickListener(this);

        //目的地选择
        mOrderSelectDestination = (Button) findViewById(R.id.order_select_destination);
        mOrderSelectDestination.setOnClickListener(this);

        mOrderCreateBtn = (Button) findViewById(R.id.order_create_btn);
        mOrderCreateBtn.setOnClickListener(this);

        mOrderCreateAdvancedBtn = (Button) findViewById(R.id.order_create_advanced_btn);
        mOrderCreateAdvancedBtn.setOnClickListener(this);
    }

    //Button 的监听事件
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.order_select_departure:
                intent = new Intent(OrderCreateActivity.this, MapLocationActivity.class);
                startActivityForResult(intent,DEPARTURE_CODE);
                break;

            case R.id.order_select_destination:
                intent = new Intent(OrderCreateActivity.this, MapLocationActivity.class);
                startActivityForResult(intent,DESTINATION_CODE);
                break;

            case R.id.order_create_btn:
                //获取当前日期
                String startDate = getCurrentTime();
                state = OrderCreate(startDate);
                if(state<0){
                    ErrorShow(state);
                }
                break;

            case R.id.order_create_advanced_btn:
                //先跳转到另外一个日期选择界面

                //得到预约日期
                String advancedDate = "";
                state = OrderCreate(advancedDate);
                if(state<0){
                    ErrorShow(state);
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case DEPARTURE_CODE:
                if(resultCode == RESULT_OK){
                    SharedPreferences pref = getSharedPreferences("location",MODE_PRIVATE);
                    stLocation = new LocationBean(
                            pref.getString("city",""),
                            pref.getString("address",""),
                            pref.getFloat("lng",0),
                            pref.getFloat("lat",0));
                    Log.d("return_data",stLocation.toString());
                    mOrderSelectDeparture.setText(stLocation.getCity()+": "+stLocation.getAddress());
                    if(mOrderSelectDeparture.getText() != ""  && mOrderSelectDestination.getText() != ""){
                        getDistance(stLocation,enLocation);
                    }
                }
                break;
            case DESTINATION_CODE:
                if(resultCode == RESULT_OK){
                    SharedPreferences pref = getSharedPreferences("location",MODE_PRIVATE);
                    enLocation = new LocationBean(
                            pref.getString("city",""),
                            pref.getString("address",""),
                            pref.getFloat("lng",0),
                            pref.getFloat("lat",0));
                    Log.d("return_data",enLocation.toString());
                    mOrderSelectDestination.setText(enLocation.getCity()+": "+enLocation.getAddress());
                    if(mOrderSelectDeparture.getText() != ""  && mOrderSelectDestination.getText() != ""){
                        getDistance(stLocation,enLocation);
                    }
                }
                break;
        }
    }

    //匹配错误码并展示错误
    private void ErrorShow(Integer state){
        String errorMessage = "";
        switch (state){
            case ORDER_FOLLOWERS_ERROR://跟车人数错误
                errorMessage = "跟车人数不正确，请仔细阅读要求";
                break;
        }
        AlertDialog.Builder errorBuilder = new AlertDialog.Builder(OrderCreateActivity.this);
        errorBuilder.setTitle("错误提示");
        errorBuilder.setMessage(errorMessage);
        errorBuilder.setPositiveButton("确定",null);
        errorBuilder.show();
    }

    //得到路程
    private void getDistance(LocationBean st,LocationBean en){
        try {
            LatLng stLatLng = new LatLng(st.getLat(),st.getLng());
            LatLng enLatLng = new LatLng(en.getLat(),en.getLng());

            routePlan = new RoutePlan();
            routePlan.getDistance(stLatLng, enLatLng ,new DistanceCallBack() {
                @Override
                public void onDataReceiveSuccess(Integer distance) {
                    mDistance = distance;
                    Log.d("RouteResult",mDistance.toString());
                }

                @Override
                public void onDataReceiveFailed(Exception e) {
                    Log.d("return","failed");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
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
        //用户id
        Integer fkUserId = 0;//上一个活动传到这个活动中,这里最好设置为私有成员
        order.setFk_user_id(fkUserId);
        //订单号
        order.setOrder_number(getOrderNumber(startDate,fkUserId.toString()));//用户id(最多10位)+时间（最多14位）
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
        orderDao = new OrderDao(OrderCreateActivity.this);
        state = orderDao.addOrder(order);
        return state;
    }
}
