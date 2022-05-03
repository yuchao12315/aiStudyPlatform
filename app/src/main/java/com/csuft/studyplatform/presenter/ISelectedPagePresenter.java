package com.csuft.studyplatform.presenter;

import com.csuft.studyplatform.base.IBasePresenter;
import com.csuft.studyplatform.model.domain.SelectedPageCategory;
import com.csuft.studyplatform.view.ISelectedPageCallback;

public interface ISelectedPagePresenter extends IBasePresenter<ISelectedPageCallback> {

    /**
     * 获取分类
     */
    void getCategories();

    /**
     * 根据分类获取内容
     *
     * @param item
     */
    void getContentByCategory(SelectedPageCategory.DataBean item);

    /**
     * 重新加载内容
     */
    void reloadContent();

}
