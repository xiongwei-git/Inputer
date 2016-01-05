package com.android.ted.inputer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Description:
 * Creator: ZhangJinWei
 * Date:15/12/18 下午4:24
 */
public abstract class BaseDataHelper {

    private Context mContext;

    public BaseDataHelper(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    protected abstract Uri getContentUri();

    public void notifyChange() {
        mContext.getContentResolver().notifyChange(getContentUri(), null);
    }

    protected final Cursor query(String[] projection, String selection,
                                 String[] selectionArgs, String sortOrder) {
        return mContext.getContentResolver().query(getContentUri(), projection, selection, selectionArgs,
                sortOrder);
    }

    protected final int delete(String selection, String[] selectionArgs) {
        return mContext.getContentResolver().delete(getContentUri(), selection, selectionArgs);
    }

    protected final Uri insert(ContentValues values) {
        return mContext.getContentResolver().insert(getContentUri(), values);
    }
}
