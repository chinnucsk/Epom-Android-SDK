package com.epom.android.listeners;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import com.epom.android.Util;
import com.epom.android.type.AdNetwork;
import com.epom.android.synchronization.EpomSynchronizer;
import com.epom.android.synchronization.ImpressionsTrackingBean;
import com.epom.android.type.SynchronizationType;
import com.epom.android.view.EpomView;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 6/6/12
 * Time: 3:17 PM
 */

public abstract class AbstractListener implements EpomAdListener {
    private final AdNetwork adNetwork;
    private int delay;

    public AbstractListener(AdNetwork adNetwork) {
        this.adNetwork = adNetwork;
    }

    @Override
    public void onReceivedAd(final EpomView epomView) {
        if (epomView == null) {
            Log.w(Util.EPOM_LOG_TAG, "No source view found. Aborting impression synchronization");
            return;
        }
        Log.d(Util.EPOM_LOG_TAG, new StringBuilder("(").append(adNetwork.getName()).append(")Successful ad request...").toString());
        final ImpressionsTrackingBean i = epomView.getImpressionsTrackingBean();
        delay = 0;
        epomView.post(new Runnable() {
            @Override
            public void run() {
                epomView.setVisibility(View.VISIBLE);
                if (epomView.switchAd(adNetwork)) {
                    Log.d(Util.EPOM_LOG_TAG, "(" + adNetwork.getName() + ")Informing server for successful ad...");
                    if (i != null) {
                        try {
                            Intent syncIntent = new Intent(epomView.getContext(), EpomSynchronizer.class);
                            syncIntent.putExtra("syncData", i);
                            syncIntent.putExtra("syncType", SynchronizationType.IMPRESSION_SYN);
                            epomView.getContext().startService(syncIntent);
                        } catch (Exception e) {
                            Log.e(Util.EPOM_LOG_TAG, "Error while requesting impression synchronization.", e);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onFailedToReceiveAd(final EpomView epomView) {
        if (epomView == null) {
            Log.w(Util.EPOM_LOG_TAG, "No source view found. Aborting impression synchronization");
            return;
        }
        Log.d(Util.EPOM_LOG_TAG, "(" + adNetwork.getName() + ")Informing server for unsuccessful request");
        epomView.post(new Runnable() {
            @Override
            public void run() {
                epomView.excludeCurrentBanner();
                if (delay > MAX_RETRY_INTERVAL) {
                    delay = 0;
                }

                epomView.getRedrowHandler().sleep(delay += INCREMENT);
            }
        });
    }

    @Override
    public void onShowAdScreen(final EpomView epomView) {
        if (epomView == null) {
            Log.w(Util.EPOM_LOG_TAG, "No source view found. Aborting impression synchronization");
            return;
        }
        EpomSynchronizer.setCallBackView(epomView);
        ImpressionsTrackingBean i;
        if ((i = epomView.getImpressionsTrackingBean()) != null) {
            try {
                Intent syncIntent = new Intent(epomView.getContext(), EpomSynchronizer.class);
                syncIntent.putExtra("syncData", i);
                syncIntent.putExtra("syncType", SynchronizationType.CLICK_SYN);
                epomView.getContext().startService(syncIntent);
            } catch (Exception e) {
                Log.e(Util.EPOM_LOG_TAG, "Error while requesting click synchronization.", e);
            }
            Log.d(Util.EPOM_LOG_TAG, "Informing server for click...");
        }
    }
}
