package com.epom.android.listeners;

import com.epom.android.type.AdNetwork;
import com.epom.android.view.EpomView;
import com.tapit.adview.AdViewCore;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 6/26/12
 * Time: 11:23 AM
 */
public class EpomTapItListener extends AbstractListener implements AdViewCore.OnAdDownload, AdViewCore.OnAdClickListener {
    private final EpomView epomView;

    public EpomTapItListener(EpomView view) {
        super(AdNetwork.TAP_IT);
        this.epomView = view;
    }

    @Override
    public void click(String s) {
        onShowAdScreen(epomView);
    }

    @Override
    public void begin() {
    }

    @Override
    public void end() {
        onReceivedAd(epomView);
    }

    @Override
    public void error(String s) {
        onFailedToReceiveAd(epomView);
    }
}
