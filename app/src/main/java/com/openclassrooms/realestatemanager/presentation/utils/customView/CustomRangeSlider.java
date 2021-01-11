package com.openclassrooms.realestatemanager.presentation.utils.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.slider.RangeSlider;
import com.openclassrooms.realestatemanager.R;

public class CustomRangeSlider extends ConstraintLayout {

    private Context context;
    private TextView title;
    private RangeSlider rangeSlider;

    public CustomRangeSlider(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomRangeSlider);
        initView(attributes);
    }


    private void initView(@Nullable TypedArray attributes ){
        inflate(context, R.layout.custom_bottom_sheet_range_slider,this);

        title = findViewById(R.id.custom_range_slider_title);
        rangeSlider = findViewById(R.id.custom_range_slider);


        rangeSlider.setValueFrom(0);
        rangeSlider.setValueTo(10000);


        title.setText(attributes.getText(R.styleable.CustomRangeSlider_title));
        rangeSlider.setStepSize(attributes.getFloat(R.styleable.CustomRangeSlider_android_stepSize,10.0f));
        attributes.recycle();
    }

    public void setRangeSliderValue(float valueFrom, float valueTo){
        rangeSlider.setValueFrom(valueFrom);
        rangeSlider.setValueTo(valueTo);
    }
}
