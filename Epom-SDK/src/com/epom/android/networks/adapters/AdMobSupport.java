package com.epom.android.networks.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import com.epom.android.Util;
import com.epom.android.listeners.EpomAdMobListener;
import com.epom.android.type.AdNetwork;
import com.epom.android.type.AdType;
import com.epom.android.view.EpomView;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 2/28/12
 * Time: 12:45 PM
 */

public class AdMobSupport extends AbstractAdNetworkSupport{
    private final static AdNetwork AD_NETWORK = AdNetwork.AD_MOB;

    public AdMobSupport(JSONObject jsonResponse) throws JSONException {
        super(jsonResponse);
    }

    @Override
    public AdNetwork getAdNetwork() {
        return AD_NETWORK;
    }

    @Override
    public View getProvidedView(Context context, AdType adType, EpomView epomView) {
        AdSize adSize = (AdType.getBannerSizeMappings().get(AD_NETWORK).get(adType) != null ? (AdSize) AdType.getBannerSizeMappings().get(AD_NETWORK).get(adType) : null);
        if (adSize == null) {
            Log.e(Util.EPOM_LOG_TAG, "AdMob doesn't support specified format");
            return null;
        }
        AdView adView = new AdView((Activity) context, adSize, adNetwokCredentials.get(AD_NETWORK.getParameters()[0]));
        adView.setAdListener(new EpomAdMobListener(epomView));
        adView.loadAd(new AdRequest());
        return adView;
    }
}
