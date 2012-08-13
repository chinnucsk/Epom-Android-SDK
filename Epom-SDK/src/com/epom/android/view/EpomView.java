package com.epom.android.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.epom.android.Util;
import com.epom.android.listeners.EpomAdListener;
import com.epom.android.location.LocationWatcher;
import com.epom.android.managers.*;
import com.epom.android.synchronization.EpomSynchronizationListener;
import com.epom.android.synchronization.ImpressionsTrackingBean;
import com.epom.android.type.AdNetwork;
import com.epom.android.type.AdType;
import com.epom.android.type.ParamsType;
import com.epom.android.type.SDKSupportedNetworks;
import com.google.ads.AdView;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.epom.android.type.ParamsType.*;

/**
 * Created by IntelliJ IDEA.
 * User: anton
 * Date: 2/8/12
 * Time: 5:17 PM
 */
public class EpomView extends WebView {
    public static final String SDK_VERSION = "1.0.4";
    public static final int DEFAULT_HTTP_TIMEOUT = 2000;
    //todo: switch before commit
    public static final String BASE_SERVICE_URL = "http://api.epom.com/ads-api?version=" + SDK_VERSION + "&";
    //public static final String BASE_SERVICE_URL = "http://192.168.2.158:8080/ads-api?version=" + SDK_VERSION + "&";

    public static DefaultHttpClient httpClient;
    public static String USER_AGENT;

    private AttributeSet attrs;
    private Context context;
    private StringBuilder baseUrl;
    private ParamParser parser;
    private LocationWatcher locationWatcher;
    private CookiesManager cookiesManager;
    private TrackingManager trackingManager;
    private AdType adType = AdType.BANNER_320X50;
    private View oldView;
    private View newView;
    private RefreshHandler mRedrawHandler = new RefreshHandler();
    private ExcludedAdsManager excludedBannersList = new ExcludedAdsManager();
    private int currentBannerId;
    private ImpressionsTrackingBean impressionsTrackingBean = new ImpressionsTrackingBean();
    private boolean paused;
    private volatile boolean init = true;
    private long startShowingCurrentAdTime = System.currentTimeMillis();
    private EpomAdListener epomAdListener;

    static {
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, DEFAULT_HTTP_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, DEFAULT_HTTP_TIMEOUT);
        httpClient = new DefaultHttpClient();
        httpClient.setParams(httpParameters);

