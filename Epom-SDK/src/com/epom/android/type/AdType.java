package com.epom.android.type;

import com.google.ads.AdSize;
import com.inmobi.androidsdk.IMAdView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 3/1/12
 * Time: 9:54 AM
 */

public enum AdType {
    BANNER_320X50(320, 50),               //AdMob all InMobi all, Epom default
    BANNER_300X250(300, 250),        //AdMob tablets InMobi all
    BANNER_468X60(468, 60),           //AdMob tablets, InMobi tablets
    BANNER_728X90(728, 90),           //AdMob tablets, InMobi tablets
    BANNER_120X600(120, 600)         //InMobi tablets
    ;

    private int height;
    private int width;
    private static Map<AdNetwork, Map<AdType, Object>> bannerSizeMappings = new HashMap<AdNetwork, Map<AdType, Object>>();

    static {
        Map<AdType, Object> adMobMappings = new HashMap<AdType, Object>();
        adMobMappings.put(BANNER_320X50, AdSize.BANNER);
        adMobMappings.put(BANNER_300X250, AdSize.IAB_MRECT);
        adMobMappings.put(BANNER_468X60, AdSize.IAB_BANNER);
        adMobMappings.put(BANNER_728X90, AdSize.IAB_LEADERBOARD);
        adMobMappings.put(BANNER_120X600, null);

        Map<AdType, Object> inMobiMappings = new HashMap<AdType, Object>();
        inMobiMappings.put(BANNER_320X50, IMAdView.INMOBI_AD_UNIT_320X50);
        inMobiMappings.put(BANNER_300X250, IMAdView.INMOBI_AD_UNIT_300X250);
        inMobiMappings.put(BANNER_468X60, IMAdView.INMOBI_AD_UNIT_468X60);
        inMobiMappings.put(BANNER_728X90, IMAdView.INMOBI_AD_UNIT_728X90);
        inMobiMappings.put(BANNER_120X600, IMAdView.INMOBI_AD_UNIT_120X600);

        Map<AdType, Object> smaatoMappings = new HashMap<AdType, Object>();
        smaatoMappings.put(BANNER_320X50, AdSize.BANNER);
        smaatoMappings.put(BANNER_300X250, AdSize.IAB_MRECT);
        smaatoMappings.put(BANNER_468X60, null);
        smaatoMappings.put(BANNER_728X90, null);
        smaatoMappings.put(BANNER_120X600, null);

        bannerSizeMappings.put(AdNetwork.AD_MOB, adMobMappings);
        bannerSizeMappings.put(AdNetwork.IN_MOBI, inMobiMappings);
        bannerSizeMappings.put(AdNetwork.SMAATO, smaatoMappings);
    }

    AdType(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public static Map<AdNetwork, Map<AdType, Object>> getBannerSizeMappings() {
        return bannerSizeMappings;
    }

    public static AdType getDefault() {
        return BANNER_320X50;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
