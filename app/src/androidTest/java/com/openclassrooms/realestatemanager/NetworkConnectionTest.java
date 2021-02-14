package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.openclassrooms.realestatemanager.presentation.utils.Utils.isInternetAvailable;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NetworkConnectionTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void testConnectivityWhenAllEnable() {
        assertTrue(isInternetAvailable(ApplicationProvider.getApplicationContext()));
    }

    @Test
    public void isWifiEnabled() {
        WifiManager wifiManager = (WifiManager) ApplicationProvider.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        assert wifiManager != null;
        wifiManager.setWifiEnabled(false);
        wifiManager.disconnect();

        ConnectivityManager connMgr = (ConnectivityManager)
                ApplicationProvider.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        assert networkInfo != null;
        assertFalse(wifiManager.isWifiEnabled());
        //assertNotEquals(networkInfo.getType(), ConnectivityManager.TYPE_WIFI);
    }
}