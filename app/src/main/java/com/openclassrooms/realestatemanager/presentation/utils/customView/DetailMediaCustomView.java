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

    public DetailMediaCustomView(@NonNull Context context) {
        super(context);
    }

    public DetailMediaCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DetailMediaCustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DetailMediaCustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        View detailMedia = LayoutInflater.from(getContext()).inflate(R.layout.custom_view_detail_media,this,false);
        addView(detailMedia);
    }
}
