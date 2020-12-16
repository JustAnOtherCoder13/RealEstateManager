package com.openclassrooms.realestatemanager.presentation.utils.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.CustomViewDetailMediaBinding;

public class DetailMediaCustomView extends ConstraintLayout {

    public DetailMediaCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    private void initView() {
        inflate(getContext(), R.layout.custom_view_detail_media, this);
    }
}
