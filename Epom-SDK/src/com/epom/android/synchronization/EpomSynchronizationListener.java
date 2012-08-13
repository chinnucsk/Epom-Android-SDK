package com.epom.android.synchronization;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.epom.android.Util;
import com.epom.android.view.EpomView;
import com.epom.android.type.SynchronizationType;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 3/5/12
 * Time: 3:02 PM
 */
public class EpomSynchronizationListener implements View.OnTouchListener {
    private Context context;
    private EpomView sourceView;

    public EpomSynchronizationListener(Context context, EpomView view) {
        this.context = context;
        this.sourceView = view;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            Log.d(Util.EPOM_LOG_TAG, "Synchronization started...");
            try {
                if(sourceView != null){
                    EpomSynchronizer.setCallBackView(sourceView);
                    Intent syncIntent = new Intent(context, EpomSynchronizer.class);
                    syncIntent.putExtra("syncData", sourceView.getImpressionsTrackingBean());
                    syncIntent.putExtra("syncType", SynchronizationType.CLICK_SYN);
                    Log.d(Util.EPOM_LOG_TAG, "Synchronization data : " + sourceView.getImpressionsTrackingBean());
                    context.startService(syncIntent);
                }
            } catch (Exception e) {
                Log.e(Util.EPOM_LOG_TAG, "Error while synchronizing click data.", e);
            }
            return false;
        } else {
            return (motionEvent.getAction() == MotionEvent.ACTION_MOVE);
        }
    }
}
