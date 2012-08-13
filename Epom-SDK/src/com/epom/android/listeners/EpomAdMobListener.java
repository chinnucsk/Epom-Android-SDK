package com.epom.android.listeners;

import com.epom.android.type.AdNetwork;
import com.epom.android.view.EpomView;
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 3/2/12
 * Time: 12:13 PM
 */

public class EpomAdMobListener extends AbstractListener implements AdListener {
    private final EpomView epomView;

    public EpomAdMobListener( EpomView view) {
        super(AdNetwork.AD_MOB);
        this.epomView = view;
    }

    @Override
    public void onFailedToReceiveAd(Ad ad, AdRequest.ErrorCode errorCode) {
        onFailedToReceiveAd(epomView);
    }

    @Override
    public void onReceiveAd(Ad ad) {
        onReceivedAd(epomView);
    }

    @Override
    public void onPresentScreen(Ad ad) {
        onShowAdScreen(epomView);
    }

    @Override
    public void onDismissScreen(Ad ad) {
    }

    @Override
    public void onLeaveApplication(Ad ad) {
    }
}
