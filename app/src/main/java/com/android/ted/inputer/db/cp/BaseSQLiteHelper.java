package com.android.ted.inputer.db.cp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ted on 2016/1/25.
 *
 * @ com.android.ted.inputer.db.cp
 */
public class BaseSQLiteHelper extends SQLiteOpenHelper {
  public static final String DATABASE_NAME = "inputer.db";
  public static final int DATABASE_VERSION = 1;

  public BaseSQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    ArgotDb.updateDb(db, oldVersion, newVersion);
    AppExDb.updateDb(db, oldVersion, newVersion);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    ArgotDb.createDb(db);
    AppExDb.createDb(db);
  }
}
