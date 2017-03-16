package com.hdu.truckrental.driver;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hdu.truckrental.LoginActivity;
import com.hdu.truckrental.R;
import com.hdu.truckrental.dao.DriverDao;
import com.hdu.truckrental.tools.Check;
import com.hdu.truckrental.tools.Encrypt;
import com.hdu.truckrental.user.OrderCreateActivity;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * Created by Even on 2017/1/25.
 */

public class DriverLoginActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener,TextView.OnEditorActionListener {
    private String tag = "DriverLoginActivity";
    /**
     * Id to identity READ_CONTACTS permission request.
     * 读取用户通讯录
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     * 声明用户登录引用，方便随时撤销登录请求
     */
    private DriverLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mAccountView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    //Button
    private Button mAccountSignInBtn;
    private ImageButton mExchangeToUserBtn;

    private DriverDao driverDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_driver);
        initView();
        // Set up the login form.
        populateAutoComplete();
        setListener();

        driverDao = new DriverDao(DriverLoginActivity.this);
        Log.d("##Driver:",driverDao.findAllDriver().get(0).getDriver_pwd().toString());
    }

    /**
     * 初始化界面
     */
    private void initView(){
        mAccountView = (AutoCompleteTextView) findViewById(R.id.driver_account);
        mPasswordView = (EditText) findViewById(R.id.driver_password);

        mAccountSignInBtn = (Button) findViewById(R.id.driver_sign_in_btn);
        mExchangeToUserBtn = (ImageButton) findViewById(R.id.exchange_to_user_btn);
        //登录界面和进度条界面
        mLoginFormView = findViewById(R.id.driver_login_form);
        mProgressView = findViewById(R.id.driver_login_progress);
    }
    /**
     * 设置监听事件
     */
    private void setListener(){
        mPasswordView.setOnEditorActionListener(this);
        mAccountSignInBtn.setOnClickListener(this);
        mExchangeToUserBtn.setOnClickListener(this);
    }
    /**
     * 监听事件具体逻辑
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.driver_sign_in_btn:
                attemptLogin();
                break;

            case R.id.exchange_to_user_btn:
                intent = new Intent(DriverLoginActivity.this, LoginActivity.class);//测试用
                startActivity(intent);
                break;

        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //当用户处在输入密码编辑框，软键盘打开，输入确定按钮(EditorInfo.IME_NUL)时尝试登录
        //按回车键进行登录
        if (actionId == R.id.login || actionId == EditorInfo.IME_NULL) {
            attemptLogin();
            Toast.makeText(DriverLoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    //加载本地账户
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mAccountView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     * 在登录界面尝试注册登录账户，如果登录信息不满足要求显示出错信息
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return ;
        }

        // Reset errors.
        mAccountView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String account = mAccountView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(password)){
            //错误处理
        }

        // Check for a valid password, if the btn_home_user entered one.
        //检查密码有效性
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid account address.
        if (TextUtils.isEmpty(account)) {
            mAccountView.setError(getString(R.string.error_field_required));
            focusView = mAccountView;
            cancel = true;
        } else if (!isAccountValid(account)) {
            mAccountView.setError(getString(R.string.error_invalid_email));
            focusView = mAccountView;
            cancel = true;
        }

        //取消登录
        if (cancel) {
            // 显示错误
            focusView.requestFocus();
        } else {
            // 显示进度条，并启动后台任务尝试登录
            showProgress(true);
            mAuthTask = new DriverLoginTask(account, password);
            mAuthTask.execute((Void) null);//执行异步任务
        }
    }
    private boolean isAccountValid(String account) {
        //判断账号有效性
        if(Check.checkPhone(account) == Check.SUCCEED){
            return true;
        }
        return false;
    }

    private boolean isPasswordValid(String password) {
        //密码有效性判断，必须长度大于4
        return password.length() > 4;
    }

    /**
     * 显示进度条界面，关闭登录窗体
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // 若版本在13以上使用渐入渐出动画
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // 否则使用简单的隐藏
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                //遍历用户的profile数据
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only account addresses.
                // 选择账号地址
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Phone
                .CONTENT_ITEM_TYPE},

                // 按照用户的输入显示排序的账号
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> accounts = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            accounts.add(cursor.getString(ProfileQuery.NUMBER));
            cursor.moveToNext();
        }

        addAccountsToAutoComplete(accounts);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.IS_PRIMARY,
        };

        int NUMBER = 0;
        int IS_PRIMARY = 1;
    }


    private void addAccountsToAutoComplete(List<String> accountAddressCollection) {
        //创建自动填充的提示列表适配器
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(DriverLoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, accountAddressCollection);

        mAccountView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the btn_home_user.
     * 显示异步登录注册任务，继承异步任务
     */
    public class DriverLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mAccount;
        private final String mPassword;

        DriverLoginTask(String account, String password) {
            mAccount = account;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                //通过网络服务尝试获取授权
                Thread.sleep(500);
                driverDao = new DriverDao(DriverLoginActivity.this);
                Log.d("##Driver:",Encrypt.getEncryption(mPassword).toString());
                if(Encrypt.getEncryption(mPassword).toString().equals(driverDao.findAllDriver().get(0).getDriver_pwd())){
                    Log.d("##Driver","right!!");
                }
                if(driverDao.findDriverByPhone(mAccount).equals(Encrypt.getEncryption(mPassword))){
                    return true;
                }
            } catch (InterruptedException e) {
                return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
                Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DriverLoginActivity.this, OrderCreateActivity.class);
                startActivity(intent);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
