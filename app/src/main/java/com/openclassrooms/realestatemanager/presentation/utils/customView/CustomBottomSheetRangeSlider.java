package com.openclassrooms.realestatemanager.presentation.utils.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;
import com.openclassrooms.realestatemanager.R;

import java.util.ArrayList;
import java.util.List;

import static com.openclassrooms.realestatemanager.presentation.utils.Utils.formatWithSpace;

public class CustomBottomSheetRangeSlider extends ConstraintLayout {

    private Context context;
    private TextView title;
    private RangeSlider rangeSlider;
    private TextView startValue;
    private TextView endValue;

    public CustomBottomSheetRangeSlider(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomBottomSheetRangeSlider);
        initView(attributes);
    }

    private void initView(@Nullable TypedArray attributes) {
        inflate(context, R.layout.custom_bottom_sheet_range_slider, this);

        title = findViewById(R.id.custom_range_slider_title);
        rangeSlider = findViewById(R.id.custom_range_slider);
        startValue = findViewById(R.id.custom_range_slider_start_value);
        endValue = findViewById(R.id.custom_range_slider_end_value);

        setRangeSliderTouchListener();

        assert attributes != null;
        title.setText(attributes.getText(R.styleable.CustomBottomSheetRangeSlider_title));
        attributes.recycle();
    }

    private void setRangeSliderTouchListener() {
        rangeSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
                rangeSlider.addOnChangeListener((slider1, value, fromUser) -> startValue.setText(formatWithSpace().format(slider.getValues().get(0).intValue())));
                rangeSlider.addOnChangeListener((slider1, value, fromUser) -> endValue.setText(formatWithSpace().format(slider.getValues().get(1).intValue())));
            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                rangeSlider.addOnChangeListener((slider1, value, fromUser) -> startValue.setText(formatWithSpace().format(slider.getValues().get(0).intValue())));
                rangeSlider.addOnChangeListener((slider1, value, fromUser) -> endValue.setText(formatWithSpace().format(slider.getValues().get(1).intValue())));
            }
        });
    }

    public void setRangeSliderValue(float valueFrom, float valueTo, float stepSize) {
        int startValueInt = (int)valueFrom;
        int endValueInt = (int)valueTo;
        rangeSlider.setValueFrom(startValueInt);
        rangeSlider.setValueTo(endValueInt);
        List<Float> values = new ArrayList<>();
        values.add(valueFrom);
        values.add(valueTo);
        rangeSlider.setValues(values);
        rangeSlider.setStepSize(stepSize);
        startValue.setText(formatWithSpace().format(startValueInt));
        endValue.setText(formatWithSpace().format(endValueInt));
    }

    public float getStartValue(){return Float.parseFloat(startValue.getText().toString());}
    public float getEndValue(){return Float.parseFloat(endValue.getText().toString());}

}
