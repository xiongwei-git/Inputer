package com.android.ted.inputer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.ted.inputer.db.opt.KeyWordTable;

/**
 * Description:
 * Creator: ZhangJinWei
 * Date:15/12/17 下午2:33
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "ted_inputer.db";
    public static final int VERSION = 1;
    public DbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(KeyWordTable.toTable());
        ArgotDbOperator.createDb(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
