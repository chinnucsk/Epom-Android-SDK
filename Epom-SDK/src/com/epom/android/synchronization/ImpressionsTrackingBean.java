package com.epom.android.synchronization;

import com.epom.android.type.AdNetwork;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 3/2/12
 * Time: 4:29 PM
 */
public class ImpressionsTrackingBean implements Serializable {
    private AdNetwork adNetwork;
    private Long bannerId;
    private Long placementId;
    private Long campaignId;
    private String country;
    private String hash;
    private String signature;
    private boolean valid;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Long getBannerId() {
        return bannerId;
    }

    public void setBannerId(Long bannerId) {
        this.bannerId = bannerId;
    }

    public Long getPlacementId() {
        return placementId;
    }

    public AdNetwork getAdNetwork() {
        return adNetwork;
    }

    public void setAdNetwork(AdNetwork adNetwork) {
        this.adNetwork = adNetwork;
    }

    public void setPlacementId(Long placementId) {
        this.placementId = placementId;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
