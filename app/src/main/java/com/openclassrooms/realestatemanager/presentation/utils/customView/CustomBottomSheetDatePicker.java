package com.openclassrooms.realestatemanager.presentation.utils.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.presentation.utils.ShowDatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomBottomSheetDatePicker extends ShowDatePicker {


    public CustomBottomSheetDatePicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomBottomSheetDatePicker);
        initView(attributes,context);
    }


    private void initView(@NonNull TypedArray attributes,Context context){
        inflate(context,R.layout.custom_bottom_sheet_date_picker,this);
        TextView title = findViewById(R.id.bottom_sheet_date_picker_title);
        ImageButton datePicker = findViewById(R.id.bottom_sheet_date_picker_icon);
        datePicker.setOnClickListener(v -> showDatePicker(context));
        title.setText(attributes.getText(R.styleable.CustomBottomSheetDatePicker_title));
        attributes.recycle();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (month< Calendar.MONTH)month=month+1;
        if (month>Calendar.MONTH)month=0;
        Date value = null;
        try {
            value = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).parse(dayOfMonth + "/" + month + "/" + year);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(value);
        TextView dateTextView = findViewById(R.id.bottom_sheet_date_picker_date_text_view);
        dateTextView.setText(date);
    }
}
