package com.hdu.truckrental.driver;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.hdu.truckrental.R;

/**
 * Created by Even on 2017/3/16.
 * 抢单页面
 */

public class DriverOrderActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{
    private static final String TAG = "DriverMainActivity";
    private static final int RUNNING_ORDER = 0;
    private static final int SELECT_ORDER = 1;
    private static final int ALL_ORDER = 2;
    private static final int UNRECEIVED = 0;

    //顶部标题
    private TextView mToolbarTitle;
    //侧滑菜单
    private Toolbar driverToolbar;
    private DrawerLayout driverDrawerLayout;
    private ActionBarDrawerToggle driverDrawerToggle;
    private NavigationView driverLeftMenu;
    //底端导航栏
    private BottomNavigationBar mBottomNavigationBar;
    //switch
    private Switch mySwitch;
    private boolean isWork = false;

    //碎片
    private DriverRunningOrdersShowFragment mRunningOrderFragment;
    private DriverAllOrdersShowFragment mAllOrderFragment;
    private DriverOrderShowFragment mOrderShowFragment;

    //UI
    private ImageView mRestingCup;
    private TextView mRestingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_order);
        initView();

        //标题栏
        driverToolbar.setTitle("");
        this.setSupportActionBar(driverToolbar);
        //导航栏
        getSupportActionBar().setHomeButtonEnabled(true);//设置返回键可用
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true); //创建返回键，并实现打开关闭监听
        driverDrawerToggle = new ActionBarDrawerToggle(this,driverDrawerLayout,driverToolbar,
                R.string.drawer_open,R.string.drawer_close) {
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
        driverDrawerToggle.syncState();
        driverDrawerLayout.addDrawerListener(driverDrawerToggle);

        //底部导航栏
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.btm_order_running,"待完成"))
                .addItem(new BottomNavigationItem(R.drawable.btm_select_order,"抢单"))
                .addItem(new BottomNavigationItem(R.drawable.btm_order,"所有订单"))
                .setFirstSelectedPosition(1)
                .initialise();
        mBottomNavigationBar.setTabSelectedListener(this);

        /**
         * switch点击事件
         */
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    isWork = true;
                    hideRestingText();
                    replaceFragment(mOrderShowFragment);
                } else {
                    isWork = false;
                    removeFragment(mOrderShowFragment);
                    showRestingText();
                }
            }
        });

        /**
         * 左侧导航栏点击事件
         */
        driverLeftMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    case R.id.nav_info_driver:
                        intent = new Intent(DriverOrderActivity.this,
                                DriverInfoShowActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_change_pwd_driver:
                        intent = new Intent(DriverOrderActivity.this,
                                DriverChangePwdActivity.class);
                        startActivity(intent);
                        break;
                }
                driverDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    /*
     * 底部导航栏点击事件
     */
    @Override
    public void onTabSelected(int position) {
        switch (position){
            case RUNNING_ORDER:
                hideRestingText();
                mToolbarTitle.setText("待完成订单");
                if(mRunningOrderFragment == null){
                    mRunningOrderFragment = new DriverRunningOrdersShowFragment();
                }
                replaceFragment(mRunningOrderFragment);
                mySwitch.setVisibility(View.GONE);
                break;
            case SELECT_ORDER:
                mToolbarTitle.setText("货滴滴");
                if(mOrderShowFragment == null){
                    mOrderShowFragment = new DriverOrderShowFragment();
                }
                replaceFragment(mOrderShowFragment);
                if(!isWork){
                    removeFragment(mOrderShowFragment);
                    showRestingText();
                }
                mySwitch.setVisibility(View.VISIBLE);
                break;
            case ALL_ORDER:
                hideRestingText();
                mToolbarTitle.setText("所有订单");
                if(mAllOrderFragment == null){
                    mAllOrderFragment = new DriverAllOrdersShowFragment();
                }
                replaceFragment(mAllOrderFragment);
                mySwitch.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    private void initView(){
        driverToolbar = (Toolbar) findViewById(R.id.toolbar_driver);
        driverLeftMenu = (NavigationView) findViewById(R.id.nav_view_driver);
        driverDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout_driver);
        mToolbarTitle = (TextView)findViewById(R.id.toolbar_title);
        mySwitch = (Switch) findViewById(R.id.switch1);

        mRestingCup = (ImageView) findViewById(R.id.resting_cup);
        mRestingText = (TextView) findViewById(R.id.resting_text);

        mBottomNavigationBar = (BottomNavigationBar)findViewById(R.id.bottom_navigation_bar);
        setDefaultFragment();
    }

    //设置默认碎片
    private void setDefaultFragment(){
        if(mOrderShowFragment == null){
            mOrderShowFragment = new DriverOrderShowFragment();
        }
    }
    //替换碎片
    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }
    //移除碎片
    private void removeFragment(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }
    //隐藏休息中样式
    private void hideRestingText(){
        mRestingCup.setVisibility(View.GONE);
        mRestingText.setVisibility(View.GONE);
    }
    //显示休息中样式
    private void showRestingText(){
        mRestingCup.setVisibility(View.VISIBLE);
        mRestingText.setVisibility(View.VISIBLE);
    }
}
