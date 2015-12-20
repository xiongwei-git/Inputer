package com.android.ted.inputer.db.opt;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.ted.inputer.db.DBManager;
import com.android.ted.inputer.db.KeyWordTable;
import com.android.ted.inputer.db.KeyWordTb;

import java.util.ArrayList;


/**
 * Description:
 * Creator: ZhangJinWei
 * Date:15/12/17 下午2:46
 */
public class KeyWordTableOpt {

    public static ArrayList<KeyWordTb> getWord(String key) {
        ArrayList<KeyWordTb> results = new ArrayList<>();
        KeyWordTb item = null;
        SQLiteDatabase db = DBManager.getInstance().getDbHelper().getReadableDatabase();
        String sql = " select * from " + KeyWordTable.TABLE_NAME + " where " +
                KeyWordTable.KEY + " = ? ";
        String[] args = new String[]{key};
        Cursor cursor = db.rawQuery(sql, args);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                item = new KeyWordTb();
                item.key = key;
                int indexWord = cursor.getColumnIndex(KeyWordTable
                        .WORD);
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

    /**
     * @param keyWordTb
     * @return 0 means insert success.-1 means if this key has saved in this table ,-2 means insert
     * failed.
     */
    public static int insert(@NonNull KeyWordTb keyWordTb) {
        ArrayList<KeyWordTb> word = getWord(keyWordTb.key);
        if (word != null && word.size() > 0) {//存在过这个key
            return -1;
        }
        SQLiteDatabase db = DBManager.getInstance().getWritableDatabase();
        ContentValues value = getContentValues(keyWordTb);
        long insert = db.insert(KeyWordTable.TABLE_NAME, null, value);
        if (insert < 0) {
            return -2;
        }

        return 0;
    }

    public static boolean hasThisKey(KeyWordTb keyWordTb) {
        ArrayList<KeyWordTb> word = getWord(keyWordTb.key);
        if (word != null && word.size() > 0) {//存在过这个key
            return true;
        }
        return false;
    }

    @NonNull
    private static ContentValues getContentValues(@NonNull KeyWordTb keyWordTb) {
        ContentValues value = new ContentValues();
        value.put(KeyWordTable.KEY, keyWordTb.key);
        value.put(KeyWordTable.WORD, keyWordTb.word);
        return value;
    }

    /**
     * @param keyWordTb
     * @return 0 means success .and -1 means failed
     */
    public static int removeTheSameAndSaveNew(@NonNull KeyWordTb keyWordTb) {
        if (hasThisKey(keyWordTb)) {
            delete(keyWordTb);
        }
        SQLiteDatabase db = DBManager.getInstance().getWritableDatabase();
        ContentValues value = getContentValues(keyWordTb);
        long replace = db.replace(KeyWordTable.TABLE_NAME, null, value);
        if (replace < 0) {
            return -1;
        }

        return 0;
    }

    public static void delete(KeyWordTb keyWordTb) {
        SQLiteDatabase db = DBManager.getInstance().getWritableDatabase();
        int delete = db.delete(KeyWordTable.TABLE_NAME, KeyWordTable.KEY + " = ?" +
                " ", new String[]{keyWordTb.key});
        Log.e("db", "delete = " + delete);
    }
}
