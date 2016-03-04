package com.mrxiong.argot.store.cp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Ted on 2016/1/25.
 *
 * @ com.mrxiong.argot.db.cp
 */
public class ArgotContentProvider extends ContentProvider {
  public static final Uri baseURI =
      Uri.parse("content://com.mrxiong.argot.db.cp.ArgotContentProvider/ARGOT");
  public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
  private BaseSQLiteHelper baseSQLiteHelper;

  static {
    uriMatcher.addURI("com.mrxiong.argot.db.cp.ArgotContentProvider", "ARGOT", 10);
    uriMatcher.addURI("com.mrxiong.argot.db.cp.ArgotContentProvider", "ARGOT/#", 20);
  }

  @SuppressWarnings("unchecked") private void checkTableColumns(String[] columns) {
    String[] fieldName = ArgotDb.FIELD_NAME;
    if (columns != null) {
      if (!new HashSet(Arrays.asList(fieldName)).containsAll(new HashSet(Arrays.asList(columns)))) {
        throw new IllegalArgumentException("Unknown columns in the projection");
      }
    }
  }

  @Override public boolean onCreate() {
    this.baseSQLiteHelper = new BaseSQLiteHelper(getContext());
    return false;
  }

  @Nullable @Override public Cursor query(@NonNull Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
    checkTableColumns(projection);
    queryBuilder.setTables("argot");
    if (uriMatcher.match(uri) == 20) {
      queryBuilder.appendWhere("_id=" + uri.getLastPathSegment());
    } else if (uriMatcher.match(uri) != 10) {
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    Cursor cursor =
        queryBuilder.query(this.baseSQLiteHelper.getWritableDatabase(), projection, selection,
            selectionArgs, null, null, sortOrder);
    if (null != getContext()) cursor.setNotificationUri(getContext().getContentResolver(), uri);
    return cursor;
  }

  @Nullable @Override public Uri insert(@NonNull Uri uri, ContentValues values) {
    SQLiteDatabase database = this.baseSQLiteHelper.getWritableDatabase();
    if (uriMatcher.match(uri) == 10) {
      long id = database.insert("argot", null, values);
      if (null != getContext() && null != getContext().getContentResolver()) {
        getContext().getContentResolver().notifyChange(uri, null);
      }
      return Uri.parse(baseURI + "/" + (int) id);
    }
    throw new IllegalArgumentException("Unknown URI: " + uri);
  }

  @Override public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
    SQLiteDatabase database = this.baseSQLiteHelper.getWritableDatabase();
    int count;
    if (uriMatcher.match(uri) == 20) {
      String str = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
        count = database.delete("argot", "_id=" + str, selectionArgs);
      } else {
        count = database.delete("argot", "_id=" + str + "and" + selection, selectionArgs);
      }
    } else if (uriMatcher.match(uri) == 10) {
      count = database.delete("argot", selection, selectionArgs);
    } else {
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    if (null != getContext() && null != getContext().getContentResolver()) {
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return count;
  }

  @Override public int update(@NonNull Uri uri, ContentValues values, String selection,
      String[] selectionArgs) {
    SQLiteDatabase database = this.baseSQLiteHelper.getWritableDatabase();
    int count;
    if (uriMatcher.match(uri) == 20) {
      String str = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
        count = database.update("argot", values, "_id=" + str, selectionArgs);
      } else {
        count = database.update("argot", values, "_id=" + str + "and" + selection, selectionArgs);
      }
    } else if (uriMatcher.match(uri) == 10) {
      count = database.update("argot", values, selection, selectionArgs);
    } else {
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    if (null != getContext() && null != getContext().getContentResolver()) {
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return count;
  }

  @Nullable @Override public String getType(@NonNull Uri uri) {
    return null;
  }
}
