package com.epom.android.listeners;

import com.MASTAdView.MASTAdView;
import com.MASTAdView.MASTAdViewCore;
import com.epom.android.type.AdNetwork;
import com.epom.android.view.EpomView;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 6/11/12
 * Time: 3:05 PM
 */
public class EpomYOCDListener extends AbstractListener implements MASTAdViewCore.MASTOnAdDownload, MASTAdViewCore.MASTOnAdClickListener{
    private final EpomView view;
    
    public EpomYOCDListener(EpomView view) {
        super(AdNetwork.YOC_PERFORMANCE);
        this.view = view;
    }

    @Override
    public void begin(MASTAdView sender) {
    }

    @Override
    public void end(MASTAdView sender) { 
        onReceivedAd(view);
    }

    @Override
    public void error(MASTAdView sender, String error) {
        onFailedToReceiveAd(view);
    }

    @Override
    public void click(MASTAdView sender, String url) {
        onShowAdScreen(view);
    }
}
