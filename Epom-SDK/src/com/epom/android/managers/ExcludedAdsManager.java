package com.epom.android.managers;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.DelayQueue;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 3/2/12
 * Time: 2:02 PM
 */
public class ExcludedAdsManager {
    public static final int DEFAULT_EXCLUSION_PERIOD = 60;

    private DelayQueue<ExcludedBanner> excludedBanners = new DelayQueue<ExcludedBanner>();
    private Set<Long> permanentlyExcludedBanners = new HashSet<Long>();

    public synchronized int updateExcludedBannerList() {
        int activatedRecords = 0;
        while (excludedBanners.poll() != null) {
            activatedRecords++;
        }

        return activatedRecords;
    }

    public void addBanner(ExcludedBanner banner) {
        if (!excludedBanners.contains(banner)) {
            excludedBanners.offer(banner);
        }
    }

    public Object[] getExcludedBanners() {
        return excludedBanners.toArray();
    }

    public Set<Long> getPermanentlyExcludedBanners() {
        return permanentlyExcludedBanners;
    }

    public String getAsUrl() {
        StringBuilder tmp = new StringBuilder();
        for (Object b : getExcludedBanners()) {
            tmp.append("&excluded=").append(((ExcludedBanner) b).getId());
        }
        for (Long excludedBannerId : getPermanentlyExcludedBanners()) {
            tmp.append("&excluded=").append(excludedBannerId);
        }
        return tmp.toString();
    }

    public void excludePermanently(Long bannerId) {
        permanentlyExcludedBanners.add(bannerId);
    }
}
