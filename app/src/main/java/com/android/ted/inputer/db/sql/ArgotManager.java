package com.android.ted.inputer.db.sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import android.text.TextUtils;
import com.android.ted.inputer.db.cp.ArgotContentProvider;
import com.android.ted.inputer.db.cp.ArgotDb;
import com.android.ted.inputer.model.ArgotRecord;

import java.util.ArrayList;

/**
 * Created by Ted on 2015/12/31.
 *
 * @ com.android.ted.inputer.db
 */
public class ArgotManager {
  public static final String TABLE_NAME = "argot_table";
  public static final String[] FIELD_NAME = {
      "_id", "shortcut", "phrase", "description", "usage_count", "labels", "timestamp",
      "expands_immediately", "expands_within_word"
  };

  public static void createDb(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL("CREATE TABLE "
        + TABLE_NAME
        + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, shortcut TEXT NOT NULL, description TEXT, phrase LONGTEXT NOT NULL, usage_count INTEGER,labels TEXT, timestamp INTEGER NOT NULL, expands_immediately INTEGER NOT NULL DEFAULT 0, expands_within_word INTEGER NOT NULL DEFAULT 0)");
  }

  public static void updateDb(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    //        if ((oldVersion == 4) && (newVersion == 5)) {
    //            sqLiteDatabase.execSQL("ALTER TABLE argot ADD COLUMN  expands_immediately INTEGER NOT NULL DEFAULT 0");
    //            sqLiteDatabase.execSQL("ALTER TABLE argot ADD COLUMN  expands_within_word INTEGER NOT NULL DEFAULT 0");
    //        }
  }

  public ArrayList<ArgotRecord> query(String selection, String[] selectionArgs) {
    SQLiteDatabase sqLiteDatabase = DBManager.getInstance().getReadableDatabase();
    ArrayList<ArgotRecord> result = new ArrayList<>();
    Cursor cursor = null;
    try {
      cursor = sqLiteDatabase.query(TABLE_NAME, FIELD_NAME, selection, selectionArgs, "usage_count",
          null, "DESC", "3");
      result = queryAll(cursor);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (null != cursor) cursor.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  /**
   * 精确查询
   *
   * @param shortcut 简称
   * @return ArrayList
   */
  public static ArrayList<ArgotRecord> exactQuery(String shortcut) {
    SQLiteDatabase sqLiteDatabase = DBManager.getInstance().getReadableDatabase();
    String sql = " select * from " + TABLE_NAME + " where " + FIELD_NAME[1] + " = ? ";
    String[] args = new String[] { shortcut };
    Cursor cursor = sqLiteDatabase.rawQuery(sql, args);
    ArrayList<ArgotRecord> results = queryAll(cursor);
    try {
      if (cursor != null) {
        cursor.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return results;
  }

  public static ArrayList<ArgotRecord> fuzzyQuery(String shortcut) {
    SQLiteDatabase sqLiteDatabase = DBManager.getInstance().getReadableDatabase();
    Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, FIELD_NAME[1] + " LIKE '" +
        shortcut + "%'", null, null, null, null);
    ArrayList<ArgotRecord> results = queryAll(cursor);
    try {
      if (cursor != null) {
        cursor.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return results;
  }

  /**
   * @return 0 means insert success.-1 means if this key has saved in this table ,-2 means insert
   * failed.
   */
  public static int insert(@NonNull ArgotRecord argot) {
    if (has(argot)) return -1;
    SQLiteDatabase db = DBManager.getInstance().getWritableDatabase();
    ContentValues value = getContentValues(argot);
    long insert = db.insert(TABLE_NAME, null, value);
    if (insert < 0) {
      return -2;
    }
    return 0;
  }

  public static boolean has(ArgotRecord argot) {
    ArrayList<ArgotRecord> list = exactQuery(argot.getShortcut());
    return list != null && list.size() > 0;
  }

  /**
   * @param argot argot
   * @return >0 means success .and -1 means failed
   */
  public static int update(@NonNull ArgotRecord argot) {
    SQLiteDatabase db = DBManager.getInstance().getWritableDatabase();
    ContentValues value = getContentValues(argot);
    return db.update(TABLE_NAME, value, null, null);
  }

  public static int delete(long id) {
    SQLiteDatabase db = DBManager.getInstance().getWritableDatabase();
    return db.delete(TABLE_NAME, FIELD_NAME[1] + " = ?" +
        " ", new String[] { String.valueOf(id) });
  }

  public static void deleteAll() {
    String sql = "DELETE FROM " + TABLE_NAME + ";";
    SQLiteDatabase db = DBManager.getInstance().getWritableDatabase();
    db.execSQL(sql);
    String revertSql = "update sqlite_sequence set seq=0 where name='" + TABLE_NAME + "'";
    db.execSQL(revertSql);
  }

  public static ArrayList<ArgotRecord> queryAll(Cursor cursor) {
    ArrayList<ArgotRecord> results = new ArrayList<>();
    if (cursor == null) {
      return results;
    }
    if (cursor.getCount() > 0) {
      cursor.moveToFirst();
      int indexId = cursor.getColumnIndexOrThrow("_id");
      int indexShortcut = cursor.getColumnIndexOrThrow("shortcut");
      int indexPhrase = cursor.getColumnIndexOrThrow("phrase");
      int indexDes = cursor.getColumnIndexOrThrow("description");
      int indexUsage = cursor.getColumnIndexOrThrow("usage_count");
      int indexLabels = cursor.getColumnIndexOrThrow("labels");
      int indexTimestamp = cursor.getColumnIndexOrThrow("timestamp");
      int indexImmediately = cursor.getColumnIndexOrThrow("expands_immediately");
      int indexInWord = cursor.getColumnIndexOrThrow("expands_within_word");
      while (!cursor.isAfterLast()) {
        ArgotRecord argot = new ArgotRecord();
        argot.set_id(cursor.getLong(indexId));
        argot.setShortcut(cursor.getString(indexShortcut));
        argot.setPhrase(cursor.getString(indexPhrase));
        argot.setDescription(cursor.getString(indexDes));
        argot.setUsage_count(cursor.getInt(indexUsage));
        argot.setLabels(cursor.getString(indexLabels));
        argot.setTimestamp(cursor.getLong(indexTimestamp));
        argot.setExpands_immediately(cursor.getInt(indexImmediately) >= 1);
        argot.setExpands_within_word(cursor.getInt(indexInWord) >= 1);
        results.add(argot);
        cursor.moveToNext();
      }
    }
    try {
      cursor.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return results;
  }

  private static ContentValues getContentValues(@NonNull ArgotRecord argot) {
    ContentValues value = new ContentValues();
    value.put(FIELD_NAME[1], argot.getShortcut());
    value.put(FIELD_NAME[2], argot.getPhrase());
    value.put(FIELD_NAME[3], argot.getDescription());
    value.put(FIELD_NAME[4], argot.getUsage_count());
    value.put(FIELD_NAME[5], argot.getLabels());
    value.put(FIELD_NAME[6], argot.getTimestamp());
    value.put(FIELD_NAME[7], argot.isExpands_immediately());
    value.put(FIELD_NAME[8], argot.isExpands_within_word());
    return value;
  }
}
