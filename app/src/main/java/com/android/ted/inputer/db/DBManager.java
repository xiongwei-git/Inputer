package com.android.ted.inputer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Description:
 * Creator: ZhangJinWei
 * Date:15/12/17 下午2:36
 */
public class DBManager {

    private static DBManager mDBManager;
    private DbHelper mDbHelper;
    private Context mContext;

    private DBManager( ) {
        mContext = LoaderSdk.getInstance().getContext();
    }

    public static DBManager getInstance() {
        if (mDBManager == null) {
            mDBManager = new DBManager();
        }
        return mDBManager;
    }

    public DbHelper getDbHelper() {
        if (mDbHelper == null) {
            mDbHelper = new DbHelper(mContext);
        }
        return mDbHelper;
    }

    public SQLiteDatabase getWritableDatabase(){
        return getDbHelper().getWritableDatabase();
    }

    public SQLiteDatabase getReadableDatabase(){
        return getDbHelper().getReadableDatabase();
    }
}
