package com.openclassrooms.realestatemanager.presentation.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class NetworkUtils {

    public static boolean isNetworkAvailable(@NonNull Context context) {
        //check general connectivity
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr != null) {
            // active connection
            return conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isConnected();
        }
        // no connection
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isConnected(@NonNull Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = conMgr.getNetworkCapabilities(conMgr.getActiveNetwork());
                return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
            } else {
                NetworkInfo info = conMgr.getNetworkInfo(conMgr.getActiveNetwork());
                return (info != null && info.isConnected());
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isConnectedWifi(@NonNull Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = conMgr.getNetworkCapabilities(conMgr.getActiveNetwork());
                return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
            } else {
                NetworkInfo info = conMgr.getNetworkInfo(conMgr.getActiveNetwork());
                return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isConnectedMobile(@NonNull Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = conMgr.getNetworkCapabilities(conMgr.getActiveNetwork());
                return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
            } else {
                NetworkInfo info = conMgr.getNetworkInfo(conMgr.getActiveNetwork());
                return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
            }
        }
        return false;
    }
}
