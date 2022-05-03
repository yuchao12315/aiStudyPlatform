package com.csuft.studyplatform.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csuft.studyplatform.R;
import com.csuft.studyplatform.base.BaseActivity;
import com.csuft.studyplatform.base.BaseApplication;
import com.csuft.studyplatform.helper.DemoHelper;
import com.csuft.studyplatform.model.Api;
import com.csuft.studyplatform.model.LoginVo;
import com.csuft.studyplatform.model.UserVoResponse;
import com.csuft.studyplatform.model.UserVoResponse.UserVo;
import com.csuft.studyplatform.utils.Constants;
import com.csuft.studyplatform.utils.IntentUtil;
import com.csuft.studyplatform.utils.LogUtils;
import com.csuft.studyplatform.utils.MD5Util;
import com.csuft.studyplatform.utils.RetrofitManager;
import com.csuft.studyplatform.utils.UIUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseCommonUtils;

import retrofit2.Callback;
import retrofit2.Retrofit;

import static android.content.SharedPreferences.Editor;

public class LoginActivity extends BaseActivity implements OnClickListener {

    private EditText mEditUserName;
    private EditText mEditPwd;
    private Button mBtnLogin;
    private LinearLayout mLinearRegister;
    private TextView mTextFindPwd;
    private boolean autoLogin = false;
    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        List<String> stringList = MiPushClient.getAllAlias(BaseApplication.getBaseApplication());
//        LogUtils.e("pushtest", "run: size:" + stringList.size());
//        for (int i = 0; i < stringList.size(); i++) {
//            LogUtils.e("pushtest", "stringList," + i + "=> " + stringList.get(i));
//        }
//
//        sp = getSharedPreferences("user.temp", MODE_PRIVATE);
//        if (DemoHelper.getInstance().isLoggedIn()) {
//            autoLogin = true;
//
//            String username = sp.getString("username", "");
//            String password = sp.getString("password", "");
//
//            AppService.getInstance().loginAsync(username, password, new JsonCallback<LslResponse<User>>() {
//                @Override
//                public void onSuccess(LslResponse<User> userLslResponse, Call call, Response response) {
//                    if (userLslResponse.code == LslResponse.RESPONSE_OK) {
//                        setUserInfo(userLslResponse.data);
//                        IntentUtil.newIntent(LoginActivity.this, MainActivity.class);
//                        LoginActivity.this.finish();
//                        return;
//                    }
//                    ToastUtil.showToast("当前用户登录信息已过期，请重新登录！");
//                }
//            });
//
//        }
        checkIsLogin();
    }

    private void checkIsLogin() {
        Retrofit retrofit = RetrofitManager.instance().getRetrofit();
        Api api = retrofit.create(Api.class);
        retrofit2.Call<UserVoResponse> task = api.checkToken();
        task.enqueue(new Callback<UserVoResponse>() {
            @Override
            public void onResponse(retrofit2.Call<UserVoResponse> call, retrofit2.Response<UserVoResponse> response) {
                if (response.code() == Constants.HTTP_SUCCESS_CODE) {
                    UserVoResponse body = response.body();
                    UserVo userVo = body.getData();
                    mEditUserName.setText(userVo.getUserName());
                    mEditPwd.setText(userVo.getPassword());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<UserVoResponse> call, Throwable t) {

            }
        });
    }

    @Override
    protected void initPresenter() {
        setListener();
    }

    @Override
    protected void initView() {
        bindView();

    }


    private void setListener() {
        mBtnLogin.setOnClickListener(this);
        mLinearRegister.setOnClickListener(this);
        mTextFindPwd.setOnClickListener(this);
    }

    private void bindView() {
        mEditUserName = (EditText) findViewById(R.id.login_edt_username);
        mEditPwd = (EditText) findViewById(R.id.login_edt_pwd);
        mBtnLogin = (Button) findViewById(R.id.login_btn_login);
        mLinearRegister = (LinearLayout) findViewById(R.id.linear_layout_btn_register);

        mTextFindPwd = (TextView) findViewById(R.id.login_find_pwd);

        //  if user changed, clear the password
        mEditUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mEditPwd.setText(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("register_data", MODE_PRIVATE);
        String nickName = sharedPreferences.getString("nickName", "");
        boolean isRegister = sharedPreferences.getBoolean("isRegister", false);
        if (isRegister) {
            mEditUserName.setText(nickName);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn_login:
                login();
                break;
            case R.id.linear_layout_btn_register:
                IntentUtil.newIntent(this, RegisterActivity.class);
//                String phone1 = mEditUserName.getText().toString().trim();
//                Intent intent1 = new Intent(this,RegisterActivity2.class);
//                intent1.putExtra("phone",phone1);
//                startActivity(intent1);
                break;
            case R.id.login_find_pwd:
//                UIUtil.showToast(this,"你点击了找回密码！");

                String email = mEditUserName.getText().toString().trim();
                Intent intent = new Intent(this, ResetPwdActivity.class);
                intent.putExtra("phone", email);
                startActivity(intent);
                break;
        }
    }

    private void login() {

        if (!EaseCommonUtils.isNetWorkConnected(this)) {
            UIUtil.showToast("网络连接不可用，请检查！");
            return;
        }
        final String currentUsername = mEditUserName.getText().toString().trim();
        final String currentPassword = mEditPwd.getText().toString().trim();

        LogUtils.e(TAG, "得到的用户名和密码：" + currentUsername + " ---  " + currentPassword);

        if (TextUtils.isEmpty(currentUsername)) {
            UIUtil.showToast("用户名不能为空!");
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            UIUtil.showToast("密码不能为空！");
            return;
        }
        LoginVo loginVo = new LoginVo();
        loginVo.setName(currentUsername);
        loginVo.setPassword(MD5Util.encrypt(currentPassword));
        Retrofit retrofit = RetrofitManager.instance().getRetrofit();
        Api api = retrofit.create(Api.class);
        retrofit2.Call<UserVoResponse> task = api.doLogin(loginVo);
        task.enqueue(new Callback<UserVoResponse>() {
            @Override
            public void onResponse(retrofit2.Call<UserVoResponse> call, retrofit2.Response<UserVoResponse> response) {
                UserVoResponse body = response.body();
                if (body.getCode() == Constants.HTTP_SUCCESS_CODE) {
                    //存储信息，跳转到对应节面（我页）
                    //把昵称和头像传给我页
                    setUserInfo(body.getData());
                    LoginActivity.this.finish();
                } else {
                    //给出提示
                    UIUtil.showToast("错误：" + response.message());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<UserVoResponse> call, Throwable t) {

            }
        });

        // showLoading(this);
//        AppService.getInstance().loginAsync(currentUsername, currentPassword, new JsonCallback<LslResponse<User>>() {
//            @Override
//            public void onSuccess(LslResponse<User> userLslResponse, Call call, Response response) {
//                if (userLslResponse.code == LslResponse.RESPONSE_ERROR) {
//                    UIUtil.showToast(userLslResponse.msg);
//                    //TODO: stopLoading();
//                } else {
//
//                    LogUtils.e(TAG, "登陆爱吖服务器成功！");
//                    loginXin(currentUsername, currentPassword);
//                }
//            }
//        });
    }


    /**
     * 把信息存储起来
     *
     * @param data
     */
    @SuppressWarnings("ConstantConditions")
    private void setUserInfo(UserVo data) {
        LogUtils.e(TAG, "nickname：" + data.getUserName());
        LogUtils.e(TAG, "password：" + data.getPassword());
        LogUtils.e(TAG, "status：" + data.getStatus());
        LogUtils.e(TAG, "avatar：" + data.getAvatar());
//        if (!TextUtils.isEmpty(data.icon)) {
//            data.icon = Consts.API_SERVICE_HOST + data.icon;
//        }
//        LogUtils.e(TAG, "avatar：" + data.icon);
//        if (!TextUtils.isEmpty(data.childAvatar)) {
//            data.childAvatar = Consts.API_SERVICE_HOST + data.childAvatar;
//        }
//        LogUtils.e(TAG, "childAvatar：" + data.childAvatar);
//
//        AppService.getInstance().setCurrentUser(data);
//
//        MiPushClient.subscribe(BaseApplication.getBaseApplication(), data.classid + "", null);
//
//
//        MiPushClient.setAlias(BaseApplication.getBaseApplication(), data.classid + "", null);
//
        SharedPreferences sharedPreferences = getSharedPreferences("login_data", MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString("username", data.getUserName());
        editor.putString("avatar", data.getAvatar());
        editor.putBoolean("isLogin", true);
        editor.apply();
    }


    /**
     * 登录环信即时通讯需要
     *
     * @param currentUsername 当前输入的用户名
     * @param currentPassword 密码
     */
    private void loginXin(String currentUsername, String currentPassword) {
        //登录前关闭，以确保解调器不重叠
//        DemoDBManager.getInstance().closeDB();

        // reset current user name before login
        DemoHelper.getInstance().setCurrentUserName(currentUsername);

        boolean flag = EMClient.getInstance().isLoggedInBefore();
        LogUtils.e(TAG, flag + "  --------");

        if (flag) { //  如果已经有登录用户，先调用登出
            EMClient.getInstance().logout(false);
        }

        // go login 环信
        EMClient.getInstance().login(currentUsername, currentPassword, new EMCallBack() {
            @Override
            public void onSuccess() {
                LogUtils.d(TAG, "login: onSuccess");

                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                // update current user's display name for APNs
                boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
                        BaseApplication.currentUserNick.trim());
                if (!updatenick) {
                    LogUtils.e("LoginActivity", "update current user nick fail");
                }

                // 获取用户信息（应该从应用服务器或第三方服务获取
//                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UIUtil.showToast("登录成功！");
                        //TODO: stopLoading();
                    }
                });

                IntentUtil.newIntent(LoginActivity.this, MainActivity.class);
                finish();
            }

            @Override
            public void onError(final int code, final String message) {

                LogUtils.d(TAG, "login: onError: " + code);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //TODO:stopLoading();
                        UIUtil.showToast("登陆IM失败:" + message + ",code:" + code);
                    }
                });
            }

            @Override
            public void onProgress(final int code, final String message) {
                LogUtils.d(TAG, "login: onProgress");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 未登录状态下，清空用户信息
//        QiYuCloudServerHelper.setUserInfo(false);
        if (autoLogin) {
            return;
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }


}
