package com.epom.android.networks;

import com.epom.android.type.AdNetwork;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 4/19/12
 * Time: 10:29 AM
 */
public interface BaseAdNetworkSupport {
    String JSON_AD_NETWORK_PARAMETERS = "AD_NETWORK_PARAMETERS";

    public void setAdNetworkCredentials(Map<String, String> adNetworkCredentials);

    public Map<String, String> getAdNetworkCredentials();

    public AdNetwork getAdNetwork();
}
