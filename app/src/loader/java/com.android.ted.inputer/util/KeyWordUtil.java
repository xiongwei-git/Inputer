package com.android.ted.inputer.util;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.util.Log;

import com.android.ted.inputer.model.ArgotRecord;

import java.util.ArrayList;

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
        mCursorLoader.setSortOrder(ArgotRecord._ID + " DESC");
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
        mCursorLoader.setSortOrder(ArgotRecord._ID + " DESC");
        mCursorLoader.setSelection(ArgotRecord.KEY + " LIKE ?" );
        mCursorLoader.setSelectionArgs(new String[]{ "" + key + "%"});
        mCursorLoader.startLoading();
        return this;
    }

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
        ArrayList<String> words = new ArrayList<>();
        Log.e("ABC", "curKey = " + curKey + "\n" + "cursor.getcount ==" + cursor.getCount());
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int indexWord = cursor.getColumnIndex(ArgotRecord
                        .WORD);
                int indexKey = cursor.getColumnIndex(ArgotRecord
                        .KEY);
                String word = cursor.getString(indexWord);
                    Log.e("ABC", "curKey = " + curKey + "\n" + "word = " + word );
                    words.add(word);
                cursor.moveToNext();
            }
        }
        if (mCallBack != null) {
            boolean isSuccess = words.size() > 0;
            mCallBack.getKeyWordSuccess(isSuccess, words);
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
