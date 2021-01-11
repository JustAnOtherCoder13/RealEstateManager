package com.openclassrooms.realestatemanager.presentation.utils.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.openclassrooms.realestatemanager.R;

public class CustomBottomSheetPropertyTypeCheckBox extends ConstraintLayout {

    public CustomBottomSheetPropertyTypeCheckBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomBottomSheetPropertyTypeCheckBox);
        initView(attributes);
    }
    private void initView(@NonNull TypedArray attributes){
        inflate(getContext(),R.layout.custom_bottom_sheet_property_type_checkbox,this);
        TextView title = findViewById(R.id.bottom_sheet_property_type_title);
        title.setText(attributes.getText(R.styleable.CustomBottomSheetPropertyTypeCheckBox_title));
        attributes.recycle();
    }
}
