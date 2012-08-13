package com.epom.android.managers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.Log;
import com.epom.android.Util;
import com.epom.android.type.ParamsType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.epom.android.type.ParamsType.*;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 2/20/12
 * Time: 10:33 AM
 */
public class ParamParser {
    public static final int MAX_REFRESH_INTERVAL = 90;
    public static final int MIN_REFRESH_INTERVAL = 15;

    private AttributeSet attrs;
    private Context context;
    private Map<ParamsType, Object> paramsMap;

    public ParamParser() {
    }

    public ParamParser(AttributeSet attrs, Context context) {
        this.attrs = attrs;
        this.context = context;
    }

    public static boolean checkPermissionsConfiguration(Context context) {
        try {
            if (!Arrays.asList(context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions).contains(Manifest.permission.INTERNET)) {
                Log.e(Util.EPOM_LOG_TAG, "Cannot perform ad request without Internet permission! Open manifest.xml and just before the final </manifest> tag add: <uses-permission android:name=\"android.permission.INTERNET\" />");
                return false;
            }
            if (!Arrays.asList(context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions).contains(Manifest.permission.READ_PHONE_STATE)) {
                Log.w(Util.EPOM_LOG_TAG, "Cannot get device ID (IMEI) without READ_PHONE_STATE permission! Open manifest.xml and just add before the final </manifest> tag: <uses-permission android:name=\"android.permission.READ_PHONE_STATE\" />");
            }
            if (!(Arrays.asList(context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions).contains(Manifest.permission.ACCESS_FINE_LOCATION) || Arrays.asList(context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions).contains(Manifest.permission.ACCESS_COARSE_LOCATION))) {
                Log.w(Util.EPOM_LOG_TAG, "No location information sources allowed.To enable geolocation targeting add <uses-permission android:name=\"android.permission.ACCESS_COARSE_LOCATION\" /> or <uses-permission android:name=\"android.permission.ACCESS_FINE_LOCATION\" /> before the final </manifest> tag in your manifest.xml file");
            }
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(Util.EPOM_LOG_TAG, "Error while obtaining permissions configuration.", e);
            return false;
        }
    }

    public Map<ParamsType, Object> getParamsMap() {
        if (paramsMap != null) {
            return Collections.unmodifiableMap(paramsMap);
        }
        if (attrs == null) {
            Log.e(Util.EPOM_LOG_TAG, "No configuration parameters found.");
            return null;
        }
        if (checkPermissionsConfiguration(context)) {
            Map<ParamsType, Object> tmp = new HashMap<ParamsType, Object>();
            String namespace = "http://schemas.android.com/apk/res/" + context.getPackageName();
            Log.d(Util.EPOM_LOG_TAG, "Starting parsing data.");
            Integer color;
            try {
                String value = attrs.getAttributeValue(namespace, VIEW_BACKGROUND.getUrlField());
                if (value != null) {
                    color = Color.parseColor(value);
                } else {
                    color = Color.BLACK;
                    Log.d(Util.EPOM_LOG_TAG, "viewBackground applying defaults: black.");
                }
            } catch (Exception e) {
                Log.e(Util.EPOM_LOG_TAG, "Error while parsing viewBackground parameter value, applying defaults: black.", e);
                color = Color.BLACK;
            }
            tmp.put(VIEW_BACKGROUND, color);
            String key = attrs.getAttributeValue(namespace, KEY.getUrlField());
            if (key != null && !key.equals("")) {
                tmp.put(KEY, key.trim());
            } else {
                Log.e(Util.EPOM_LOG_TAG, "No key found.");
                return null;
            }
            tmp.put(ADTYPE, attrs.getAttributeValue(namespace, ADTYPE.getUrlField()));
            Integer interval = attrs.getAttributeIntValue(namespace, REFRESH_INTERVAL.getUrlField(), MIN_REFRESH_INTERVAL);
            if ((interval < MIN_REFRESH_INTERVAL)) {
                interval = MIN_REFRESH_INTERVAL;
                Log.d(Util.EPOM_LOG_TAG, "Minimum refresh interval exceed - setting to default " + MIN_REFRESH_INTERVAL + " sec");
            }
            if ((interval > MAX_REFRESH_INTERVAL)) {
                interval = MAX_REFRESH_INTERVAL;
                Log.d(Util.EPOM_LOG_TAG, "Maximum refresh interval exceed - setting to default " + MAX_REFRESH_INTERVAL + " sec");
            }
            tmp.put(REFRESH_INTERVAL, interval);

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            tmp.put(ANDROID_IDENT, tm.getDeviceId());
            Log.d(Util.EPOM_LOG_TAG, "Finished parsing data.");
            paramsMap = tmp;
            return tmp;
        } else {
            Log.e(Util.EPOM_LOG_TAG, "Execution aborted due to configuration errors. See debug log for more information.");
            return null;
        }

    }

    public ParamParser setParamsMap(HashMap<ParamsType, Object> paramsMap) {
        this.paramsMap = paramsMap;
        return this;
    }

    public String getAsUrl() {
        StringBuilder tmp = new StringBuilder();
        for (Map.Entry<ParamsType, Object> e : getParamsMap().entrySet()) {
            if (e.getKey() != REFRESH_INTERVAL && e.getKey() != VIEW_BACKGROUND && e.getValue() != null) {
                tmp.append(e.getKey().getUrlField()).append("=").append(e.getValue()).append("&");
            }
        }
        return tmp.deleteCharAt(tmp.lastIndexOf("&")).toString();
    }
}
