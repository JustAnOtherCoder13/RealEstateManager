package com.openclassrooms.realestatemanager.presentation.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    public static int convertDollarToEuro(int dollars) {
        return (int) Math.round(dollars * 0.83);//update with actual exchange rate
    }

    public static int convertEuroToDollar(int euro) {
        return (int) Math.round(euro * 1.21);
    }

    public static boolean isGpsAvailable(@NonNull Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        assert locationManager != null;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    //check for all network and return the first connected or null if internet not available
    @NonNull
    public static Boolean isInternetAvailable(@NonNull Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static Date formatStringToDate(String dateStr) {
        Date date = new Date();
        try {
            date = new SimpleDateFormat("dd/MM/yyy", Locale.FRANCE).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //refactor getTodayDateMethod in core/utils/ConstantParameters.
}
