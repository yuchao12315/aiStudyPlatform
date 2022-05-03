package com.csuft.studyplatform.presenter.impl;

import com.csuft.studyplatform.model.domain.SelectedContent;
import com.csuft.studyplatform.view.ISelectedPageCallback;
import com.csuft.studyplatform.model.Api;
import com.csuft.studyplatform.model.domain.SelectedPageCategory;
import com.csuft.studyplatform.presenter.ISelectedPagePresenter;
import com.csuft.studyplatform.utils.LogUtils;
import com.csuft.studyplatform.utils.RetrofitManager;
import com.csuft.studyplatform.utils.UrlUtils;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectedPagePresenterImpl implements ISelectedPagePresenter {

    private final Api mApi;
    public SelectedPagePresenterImpl() {
        //拿retrofit
        Retrofit retrofit = RetrofitManager.instance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    private ISelectedPageCallback mViewCallback = null;

    @Override
    public void getCategories() {
        if(mViewCallback != null) {
            mViewCallback.onLoading();
        }
        Call<SelectedPageCategory> task = mApi.getSelectedPageCategories();
        task.enqueue(new Callback<SelectedPageCategory>() {
            @Override
            public void onResponse(Call<SelectedPageCategory> call,Response<SelectedPageCategory> response) {
                int resultCode = response.code();
                LogUtils.d(SelectedPagePresenterImpl.this,"result code -- > " + resultCode);
                if(resultCode == HttpURLConnection.HTTP_OK) {
                    SelectedPageCategory result = response.body();
                    //通知UI更新
                    if(mViewCallback != null) {
                        mViewCallback.onCategoriesLoaded(result);
                    }
                } else {
                    onLoadedError();
                }
            }

            @Override
            public void onFailure(Call<SelectedPageCategory> call,Throwable t) {
                onLoadedError();
            }
        });
    }

    private void onLoadedError() {
        if(mViewCallback != null) {
            mViewCallback.onError();
        }
    }

    @Override
    public void getContentByCategory(SelectedPageCategory.DataBean item) {
        int categoryId = item.getFavorites_id();
        LogUtils.d(this,"categoryId -= > " + categoryId);
        String targetUrl = UrlUtils.getSelectedPageContentUrl(categoryId);
        Call<SelectedContent> task = mApi.getSelectedPageContent(targetUrl);
        task.enqueue(new Callback<SelectedContent>() {
            @Override
            public void onResponse(Call<SelectedContent> call,Response<SelectedContent> response) {
                int resultCode = response.code();
                LogUtils.d(SelectedPagePresenterImpl.this,"getContentByCategory result code -- > " + resultCode);
                if(resultCode == HttpURLConnection.HTTP_OK) {
                    SelectedContent result = response.body();
                    if(mViewCallback != null) {
                        mViewCallback.onContentLoaded(result);
                    }
                } else {
                    onLoadedError();
                }
            }

            @Override
            public void onFailure(Call<SelectedContent> call,Throwable t) {
                onLoadedError();
            }
        });
    }

    @Override
    public void reloadContent() {
        this.getCategories();
    }

    @Override
    public void registerViewCallback(ISelectedPageCallback callback) {
        this.mViewCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISelectedPageCallback callback) {
        this.mViewCallback = null;
    }
}
