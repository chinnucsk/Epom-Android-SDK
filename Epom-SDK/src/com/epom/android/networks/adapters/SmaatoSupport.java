package com.epom.android.networks.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import com.epom.android.Util;
import com.epom.android.listeners.EpomSmaatoListener;
import com.epom.android.type.AdNetwork;
import com.epom.android.type.AdType;
import com.epom.android.view.EpomView;
import com.google.ads.AdSize;
import com.smaato.SOMA.AdDownloader;
import com.smaato.SOMA.SOMABanner;
import com.smaato.SOMA.SOMAMedRectBanner;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 4/17/12
 * Time: 11:23 AM
 */
public class SmaatoSupport extends AbstractAdNetworkSupport{
    private final static AdNetwork AD_NETWORK = AdNetwork.SMAATO;

    public SmaatoSupport(JSONObject jsonResponse) throws JSONException {
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
            Log.e(Util.EPOM_LOG_TAG, "Smaato doesn't support specified format.");
            return null;
        }

            SOMABanner banner;
            if (adType == AdType.BANNER_320X50) {
                banner = new SOMABanner(context);
            } else {
                banner = new SOMAMedRectBanner(context);
            }

            banner.setLayoutParams(new RelativeLayout.LayoutParams(adType.getWidth(), adType.getHeight()));
            banner.setPublisherId(Integer.valueOf(adNetwokCredentials.get(AD_NETWORK.getParameters()[0])));
            banner.setAdSpaceId(Integer.valueOf(adNetwokCredentials.get(AD_NETWORK.getParameters()[1])));
            banner.setMediaType(AdDownloader.MediaType.IMG);
            banner.setAutoRefresh(false);
            banner.setAnimationType(SOMABanner.AnimationType.NO_ANIMATION);
            banner.addAdListener(new EpomSmaatoListener(epomView));
            banner.asyncLoadNewBanner();
            return banner;
    }
}
