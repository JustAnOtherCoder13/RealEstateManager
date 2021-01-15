package com.openclassrooms.realestatemanager.presentation.utils.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.presentation.utils.ShowDatePicker;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.picone.core.utils.CalendarStaticValues.MY_MONTH;

public class CustomBottomSheetDatePicker extends ShowDatePicker {

    private TextView dateTextView;

    public CustomBottomSheetDatePicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomBottomSheetDatePicker);
        initView(attributes, context);
    }

    private void initView(@NonNull TypedArray attributes, Context context) {
        inflate(context, R.layout.custom_bottom_sheet_date_picker, this);
        TextView title = findViewById(R.id.bottom_sheet_date_picker_title);
        ImageButton datePicker = findViewById(R.id.bottom_sheet_date_picker_icon);
        dateTextView = findViewById(R.id.bottom_sheet_date_picker_date_text_view);

        datePicker.setOnClickListener(v -> showDatePicker());
        title.setText(attributes.getText(R.styleable.CustomBottomSheetDatePicker_title));
        attributes.recycle();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.e("TAG", "onDateSet: " + month + " " + MY_MONTH);
        month = month + 1;
        if (month == 13) month = 0;
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(formatPickedDate(dayOfMonth, month, year));
        dateTextView.setText(date);
    }

    public String getDate() {
        return dateTextView.getText().toString();
    }
    public void resetDate(){
        dateTextView.setText(R.string.dd_mm_yyyy);
    }
}
