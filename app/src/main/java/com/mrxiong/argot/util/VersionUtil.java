package com.mrxiong.argot.util;

import android.os.Build;

/**
 * Created by Ted on 2015/11/27.
 * VersionUtil
 */
public class VersionUtil {
    public static boolean isM(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
