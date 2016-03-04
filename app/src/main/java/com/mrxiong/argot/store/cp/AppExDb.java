package com.mrxiong.argot.store.cp;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Ted on 2015/12/31.
 *
 * @ com.mrxiong.argot.db
 */
public class AppExDb {
  public static final String[] FEILD_NAME = { "_id", "package_name" };

  public static void createDb(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(
        "CREATE TABLE app_exclusions (_id INTEGER PRIMARY KEY AUTOINCREMENT, package_name TEXT NOT NULL)");
  }

  public static void updateDb(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS app_exclusions");
    createDb(sqLiteDatabase);
  }
}
