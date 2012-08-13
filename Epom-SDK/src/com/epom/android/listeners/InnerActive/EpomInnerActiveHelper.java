package com.epom.android.listeners.InnerActive;

import com.epom.android.listeners.AbstractListener;
import com.epom.android.type.AdNetwork;
import com.epom.android.view.EpomView;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 6/7/12
 * Time: 1:58 PM
 */
public class EpomInnerActiveHelper extends AbstractListener {
    private EpomView epomView;
    private long lastCountTimestamp;

    public EpomInnerActiveHelper(EpomView epomView) {
        super(AdNetwork.INNERACTIVE);
        this.epomView = epomView;
    }

    public void onReceivedAd(){
        if(System.currentTimeMillis() - lastCountTimestamp > epomView.getRefreshInterval()){
            onReceivedAd(epomView);
        }
        lastCountTimestamp = System.currentTimeMillis();
    }

    public void onFailedToReceiveAd(){
        onFailedToReceiveAd(epomView);
    }

    public void onShowAdScreen(){
        onShowAdScreen(epomView);
    }
}
