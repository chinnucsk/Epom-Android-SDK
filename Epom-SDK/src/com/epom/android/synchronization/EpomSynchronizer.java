package com.epom.android.synchronization;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.epom.android.Util;
import com.epom.android.view.EpomView;
import com.epom.android.type.SynchronizationType;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 3/5/12
 * Time: 1:31 PM
 */
public class EpomSynchronizer extends IntentService {
    public static final int DEFAULT_HTTP_TIMEOUT = 2000;
    private static final String CLICK_BEACON_IMG = "%s/click.gif?b=%s&p=%s&c=%s&l=%s&h=%s&t=%s&s=%s";
    //todo: switch before commit
    private static final String EPOM_HOST_BASENAME = "http://api.epom.com";
    //private static final String EPOM_HOST_BASENAME = "http://192.168.2.158:8080";
    private static final String BEACON_IMG = "%s/impression.gif?b=%s&p=%s&c=%s&l=%s&h=%s&t=%s&s=%s";
    private static final HttpClient httpclient;
    private static EpomView callBackView;

    static {
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, DEFAULT_HTTP_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, DEFAULT_HTTP_TIMEOUT);
        httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, EpomView.USER_AGENT);
    }

    public static void setCallBackView(EpomView callBackView) {
        EpomSynchronizer.callBackView = callBackView;
    }

    public EpomSynchronizer() {
        super("EpomSynchronizerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ImpressionsTrackingBean i;
        String url;
        Log.d(Util.EPOM_LOG_TAG, "Synchronization intent received...");
        try {
            if ((i = (ImpressionsTrackingBean) intent.getExtras().get("syncData")) != null && i.isValid()) {
                switch ((SynchronizationType) intent.getExtras().get("syncType")) {
                    case CLICK_SYN:
                        Log.d(Util.EPOM_LOG_TAG, "Synchronizing click event.");
                        httpclient.execute(new HttpGet(url = String.format(CLICK_BEACON_IMG, EPOM_HOST_BASENAME, i.getBannerId(), i.getPlacementId(), i.getCampaignId(),
                                i.getCountry(), i.getHash(), System.currentTimeMillis(), i.getSignature())));
                        Log.d(Util.EPOM_LOG_TAG, "Requesting " + url);
                        break;
                    case IMPRESSION_SYN:
                        Log.d(Util.EPOM_LOG_TAG, "Synchronizing impression event.");
                        httpclient.execute(new HttpGet(url = String.format(BEACON_IMG, EPOM_HOST_BASENAME, i.getBannerId(), i.getPlacementId(), i.getCampaignId(),
                                i.getCountry(), i.getHash(), System.currentTimeMillis(), i.getSignature())));
                        Log.d(Util.EPOM_LOG_TAG, "Requesting " + url);
                        break;
                    default:
                        Log.e(Util.EPOM_LOG_TAG, "Unknown synchronization type received.");
                }
            }
            if (callBackView != null && intent.getExtras().get("syncType") == SynchronizationType.CLICK_SYN) {
                if (callBackView.getEpomAdListener() != null) {
                    callBackView.getEpomAdListener().onShowAdScreen(callBackView);
                }
            }
        } catch (Exception e) {
            Log.e(Util.EPOM_LOG_TAG, "Synchronization has failed", e);
        }
        Log.d(Util.EPOM_LOG_TAG, "Done.");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Util.EPOM_LOG_TAG, "Synchronizing service started...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Util.EPOM_LOG_TAG, "Synchronizing service stopped...");
    }
}
