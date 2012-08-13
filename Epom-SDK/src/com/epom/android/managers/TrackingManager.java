package com.epom.android.managers;

import android.util.Log;
import com.epom.android.Util;
import com.epom.android.type.AdNetwork;
import com.epom.android.view.EpomView;
import com.epom.android.synchronization.ImpressionsTrackingBean;
import org.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 4/25/12
 * Time: 2:54 PM
 */
public class TrackingManager {
    public static final String JSON_ADNETWORK_FIELD = "AD_NETWORK";
    public static final String JSON_CONTENT_FIELD = "CONTENT";
    public static final String JSON_BANNER_ID_FIELD = "BANNER_ID";
    public static final String JSON_CLICK_AWARE = "IS_CLICK_AWARE";
    private static final String JSON_PLACEMENT_ID_FIELD = "PLACEMENT_ID";
    private static final String JSON_CAMPAIGN_ID_FIELD = "CAMPAIGN_ID";
    private static final String JSON_HASH_FIELD = "HASH";
    private static final String JSON_COUNTRY_FIELD = "COUNTRY";
    private static final String JSON_SIGNATURE_FIELD = "SIGNATURE";

    private EpomView epomView;

    public TrackingManager(){
    }

    public TrackingManager(EpomView epomView){
        this.epomView = epomView;
    }

    public ImpressionsTrackingBean prepareImpressionsTrackingBean(JSONObject jsonResponse) {
        ImpressionsTrackingBean result = new ImpressionsTrackingBean();
        try {
            int currentBannerId = jsonResponse.getInt(JSON_BANNER_ID_FIELD);
            long placementId = jsonResponse.getLong(JSON_PLACEMENT_ID_FIELD);
            long campaignId = jsonResponse.getLong(JSON_CAMPAIGN_ID_FIELD);
            String country = jsonResponse.getString(JSON_COUNTRY_FIELD);
            String hash = jsonResponse.getString(JSON_HASH_FIELD);
            String signature = jsonResponse.getString(JSON_SIGNATURE_FIELD);
            if(epomView != null){
                epomView.setCurrentBannerId(currentBannerId);
            }

            result.setBannerId((long) currentBannerId);
            result.setPlacementId(placementId);
            result.setCampaignId(campaignId);
            result.setCountry(country);
            result.setHash(hash);
            result.setSignature(signature);

            result.setValid(true);
            try {
                String currentAdNetwork = jsonResponse.getString(JSON_ADNETWORK_FIELD);
                result.setAdNetwork(AdNetwork.valueOf(currentAdNetwork));
            } catch (Exception e) {
                result.setAdNetwork(null);
            }
        } catch (Exception e) {
            Log.e(Util.EPOM_LOG_TAG, e.getMessage());
            return null;
        }
        return result;
    }
}
