package com.philliphsu.bottomsheetpickers;

import android.content.Context;
import android.os.Bundle;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import com.google.android.material.bottomsheet.BottomSheetDialog;

final class CustomWidthBottomSheetDialog extends BottomSheetDialog {
    public CustomWidthBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int width = getContext().getResources().getDimensionPixelSize(R.dimen.bsp_bottom_sheet_width);
        getWindow().setLayout(width > 0 ? width : ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
