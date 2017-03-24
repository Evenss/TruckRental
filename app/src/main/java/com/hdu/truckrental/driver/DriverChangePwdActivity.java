package com.hdu.truckrental.driver;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hdu.truckrental.R;
import com.hdu.truckrental.dao.DriverDao;
import com.hdu.truckrental.domain.Driver;
import com.hdu.truckrental.tools.Encrypt;

/**
 * Created by Even on 2017/3/16.
 * 司机修改密码
 */

public class DriverChangePwdActivity extends AppCompatActivity {

    private DriverDao driverDao;
    private Driver driver;

    private EditText oldPwdEditText;
    private EditText newPwdEditText;
    private EditText confirmPwdEditText;
    private Button confirmChangeButton;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_change_pwd);
        initView();

        SharedPreferences pref = getSharedPreferences("driver",MODE_PRIVATE);
        driverDao = new DriverDao(this);
        driver = driverDao.findDriverById(pref.getInt("id",-1));
        password = driver.getDriver_pwd();

        //监听事件
        confirmPwdEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptChange();
                    return true;
                }
                return false;
            }
        });

        confirmChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptChange();
            }
        });

    }

    private void initView(){
        oldPwdEditText = (EditText)findViewById(R.id.driver_old_pwd);
        newPwdEditText = (EditText)findViewById(R.id.driver_new_pwd);
        confirmPwdEditText = (EditText)findViewById(R.id.driver_confirm_pwd);
        confirmChangeButton = (Button)findViewById(R.id.confirm_change_btn);
    }

    //验证函数
    private void attemptChange(){

        oldPwdEditText.setError(null);
        newPwdEditText.setError(null);
        confirmPwdEditText.setError(null);


        String oldPwd = oldPwdEditText.getText().toString();
        String newPwd = newPwdEditText.getText().toString();
        String confirmPwd = confirmPwdEditText.getText().toString();
        View focusView = null;
        boolean cancel = false;

        if (TextUtils.isEmpty(confirmPwd)){
            confirmPwdEditText.setError(getString(R.string.prompt_input_pwd));
            focusView = confirmPwdEditText;
            cancel = true;
        }else if (!newPwd.equals(confirmPwd)){
            confirmPwdEditText.setError(getString(R.string.prompt_confirm_pwd_error));
            focusView = confirmPwdEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(newPwd)){
            newPwdEditText.setError(getString(R.string.prompt_input_pwd));
            focusView = newPwdEditText;
            cancel = true;
        }else if(!isPasswordValid(newPwd)){
            newPwdEditText.setError(getString(R.string.prompt_pwd_invalid));
            focusView = newPwdEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(oldPwd)){
            oldPwdEditText.setError(getString(R.string.prompt_input_pwd));
            focusView = oldPwdEditText;
            cancel = true;
        }

        if (cancel){
            focusView.requestFocus();
        }else {
            if(password.equals(Encrypt.getEncryption(oldPwd))){
                password = Encrypt.getEncryption(newPwd);
                Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
                //mHandler.postDelayed(r, 100);//延时
                driverDao.updateDriverPwd(driver.getDriver_id(),password);
                finish();
            }else {
                oldPwdEditText.setError(getString(R.string.prompt_old_pwd_error));
                oldPwdEditText.requestFocus();
            }
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}