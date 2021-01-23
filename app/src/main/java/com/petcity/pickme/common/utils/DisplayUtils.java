package com.petcity.pickme.common.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @ClassName DisplayUtils
 * @Description DisplayUtils
 * @Author sherily
 * @Date 22/01/21 10:13 PM
 * @Version 1.0
 */
public class DisplayUtils {

    public static int dip2px(Context context, float dipValue) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        final float scale = displayMetrics.density;
        return (int) (dipValue * scale + 0.5f);
    }
}
