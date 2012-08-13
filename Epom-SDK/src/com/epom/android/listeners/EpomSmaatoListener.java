package com.epom.android.listeners;

import com.epom.android.type.AdNetwork;
import com.epom.android.view.EpomView;
import com.smaato.SOMA.AdDownloader;
import com.smaato.SOMA.AdListener;
import com.smaato.SOMA.ErrorCode;
import com.smaato.SOMA.SOMAReceivedBanner;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 4/17/12
 * Time: 12:29 PM
 */
public class EpomSmaatoListener extends AbstractListener implements AdListener {
    private final EpomView epomView;

    public EpomSmaatoListener(EpomView view) {
        super(AdNetwork.SMAATO);
        this.epomView = view;
    }

    @Override
    public void onFailedToReceiveAd(AdDownloader adDownloader, ErrorCode errorCode) {
        onFailedToReceiveAd(epomView);
    }

    @Override
    public void onReceiveAd(AdDownloader adDownloader, SOMAReceivedBanner somaReceivedBanner) {
        onReceivedAd(epomView);
    }
}
