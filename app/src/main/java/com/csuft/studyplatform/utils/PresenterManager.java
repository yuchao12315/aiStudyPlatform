package com.csuft.studyplatform.utils;

import com.csuft.studyplatform.presenter.ICategoryPagerPresenter;
import com.csuft.studyplatform.presenter.IHomePresenter;
import com.csuft.studyplatform.presenter.IOnSellPagePresenter;
import com.csuft.studyplatform.presenter.ISearchPresenter;
import com.csuft.studyplatform.presenter.impl.CategoryPagePresenterImpl;
import com.csuft.studyplatform.presenter.impl.OnSellPagePresenterImpl;
import com.csuft.studyplatform.presenter.impl.SearchPresenter;
import com.csuft.studyplatform.presenter.impl.SelectedPagePresenterImpl;
import com.csuft.studyplatform.presenter.impl.TicketPresenterImpl;
import com.csuft.studyplatform.presenter.ISelectedPagePresenter;
import com.csuft.studyplatform.presenter.ITicketPresenter;
import com.csuft.studyplatform.presenter.impl.HomePresenterImpl;

public class PresenterManager {
    private volatile static PresenterManager sPresenterManager;
    private final ICategoryPagerPresenter mCategoryPagePresenter;
    private final IHomePresenter mHomePresenter;
    private final ITicketPresenter mTicketPresenter;
    private final ISelectedPagePresenter mSelectedPagePresenter;
    private final IOnSellPagePresenter mOnSellPagePresenter;
    private final ISearchPresenter mSearchPresenter;

    public ITicketPresenter getTicketPresenter() {
        return mTicketPresenter;
    }

    public IHomePresenter getHomePresenter() {
        return mHomePresenter;
    }

    public ICategoryPagerPresenter getCategoryPagePresenter() {
        return mCategoryPagePresenter;
    }

    public static PresenterManager instance() {
        if (sPresenterManager == null) {
            synchronized (PresenterManager.class) {
                if (sPresenterManager == null) {
                    sPresenterManager = new PresenterManager();
                }
            }
        }
        return sPresenterManager;
    }

    public ISelectedPagePresenter getSelectedPagePresenter() {
        return mSelectedPagePresenter;
    }

    public IOnSellPagePresenter getOnSellPagePresenter() {
        return mOnSellPagePresenter;
    }

    public ISearchPresenter getSearchPresenter() {
        return mSearchPresenter;
    }

    private PresenterManager() {
        mCategoryPagePresenter = new CategoryPagePresenterImpl();
        mHomePresenter = new HomePresenterImpl();
        mTicketPresenter = new TicketPresenterImpl();
        mSelectedPagePresenter = new SelectedPagePresenterImpl();
        mOnSellPagePresenter = new OnSellPagePresenterImpl();
        mSearchPresenter = new SearchPresenter();
    }
}
