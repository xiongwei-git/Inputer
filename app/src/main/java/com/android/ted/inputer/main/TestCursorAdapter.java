package com.android.ted.inputer.main;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ted.inputer.R;
import com.android.ted.inputer.db.KeyWordTable;


/**
 * Description:
 * Creator: ZhangJinWei
 * Date:15/12/17 下午4:50
 */
public class TestCursorAdapter extends CursorAdapter {

    private LayoutInflater mLayoutInflater = null;
    private Context mContext = null;

    public TestCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        init(context);
    }

    public TestCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        init(context);
    }

    public TestCursorAdapter(Context context, Cursor c) {
        super(context, c);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void bindView(View arg0, Context arg1, Cursor arg2) {
    }

    @Override
    public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
        return null;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        arg1 = mLayoutInflater
                .inflate(R.layout.item_test_key_value, null);
        TextView itemKey = (TextView) arg1.findViewById(R.id.itemKey);
        TextView itemValue = (TextView) arg1.findViewById(R.id.itemValue);

        if (!mCursor.moveToPosition(arg0)) {
            return null;
        }
        String itemKeyStr = mCursor.getString(mCursor
                .getColumnIndex(KeyWordTable.KEY));
        String itemValueStr = mCursor.getString(mCursor
                .getColumnIndex(KeyWordTable.WORD));
        itemKey.setText(itemKeyStr);
        itemValue.setText(itemValueStr);

        return arg1;
    }

}
