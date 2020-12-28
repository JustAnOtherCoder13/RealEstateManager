package com.openclassrooms.realestatemanager.presentation.utils.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.openclassrooms.realestatemanager.R;

public class DetailInformationCustomView extends ConstraintLayout {


    public DetailInformationCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        inflate(getContext(), R.layout.custom_view_detail_information, this);

        ImageView detailInformationIcon = findViewById(R.id.detail_information_custom_view_icon);
        TextView detailInformationTitle = findViewById(R.id.detail_information_custom_view_title);

        TypedArray attributes = getContext().obtainStyledAttributes(attrs,R.styleable.DetailInformationCustomView);

        detailInformationIcon.setImageDrawable(attributes.getDrawable(R.styleable.DetailInformationCustomView_icon));
        detailInformationTitle.setText(attributes.getText(R.styleable.DetailInformationCustomView_title));
        attributes.recycle();
    }
}
