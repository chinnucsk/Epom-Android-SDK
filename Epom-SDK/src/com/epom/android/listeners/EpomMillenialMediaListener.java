package com.epom.android.listeners;

import com.epom.android.type.AdNetwork;
import com.epom.android.view.EpomView;
import com.millennialmedia.android.MMAdView;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 6/6/12
 * Time: 11:56 AM
 */

public class EpomMillenialMediaListener extends AbstractListener implements MMAdView.MMAdListener {
    private final EpomView epomView;

    public EpomMillenialMediaListener(EpomView view) {
        super(AdNetwork.MILLENNIAL_MEDIA);
        epomView = view;
    }

    @Override
    public void MMAdReturned(MMAdView mmAdView) {
        onReceivedAd(epomView);
    }

    @Override
    public void MMAdFailed(MMAdView mmAdView) {
        onFailedToReceiveAd(epomView);
    }

    @Override
    public void MMAdClickedToNewBrowser(MMAdView mmAdView) {
        onShowAdScreen(epomView);
    }

    @Override
    public void MMAdClickedToOverlay(MMAdView mmAdView) {
       MMAdClickedToNewBrowser(mmAdView);
    }

    @Override
    public void MMAdOverlayLaunched(MMAdView mmAdView) {
    }

    @Override
    public void MMAdRequestIsCaching(MMAdView mmAdView) {
    }

    @Override
    public void MMAdCachingCompleted(MMAdView mmAdView, boolean b) {
    }
}
