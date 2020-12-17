package com.openclassrooms.realestatemanager.presentation.utils.customView;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.openclassrooms.realestatemanager.R;

public class TopAppBarCustomView extends ConstraintLayout {

    public TopAppBarCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.custom_view_top_nav_bar, this);
    }
}
