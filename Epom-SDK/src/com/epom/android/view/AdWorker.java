package com.epom.android.view;

import android.app.KeyguardManager;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import com.epom.android.Util;
import com.epom.android.managers.TrackingManager;
import com.epom.android.networks.AdNetworkSupport;
import com.epom.android.networks.AdNetworkSupportFabric;
import com.epom.android.type.AdNetwork;
import com.epom.android.type.SDKSupportedNetworks;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 4/25/12
 * Time: 3:45 PM
 */
public class AdWorker implements Runnable {
    public static final String BASE_SERVER_URL = "http://api.epom.com/";
    //public static final String BASE_SERVER_URL = "http://localhost:8080/";

    private EpomView view;

    public AdWorker(EpomView view) {
        this.view = view;
    }

    @Override
    public void run() {
        KeyguardManager kgMgr = (KeyguardManager) view.getContext().getSystemService(Context.KEYGUARD_SERVICE);

        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, view.getAdType().getWidth(), view.getResources().getDisplayMetrics());
        final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, view.getAdType().getHeight(), view.getResources().getDisplayMetrics());
        if (EpomView.httpClient.getCookieStore() != null && EpomView.httpClient.getCookieStore().getCookies().isEmpty()) {
            Log.d(Util.EPOM_LOG_TAG, "Cookies is empty");
        } else {
            Log.d(Util.EPOM_LOG_TAG, "Cookies is not empty : ");
            for (Cookie c : EpomView.httpClient.getCookieStore().getCookies()) {
                Log.d(Util.EPOM_LOG_TAG, "Cookie: " + c);
            }
        }
        if (!Util.isOnline(view.getContext())) {
            Log.w(Util.EPOM_LOG_TAG, "No network connection detected");
            view.adFailedToReceiveCallback();
            Log.d(Util.EPOM_LOG_TAG, "Rescheduling ad request on to " + view.getRefreshInterval());
            view.getRedrowHandler().sleep(view.getRefreshInterval() * 1000);
            return;
        }
        Log.d(Util.EPOM_LOG_TAG, "Updating excluded banner list");
        int removedCount = view.getExcludedAdsManager().updateExcludedBannerList();
        Log.d(Util.EPOM_LOG_TAG, "Update finished.Records removed from exclusion: " + removedCount);
        String url = view.getRequestURL();

        Log.d(Util.EPOM_LOG_TAG, "Reloading " + url);
        if (!kgMgr.inKeyguardRestrictedInputMode()) {
            try {
                final StringBuilder response = Util.inputStreamToString(EpomView.httpClient.execute(new HttpGet(url)).getEntity().getContent());
                final JSONObject jsonResponse = Util.testJSON(response);

                if (response.length() > 0) {
                    Log.d(Util.EPOM_LOG_TAG, "Response: " + response);
                    if (jsonResponse != null) {
                        Log.d(Util.EPOM_LOG_TAG, "Response from mobile advertising network");
                        AdNetwork currentAdNetwork = null;
                        String adNetworkRaw = null;
                        try {
                            adNetworkRaw = jsonResponse.getString(TrackingManager.JSON_ADNETWORK_FIELD);
                            currentAdNetwork = AdNetwork.valueOf(adNetworkRaw);
                        } catch (Exception e) {
                            if (adNetworkRaw != null) {
                                Log.w(Util.EPOM_LOG_TAG, new StringBuilder("Unknown Ad Source: ").append(adNetworkRaw).append(". Excluding permanently...").toString());
                                view.getExcludedAdsManager().excludePermanently(jsonResponse.getLong(TrackingManager.JSON_BANNER_ID_FIELD));
                                view.getRedrowHandler().sleep(10);
                                return;
                            }
                            Log.e(Util.EPOM_LOG_TAG, e.getMessage());
                        }
                        try {
                            view.prepareImpressionsBean(jsonResponse);
                            if (currentAdNetwork != null) {
                                if (SDKSupportedNetworks.contains(currentAdNetwork)) {
                                    final AdNetworkSupport adNetworkSupport = (AdNetworkSupport) AdNetworkSupportFabric.getInstance().getAdNetworkSupport(currentAdNetwork, jsonResponse);
                                    if (adNetworkSupport != null) {
                                        Log.d(Util.EPOM_LOG_TAG, "Mounting ad on current view");
                                        view.post(new Runnable() {
                                            public void run() {
                                                if ((view.setNewView(adNetworkSupport.loadAd(view.getContext(), view.getAdType(), view))) != null) {
                                                    view.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    view.getExcludedAdsManager().excludePermanently(jsonResponse.getLong(TrackingManager.JSON_BANNER_ID_FIELD));
                                    view.getRedrowHandler().sleep(10);
                                    return;
                                }
                            } else {
                                try {
                                    if (jsonResponse.getString(TrackingManager.JSON_CONTENT_FIELD) != null) {
                                        Log.d(Util.EPOM_LOG_TAG, "Relayed response received.");
                                        view.post(new Runnable() {
                                            public void run() {
                                                view.clearTmpView();
                                            }
                                        });
                                        if (jsonResponse.getBoolean(TrackingManager.JSON_CLICK_AWARE)) {
                                            view.invalidateImpressionsTrackingBean();
                                        }
                                        final String html = new StringBuffer("<body style=\"margin:0px;padding:0px;\"><center>").append(jsonResponse.getString(TrackingManager.JSON_CONTENT_FIELD)).append("</center></body>").toString();
                                        view.post(new Runnable() {
                                            public void run() {
                                                view.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                                                view.clearView();
                                                view.loadDataWithBaseURL(BASE_SERVER_URL, html, "text/html", null, null);
                                                view.setVisibility(View.VISIBLE);
                                            }
                                        });
                                        if (view.isInit()) {
                                            view.setInit(false);
                                        }
                                        view.adReceivedCallback();
                                    }
                                    view.setOldView(null);
                                } catch (Throwable e) {
                                    Log.e(Util.EPOM_LOG_TAG, "Error while displaying new ad.", e);
                                    view.adFailedToReceiveCallback();
                                }
                            }
                        } catch (Exception e) {
                            Log.e(Util.EPOM_LOG_TAG, "Error while parsing json response", e);
                            view.adFailedToReceiveCallback();
                        }
                    } else {
                        Log.d(Util.EPOM_LOG_TAG, "Simple response");
                        try {
                            view.invalidateImpressionsTrackingBean();
                            view.clearTmpView();
                            view.post(new Runnable() {
                                public void run() {
                                    view.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                                    view.loadDataWithBaseURL(BASE_SERVER_URL, new StringBuffer("<body style=\"margin:0px;padding:0px;\"><center>").append(response).append("</center></body>").toString(), "text/html", null, null);
                                    view.setVisibility(View.VISIBLE);
                                }
                            });

                            if (view.isInit()) {
                                view.setInit(false);
                            }
                            view.adReceivedCallback();
                            view.setOldView(null);
                        } catch (Throwable e) {
                            Log.e(Util.EPOM_LOG_TAG, "Error while displaying new ad.", e);
                            view.adFailedToReceiveCallback();
                        }
                    }
                } else {
                    Log.d(Util.EPOM_LOG_TAG, "Empty response received");
                    view.adFailedToReceiveCallback();
                }
            } catch (Exception e) {
                Log.e(Util.EPOM_LOG_TAG, "Cannot perform get request to " + url);
                view.adFailedToReceiveCallback();
            }
        }
        if (!view.isPaused()) {
            Log.d(Util.EPOM_LOG_TAG, "Rescheduling ad request on to " + view.getRefreshInterval());
            view.getRedrowHandler().sleep(view.getRefreshInterval() * 1000);
        }
        view.persistCookies();
    }
}