        for (AdNetwork adNetwork : AdNetwork.values()) {
            if (SDKSupportManager.isSupported(adNetwork)) {
                SDKSupportedNetworks.addNetwork(adNetwork);
            } else {
                Log.w(Util.EPOM_LOG_TAG, new StringBuilder("Required API level for ").append(adNetwork.getName())
                        .append(" is ").append(SDKSupportManager.getSDKtoAPIMappings().get(adNetwork))
                        .append(" found ").append(Util.getAPILevel()).append(".Excluding Ad Source...")
                        .toString());
            }
        }
    }

    public EpomView(Context context) {
        super(context);
        this.context = context;
        setOnTouchListener(new EpomSynchronizationListener(getContext(), this));
        USER_AGENT = getSettings().getUserAgentString();
        init();
    }

    public EpomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
        this.context = context;
        setOnTouchListener(new EpomSynchronizationListener(getContext(), this));
        USER_AGENT = getSettings().getUserAgentString();
        init();
    }

    public EpomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.attrs = attrs;
        this.context = context;
        setOnTouchListener(new EpomSynchronizationListener(getContext(), this));
        USER_AGENT = getSettings().getUserAgentString();
        init();
    }

    public void setEpomAdListener(EpomAdListener epomAdListener) {
        this.epomAdListener = epomAdListener;
    }

    public EpomAdListener getEpomAdListener() {
        return epomAdListener;
    }

    public boolean isInit() {
        return init;
    }

    public void setOldView(View oldView) {
        this.oldView = oldView;
    }

    public AdType getAdType() {
        return adType;
    }

    public View setNewView(View newView) {
        return this.newView = newView;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    public void setCurrentBannerId(int currentBannerId) {
        this.currentBannerId = currentBannerId;
    }

    public void pause() {
        this.paused = true;
    }

    public void launch() {
        if (paused) {
            this.paused = false;
            init();
        }
    }

    public void clearTmpView() {
        clearTmpView(oldView);
    }

    public boolean switchAd(AdNetwork adNetwork) {
        Log.d(Util.EPOM_LOG_TAG, "Starting ad switch...");
        if (!init && System.currentTimeMillis() - startShowingCurrentAdTime < 0.8 * (Integer)(parser.getParamsMap().get(ParamsType.REFRESH_INTERVAL)) * 1000) {
            Log.d(Util.EPOM_LOG_TAG, "Premature switch, aborting...");
            return false;
        }
        if (newView != null) {
            newView.setVisibility(VISIBLE);
            if (!(adNetwork.getViewClass().isAssignableFrom( newView.getClass()))) {
                Log.d(Util.EPOM_LOG_TAG, "Trying to switch to non-self view, aborting...");
                return false;
            }
            if (!init) {
                if (oldView != null) {
                    clearTmpView(oldView);
                    Log.d(Util.EPOM_LOG_TAG, "Destroying old view...");
                } else {
                    loadUrl("about:blank");
                    Log.d(Util.EPOM_LOG_TAG, "Cleaning old view...");
                }
            }
            Log.d(Util.EPOM_LOG_TAG, "Switching " + oldView + " to " + newView);
            clearView();
            removeAllViews();
            try {
                EpomView.this.removeView(newView);

                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, EpomView.this.adType.getWidth(), getResources().getDisplayMetrics());
                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, EpomView.this.adType.getHeight(), getResources().getDisplayMetrics());
                newView.invalidate();
                EpomView.this.addView(newView, new ViewGroup.LayoutParams(width, height));

                oldView = newView;
                newView = null;
                startShowingCurrentAdTime = System.currentTimeMillis();
                if (init) {
                    init = false;
                    Log.d(Util.EPOM_LOG_TAG, "Init finished...");
                }
                if (epomAdListener != null) {
                    epomAdListener.onReceivedAd(this);
                }
            } catch (Exception e) {
                Log.e(Util.EPOM_LOG_TAG, "Error while switching ad.", e);
                if (getEpomAdListener() != null) {
                    getEpomAdListener().onFailedToReceiveAd(this);
                }
                return false;
            }
            return true;
        } else {
            if (getEpomAdListener() != null) {
                getEpomAdListener().onFailedToReceiveAd(this);
            }
            return false;
        }
    }

    public LocationWatcher getLocationWatcher() {
        return locationWatcher;
    }

    public StringBuilder getBaseUrl() {
        return baseUrl;
    }

    public void excludeCurrentBanner() {
        excludedBannersList.addBanner(new ExcludedBanner(ExcludedAdsManager.DEFAULT_EXCLUSION_PERIOD, currentBannerId));
    }

    public class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            EpomView.this.init();
        }

        public void flush() {
            if (hasMessages(0)) {
                this.removeMessages(0);
            }
        }

        public synchronized void sleep(long delayMillis) {
            if (hasMessages(0)) {
                removeMessages(0);
            }
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    }

    @Override
    public void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.GONE) {
            try {
                getRedrowHandler().flush();
                this.paused = true;
            } catch (Exception e) {
                Log.d(Util.EPOM_LOG_TAG, "Error while pausing view.");
            }
        } else if (visibility == View.VISIBLE) {
            try {
                if (paused) {
                    Log.d(Util.EPOM_LOG_TAG, "Init due to visibility change.");
                    this.paused = false;
                    init();
                }
            } catch (Exception e) {
                Log.d(Util.EPOM_LOG_TAG, "Error while resuming view.");
            }
        }
    }

    public ExcludedAdsManager getExcludedAdsManager() {
        return excludedBannersList;
    }

    public void prepareImpressionsBean(JSONObject jsonResponse) {
        if (trackingManager == null) {
            trackingManager = new TrackingManager();
            Log.w(Util.EPOM_LOG_TAG, "Tracking manager is not ready.");
        }
        impressionsTrackingBean = trackingManager.prepareImpressionsTrackingBean(jsonResponse);
    }

    public void setConfiguration(HashMap<ParamsType, Object> configuration) {
        if (ParamParser.checkPermissionsConfiguration(context)) {
            this.parser = new ParamParser().setParamsMap(configuration);
        } else {
            Log.e(Util.EPOM_LOG_TAG, "Error in configuration.");
        }
    }

    public void adReceivedCallback() {
        if (epomAdListener != null) {
            epomAdListener.onReceivedAd(this);
        }
    }

    public void adFailedToReceiveCallback() {
        if (epomAdListener != null) {
            epomAdListener.onFailedToReceiveAd(this);
        }
    }

    public int getRefreshInterval() {
        try {
            return (Integer)(parser.getParamsMap().get(REFRESH_INTERVAL));
        } catch (Exception e) {
            Log.e(Util.EPOM_LOG_TAG, "Error while obtaining refresh interval,switching to default.", e);
            return ParamParser.MIN_REFRESH_INTERVAL;
        }
    }

    public void persistCookies() {
        cookiesManager.persistCookies();
    }

    public void invalidateImpressionsTrackingBean() {
        impressionsTrackingBean.setValid(false);
    }

    public boolean isPaused() {
        return paused;
    }

    public RefreshHandler getRedrowHandler() {
        return mRedrawHandler;
    }

    public ImpressionsTrackingBean getImpressionsTrackingBean() {
        return impressionsTrackingBean;
    }

    public String getRequestURL() {
        return getBaseUrl() + getLocationWatcher().getCoordsAsUrl() + getExcludedAdsManager().getAsUrl();
    }

    private void clearTmpView(View view) {
        if (view != null) {
            if (view instanceof AdView) {
                ((AdView) view).destroy();
            }
            removeView(view);
        }
    }

    public void init() {
        if (cookiesManager == null) {
            cookiesManager = new CookiesManager(httpClient, context);
        }
        if (parser == null) {
            Log.d(Util.EPOM_LOG_TAG, "Initializing parameters");
            parser = new ParamParser(attrs, context);
            cookiesManager.restoreCookies();
            if (parser.getParamsMap() == null) {
                Log.e(Util.EPOM_LOG_TAG, "No configuration found");
                return;
            }
            setPadding(0, 0, 0, 0);
            setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            int background = (Integer)(parser.getParamsMap().get(VIEW_BACKGROUND));
            Log.d(Util.EPOM_LOG_TAG, "Setting view background to " + background);
            setBackgroundColor(background);
            baseUrl = new StringBuilder(BASE_SERVICE_URL).append(parser.getAsUrl());
            getSettings().setJavaScriptEnabled(true);
            getSettings().setSupportZoom(true);
        }
        if (trackingManager == null) {
            trackingManager = new TrackingManager(this);
        }
        final Map<ParamsType, Object> paramsMap = parser.getParamsMap();
        if (locationWatcher == null) {
            Log.d(Util.EPOM_LOG_TAG, "Initializing location watcher");
            locationWatcher = new LocationWatcher(context);
            locationWatcher.watch();
        }
        if (init) {
            httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, USER_AGENT);
            setVisibility(GONE);
        }
        String adType = (String)paramsMap.get(ADTYPE);
        if (adType != null) {
            try {
                EpomView.this.adType = AdType.valueOf(adType);
            } catch (Exception e) {
                EpomView.this.adType = AdType.getDefault();
                Log.e(Util.EPOM_LOG_TAG, "Invalid ad type, applying defaults: " + EpomView.this.adType);
            }
        } else {
            Log.w(Util.EPOM_LOG_TAG, "No adType found applying defaults: " + AdType.BANNER_320X50);
            EpomView.this.adType = AdType.getDefault();
        }

        new Thread(new AdWorker(this)).start();
    }
}

