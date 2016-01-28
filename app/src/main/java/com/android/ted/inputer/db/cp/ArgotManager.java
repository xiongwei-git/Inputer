package com.android.ted.inputer.db.cp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.android.ted.inputer.model.Argot;
import java.util.ArrayList;

/**
 * Created by Ted on 2016/1/25.
 *
 * @ com.android.ted.inputer.db.cp
 */
public class ArgotManager {
  private Context context;

  public ArgotManager(Context paramContext) {
    this.context = paramContext;
  }

  public int insert(Argot argot) {
    //if (queryAll(null, null, null).size() >= 10) {
    //  return 0;
    //}
    ContentValues contentValues = new ContentValues();
    contentValues.put("shortcut", argot.getShortcut());
    contentValues.put("phrase", argot.getPhrase());
    contentValues.put("description", argot.getDescription());
    contentValues.put("usage_count", argot.getUsage_count());
    contentValues.put("labels", argot.getLabels());
    contentValues.put("timestamp", argot.getTimestamp());
    contentValues.put("expands_immediately", argot.isExpands_immediately());
    contentValues.put("expands_within_word", argot.isExpands_within_word());
    Uri uri =
        this.context.getContentResolver().insert(ArgotContentProvider.baseURI, contentValues);
    String result = "-1";
    if (null != uri) result = uri.getLastPathSegment();
    return Integer.valueOf(result);
  }



  public ArrayList<Argot> query(String selection, String selectionArgs) {
    ArrayList<Argot> result = new ArrayList<>();
    if (TextUtils.isEmpty(selectionArgs)) return result;
    Cursor cursor = null;
    try {
      cursor = this.context.getContentResolver()
          .query(ArgotContentProvider.baseURI, ArgotDb.FIELD_NAME, selection + " LIKE ? ",
              new String[] { "%" + selectionArgs + "%" }, "usage_count DESC LIMIT 3");
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

  public ArrayList<Argot> queryAll(String selection, String[] selectionArgs,
      String sortOrder) {
    ArrayList<Argot> results = new ArrayList<>();
    Cursor cursor = null;
    try {
      cursor = this.context.getContentResolver()
          .query(ArgotContentProvider.baseURI, ArgotDb.FIELD_NAME, selection, selectionArgs,
              sortOrder);
      results = queryAll(cursor);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (null != cursor) cursor.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return results;
  }

  public void delete(long id) {
    this.context.getContentResolver()
        .delete(ArgotContentProvider.baseURI, "_id = ?", new String[] { String.valueOf(id) });
  }

  public int deleteAll() {
    ArrayList<Argot> allArgot = queryAll(null,null,null);
    if(null != allArgot){
      int count = 0;
      for (Argot argot : allArgot){
        delete(argot.get_id());
        count++;
      }
      return count;
    }else return -1;
  }

  public int update(Argot record) {
    ContentValues contentValues = new ContentValues();
    contentValues.put("shortcut", record.getShortcut());
    contentValues.put("phrase", record.getPhrase());
    contentValues.put("description", record.getDescription());
    contentValues.put("usage_count", record.getUsage_count());
    contentValues.put("labels", record.getLabels());
    contentValues.put("timestamp", record.getTimestamp());
    contentValues.put("expands_immediately", record.isExpands_immediately());
    contentValues.put("expands_within_word", record.isExpands_within_word());
    return this.context.getContentResolver()
        .update(ArgotContentProvider.baseURI, contentValues, "_id = ?",
            new String[] { String.valueOf(record.get_id()) });
  }

  public boolean has(String shortcut) {
    for (String str : queryAllShortcut(null, null, null)) {
      if (str.equals(shortcut)) {
        return true;
      }
    }
    return false;
  }

  public ArrayList<String> queryAllShortcut(String selection, String[] selectionArgs,
      String sortOrder) {
    ArrayList<Argot> allRecords = queryAll(selection, selectionArgs, sortOrder);
    ArrayList<String> shortcutList = new ArrayList<>();
    if (null == allRecords) return shortcutList;
    for (Argot argot : allRecords) {
      shortcutList.add(argot.getShortcut().trim());
    }
    return shortcutList;
  }

  public ArrayList<Argot> queryAll(Cursor cursor) {
    ArrayList<Argot> results = new ArrayList<>();
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
        Argot argot = new Argot();
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
}
