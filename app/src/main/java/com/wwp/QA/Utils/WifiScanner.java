package com.wwp.QA.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.util.List;
import java.util.Locale;

/**
 * Portat din pms_3u354/mm (models/WifiScanner.java resp. Utils/WifiScanner.java) - imbunatatire de
 * stabilitate a conexiunii WiFi.
 *
 * In loc sa citeasca SSID-ul curent direct si sincron via wifiManager.getConnectionInfo() (risc de NPE
 * pe Android 10+), inregistreaza un ConnectivityManager.NetworkCallback care actualizeaza un cache
 * lastSSID la fiecare schimbare de retea; getCurrentConnectedSSID() doar citeste acest cache.
 *
 * prioritizeWifiNetwork() incearca sa prefere o retea cunoscuta, dar functioneaza doar sub Android 10 -
 * de la Android 10 (Q) incolo, API-urile necesare (getConfiguredNetworks/updateNetwork/saveConfiguration)
 * sunt deprecate fara inlocuitor pentru aplicatii obisnuite.
 *
 * Clasa e auto-continuta: nu e cablata in ciclul de viata al vreunei Activity.
 */
public class WifiScanner {

    private Context context;

    private WifiManager wifiManager;

    private boolean isNetworkCallbackRegistered = false;

    private String lastSSID;

    private NetworkCallback networkCallback;

    public WifiScanner(Context context) {
        this.context = context;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public void startScanning() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);

            NetworkRequest networkRequest = builder.build();

            networkCallback = new NetworkCallback();
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
            isNetworkCallbackRegistered = true;
        }
    }

    public void stopScanning() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isNetworkCallbackRegistered) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            try {
                connectivityManager.unregisterNetworkCallback(networkCallback);
                isNetworkCallbackRegistered = false;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public void prioritizeWifiNetwork(String ssid, int priority) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // getConfiguredNetworks(), WifiConfiguration, updateNetwork(), saveConfiguration()
            // are all deprecated on API 29+ with no available replacement for regular apps.
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
            if (configuredNetworks != null) {
                for (WifiConfiguration config : configuredNetworks) {
                    if (config.SSID != null && config.SSID.equals("\"" + ssid + "\"")) {
                        config.priority = priority;
                        wifiManager.updateNetwork(config);
                        wifiManager.saveConfiguration();
                    }
                }
            }
        }
    }

    public String getCurrentConnectedSSID() {

        if (lastSSID != null && !lastSSID.isEmpty()) {
            return lastSSID;
        }

        // Return empty string if SSID cannot be determined (avoids null in callers)
        return "";
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class NetworkCallback extends ConnectivityManager.NetworkCallback {

        @Override
        public void onAvailable(Network network) {
            lastSSID = getWifiSSID(network);
        }

        @Override
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            lastSSID = getWifiSSID(network);
        }

        @Override
        public void onLost(Network network) {
            // network gone - lastSSID keeps the last known value on purpose (matches pms_3u354 behavior)
        }

        private String getWifiSSID(Network network) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                // For Android 10 (Q) and above, use ConnectivityManager to get NetworkCapabilities
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                if (capabilities != null) {
                    WifiInfo wifiInfo = (WifiInfo) capabilities.getTransportInfo();
                    if (wifiInfo != null) {
                        String ssid = wifiInfo.getSSID();
                        if (ssid != null && ssid.startsWith("\"") && ssid.endsWith("\"")) {
                            ssid = ssid.substring(1, ssid.length() - 1);
                        }
                        Log.d("SSID", "Connected to: " + ssid);
                        return ssid;
                    }
                }
                return null;

            } else {

                // For Android versions below 10, use WifiManager to get WifiInfo
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();

                Log.i("WifiScanner", "getWifiSSID - Android bellow 10");

                if (wifiInfo != null && wifiInfo.getNetworkId() != -1) {
                    Log.i("WifiScanner", String.format(Locale.US, "getWifiSSID %s", wifiInfo.getSSID().replace("\"", "")));
                    return wifiInfo.getSSID().replace("\"", "");
                } else {
                    Log.i("WifiScanner", "getWifiSSID - wifiInfo.getSSID() is null because device is not connected");
                }
            }

            return null;
        }
    }
}
