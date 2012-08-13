package com.epom.android.networks.adapters;

import android.content.Context;
import android.view.View;
import com.epom.android.listeners.EpomWapStartListener;
import com.epom.android.type.AdNetwork;
import com.epom.android.type.AdType;
import com.epom.android.view.EpomView;
import org.json.JSONException;
import org.json.JSONObject;
import ru.wapstart.plus1.sdk.Plus1BannerAsker;
import ru.wapstart.plus1.sdk.Plus1BannerRequest;
import ru.wapstart.plus1.sdk.Plus1BannerView;

public class WapStartSupport extends AbstractAdNetworkSupport{
    private final static AdNetwork AD_NETWORK = AdNetwork.WAP_START;

    public WapStartSupport(JSONObject jsonResponse) throws JSONException {
        super(jsonResponse);
    }

    @Override
    public AdNetwork getAdNetwork() {
        return AD_NETWORK;
    }

    @Override
    public View getProvidedView(Context context, AdType adType, EpomView epomView) {
        Plus1BannerView adView = new Plus1BannerView(context);

        Plus1BannerAsker asker = new Plus1BannerAsker(
                        Plus1BannerRequest
                                .create()
                                .setApplicationId(Integer.parseInt(adNetwokCredentials.get(AD_NETWORK.getParameters()[0]))),
                        adView
                                .enableAnimationFromTop()
                );

        EpomWapStartListener listener = new EpomWapStartListener(epomView);
        asker.setDownloadListener(listener);
        asker.setViewStateListener(listener);
        asker.startOnce();

        return adView;
    }
}
