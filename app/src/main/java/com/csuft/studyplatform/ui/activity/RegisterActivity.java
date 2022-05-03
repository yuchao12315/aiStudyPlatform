package com.csuft.studyplatform.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.csuft.studyplatform.R;
import com.csuft.studyplatform.base.BaseActivity;
import com.csuft.studyplatform.model.Api;
import com.csuft.studyplatform.model.ResponseWithoutData;
import com.csuft.studyplatform.model.RegisterVo;
import com.csuft.studyplatform.model.domain.VerifiCode;
import com.csuft.studyplatform.ui.custom.TitleView;
import com.csuft.studyplatform.utils.LogUtils;
import com.csuft.studyplatform.utils.MD5Util;
import com.csuft.studyplatform.utils.RetrofitManager;
import com.csuft.studyplatform.utils.TextUtil;
import com.csuft.studyplatform.utils.UIUtil;
import com.csuft.studyplatform.utils.UrlUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class RegisterActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = "RegisterActivity";
    private static final String COUNTRY_CODE = "121";
    private static final int TAG_USERNAME = 1;
    private static final int TAG_PWD1 = 2;
    private static final int TAG_PWD2 = 3;
    private static final int TAG_VERCODE = 4;


    private TitleView mTitleBar;
    private EditText mEditEmailName;
    private EditText mEditPwd1;
    private EditText mEditPwd2;
    private EditText mEditVercode;
    private Button mBtnVerify;
    private Button mBtnGetVercode;
    private Timer timer;
    private TimeCount timeCount;//用于倒计时任务
    private String email;
    private String editVerCode;
    private String mCorrectCode;
    private static final int GET_VERCODE_SUC = 0x1;
    private static final int GET_VERCODE_FAILED = 0x2;
    private static final int VERIFY_SUC = 0x3;
    private static final int VERIFY_FAILED = 0x4;

    private TextInputLayout mInputUsername;
    private TextInputLayout mInputPwd1;
    private TextInputLayout mInputPwd2;
    private TextInputLayout mInputCode;


    private MaterialEditText mEditPhone;
    private EditText mUserPwd;
    private EditText mUserName;


    /**
     * 检测完毕，开始注册
     */
    private void startRegister() {
        //   stopLoading();
        SharedPreferences sharedPreferences = getSharedPreferences("register_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nickName", mUserName.getText().toString().trim());
        editor.putBoolean("isRegister", true);
        editor.apply();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
//        UIUtil.showToast(RegisterActivity.this,"注册成功！");
        RegisterActivity.this.finish();
    }


    @Override
    protected void initPresenter() {
        mEditPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtil.isEmail(mEditPhone.getText().toString().trim())) {
                    mEditPhone.setHelperTextAlwaysShown(true);
                    mEditPhone.setHelperText("邮箱格式不正确");
                } else {
                    mEditPhone.setHelperTextAlwaysShown(false);
                }
            }
        });
    }

    @Override
    protected void initView() {
        // 短信验证需要先注册相关信息   注意必须在合适的位置取消注册 否则会造成内存泄漏
//        SMSSDK.registerEventHandler(eh);

        bindView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeCount = null;
        Log.e(TAG, "Activity成功销毁");
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_register;
    }

    private void bindView() {
        mTitleBar = (TitleView) findViewById(R.id.register_titleBar);
        mEditEmailName = (EditText) findViewById(R.id.register_edt_username);
        mEditPwd1 = (EditText) findViewById(R.id.register_edt_pwd1);
        mEditPwd2 = (EditText) findViewById(R.id.register_edt_pwd2);

        mUserName = (EditText) findViewById(R.id.register_edt_nickname);
        mUserPwd = (EditText) findViewById(R.id.register_edt_user_pwd);

        mEditVercode = (EditText) findViewById(R.id.register_edt_vercode);
        mBtnVerify = (Button) findViewById(R.id.register_btn_verify);

        mBtnGetVercode = (Button) findViewById(R.id.register_btn_getVercode);


        mInputUsername = (TextInputLayout) findViewById(R.id.register_layout_username);
        mInputPwd1 = (TextInputLayout) findViewById(R.id.register_layout_pwd1);
        mInputPwd2 = (TextInputLayout) findViewById(R.id.register_layout_pwd2);
        mInputCode = (TextInputLayout) findViewById(R.id.register_layout_code);


        mTitleBar.setLeftButtonAsFinish(this);
        mTitleBar.setTitle("邮箱验证");

        mBtnVerify.setOnClickListener(this);
        mBtnGetVercode.setOnClickListener(this);


        mEditEmailName.addTextChangedListener(new MyTextWatcher(this, TAG_USERNAME));
        mEditPwd1.addTextChangedListener(new MyTextWatcher(this, TAG_PWD1));
        mEditPwd2.addTextChangedListener(new MyTextWatcher(this, TAG_PWD2));
        mEditVercode.addTextChangedListener(new MyTextWatcher(this, TAG_VERCODE));
        mEditVercode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEditPhone = (MaterialEditText) findViewById(R.id.register_edt_username1);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_btn_verify:
                Log.e(TAG, "你点击了验证按钮!");
                register();
                hideKeyboard();//关闭键盘
//                finish();
                break;
            case R.id.register_btn_getVercode:
                Log.e(TAG, "你点击了获取验证码按钮!");
                getVerCode();// 获取验证码
                break;
        }
    }

    /**
     * 点击验证后执行的方法
     */
    private void register() {
        email = mEditEmailName.getText().toString().trim();
        String nickName = mUserName.getText().toString().trim();
        String pwd = mUserPwd.getText().toString().trim();
        editVerCode = mEditVercode.getText().toString().trim();

        Log.e(TAG, "mailAddress:" + email + "nickName:" + nickName + "pwd:" + pwd + "verCode:" + editVerCode);

//        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd1)
//                || TextUtils.isEmpty(pwd2) || TextUtils.isEmpty(verCode)){
//            UIUtil.showToast(this,"请输入必要信息！");
//            return;
//        }

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(editVerCode)) {
            UIUtil.showToast(this, "请输入必要信息！");
            return;
        }


        if (!TextUtil.isEmail(email)) {
            UIUtil.showToast(this, "邮箱格式不正确!");
            return;
        }

        if (!editVerCode.equals(mCorrectCode)) {
            UIUtil.showToast(this, "验证码不正确!");
            return;
        }
        if (nickName.isEmpty()) {
            UIUtil.showToast(this, "请输入昵称！");
            return;
        }
        if (pwd.isEmpty()) {
            UIUtil.showToast(this, "请输入密码！");
            return;
        }

