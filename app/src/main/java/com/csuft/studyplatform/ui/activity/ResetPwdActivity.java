package com.csuft.studyplatform.ui.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.csuft.studyplatform.AppService;
import com.csuft.studyplatform.R;
import com.csuft.studyplatform.base.BaseActivity;
import com.csuft.studyplatform.model.Api;
import com.csuft.studyplatform.model.RegisterVo;
import com.csuft.studyplatform.model.ResponseWithoutData;
import com.csuft.studyplatform.model.User;
import com.csuft.studyplatform.model.domain.VerifiCode;
import com.csuft.studyplatform.network.okgo.JsonCallback;
import com.csuft.studyplatform.network.okgo.LslResponse;
import com.csuft.studyplatform.ui.custom.TitleView;
import com.csuft.studyplatform.utils.Constants;
import com.csuft.studyplatform.utils.LogUtils;
import com.csuft.studyplatform.utils.MD5Util;
import com.csuft.studyplatform.utils.RetrofitManager;
import com.csuft.studyplatform.utils.TextUtil;
import com.csuft.studyplatform.utils.UIUtil;
import com.csuft.studyplatform.utils.UrlUtils;

import java.net.HttpURLConnection;

import butterknife.BindView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 重置密码页面
 */
public class ResetPwdActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = "ResetPwdActivity";

    private EditText mEditEmail;
    private EditText mEditVercode;
    private Button mBtnGetCode;
    private EditText mEditPwd1;
    private EditText mEditPwd2;
    private String mCorrectCode;

    @BindView(R.id.reset_btn_back)
    public ImageView resetBack;

    @Override
    protected void initPresenter() {
        resetBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPwdActivity.this, LoginActivity.class));
                ResetPwdActivity.this.finish();
            }
        });
    }

    @Override
    protected void initView() {
        bindView();
    }

    private void bindView() {
        mEditEmail = (EditText) findViewById(R.id.reset_pwd_phone);
        mEditVercode = (EditText) findViewById(R.id.reset_pwd_code);
        mBtnGetCode = (Button) findViewById(R.id.reset_pwd_get_code);
        mEditPwd1 = (EditText) findViewById(R.id.reset_pwd_pwd1);
        mEditPwd2 = (EditText) findViewById(R.id.reset_pwd_pwd2);

        String eamil = getIntent().getStringExtra("eamil");
        if (!TextUtils.isEmpty(eamil)) {
            mEditEmail.setText(eamil);
        }

        mBtnGetCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_pwd_title_commit:
//                Crouton.makeText(this,"你点击了提交按钮", Style.ALERT,R.id.reset_pwd_pwd2_layout).show();
                getCode = true;
                submit();
                break;
            case R.id.reset_pwd_get_code:
//                Crouton.makeText(this,"你点击了获取验证码", Style.ALERT).show();
                getVerCode();
                break;
        }
    }

    private String mail;
    private String pwd1;


    private void submit() {
        mail = mEditEmail.getText().toString().trim();
        final String code = mEditVercode.getText().toString().trim();
        pwd1 = mEditPwd1.getText().toString().trim();
        String pwd2 = mEditPwd2.getText().toString().trim();

        if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(code) || TextUtils.isEmpty(pwd1)
                || TextUtils.isEmpty(pwd2)) {
            showCrouton("请填写必要信息！");
            return;
        }
        if (!TextUtil.isEmail(mail)) {
            showCrouton("邮箱格式不正确！");
            return;
        }
        if (!pwd1.equals(pwd2)) {
            showCrouton("两次输入的密码不一致！");
            return;
        }
        if (pwd1.length() < 6) {
            showCrouton("密码长度不能小于6!");
            return;
        }
        if (!code.equals(mCorrectCode)) {
            UIUtil.showToast(this, "验证码不正确!");
            return;
        }

        RegisterVo registerVo = new RegisterVo();
        registerVo.setMail(mail);
        registerVo.setPassword(MD5Util.encrypt(mEditPwd1.getText().toString().trim()));
        Retrofit retrofit = RetrofitManager.instance().getRetrofit();
        Api api = retrofit.create(Api.class);
        retrofit2.Call<ResponseWithoutData> task = api.resetPassword(code, registerVo);
        task.enqueue(new Callback<ResponseWithoutData>() {
            @Override
            public void onResponse(Call<ResponseWithoutData> call, Response<ResponseWithoutData> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    startActivity(new Intent(ResetPwdActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onFailure(Call<ResponseWithoutData> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void showCrouton(String desc) {
//        Crouton.makeText(this,desc,Style.ALERT,R.id.reset_location).show();
    }

    /**
     * 获取验证码
     */
    private void getVerCode() {
        String email = mEditEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            showCrouton("邮箱不能为空！");
            return;
        }
        if (!TextUtil.isEmail(email)) {
            showCrouton("邮箱格式不正确！");
            return;
        }

        Retrofit retrofit = RetrofitManager.instance().getRetrofit();
        Api api = retrofit.create(Api.class);
        retrofit2.Call<VerifiCode> task = api.sendResetMailCode(email);
        task.enqueue(new Callback<VerifiCode>() {
            @Override
            public void onResponse(retrofit2.Call<VerifiCode> call, retrofit2.Response<VerifiCode> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    VerifiCode body = response.body();
                    if (body.getCode() == Constants.HTTP_SUCCESS_CODE) {
                        //请求成功 给出提示，
                        UIUtil.showToast("正在获取验证码");
                        // 禁止发送按钮
                        // 开始倒计时
                        countDown();
                        //TODO: 验证码注入为空
                        LogUtils.d("请求数据 --->", String.valueOf(body.getData()));
                        LogUtils.d("请求数据 --->", body.toString());
                        mCorrectCode = String.valueOf(body.getData());
                    } else {
                        UIUtil.showToast(body.getMsg());
                    }

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
        timeCount = new TimeCount(60 * 1000, 1000, this);
        timeCount.start();
    }

    private static final int GET_VERCODE_SUC = 0x1;
    private static final int GET_VERCODE_FAILED = 0x2;
    private static final int VERIFY_SUC = 0x3;
    private static final int VERIFY_FAILED = 0x4;
    private static final String COUNTRY_CODE = "86";
    private boolean getCode = false;
    private TimeCount timeCount;


    /**
     * 开始注册
     */
    private void startResetPwd() {
//                    showCrouton("密码重置成功！");
        UIUtil.showToast("密码重置成功！");
        ResetPwdActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeCount = null;
        Log.e(TAG, "Activity成功销毁");
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_reset_pwd;
    }

    /**
     * 专用于倒计时处理的类
     */
    private static class TimeCount extends CountDownTimer {
        private ResetPwdActivity activity;

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval, ResetPwdActivity activity) {
            super(millisInFuture, countDownInterval);
            this.activity = activity;
        }

        /**
         * 计时过程显示
         *
         * @param millisUntilFinished
         */
        @Override
        public void onTick(long millisUntilFinished) {
            // 设置为灰色
            activity.mBtnGetCode.setBackgroundResource(R.drawable.button_border_radius_no);
            activity.mBtnGetCode.setClickable(false);
            activity.mBtnGetCode.setText(millisUntilFinished / 1000 + "秒后可重试");
        }

        /**
         * 计时结束调用
         */
        @Override
        public void onFinish() {
            activity.mBtnGetCode.setClickable(true);
            activity.mBtnGetCode.setBackgroundResource(R.drawable.reset_pwd_btn_bg);
            activity.mBtnGetCode.setText("获取验证码");
        }
    }

}
