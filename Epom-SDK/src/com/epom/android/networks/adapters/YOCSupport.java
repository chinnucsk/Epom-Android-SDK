package com.epom.android.networks.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.MASTAdView.MASTAdView;
import com.epom.android.listeners.EpomYOCDListener;
import com.epom.android.type.AdNetwork;
import com.epom.android.type.AdType;
import com.epom.android.view.EpomView;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 6/11/12
 * Time: 1:05 PM
 */
public class YOCSupport extends AbstractAdNetworkSupport{
    private final static AdNetwork AD_NETWORK = AdNetwork.YOC_PERFORMANCE;

    public YOCSupport(JSONObject jsonResponse) throws JSONException {
        super(jsonResponse);
    }

    @Override
    public AdNetwork getAdNetwork() {
        return AD_NETWORK;
    }

    @Override
    public View getProvidedView(Context context, AdType adType, EpomView epomView) {
        MASTAdView adserverView = new MASTAdView(context, Integer.parseInt(adNetwokCredentials.get(AD_NETWORK.getParameters()[0])),
                                                          Integer.parseInt(adNetwokCredentials.get(AD_NETWORK.getParameters()[1])));
        adserverView.setLayoutParams(new ViewGroup.LayoutParams(adType.getWidth(), adType.getHeight()));
        adserverView.setMinSizeX(adType.getWidth());
        adserverView.setMinSizeY(adType.getHeight());
        adserverView.setMaxSizeX(adType.getWidth());
        adserverView.setMaxSizeY(adType.getHeight());
        EpomYOCDListener listener = new EpomYOCDListener(epomView);
        adserverView.setOnAdDownload(listener);
        adserverView.setOnAdClickListener(listener);
        adserverView.update();
        return adserverView;
    }
}