//        AppService.getInstance().isUsableMobileAsync(email, new JsonCallback<LslResponse<User>>() {
//            @Override
//            public void onSuccess(LslResponse<User> userLslResponse, Call call, Response response) {
//                if (userLslResponse.code == LslResponse.RESPONSE_OK) {
//                    SMSSDK.submitVerificationCode(COUNTRY_CODE, email, editVerCode);
//                    Log.e(TAG, userLslResponse.msg);
//                } else {
//                    UIUtil.showToast("错误：" + userLslResponse.msg);
//                    LogUtils.e(TAG, "错误：" + userLslResponse.msg);
//
//                }
//            }
//        });

        RegisterVo registerVo = new RegisterVo();
        registerVo.setMail(email);
        registerVo.setNickname(mUserName.getText().toString().trim());
        registerVo.setPassword(MD5Util.encrypt(mUserPwd.getText().toString().trim()));
        Retrofit retrofit = RetrofitManager.instance().getRetrofit();
        Api api = retrofit.create(Api.class);
        retrofit2.Call<ResponseWithoutData> task = api.registerAccount(editVerCode, registerVo);
        task.enqueue(new Callback<ResponseWithoutData>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseWithoutData> call, retrofit2.Response<ResponseWithoutData> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    //跳转到登录节面
                    UIUtil.showToast(response.message());
                    startRegister();
                } else {
                    //给出提示
                    UIUtil.showToast("错误：" + response.message());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseWithoutData> call, Throwable t) {
                UIUtil.showToast("验证码获取失败");
            }
        });

    }

    /**
     * 关闭键盘
     */
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 点击获取验证码后执行的方法
     */
    private void getVerCode() {
        email = mEditEmailName.getText().toString().trim();
        if (!TextUtil.isEmail(email)) {
            UIUtil.showToast(this, "邮箱格式不正确，请检查！");
            return;
        }
//        mInputUsername.setErrorEnabled(false);// 隐藏
        // 请求获取验证码
        Retrofit retrofit = RetrofitManager.instance().getRetrofit();
        Api api = retrofit.create(Api.class);
        retrofit2.Call<VerifiCode> task = api.sendMailCode(email);
        task.enqueue(new Callback<VerifiCode>() {
            @Override
            public void onResponse(retrofit2.Call<VerifiCode> call, retrofit2.Response<VerifiCode> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    //请求成功
                    //给出提示，
                    UIUtil.showToast("正在获取验证码");
                    // 禁止发送按钮
                    // 开始倒计时
                    countDown();
                    //TODO: 验证码注入为空
                    LogUtils.d("请求数据 --->", String.valueOf(response.body().getData()));
                    VerifiCode body = response.body();
                    LogUtils.d("请求数据 --->", body.toString());
                    mCorrectCode = String.valueOf(body.getData());


                }
            }

            @Override
            public void onFailure(retrofit2.Call<VerifiCode> call, Throwable t) {
                UIUtil.showToast("验证码获取失败");
            }
        });
    }

    private void countDown() {
        timeCount = null;
        timeCount = new TimeCount(60 * 1000, 1000, RegisterActivity.this);
        timeCount.start();
    }


