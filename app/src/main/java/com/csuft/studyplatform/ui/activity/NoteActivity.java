package com.csuft.studyplatform.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csuft.studyplatform.R;
import com.csuft.studyplatform.base.BaseActivity;
import com.csuft.studyplatform.ui.adapter.LinearItemContentAdapter;
import com.csuft.studyplatform.ui.adapter.NoteItemAdapter;

import butterknife.BindView;

public class NoteActivity extends BaseActivity {

    @BindView(R.id.note_book_icon)
    public TextView mNoteBookIcon;

    @BindView(R.id.write_note_icon)
    public ImageView mWriteNoteIcon;

    @BindView(R.id.write_note)
    public TextView mWriteNote;

    @BindView(R.id.no_note_tips)
    public LinearLayout mNoteTips;

    @BindView(R.id.note_item)
    public RecyclerView mRecyclerView;

    private NoteItemAdapter mNoteAdapter;

    @Override
    protected void initPresenter() {

        mWriteNoteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //写笔记编辑页
            }
        });

        mWriteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //写笔记编辑页
            }
        });

        mNoteBookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //笔记本页
            }
        });

    }

    @Override
    protected void initView() {
       if (isNoNote()){
           mNoteTips.setVisibility(View.VISIBLE);
       }else {
           mNoteTips.setVisibility(View.GONE);
       }
       mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mNoteAdapter = new NoteItemAdapter();
        mRecyclerView.setAdapter(mNoteAdapter);
    }

    private Boolean isNoNote() {
        return true;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_note;
    }
}
