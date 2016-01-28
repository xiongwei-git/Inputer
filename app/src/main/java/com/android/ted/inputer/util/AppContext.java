package com.android.ted.inputer.util;

import android.content.Context;

/**
 * Description:
 * Creator: ZhangJinWei
 * Date:15/12/17 下午2:52
 */
public class AppContext {
    public Context mContext;
    private static AppContext sAppContext;

    public  void init(Context context){
        mContext = context;
    }

    private AppContext() {
    }

    public Context getContext() {
        return mContext;
    }

    public static AppContext getIns() {
        if (sAppContext == null) {
            sAppContext = new AppContext();
        }
        return sAppContext;
    }
}
