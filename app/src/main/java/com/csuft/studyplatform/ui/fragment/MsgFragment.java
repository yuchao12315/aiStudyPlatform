package com.csuft.studyplatform.ui.fragment;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csuft.studyplatform.R;
import com.csuft.studyplatform.base.BaseFragment;
import com.csuft.studyplatform.model.domain.IBaseInfo;
import com.csuft.studyplatform.model.domain.SelectedContent;
import com.csuft.studyplatform.model.domain.SelectedPageCategory;
import com.csuft.studyplatform.presenter.ISelectedPagePresenter;
import com.csuft.studyplatform.ui.adapter.SelectedPageContentAdapter;
import com.csuft.studyplatform.ui.adapter.SelectedPageLeftAdapter;
import com.csuft.studyplatform.utils.LogUtils;
import com.csuft.studyplatform.utils.PresenterManager;
import com.csuft.studyplatform.utils.SizeUtils;
import com.csuft.studyplatform.utils.TicketUtil;
import com.csuft.studyplatform.view.ISelectedPageCallback;

import butterknife.BindView;

public class MsgFragment extends BaseFragment
        implements ISelectedPageCallback,
        SelectedPageLeftAdapter.OnLeftItemClickListener,
        SelectedPageContentAdapter.OnSelectedPageContentItemClickListener {

    @BindView(R.id.left_category_list)
    public RecyclerView leftCategoryList;


    @BindView(R.id.right_content_list)
    public RecyclerView rightContentList;


    @BindView(R.id.fragment_bar_title_tv)
    public TextView barTitleTv;

    @Override
    protected View loadRootView(LayoutInflater inflater,ViewGroup container) {
        return inflater.inflate(R.layout.fragment_with_bar_layout,container,false);
    }


    private ISelectedPagePresenter mSelectedPagePresenter;
    private SelectedPageLeftAdapter mLeftAdapter;
    private SelectedPageContentAdapter mRightAdapter;

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mSelectedPagePresenter = PresenterManager.instance().getSelectedPagePresenter();
        mSelectedPagePresenter.registerViewCallback(this);
        mSelectedPagePresenter.getCategories();
    }


    @Override
    protected void onRetryClick() {
        //重试
        if(mSelectedPagePresenter != null) {
            mSelectedPagePresenter.reloadContent();
        }
    }

    @Override
    protected void release() {
        super.release();
        if(mSelectedPagePresenter != null) {
            mSelectedPagePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_msg;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        leftCategoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        mLeftAdapter = new SelectedPageLeftAdapter();
        leftCategoryList.setAdapter(mLeftAdapter);

        rightContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRightAdapter = new SelectedPageContentAdapter();
        rightContentList.setAdapter(mRightAdapter);
        rightContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect,@NonNull View view,@NonNull RecyclerView parent,@NonNull RecyclerView.State state) {
                int topAndBottom = SizeUtils.dip2px(getContext(),4);
                int leftAndRight = SizeUtils.dip2px(getContext(),6);
                outRect.left = leftAndRight;
                outRect.right = leftAndRight;
                outRect.top = topAndBottom;
                outRect.bottom = topAndBottom;
            }
        });
        barTitleTv.setText(getResources().getText(R.string.text_selected_title));
    }

    @Override
    protected void initListener() {
        super.initListener();
        mLeftAdapter.setOnLeftItemClickListener(this);
        mRightAdapter.setOnSelectedPageContentItemClickListener(this);
    }

    @Override
    public void onCategoriesLoaded(SelectedPageCategory categories) {
        setUpState(State.SUCCESS);
        mLeftAdapter.setData(categories);
        //分类内容
        // LogUtils.d(this,"onCategoriesLoaded -- > " + categories);
        //根据当前选中的分类，获取分类详情内容
        //List<SelectedPageCategory.DataBean> data = categories.getData();
        //mSelectedPagePresenter.getContentByCategory(data.get(0));
    }

    @Override
    public void onContentLoaded(SelectedContent content) {
        mRightAdapter.setData(content);
        rightContentList.scrollToPosition(0);
    }

    @Override
    public void onError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onLeftItemClick(SelectedPageCategory.DataBean item) {
        //左边的分类点击了
        mSelectedPagePresenter.getContentByCategory(item);
        LogUtils.d(this,"current selected item -- > " + item.getFavorites_title());
    }

    @Override
    public void onContentItemClick(IBaseInfo item) {
        TicketUtil.toTicketPage(getContext(),item);
    }
}