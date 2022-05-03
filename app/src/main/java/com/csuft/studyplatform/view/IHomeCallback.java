package com.csuft.studyplatform.view;

import com.csuft.studyplatform.base.IBaseCallback;
import com.csuft.studyplatform.model.domain.Categories;

public interface IHomeCallback extends IBaseCallback {

    void onCategoriesLoaded(Categories categories);

}
