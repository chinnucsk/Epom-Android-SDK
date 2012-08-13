package com.epom.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.epom.android.listeners.EpomAdListener;
import com.epom.android.view.EpomView;

/**
 * Created by IntelliJ IDEA.
 * User: anton
 * Date: 2/6/12
 * Time: 1:14 PM
 */

public class TestEpomSDK extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        EpomView epomView = ((EpomView) findViewById(R.id.epomView));
        epomView.setEpomAdListener(new EpomAdListener() {
            public void onReceivedAd(EpomView epomView) {
                Log.d("TestApp","--> Ad received callback.");
            }

            public void onFailedToReceiveAd(EpomView epomView) {
                Log.d("TestApp","--> Ad failed to be received callback.");
            }

            public void onShowAdScreen(EpomView epomView) {
                Log.d("TestApp","--> Ad show ad screen callback.");
            }
        });
        epomView.init();
        /*  // Create an ad.
        AdView adView = new AdView(this, AdSize.IAB_BANNER, "122234");

        // Create an ad request.
        AdRequest adRequest = new AdRequest();
        // Fill out ad request.

        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.

        // Start loading the ad in the background.
        adView.loadAd(adRequest);*/
    }
}
