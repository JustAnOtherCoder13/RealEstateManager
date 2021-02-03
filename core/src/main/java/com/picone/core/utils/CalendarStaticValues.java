package com.picone.core.utils;

import java.util.Calendar;

public class CalendarStaticValues {

    private final static Calendar CALENDAR = Calendar.getInstance();
    public final static int MY_DAY_OF_MONTH = CALENDAR.get(Calendar.DAY_OF_MONTH);
    public final static int MY_MONTH = CALENDAR.get(Calendar.MONTH);
    public final static int MY_YEAR = CALENDAR.get(Calendar.YEAR);
}
