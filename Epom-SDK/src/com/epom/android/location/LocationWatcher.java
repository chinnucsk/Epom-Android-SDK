package com.epom.android.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import com.epom.android.Util;
import com.epom.android.type.ParamsType;

import java.util.*;

import static com.epom.android.type.ParamsType.LATITUDE;
import static com.epom.android.type.ParamsType.LONGITUDE;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 2/20/12
 * Time: 1:08 PM
 */
public class LocationWatcher {
    private final Context context;
    private Map<ParamsType, Double> currentCoords = new HashMap<ParamsType, Double>();

    public LocationWatcher(Context context) {
        this.context = context;
    }

    public List<String> getCurrentPermissions() {
        try {
            return Arrays.asList(context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(Util.EPOM_LOG_TAG, e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean GPSAvailable() {
        LocationManager loc_manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List str = loc_manager.getProviders(true);
        return str.size() > 0;
    }

    public Map<ParamsType, Double> getCurrentCoords() {
        return currentCoords;
    }

    public String getCoordsAsUrl() {
        StringBuilder tmp = new StringBuilder();
        int i = currentCoords.size();
        for (Map.Entry<ParamsType, Double> e : currentCoords.entrySet()) {
            tmp.append(e.getKey().getUrlField()).append("=").append(e.getValue());
            if (--i > 0) {
                tmp.append("&");
            }
        }
        if (tmp.length() > 0) {
            tmp.insert(0, "&");
        }
        return tmp.toString();
    }

    public void watch() {
        if (GPSAvailable()) {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    Log.d(Util.EPOM_LOG_TAG, "Location update received " + location);
                    currentCoords.put(LATITUDE, location.getLatitude());
                    currentCoords.put(LONGITUDE, location.getLongitude());
                }

                public void onStatusChanged(String s, int i, Bundle bundle) {
                }

                public void onProviderEnabled(String s) {
                }


                public void onProviderDisabled(String s) {
                    currentCoords.clear();
                }
            };
            try {
                List<String> permissions = getCurrentPermissions();
                LocationManager locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                if (permissions.contains(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                    if (locManager.getBestProvider(criteria, true) != null) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                    }
                }
                if (permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    if (locManager.getBestProvider(criteria, true) != null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    }
                }
            } catch (Throwable e) {
                Log.e(Util.EPOM_LOG_TAG, e.getMessage(), e);
            }
        } else {
            Log.w(Util.EPOM_LOG_TAG, "No location aware devices were found.");
        }
    }
}
