package com.openclassrooms.realestatemanager.presentation.utils.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.openclassrooms.realestatemanager.R;

public class CustomBottomSheetDatePicker extends ConstraintLayout {


    public CustomBottomSheetDatePicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomBottomSheetDatePicker);
        initView(attributes,context);
    }

    private void initView(@NonNull TypedArray attributes,Context context){
        inflate(context,R.layout.custom_bottom_sheet_date_picker,this);
        TextView title = findViewById(R.id.bottom_sheet_date_picker_title);
        title.setText(attributes.getText(R.styleable.CustomBottomSheetDatePicker_title));
        attributes.recycle();
    }
}
