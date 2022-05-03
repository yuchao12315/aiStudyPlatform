package com.csuft.studyplatform.presenter;

import com.csuft.studyplatform.base.IBasePresenter;
import com.csuft.studyplatform.view.ITicketPagerCallback;

public interface ITicketPresenter extends IBasePresenter<ITicketPagerCallback> {

    /**
     * 生成淘口令
     *
     * @param title
     * @param url
     * @param cover
     */
    void getTicket(String title,String url,String cover);

}
