package com.epom.android.networks.adapters;

import android.content.Context;
import android.view.View;
import com.epom.android.listeners.EpomTapJoyListener;
import com.epom.android.type.AdNetwork;
import com.epom.android.type.AdType;
import com.epom.android.view.EpomView;
import com.epom.android.view.TapJoyView;
import com.tapjoy.TapjoyConnect;
import com.tapjoy.TapjoyDisplayAdSize;
import org.json.JSONException;
import org.json.JSONObject;

public class TapJoySupport extends AbstractAdNetworkSupport{
    private final static AdNetwork AD_NETWORK = AdNetwork.TAP_JOY;

    public TapJoySupport(JSONObject jsonResponse) throws JSONException {
        super(jsonResponse);
    }

    @Override
    public AdNetwork getAdNetwork() {
        return AD_NETWORK;
    }

    @Override
    public View getProvidedView(Context context, AdType adType, EpomView epomView) {
        TapJoyView adView = new TapJoyView(context);
        TapjoyConnect tapJoyConnect;
            TapjoyConnect.requestTapjoyConnect(context, adNetwokCredentials.get(AD_NETWORK.getParameters()[0]), adNetwokCredentials.get(AD_NETWORK.getParameters()[1]));
            tapJoyConnect = TapjoyConnect.getTapjoyConnectInstance();
            tapJoyConnect.enableBannerAdAutoRefresh(false);
            tapJoyConnect.setBannerAdSize(TapjoyDisplayAdSize.TJC_AD_BANNERSIZE_320X50);

        EpomTapJoyListener listener = new EpomTapJoyListener(epomView, adView);
        tapJoyConnect.getDisplayAd(listener);

        return  adView;
    }
}
