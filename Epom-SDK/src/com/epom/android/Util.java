package com.epom.android;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 4/19/12
 * Time: 10:52 AM
 */
public class Util {
    public static final String EPOM_LOG_TAG = "EpomSDK";

    public static StringBuilder inputStreamToString(InputStream is) {
        String line;
        StringBuilder total = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            Log.e(Util.EPOM_LOG_TAG, "Error while reading response.");
        }
        return total;
    }

    public static String md5(final String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2) {
                    h = "0" + h;
                }
                hexString.append(h);
            }
            return hexString.toString();

        } catch (Throwable e) {
            Log.e(Util.EPOM_LOG_TAG, "Error while calculating md5 hash.", e);
        }
        return null;
    }

    public static String getIpAddress() {
        String ipRegEx = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && Pattern.matches(ipRegEx, inetAddress.getHostAddress())) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e(Util.EPOM_LOG_TAG, "Error while obtaining devices IP address.", ex);
        }
        return null;
    }

    public static boolean isOnline(Context context) {
        try {
            if (Arrays.asList(context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions).contains(Manifest.permission.ACCESS_NETWORK_STATE)) {
                ConnectivityManager cm =
                        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    return true;
                }
            } else {
                Log.e(Util.EPOM_LOG_TAG, "Permission to access network state missing...");
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(Util.EPOM_LOG_TAG, e.getMessage());
        }
        return false;
    }

    public static JSONObject testJSON(StringBuilder jsonText) {
        try {
            return new JSONObject(jsonText.toString());
        } catch (JSONException e) {
            return null;
        }
    }

    public static int getAPILevel(){
        return Build.VERSION.SDK_INT;
    }
}
