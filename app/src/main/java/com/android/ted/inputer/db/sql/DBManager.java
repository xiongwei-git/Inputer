package com.android.ted.inputer.db.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Description:
 * Creator: ZhangJinWei
 * Date:15/12/17 下午2:36
 */
public class DBManager {

    private static DBManager mDBManager;
    private BaseDbHelper mBaseDbHelper;
    private Context mContext;

    private DBManager( ) {
       // mContext = LoaderSdk.getIns().getContext();
    }

    public static DBManager getInstance() {
        if (mDBManager == null) {
            mDBManager = new DBManager();
        }
        return mDBManager;
    }

    public BaseDbHelper getDbHelper() {
        if (mBaseDbHelper == null) {
            mBaseDbHelper = new BaseDbHelper(mContext);
        }
        return mBaseDbHelper;
    }

    public SQLiteDatabase getWritableDatabase(){
        return getDbHelper().getWritableDatabase();
    }

    public SQLiteDatabase getReadableDatabase(){
        return getDbHelper().getReadableDatabase();
    }
}
