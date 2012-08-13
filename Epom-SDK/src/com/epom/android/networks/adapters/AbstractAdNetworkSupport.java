package com.epom.android.networks.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import com.epom.android.Util;
import com.epom.android.type.AdNetwork;
import com.epom.android.networks.AdNetworkSupport;
import com.epom.android.type.AdType;
import com.epom.android.view.EpomView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 6/6/12
 * Time: 4:02 PM
 */
public abstract class AbstractAdNetworkSupport implements AdNetworkSupport {
    protected Map<String, String> adNetwokCredentials = new LinkedHashMap<String, String>();

    public AbstractAdNetworkSupport(JSONObject jsonResponse) throws JSONException {
        if (getAdNetwork().getParameters() != null) {
            for (String param : getAdNetwork().getParameters()) {
                adNetwokCredentials.put(param, jsonResponse.getJSONObject(JSON_AD_NETWORK_PARAMETERS).getString(param));
            }
        }
    }

    @Override
    public View loadAd(Context context, AdType adType, EpomView epomView) {
        if (!(context instanceof Activity)) {
            Log.e(Util.EPOM_LOG_TAG, "Argument must be activity context");
            return null;
        }
        Log.d(Util.EPOM_LOG_TAG, "(" + getAdNetwork().getName() + ") Loading new ad...");
        try {
            return getProvidedView(context, adType, epomView);
        } catch (Exception e) {
            Log.e(Util.EPOM_LOG_TAG, "Error occurred while retrieving ad.", e);
            return null;
        }
    }

    @Override
    public void setAdNetworkCredentials(Map<String, String> adNetworkCredentials) {
        this.adNetwokCredentials = adNetworkCredentials;
    }

    @Override
    public Map<String, String> getAdNetworkCredentials() {
        return adNetwokCredentials;
    }

    @Override
    public abstract AdNetwork getAdNetwork();

    public abstract View getProvidedView(Context context, AdType adType, EpomView epomView);
}
