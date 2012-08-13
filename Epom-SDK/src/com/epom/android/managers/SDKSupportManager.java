package com.epom.android.managers;

import com.epom.android.Util;
import com.epom.android.type.AdNetwork;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 6/11/12
 * Time: 10:37 AM
 */
public class SDKSupportManager {
    private static final Map<AdNetwork, Integer> SDKtoAPI = new HashMap<AdNetwork, Integer>();

    static{
        SDKtoAPI.put(AdNetwork.AD_MOB, 3);
        SDKtoAPI.put(AdNetwork.IN_MOBI, 4);
        SDKtoAPI.put(AdNetwork.SMAATO, 3);
        SDKtoAPI.put(AdNetwork.INNERACTIVE, 3);
        SDKtoAPI.put(AdNetwork.MILLENNIAL_MEDIA, 3);
        SDKtoAPI.put(AdNetwork.YOC_PERFORMANCE, 5);
        SDKtoAPI.put(AdNetwork.TAP_IT, 4);
        SDKtoAPI.put(AdNetwork.WAP_START, 7);
        SDKtoAPI.put(AdNetwork.TAP_JOY, 3);
    }

    public static boolean isSupported(AdNetwork adNetwork){
        return SDKtoAPI.get(adNetwork) <= Util.getAPILevel();
    }

    public static Map<AdNetwork, Integer> getSDKtoAPIMappings(){
        return Collections.unmodifiableMap(SDKtoAPI);
    }
}
