package com.epom.android.networks.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import com.epom.android.listeners.EpomInMobiListener;
import com.epom.android.type.AdNetwork;
import com.epom.android.type.AdType;
import com.epom.android.view.EpomView;
import com.inmobi.androidsdk.IMAdRequest;
import com.inmobi.androidsdk.IMAdView;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 2/29/12
 * Time: 11:28 AM
 */

public class InMobiSupport extends AbstractAdNetworkSupport {
    private final static AdNetwork AD_NETWORK = AdNetwork.IN_MOBI;

    public InMobiSupport(JSONObject jsonResponse) throws JSONException {
        super(jsonResponse);
    }

    @Override
    public AdNetwork getAdNetwork() {
        return AD_NETWORK;
    }

    @Override
    public View getProvidedView(Context context, AdType adType, EpomView epomView) {
        IMAdView imAdView = new IMAdView((Activity) context, (Integer) AdType.getBannerSizeMappings().get(AD_NETWORK).get(adType), adNetwokCredentials.get(AD_NETWORK.getParameters()[0]));
        imAdView.setRefreshInterval(IMAdView.REFRESH_INTERVAL_OFF);
        imAdView.setIMAdListener(new EpomInMobiListener(epomView));
        IMAdRequest adRequest = new IMAdRequest();
        imAdView.setIMAdRequest(adRequest);
        imAdView.loadNewAd();
        return imAdView;
    }
}
