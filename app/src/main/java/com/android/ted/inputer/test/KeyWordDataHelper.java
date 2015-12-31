package com.android.ted.inputer.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.ted.inputer.db.opt.KeyWordTable;
import com.android.ted.inputer.db.opt.KeyWordTb;
import com.android.ted.inputer.db.opt.BaseDataHelper;
import com.android.ted.inputer.db.opt.KeyWordTableOpt;
import com.android.ted.inputer.util.DataProvider;

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
        KeyWordTb item;

        Cursor cursor = query( null, KeyWordTable.KEY + " = ?", new
                String[]{key}, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                item = new KeyWordTb();
                item.key = key;
                int indexWord = cursor.getColumnIndex(KeyWordTable
                        .WORD);
                item.word = cursor.getString(indexWord);

                int indexId = cursor.getColumnIndex(KeyWordTable._ID);
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
            e.printStackTrace();
        }
        return results;
    }

    @SuppressWarnings("unused")
    public String queryWord(String key) {
        ArrayList<KeyWordTb> queryList = query(key);
        if (queryList == null) {
            return null;
        }
        for (KeyWordTb item : queryList) {
            if (item.key != null && item.key.equals(key)) {
                return item.word;
            }
        }
        return null;
    }

    /**
     * 模糊搜索，匹配以key开头
     * @param key
     * @return
     */
    public ArrayList<String> fuzzySearchWords(String key) {
        ArrayList<KeyWordTb> queryList = KeyWordTableOpt.fuzzySearch(key);
        ArrayList<String> resultWords = new ArrayList<>();
        if (queryList == null) {
            return null;
        }
        for (KeyWordTb item : queryList) {
            if (item.key != null && item.key.equals(key)) {
                Log.e("ABC", "key = " + item.key + "\n" + "word = " + item.word);
                resultWords.add(item.word);
            }
        }
        return resultWords;
    }


    public int delete(String key) {
        return delete(KeyWordTable.KEY + " = ?", new String[]{key});
    }

    public Uri insert(KeyWordTb keyWordTb) {
        ContentValues values = new ContentValues();
        values.put(KeyWordTable.KEY, keyWordTb.key);
        values.put(KeyWordTable.WORD, keyWordTb.word);
        return insert(values);
    }

    public Uri removeTheSameAndSaveNew(@NonNull KeyWordTb keyWordTb) {
        ArrayList<KeyWordTb> query = query(keyWordTb.key);
        if (query != null && query.size() > 0) {
            delete(keyWordTb.key);
        }

        return insert(keyWordTb);
    }
}
