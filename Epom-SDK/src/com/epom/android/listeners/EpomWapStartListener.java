package com.epom.android.listeners;

import com.epom.android.type.AdNetwork;
import com.epom.android.view.EpomView;
import ru.wapstart.plus1.sdk.Plus1BannerDownloadListener;
import ru.wapstart.plus1.sdk.Plus1BannerViewStateListener;

public class EpomWapStartListener extends AbstractListener implements Plus1BannerDownloadListener, Plus1BannerViewStateListener{
    private final EpomView epomView;

    public EpomWapStartListener(EpomView view) {
        super(AdNetwork.WAP_START);
        this.epomView = view;
    }

    @Override
    public void onBannerLoaded() {
        onReceivedAd(epomView);
    }

    @Override
    public void onBannerLoadFailed(LoadError loadError) {
        onFailedToReceiveAd(epomView);
    }

    @Override
    public void onShowBannerView() {
    }

    @Override
    public void onHideBannerView() {
    }

    @Override
    public void onCloseBannerView() {
    }

    @Override
    public void onClick() {
        onShowAdScreen(epomView);
    }
}
