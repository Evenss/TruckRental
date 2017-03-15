package com.hdu.truckrental;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hdu.truckrental.dao.UserDao;
import com.hdu.truckrental.domain.User;
import com.hdu.truckrental.tools.Check;

import static com.hdu.truckrental.tools.Check.USER_DUPLICATE_ERROR;

/**
 * Created by Even on 2017/1/24.
 */

public class UserRegisterActivity extends Activity implements View.OnClickListener{
    private EditText mUserPhone;
    private EditText mUserValidationCode;
    private Button mUserValidate;
    private Button mUserRegistered;

    private String code;
    private UserDao userDao ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        initView();
        setListener();
    }
    private void initView(){
        mUserPhone = (EditText)findViewById(R.id.user_phone);
        mUserValidationCode = (EditText)findViewById(R.id.user_validation_code);
        mUserValidate = (Button)findViewById(R.id.user_validate);
        mUserRegistered = (Button)findViewById(R.id.user_registered);
    }

    private void setListener(){
        mUserValidate.setOnClickListener(this);
        mUserRegistered.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_validate:
                //获取验证码
                Toast.makeText(UserRegisterActivity.this,"验证码：6666",Toast.LENGTH_SHORT).show();
                code = "6666";
                break;
            case R.id.user_registered:
                if(Check.checkPhone(mUserPhone.getText().toString())!=Check.SUCCEED){
                    Toast.makeText(UserRegisterActivity.this,"手机输入有误",Toast.LENGTH_SHORT).show();
                }
                if(!mUserValidationCode.getText().toString().equals(code)){
                    Toast.makeText(UserRegisterActivity.this,"验证码有误",Toast.LENGTH_SHORT).show();
                }
                registerUser(mUserPhone.getText().toString());
                break;
        }
    }

    private void registerUser(String phone){

        User user = new User();
        user.setUser_phone(phone);
        user.setUser_level(5);//默认用户初始等为5
        userDao = new UserDao(UserRegisterActivity.this);
        switch (userDao.addUser(user)){
            case USER_DUPLICATE_ERROR:
                Toast.makeText(UserRegisterActivity.this,"该手机号已被注册",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
