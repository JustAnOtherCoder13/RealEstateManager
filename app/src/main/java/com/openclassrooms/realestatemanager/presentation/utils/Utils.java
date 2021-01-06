package com.openclassrooms.realestatemanager.presentation.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.openclassrooms.realestatemanager.R;

import java.text.DateFormat;
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


    public static boolean isGpsAvailable(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        assert locationManager != null;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    public static Boolean isInternetAvailable(Context context){
        WifiManager wifi = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        assert wifi != null;
        return wifi.isWifiEnabled();
    }
}
