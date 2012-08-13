package com.epom.android.type;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 4/19/12
 * Time: 10:06 AM
 */
public class SDKSupportedNetworks {
    private static Set<AdNetwork> sdkSupportedNetworks = new HashSet<AdNetwork>();

    public static boolean addNetwork(AdNetwork adNetwork){
        return sdkSupportedNetworks.add(adNetwork);
    }

    public static boolean contains(AdNetwork adNetwork){
        return sdkSupportedNetworks.contains(adNetwork);
    }
    
    public static Set<AdNetwork> getSdkSupportedNetworks(){
        return sdkSupportedNetworks;
    }
}
