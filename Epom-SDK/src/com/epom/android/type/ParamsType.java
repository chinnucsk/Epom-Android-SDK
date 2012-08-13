package com.epom.android.type;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 2/20/12
 * Time: 10:26 AM
 */
public enum ParamsType {
    LONGITUDE("longitude"),
    LATITUDE("latitude"),
    ANDROID_IDENT("udid"),
    ADTYPE("adType"),
    REFRESH_INTERVAL("refreshInterval"),
    KEY("key"),
    VIEW_BACKGROUND("viewBackground");

    private String urlField;

    ParamsType(String urlField) {
        this.urlField = urlField;
    }

    public String getUrlField() {
        return urlField;
    }
}
