package com.openclassrooms.realestatemanager.presentation.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.wifi.WifiManager;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {


    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * 0.812);
    }


    public static boolean isGpsAvailable(@NonNull Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        assert locationManager != null;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    @NonNull
    public static Boolean isInternetAvailable(@NonNull Context context){
        WifiManager wifi = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        assert wifi != null;
        return wifi.isWifiEnabled();
    }

    public static Date formatStringToDate(String dateStr){
        Date date = new Date();
        try {
            date = new SimpleDateFormat("dd/MM/yyy", Locale.FRANCE).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
