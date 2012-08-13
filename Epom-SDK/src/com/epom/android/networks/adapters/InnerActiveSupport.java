package com.epom.android.networks.adapters;

import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import com.epom.android.listeners.InnerActive.EpomInnerActiveListener;
import com.epom.android.type.AdNetwork;
import com.epom.android.type.AdType;
import com.epom.android.view.EpomView;
import com.epom.android.view.InnerActiveView;
import com.inneractive.api.ads.InneractiveAd;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 6/7/12
 * Time: 12:56 PM
 */

public class InnerActiveSupport extends AbstractAdNetworkSupport{
    private final static AdNetwork AD_NETWORK = AdNetwork.INNERACTIVE;
    private static boolean firstStart = true;

    public InnerActiveSupport(JSONObject jsonResponse) throws JSONException {
        super(jsonResponse);
    }

    @Override
    public AdNetwork getAdNetwork() {
        return AD_NETWORK;
    }

    @Override
    public View getProvidedView(Context context, AdType adType, EpomView epomView) {
        InnerActiveView tmpView = new InnerActiveView(context);
        InneractiveAd.displayAd(context, tmpView, adNetwokCredentials.get(AD_NETWORK.getParameters()[0]), InneractiveAd.IaAdType.Banner, 0);
        if(firstStart){
            LocalBroadcastManager.getInstance(context).registerReceiver(EpomInnerActiveListener.getInstance(epomView), new IntentFilter("InneractiveAd"));
            firstStart = !firstStart;
        }

        return tmpView;
    }
}
