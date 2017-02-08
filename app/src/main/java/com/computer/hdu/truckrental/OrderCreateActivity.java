package com.computer.hdu.truckrental;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by Even on 2017/2/8.
 */

public class OrderCreateActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_create_order);

    }
}
