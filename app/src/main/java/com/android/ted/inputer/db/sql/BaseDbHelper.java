package com.android.ted.inputer.db.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Description:
 * Creator: ZhangJinWei
 * Date:15/12/17 下午2:33
 */
public class BaseDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "ted_inputer.db";
    public static final int VERSION = 1;

    public BaseDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ArgotManager.createDb(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
