package com.csuft.studyplatform.view;

import com.csuft.studyplatform.base.IBaseCallback;
import com.csuft.studyplatform.model.domain.TicketResult;

public interface ITicketPagerCallback extends IBaseCallback {
    /**
     * 淘口令加载结果
     *
     * @param cover
     * @param result
     */
    void onTicketLoaded(String cover, TicketResult result);
}
