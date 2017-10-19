package com.pluscubed.logcat.util;

import android.content.Context;
import android.view.View;

/**
 * Util
 */
public class Util {
    public static int convertDpToPx(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density
                + 0.5f);
    }

    public static void setVisibility(boolean visibility, View view){
        view.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }
}
