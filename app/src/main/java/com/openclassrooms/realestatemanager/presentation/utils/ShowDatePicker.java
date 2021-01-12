package com.openclassrooms.realestatemanager.presentation.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Calendar;

public abstract class ShowDatePicker extends ConstraintLayout implements DatePickerDialog.OnDateSetListener {

    private final Calendar calendar = Calendar.getInstance();

    public ShowDatePicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void showDatePicker(Context context) {
        DatePickerDialog picker = new DatePickerDialog(context, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        picker.show();
    }
}
