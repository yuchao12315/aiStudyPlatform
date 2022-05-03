package com.csuft.studyplatform.view;

import com.csuft.studyplatform.base.IBaseCallback;
import com.csuft.studyplatform.model.domain.SelectedContent;
import com.csuft.studyplatform.model.domain.SelectedPageCategory;

public interface ISelectedPageCallback extends IBaseCallback {

    /**
     * 分类内容结果
     *
     * @param categories 分类内容
     */
    void onCategoriesLoaded(SelectedPageCategory categories);


    /**
     * 内容
     *
     * @param content
     */
    void onContentLoaded(SelectedContent content);

}
