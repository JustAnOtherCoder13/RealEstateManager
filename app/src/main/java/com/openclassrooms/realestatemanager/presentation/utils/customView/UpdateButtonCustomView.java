package com.openclassrooms.realestatemanager.presentation.utils.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.openclassrooms.realestatemanager.R;

public class UpdateButtonCustomView extends ConstraintLayout {

    public ImageButton mUpdateButton;

    public UpdateButtonCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.custom_view_update_button, this);
        mUpdateButton = findViewById(R.id.custom_view_update_image_button);
    }
}