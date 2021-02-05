package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.presentation.utils.Utils;
import com.picone.core.utils.ConstantParameters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class HelperUnitTest {


    @Test
    public void convertDollarToEuro() {
        int random = (int) (Math.random());
        int expected = (int) (random * 0.83);
        int price = Utils.convertDollarToEuro(random);
        assertEquals(price, expected);
    }

    @Test
    public void convertEuroToDollar() {
        int random = (int) (Math.random());
        int expected = (int) (random * 1.21);
        int price = Utils.convertEuroToDollar(random);
        assertEquals(price, expected);
    }

    @Test
    public void getTodayDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        assertEquals(0, ConstantParameters.getTodayDate().compareTo(dateFormat.format(Calendar.getInstance().getTime())));
    }
}
