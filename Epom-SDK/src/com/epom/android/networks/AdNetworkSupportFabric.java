package com.epom.android.networks;

import android.util.Log;
import com.epom.android.Util;
import com.epom.android.networks.adapters.*;
import com.epom.android.type.AdNetwork;
import org.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 2/29/12
 * Time: 12:39 PM
 */
public class AdNetworkSupportFabric {
    private static AdNetworkSupportFabric instance;

    public static AdNetworkSupportFabric getInstance() {
        if (instance == null) {
            instance = new AdNetworkSupportFabric();
        }
        return instance;
    }

    public BaseAdNetworkSupport getAdNetworkSupport(AdNetwork adNetwork, JSONObject jsonResponse) {
        try {
            switch (adNetwork) {
                case AD_MOB:
                    return new AdMobSupport(jsonResponse);
                case IN_MOBI:
                    return new InMobiSupport(jsonResponse);
                case SMAATO:
                    return new SmaatoSupport(jsonResponse);
                case MILLENNIAL_MEDIA:
                    return new MillenialMediaSupport(jsonResponse);
                case INNERACTIVE:
                    return new InnerActiveSupport(jsonResponse);
                case YOC_PERFORMANCE:
                    return new YOCSupport(jsonResponse);
                case TAP_IT:
                    return new TapItSupport(jsonResponse);
                case WAP_START:
                    return new WapStartSupport(jsonResponse);
                case TAP_JOY:
                    return new TapJoySupport(jsonResponse);
                default: {
                    Log.e(Util.EPOM_LOG_TAG, "Can't find support class for network: " + adNetwork.getName());
                    return null;
                }
            }
        } catch (Exception e) {
            Log.e(Util.EPOM_LOG_TAG, "Exception obtaining ad network support fabric", e);
            return null;
        }
    }
}
