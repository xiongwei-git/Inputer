package com.android.ted.inputer.util;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.text.TextUtils;

import com.android.ted.inputer.db.KeyWordTable;
import com.android.ted.inputer.db.LoaderSdk;
import com.android.ted.inputer.main.MainApplication;

/**
 * Description:
 * Creator: ZhangJinWei
 * Date:15/12/20 上午11:32
 */
public class KeyWordUtil implements Loader.OnLoadCompleteListener<Cursor>{

    private CursorLoader mCursorLoader;
    private String curKey;
    private KeyWordInterface mCallBack;

    public KeyWordUtil() {
    }

    public  KeyWordUtil init() {
        mCursorLoader = new CursorLoader(LoaderSdk.getInstance().getContext());
        mCursorLoader.registerListener(0, this);
        mCursorLoader.setUri(DataProvider.KEYWORDS_CONTENT_URI);
        mCursorLoader.setSortOrder(KeyWordTable._ID + " DESC");
        mCursorLoader.startLoading();
        return this;
    }

    public KeyWordUtil readTextKeyWordFromDb(String key, KeyWordInterface callBack) {
        curKey = key;
        mCallBack = callBack;
        if (mCursorLoader == null) {
            init();
        }
        mCursorLoader.stopLoading();
        mCursorLoader.reset();
        mCursorLoader.setUri(DataProvider.KEYWORDS_CONTENT_URI);
        mCursorLoader.setSortOrder(KeyWordTable._ID + " DESC");
        mCursorLoader.setSelection(KeyWordTable.KEY + " =?");
        mCursorLoader.setSelectionArgs(new String[]{key});
        mCursorLoader.startLoading();
        return this;
    }

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int indexWord = cursor.getColumnIndex(KeyWordTable
                        .WORD);
                int indexKey = cursor.getColumnIndex(KeyWordTable
                        .KEY);
                String key = cursor.getString(indexKey);
                String word = cursor.getString(indexWord);
                if (!TextUtils.isEmpty(curKey) && key.equals(curKey)) {
                    if (mCallBack != null) {
                        boolean isSuccess = TextUtils.isEmpty(word) ? false : true;
                        mCallBack.getKeyWordSuccess(true, word);
                    }
                }
                cursor.moveToNext();
            }
        }
    }
    public void onDestroy() {
        if (null != mCursorLoader)
            mCursorLoader.stopLoading();
        mCursorLoader = null;
        mCallBack = null;
        curKey = null;
    }
}
