package com.epom.android.listeners;

import com.epom.android.type.AdNetwork;
import com.epom.android.view.EpomView;
import com.inmobi.androidsdk.IMAdListener;
import com.inmobi.androidsdk.IMAdRequest;
import com.inmobi.androidsdk.IMAdView;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 3/2/12
 * Time: 1:16 PM
 */

public class EpomInMobiListener extends AbstractListener implements IMAdListener {
    private final EpomView epomView;

    public EpomInMobiListener(EpomView epomView) {
        super(AdNetwork.IN_MOBI);
        this.epomView = epomView;
    }

    @Override
    public void onAdRequestFailed(IMAdView imAdView, IMAdRequest.ErrorCode errorCode) {
        onFailedToReceiveAd(epomView);
    }

    @Override
    public void onAdRequestCompleted(IMAdView imAdView) {
       onReceivedAd(epomView);
    }

    @Override
    public void onShowAdScreen(IMAdView imAdView) {
        onShowAdScreen(epomView);
    }

    @Override
    public void onDismissAdScreen(IMAdView imAdView) {
    }
}
