package com.sshtukin.gifsearcher;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class Utils {
    public static int convertDpToPixel(int dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return Math.round(px);
    }
}
