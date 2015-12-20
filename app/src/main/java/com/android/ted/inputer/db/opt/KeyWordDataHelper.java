package com.android.ted.inputer.db.opt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.ted.inputer.db.KeyWordTable;
import com.android.ted.inputer.db.KeyWordTb;

import java.util.ArrayList;


/**
 * Description:
 * Creator: ZhangJinWei
 * Date:15/12/18 下午4:22
 */
public class KeyWordDataHelper extends BaseDataHelper {

    public KeyWordDataHelper(Context context) {
        super(context);
    }

    @Override
    protected Uri getContentUri() {
        return DataProvider.KEYWORDS_CONTENT_URI;
    }

    public ArrayList<KeyWordTb> query(String key) {
        ArrayList<KeyWordTb> results = new ArrayList<>();
        KeyWordTb item = null;

        Cursor cursor = query(DataProvider.KEYWORDS_CONTENT_URI, null, KeyWordTable.KEY + " = ?", new
                String[]{key}, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int cursorCount = cursor.getCount();
            while (!cursor.isAfterLast()) {
                item = new KeyWordTb();
                item.key = key;
                int indexWord = cursor.getColumnIndex(KeyWordTable
                        .WORD);
                Log.e("db", "indexWord =  " + indexWord);
                item.word = cursor.getString(indexWord);

                int indexId = cursor.getColumnIndex(KeyWordTable
                        ._ID);
                item._ID = cursor.getInt(indexId);
                results.add(item);
                cursor.moveToNext();
            }
        }
        try {
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {

        }
        return results;
    }

    public int delete(String key) {
        int delete = delete(DataProvider.KEYWORDS_CONTENT_URI, KeyWordTable.KEY + " = ?", new
                String[]{key});
        return delete;
    }

    public Uri insert(KeyWordTb keyWordTb) {
        ContentValues values = new ContentValues();
        values.put(KeyWordTable.KEY, keyWordTb.key);
        values.put(KeyWordTable.WORD, keyWordTb.word);
        Uri insert = insert(values);
        return insert;
    }

    public Uri removeTheSameAndSaveNew(@NonNull KeyWordTb keyWordTb) {
        ArrayList<KeyWordTb> query = query(keyWordTb.key);
        if (query != null && query.size() > 0){
            delete(keyWordTb.key);
        }

        return insert(keyWordTb);
    }
}
