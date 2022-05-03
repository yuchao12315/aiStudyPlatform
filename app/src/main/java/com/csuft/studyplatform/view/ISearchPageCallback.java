package com.csuft.studyplatform.view;

import com.csuft.studyplatform.base.IBaseCallback;
import com.csuft.studyplatform.model.domain.Histories;
import com.csuft.studyplatform.model.domain.SearchRecommend;
import com.csuft.studyplatform.model.domain.SearchResult;

import java.util.List;

public interface ISearchPageCallback extends IBaseCallback {

    /**
     * 搜索历史结果
     *
     * @param histories
     */
    void onHistoriesLoaded(Histories histories);

    /**
     * 历史记录删除完成
     */
    void onHistoriesDeleted();

    /**
     * 搜索结果：成功
     *
     * @param result
     */
    void onSearchSuccess(SearchResult result);

    /**
     * 加载到了更多内容
     *
     * @param result
     */
    void onMoreLoaded(SearchResult result);

    /**
     * 加载更多时网络出错
     */
    void onMoreLoadedError();

    /**
     * 没有更多内容
     */
    void onMoreLoadedEmpty();

    /**
     * 推荐词获取结果
     *
     * @param recommendWords
     */
    void onRecommendWordsLoaded(List<SearchRecommend.DataBean> recommendWords);

}
