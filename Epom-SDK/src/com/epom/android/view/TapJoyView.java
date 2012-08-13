package com.epom.android.view;

import android.content.Context;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebView;
import com.epom.android.type.AdType;

public class TapJoyView extends WebView {
    private Handler uiHandler = new Handler();

    public TapJoyView(Context context) {
        super(context);
    }

    public Handler getUiHandler() {
        return uiHandler;
    }

    public void updateView(View view) {
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AdType.BANNER_320X50.getWidth(), getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AdType.BANNER_320X50.getHeight() , getResources().getDisplayMetrics());
        addView(view, width, height);
    }
}
