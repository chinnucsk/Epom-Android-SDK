package com.epom.android.listeners;

import com.epom.android.view.EpomView;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 4/13/12
 * Time: 5:16 PM
 *
 * Class is an open interface to anyone who may want to tie own custom
 * activities to Epom SDK lifecycle callbacks.
 */

public interface EpomAdListener {
    int MAX_RETRY_INTERVAL = 5000;
    int INCREMENT = 500;

    void onReceivedAd(EpomView epomView);

    void onFailedToReceiveAd(EpomView epomView);

    void onShowAdScreen(EpomView epomView);
}
