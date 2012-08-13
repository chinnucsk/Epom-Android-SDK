package com.epom.android.networks.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import com.epom.android.type.AdNetwork;
import com.epom.android.type.AdType;
import com.epom.android.view.EpomView;
import com.epom.android.listeners.EpomMillenialMediaListener;
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMAdViewSDK;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 6/6/12
 * Time: 10:54 AM
 */

public class MillenialMediaSupport extends AbstractAdNetworkSupport {
    private final static AdNetwork AD_NETWORK = AdNetwork.MILLENNIAL_MEDIA;

    public MillenialMediaSupport(JSONObject jsonResponse) throws JSONException {
        super(jsonResponse);
    }

    @Override
    public AdNetwork getAdNetwork() {
        return AD_NETWORK;
    }

    @Override
    public View getProvidedView(Context context, AdType adType, EpomView epomView) {
        MMAdView adView = new MMAdView((Activity) context, adNetwokCredentials.get(AD_NETWORK.getParameters()[0]), MMAdView.BANNER_AD_RECTANGLE, MMAdView.REFRESH_INTERVAL_OFF);
        adView.setId(MMAdViewSDK.DEFAULT_VIEWID);
        adView.setListener(new EpomMillenialMediaListener(epomView));
        adView.callForAd();
        adView.setWidth(String.valueOf(adType.getWidth()));
        adView.setHeight(String.valueOf(adType.getHeight()));
        return adView;
    }
}
