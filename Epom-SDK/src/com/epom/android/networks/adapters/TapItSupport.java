package com.epom.android.networks.adapters;

import android.content.Context;
import android.view.View;
import com.epom.android.listeners.EpomTapItListener;
import com.epom.android.type.AdNetwork;
import com.epom.android.type.AdType;
import com.epom.android.view.EpomView;
import com.tapit.adview.AdView;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 6/26/12
 * Time: 11:28 AM
 */
public class TapItSupport extends AbstractAdNetworkSupport{
    private final static AdNetwork AD_NETWORK = AdNetwork.TAP_IT;

    public TapItSupport(JSONObject jsonResponse) throws JSONException {
        super(jsonResponse);
    }

    @Override
    public AdNetwork getAdNetwork() {
        return AD_NETWORK;
    }

    @Override
    public View getProvidedView(Context context, AdType adType, EpomView epomView) {
        AdView view = new AdView(context, adNetwokCredentials.get(AD_NETWORK.getParameters()[0]));
        EpomTapItListener listener = new EpomTapItListener(epomView);
        view.setOnAdDownload(listener);
        view.setOnAdClickListener(listener);
        view.setUpdateTime(0);
        view.update(true);
        return view;
    }
}
