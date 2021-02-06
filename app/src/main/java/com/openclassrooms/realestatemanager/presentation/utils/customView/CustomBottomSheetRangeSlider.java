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

import java.util.ArrayList;
import java.util.List;

import static com.openclassrooms.realestatemanager.presentation.utils.Utils.formatWithSpace;

public class CustomBottomSheetRangeSlider extends ConstraintLayout {

    private Context mContext;
    private RangeSlider mRangeSlider;
    private TextView mStartValue, mEndValue;
    private float mStartFloatValue, mEndFloatValue;

    public CustomBottomSheetRangeSlider(@NonNull Context mContext, @Nullable AttributeSet attrs) {
        super(mContext, attrs);
        this.mContext = mContext;
        TypedArray attributes = mContext.obtainStyledAttributes(attrs, R.styleable.CustomBottomSheetRangeSlider);
        initView(attributes);
    }

    private void initView(@Nullable TypedArray attributes) {
        inflate(mContext, R.layout.custom_bottom_sheet_range_slider, this);

        TextView title = findViewById(R.id.custom_range_slider_title);
        mRangeSlider = findViewById(R.id.custom_range_slider);
        mStartValue = findViewById(R.id.custom_range_slider_start_value);
        mEndValue = findViewById(R.id.custom_range_slider_end_value);
        setRangeSliderTouchListener();
        assert attributes != null;
        title.setText(attributes.getText(R.styleable.CustomBottomSheetRangeSlider_title));
        attributes.recycle();
    }

    private void setRangeSliderTouchListener() {
        mRangeSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
                slider.addOnChangeListener((slider1, value, fromUser) -> {
                    mStartFloatValue = slider.getValues().get(0).intValue();
                    mEndFloatValue = slider.getValues().get(1).intValue();
                    mStartValue.setText(formatWithSpace().format(slider.getValues().get(0).intValue()));
                    mEndValue.setText(formatWithSpace().format(slider.getValues().get(1).intValue()));
                });
            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                slider.addOnChangeListener((slider1, value, fromUser) -> {
                    mStartValue.setText(formatWithSpace().format(slider.getValues().get(0).intValue()));
                    mEndValue.setText(formatWithSpace().format(slider.getValues().get(1).intValue()));

                });
            }
        });
    }

    public void setRangeSliderValue(float valueFrom, float valueTo, float stepSize) {
        int startValueInt = (int) valueFrom;
        int endValueInt = (int) valueTo;
        mRangeSlider.setValueFrom(startValueInt);
        mRangeSlider.setValueTo(endValueInt);
        mStartFloatValue = valueFrom;
        mEndFloatValue = valueTo;
        List<Float> values = new ArrayList<>();
        values.add(valueFrom);
        values.add(valueTo);
        mRangeSlider.setValues(values);
        mRangeSlider.setStepSize(stepSize);
        mStartValue.setText(formatWithSpace().format(startValueInt));
        mEndValue.setText(formatWithSpace().format(endValueInt));
    }

    public float getStartValue() { return mStartFloatValue; }

    public float getEndValue() { return mEndFloatValue; }
}