//    private OnSendMessageHandler listener;

//    /**
//     * 倒计时120秒的倒计时任务
//     */
//    private TimerTask timerTask = new TimerTask() {
//        int cnt = 120;
//
//        @Override
//        public void run() {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mBtnGetVercode.setText(--cnt + "秒后可再次获取");
//                }
//            });
//        }
//    };


    /**
     * 专用于倒计时处理的类
     */
    private static class TimeCount extends CountDownTimer {
        private RegisterActivity activity;

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval, RegisterActivity activity) {
            super(millisInFuture, countDownInterval);
            WeakReference<RegisterActivity> weakReference = new WeakReference<RegisterActivity>(activity);
            this.activity = weakReference.get();
        }

        /**
         * 计时过程显示
         *
         * @param millisUntilFinished
         */
        @Override
        public void onTick(long millisUntilFinished) {
            // 设置为灰色
            activity.mBtnGetVercode.setBackgroundResource(R.drawable.button_border_radius_no);
            activity.mBtnGetVercode.setClickable(false);
            activity.mBtnGetVercode.setText(millisUntilFinished / 1000 + "秒后可再次获取");
        }

        /**
         * 计时结束调用
         */
        @Override
        public void onFinish() {
            activity.mBtnGetVercode.setClickable(true);
            activity.mBtnGetVercode.setBackgroundResource(R.drawable.button_border_radius);
            activity.mBtnGetVercode.setText("点击获取验证码");
        }
    }

    private static class MyTextWatcher implements TextWatcher {

        private int tag;
        private RegisterActivity activity;
        private WeakReference<RegisterActivity> mWeakReference;

        MyTextWatcher(RegisterActivity activity, int tag) {
            mWeakReference = new WeakReference<RegisterActivity>(activity);
            RegisterActivity activity1 = mWeakReference.get();
            if (activity1 != null) {
                this.activity = activity1;
            } else {
                this.activity = activity;
            }
            this.tag = tag;
        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public void afterTextChanged(Editable s) {
            switch (tag) {
                case TAG_USERNAME:
                    String email = activity.mEditEmailName.getText().toString().trim();
                    if (!TextUtil.isEmail(email)) {
//                        activity.mInputUsername.setErrorEnabled(true);
//                        activity.mInputUsername.setError("手机号格式不正确！");
                        activity.mInputUsername.getEditText().setError("邮箱格式不正确！");
                    } else {
                        activity.mInputUsername.getEditText().setError(null);
//                        activity.mInputUsername.setErrorEnabled(false);

                    }
                    break;
                case TAG_PWD1:
                    if (activity.mEditPwd1.getText().toString().length() < 6) {
                        activity.mInputPwd1.getEditText().setError("密码不能小于6位");
                    } else {
                        activity.mInputPwd1.getEditText().setError(null);
                    }
                    break;
                case TAG_PWD2:
                    String pwd1 = activity.mEditPwd1.getText().toString().trim();
                    String pwd2 = activity.mEditPwd2.getText().toString().trim();
                    if (pwd1.equals(pwd2)) {
                        activity.mInputPwd2.getEditText().setError(null);
                    } else {
                        activity.mInputPwd2.getEditText().setError("两次的密码输入不一致!");
                    }
                    break;
                case TAG_VERCODE:
                    if (TextUtils.isEmpty(activity.mEditVercode.getText().toString())) {
                        activity.mInputCode.getEditText().setError("验证码不能为空！");

                    } else {
                        activity.mInputCode.getEditText().setError(null);
                    }
                    break;
            }
        }
    }


}
