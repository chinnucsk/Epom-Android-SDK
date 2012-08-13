package com.epom.android.networks;

import android.content.Context;
import android.view.View;
import com.epom.android.type.AdType;
import com.epom.android.view.EpomView;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 2/29/12
 * Time: 12:15 PM
 */
public interface AdNetworkSupport extends BaseAdNetworkSupport {
    public View loadAd(Context context, AdType adType, EpomView epomView);
}
