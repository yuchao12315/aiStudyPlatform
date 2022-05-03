package com.csuft.studyplatform.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.csuft.studyplatform.R;
import com.csuft.studyplatform.base.BaseFragment;
import com.csuft.studyplatform.model.Api;
import com.csuft.studyplatform.model.ResponseWithoutData;
import com.csuft.studyplatform.model.UserVoResponse;
import com.csuft.studyplatform.ui.activity.ClassActivity;
import com.csuft.studyplatform.ui.activity.LoginActivity;
import com.csuft.studyplatform.ui.activity.NoteActivity;
import com.csuft.studyplatform.utils.Constants;
import com.csuft.studyplatform.utils.LogUtils;
import com.csuft.studyplatform.utils.RetrofitManager;
import com.csuft.studyplatform.utils.UIUtil;
import com.lcodecore.tkrefreshlayout.utils.LogUtil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.SharedPreferences.*;

public class PersonalCenterFragment extends BaseFragment {

    @BindView(R.id.user_id)
    public TextView nickName;

    @BindView(R.id.user_head_image)
    public ImageView userImage;


    @BindView(R.id.logout)
    public Button mLogout;

    @BindView(R.id.fragment_bar_title_tv)
    public TextView barTitleTv;

    @BindView(R.id.login_register)
    public LinearLayout userItem;


    @BindView(R.id.users_class)
    public ConstraintLayout usersClass;

    @BindView(R.id.note_book)
    public ConstraintLayout noteBook;

    private boolean isLogin = false;
    private SharedPreferences mSharedPreferences;


    @Override
    public void onResume() {
        super.onResume();
        bindUserInfo();
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_with_bar_layout, container, false);
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_personal_center;
    }

    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        barTitleTv.setText(R.string.text_personal_center);
        mLogout.setVisibility(View.GONE);
        bindUserInfo();
    }

    private void bindUserInfo() {
        mSharedPreferences = this.getActivity().getSharedPreferences("login_data", Context.MODE_PRIVATE);
        isLogin = mSharedPreferences.getBoolean("isLogin", false);
        LogUtils.d(PersonalCenterFragment.this, "isLogin ===>" + isLogin);
        //判断是否已登录
        if (isLogin) {
            String username = mSharedPreferences.getString("username", "test");
            String avatar = mSharedPreferences.getString("avatar", "https://cdn.sunofbeaches.com/images/default_avatar.png");
            nickName.setText(username);
            Glide.with(this).load(avatar).into(userImage);
            mLogout.setVisibility(View.VISIBLE);
        } else {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
    }


    @Override
    protected void initListener() {
        super.initListener();
        userItem.setOnClickListener(v -> startActivity(new Intent(getContext(), LoginActivity.class)));
        usersClass.setOnClickListener(v -> startActivity(new Intent(getContext(), ClassActivity.class)));
        noteBook.setOnClickListener(v -> startActivity(new Intent(getContext(), NoteActivity.class)));
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickLogout();
            }
        });
    }

    private void ClickLogout() {
        Retrofit retrofit = RetrofitManager.instance().getRetrofit();
        Api api = retrofit.create(Api.class);
        retrofit2.Call<ResponseWithoutData> task = api.doLogout();
        task.enqueue(new Callback<ResponseWithoutData>() {
            @Override
            public void onResponse(Call<ResponseWithoutData> call, Response<ResponseWithoutData> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    nickName.setText("请登录");
                    userImage.setImageResource(R.mipmap.boy);
                    mLogout.setVisibility(View.GONE);
                    Editor editor = mSharedPreferences.edit();
                    editor.putBoolean("isLogin", false);
                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<ResponseWithoutData> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
