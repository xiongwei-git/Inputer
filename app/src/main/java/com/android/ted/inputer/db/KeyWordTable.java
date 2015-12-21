package com.android.ted.inputer.db;

import android.provider.BaseColumns;

/**
 * Description:
 * Creator: ZhangJinWei
 * Date:15/12/18 下午10:22
 */
public final class KeyWordTable implements BaseColumns {
    private KeyWordTable() {
    }

    public static final String TABLE_NAME = "keywords";

    public static final String _ID = "_id";

    public static final String KEY = "key";

    public static final String WORD = "word";

    public static String toTable() {
        StringBuilder bf = new StringBuilder();
        bf.append("CREATE TABLE IF NOT EXISTS " + TABLE_NAME);
        bf.append("(");
        bf.append(_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,");
        bf.append(KEY + " VARCHAR , ");
        bf.append(WORD + " VARCHAR ");
        bf.append(")");
        return bf.toString();
    }
}
