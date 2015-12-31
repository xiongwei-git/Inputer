package com.android.ted.inputer.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Ted on 2015/12/29.
 *
 * @ com.android.ted.inputer.util
 */
public class CommonUtil {

    public static Typeface getTypeface(Context paramContext) {
        return Typeface.createFromAsset(paramContext.getAssets(), "fonts/Roboto-Medium.ttf");
    }
}
