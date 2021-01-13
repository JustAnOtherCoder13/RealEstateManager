package com.openclassrooms.realestatemanager.presentation.utils.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.openclassrooms.realestatemanager.R;

import java.util.List;

public class CustomBottomSheetSpinner extends ConstraintLayout {

    private AutoCompleteTextView autoCompleteTextView;

    public CustomBottomSheetSpinner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomBottomSheetSpinner);
        initView(attributes);
    }

    private void initView(@NonNull TypedArray attributes) {
        inflate(getContext(), R.layout.custom_bottom_sheet_spiner, this);
        TextView title = findViewById(R.id.bottom_sheet_spinner_title);
        autoCompleteTextView = findViewById(R.id.bottom_sheet_spinner_text_view);
        title.setText(attributes.getText(R.styleable.CustomBottomSheetSpinner_title));
    }

    public void setSpinnerAdapter(List<String> strings) {
        AutoCompleteTextView textView = findViewById(R.id.bottom_sheet_spinner_text_view);
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, strings);
        textView.setAdapter(stringArrayAdapter);
    }

    public String getText() {
        return autoCompleteTextView.getText().toString();
    }
}
