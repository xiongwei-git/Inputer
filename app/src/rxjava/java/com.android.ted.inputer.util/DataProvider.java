package com.android.ted.inputer.util;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.android.ted.inputer.BuildConfig;
import com.android.ted.inputer.db.DBManager;
import com.android.ted.inputer.db.DbHelper;
import com.android.ted.inputer.db.KeyWordTable;

/**
 * Description:
 * Creator: ZhangJinWei
 * Date:15/12/18 下午3:19
 */
public class DataProvider extends ContentProvider {

    public static final String TAG =DataProvider.class.getSimpleName();

    public static Object DBlock = new Object();

    public static final String AUTHORITY = BuildConfig.LOADER_AUTHORITIES;

    public static final String SCHEME = "content://";

    public static final String PATH_KEYWORDS = "/keywords";

    public static final Uri KEYWORDS_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_KEYWORDS);

    public static final String KEYWORDS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.aheadlcx" +
            ".niceloader.keywords";

    private static final int KEYWORDS = 0;


    private static DbHelper mDbHelper;

    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, "keywords", KEYWORDS);
    }

    public static DbHelper getmDbHelper(){
        if (mDbHelper == null) {
            mDbHelper = DBManager.getInstance().getDbHelper();
        }
        return mDbHelper;
    }

    private String matchTable(Uri uri){
        String table = null;
        int code = sUriMatcher.match(uri);
        switch (code){
            case KEYWORDS:
                return KeyWordTable.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unkown uri = " + uri);
        }
    }


    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        synchronized (DBlock){
            SQLiteDatabase db = getmDbHelper().getReadableDatabase();
            String table = matchTable(uri);
            Cursor cursor = db.query(table, projection, selection, selectionArgs, null, null, sortOrder);

            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int code = sUriMatcher.match(uri);
        switch (code){
            case KEYWORDS:
                return KEYWORDS_CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unkown uri == " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        synchronized (DBlock){
            String table = matchTable(uri);
            SQLiteDatabase db = getmDbHelper().getWritableDatabase();
            long rowId = 0;
            rowId = db.insert(table, null, values);
            if (rowId > 0){
                getContext().getContentResolver().notifyChange(uri, null);
                Uri returnUri = ContentUris.withAppendedId(uri, rowId);
                return returnUri;
            }

        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        synchronized (DBlock){
            String table = matchTable(uri);
            SQLiteDatabase db = getmDbHelper().getWritableDatabase();
            int count = 0;
            count = db.delete(table, selection, selectionArgs);
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        synchronized (DBlock){
            String table = matchTable(uri);
            SQLiteDatabase db = getmDbHelper().getWritableDatabase();
            int count = 0;
            count = db.update(table, values, selection, selectionArgs);
            return count;
        }
    }
}
