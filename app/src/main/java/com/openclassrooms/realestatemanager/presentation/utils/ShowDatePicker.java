package com.openclassrooms.realestatemanager.presentation.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.picone.core.utils.CalendarStaticValues.MY_DAY_OF_MONTH;
import static com.picone.core.utils.CalendarStaticValues.MY_MONTH;
import static com.picone.core.utils.CalendarStaticValues.MY_YEAR;

public abstract class ShowDatePicker extends ConstraintLayout implements DatePickerDialog.OnDateSetListener {

    private DatePickerDialog picker;

    public ShowDatePicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        picker = new DatePickerDialog(context, this, MY_YEAR, MY_MONTH, MY_DAY_OF_MONTH);
    }

    public void showDatePicker() {
        picker.show();
    }

    public Date formatPickedDate(int dayOfMonth, int monthOfYear, int year) {
        Date value = null;
        try {
            value = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).parse(dayOfMonth + "/" + monthOfYear + "/" + year);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return value;
    }
}
