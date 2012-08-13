package com.epom.android.listeners;

import android.view.View;
import com.epom.android.type.AdNetwork;
import com.epom.android.view.EpomView;
import com.epom.android.view.TapJoyView;
import com.tapjoy.TapjoyDisplayAdNotifier;

public class EpomTapJoyListener extends AbstractListener implements TapjoyDisplayAdNotifier {
    private final EpomView epomView;
    private final TapJoyView carryingView;

    public EpomTapJoyListener( EpomView view, TapJoyView carryingView) {
        super(AdNetwork.TAP_JOY);
        this.epomView = view;
        this.carryingView = carryingView;
    }

    @Override
    public void getDisplayAdResponse(final View view) {
        carryingView.getUiHandler().post(
                new Runnable() {
                    @Override
                    public void run() {
                        carryingView.updateView(view);
                    }
                }
        );
        onReceivedAd(epomView);
    }

    @Override
    public void getDisplayAdResponseFailed(String s) {
        onFailedToReceiveAd(epomView);
    }

    @Override
    public void getDisplayAdClicked() {
        onShowAdScreen(epomView);
    }
}
