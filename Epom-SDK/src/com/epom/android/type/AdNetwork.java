package com.epom.android.type;

import com.MASTAdView.MASTAdView;
import com.epom.android.view.InnerActiveView;
import com.epom.android.view.TapJoyView;
import com.google.ads.AdView;
import com.inmobi.androidsdk.IMAdView;
import com.millennialmedia.android.MMAdView;
import com.smaato.SOMA.SOMABanner;
import ru.wapstart.plus1.sdk.Plus1BannerView;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 2/29/12
 * Time: 12:20 PM
 */
public enum AdNetwork {
    AD_MOB(AdView.class, "AdMob", "PUBLISHER_ID"),
    IN_MOBI(IMAdView.class, "InMobi", "APPLICATION_ID"),
    SMAATO(SOMABanner.class, "Smaato", "PUBLISHER_ID", "ADSPASE_ID"),
    MILLENNIAL_MEDIA(MMAdView.class, "Millennial Media", "PLACEMENT_ID"),
    INNERACTIVE(InnerActiveView.class, "InnerActive", "APPLICATION_ID"),
    YOC_PERFORMANCE(MASTAdView.class, "YOC Performance", "SITE_ID", "ZONE_ID"),
    TAP_IT(com.tapit.adview.AdView.class, "TapIt", "ZONE_ID"),
    WAP_START(Plus1BannerView.class, "WapStart", "APPLICATION_ID"),
    TAP_JOY(TapJoyView.class, "TAP_JOY", "APPLICATION_ID","SECRET_KEY"),;

    private String[] parameters;
    private String name;
    private Class viewClass;

    AdNetwork(Class viewClass, String name, String... parameters) {
        this.parameters = parameters;
        this.name = name;
        this.viewClass = viewClass;
    }

    public String[] getParameters() {
        return parameters;
    }

    public String getName() {
        return name;
    }

    public Class getViewClass() {
        return viewClass;
    }
}
