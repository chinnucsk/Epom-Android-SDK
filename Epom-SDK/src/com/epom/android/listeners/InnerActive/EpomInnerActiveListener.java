package com.epom.android.listeners.InnerActive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.epom.android.Util;
import com.epom.android.view.EpomView;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 6/7/12
 * Time: 2:16 PM
 */
public class EpomInnerActiveListener extends BroadcastReceiver {
    private static final String EXTRA_MESSAGE_FIELD = "message";
    private EpomInnerActiveHelper helper;
    private int lastReceived;

    public static EpomInnerActiveListener getInstance(EpomView view) {
        return new EpomInnerActiveListener(new EpomInnerActiveHelper(view));
    }

    private EpomInnerActiveListener(EpomInnerActiveHelper helper) {
        this.helper = helper;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if(intent.hashCode() != lastReceived){
                switch (InnerActiveEvent.valueOf(intent.getStringExtra(EXTRA_MESSAGE_FIELD))) {
                    case IaAdReceived:
                    case IaDefaultAdReceived:
                        helper.onReceivedAd();
                        break;
                    case IaAdFailed:
                        helper.onFailedToReceiveAd();
                        break;
                    case IaAdClicked:
                        helper.onShowAdScreen();
                        break;
                }
                lastReceived = intent.hashCode();
            }

        } catch (Exception e) {
            Log.e(Util.EPOM_LOG_TAG, "Error while processing broadcast event.", e);
        }
    }
}
