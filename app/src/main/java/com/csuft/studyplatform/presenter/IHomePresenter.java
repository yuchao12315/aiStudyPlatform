package com.csuft.studyplatform.presenter;

import com.csuft.studyplatform.base.IBasePresenter;
import com.csuft.studyplatform.view.IHomeCallback;

public interface IHomePresenter extends IBasePresenter<IHomeCallback> {
    /**
     * 获取商品分类
     */
    void getCategories();
}
