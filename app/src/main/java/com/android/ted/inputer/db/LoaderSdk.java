package com.android.ted.inputer.db;

import android.content.Context;

/**
 * Description:
 * Creator: ZhangJinWei
 * Date:15/12/17 下午2:52
 */
public class LoaderSdk {
    public Context mContext;
    private static LoaderSdk sLoaderSdk;
    public  void init(Context context){
        mContext = context;
    }

    private LoaderSdk() {
    }

    public Context getContext() {
        return mContext;
    }

    public static LoaderSdk getInstance() {
        if (sLoaderSdk == null) {
            sLoaderSdk = new LoaderSdk();
        }
        return sLoaderSdk;
    }
}
