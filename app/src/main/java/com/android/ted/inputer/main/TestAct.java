package com.android.ted.inputer.main;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ted.inputer.R;
import com.android.ted.inputer.db.KeyWordTable;
import com.android.ted.inputer.db.KeyWordTb;
import com.android.ted.inputer.util.DataProvider;
import com.android.ted.inputer.db.opt.KeyWordDataHelper;
import com.android.ted.inputer.db.opt.KeyWordTableOpt;


/**
 * Description:
 * Creator: ZhangJinWei
 * Date:15/12/17 下午3:50
 */
public class TestAct extends Activity implements View.OnClickListener, LoaderManager
        .LoaderCallbacks<Cursor> , Loader.OnLoadCompleteListener<Cursor>{

    private EditText editKey;
    private EditText editValue;
    private TextView txtSave;
    private TextView txtRemoveAndSave;
    private ListView recycleView;
    private TestCursorAdapter mAdapter;
    private KeyWordDataHelper mKeyWordDataHelper;

    private CursorLoader mCursorLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_test);
        initView();
        initListener();
        initRecycleView();
        initDb();
        initCursorLoader();
    }

    private void initCursorLoader() {
        mCursorLoader = new CursorLoader(this);
        mCursorLoader.registerListener(0,this);
        mCursorLoader.setUri(DataProvider.KEYWORDS_CONTENT_URI);
        mCursorLoader.setSortOrder(KeyWordTable._ID + " DESC");
        mCursorLoader.startLoading();

    }

    String sql;

    private void initDb() {

        mKeyWordDataHelper = new KeyWordDataHelper(this);

        sql = "select " + KeyWordTable._ID + " , " +
                KeyWordTable.KEY + " , " +
                KeyWordTable.WORD + " from " + KeyWordTable
                .TABLE_NAME;
        new LoaderManager.LoaderCallbacks<Cursor>() {

            @Override
            public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
                Log.e("db", "onCreateLoader");
//                return new MyCursorLoader(TestAct.this, sql, null);
                return new CursorLoader(TestAct.this, DataProvider.KEYWORDS_CONTENT_URI, null,
                        null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
                Log.e("db", "onLoadFinished");
                mAdapter.swapCursor(arg1);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> arg0) {
                Log.e("db", "onLoaderReset");
                mAdapter.swapCursor(null);
            }
        };
        recycleView.setAdapter(mAdapter);

    }

    private void initRecycleView() {
        mAdapter = new TestCursorAdapter(this, null);
    }

    private void initListener() {
        txtSave.setOnClickListener(this);
        txtRemoveAndSave.setOnClickListener(this);
    }

    private void initView() {
        this.recycleView = (ListView) findViewById(R.id.recycleView);
        this.txtRemoveAndSave = (TextView) findViewById(R.id.txtRemoveAndSave);
        this.txtSave = (TextView) findViewById(R.id.txtSave);
        this.editValue = (EditText) findViewById(R.id.editValue);
        this.editKey = (EditText) findViewById(R.id.editKey);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.txtSave:
                if (checkEditTextNotEmpty()) {
                    save();
                }
                break;

            case R.id.txtRemoveAndSave:
                if (checkEditTextNotEmpty()) {
                    removeAndSave();
                }
                break;

        }
    }

    private void removeAndSave() {
        KeyWordTb keyWordTb = getKeyWordTb();
        Uri insertUri = mKeyWordDataHelper.removeTheSameAndSaveNew(keyWordTb);
        if (insertUri != null) {
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            clearTxt();
        } else {
            int i = KeyWordTableOpt.removeTheSameAndSaveNew(keyWordTb);
            switch (i) {
                case 0:
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                    clearTxt();
                    break;
                case -1:
                    Toast.makeText(this, "数据库异常，请重试", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(this, "数据库异常，请重试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void clearTxt() {
        editKey.setText("");
        editValue.setText("");

    }

    private void save() {
        KeyWordTb keyWordTb = getKeyWordTb();
        Uri insertUri = mKeyWordDataHelper.insert(keyWordTb);
        if (insertUri != null) {
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            clearTxt();
        } else {
            int insert = KeyWordTableOpt.insert(keyWordTb);
            switch (insert) {
                case 0:
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                    clearTxt();
                    TestAct.this.getLoaderManager().restartLoader(0, null, this);
                    break;
                case -1:
                    Toast.makeText(this, "此key值已经保存过了", Toast.LENGTH_SHORT).show();
                    break;
                case -2:
                    Toast.makeText(this, "数据库异常，请重试", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(this, "数据库异常，请重试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @NonNull
    private KeyWordTb getKeyWordTb() {
        String key = editKey.getText().toString();
        String value = editValue.getText().toString();
        KeyWordTb keyWordTb = new KeyWordTb();
        keyWordTb.key = key;
        keyWordTb.word = value;
        return keyWordTb;
    }

    private boolean checkEditTextNotEmpty() {
        String key = editKey.getText().toString();
        String value = editValue.getText().toString();

        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            Toast.makeText(this, "请输入key和value值", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        return new MyCursorLoader(TestAct.this, sql, null);
        return new CursorLoader(TestAct.this, DataProvider.KEYWORDS_CONTENT_URI, null,
                null, null, KeyWordTable._ID + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mAdapter != null) {
            mAdapter.swapCursor(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mAdapter != null) {
            mAdapter.swapCursor(null);
        }
    }

    @Override
    protected void onDestroy() {
        if (mCursorLoader != null) {
            mCursorLoader.unregisterListener(this);
            mCursorLoader.cancelLoad();
            mCursorLoader.stopLoading();
        }
        super.onDestroy();
    }


    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
        if (mAdapter != null) {
            mAdapter.swapCursor(data);
        }
    }
}
