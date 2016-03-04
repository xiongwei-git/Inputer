package com.mrxiong.argot.store.cp;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Ted on 2015/12/31.
 *
 * @ com.mrxiong.argot.db
 */
public class ArgotDb {
  public static final String[] FIELD_NAME = {
      "_id", "shortcut", "phrase", "description", "usage_count", "labels", "timestamp",
      "expands_immediately", "expands_within_word"
  };

  public static void createDb(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(
        "CREATE TABLE argot (_id INTEGER PRIMARY KEY AUTOINCREMENT, shortcut TEXT NOT NULL, description TEXT, phrase LONGTEXT NOT NULL, usage_count INTEGER,labels TEXT, timestamp INTEGER NOT NULL, expands_immediately INTEGER NOT NULL DEFAULT 0, expands_within_word INTEGER NOT NULL DEFAULT 0)");
  }

  public static void updateDb(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    if ((oldVersion == 4) && (newVersion == 5)) {
      sqLiteDatabase.execSQL(
          "ALTER TABLE argot ADD COLUMN  expands_immediately INTEGER NOT NULL DEFAULT 0");
      sqLiteDatabase.execSQL(
          "ALTER TABLE argot ADD COLUMN  expands_within_word INTEGER NOT NULL DEFAULT 0");
    }
  }
}