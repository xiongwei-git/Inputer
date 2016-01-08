package com.android.ted.inputer.debug;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.android.ted.inputer.R;
import com.android.ted.inputer.base.BaseActivity;
import com.android.ted.inputer.model.ArgotRecord;

/**
 * Created by Ted on 2016/1/4.
 *
 * @ com.android.ted.inputer.debug
 */
public class DebugDbActivity extends BaseActivity implements View.OnClickListener {
  private TextView mTextView;

  private Handler mHandler = new Handler(new Handler.Callback() {
    @Override public boolean handleMessage(Message msg) {

      return false;
    }
  });

  @Override protected void initPresenter() {

  }

  @Override public void onClick(View v) {
    if (v.getId() == R.id.write_test_btn) {

    } else if (v.getId() == R.id.read_test_btn) {

    }
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_debug_db);
    findViewById(R.id.write_test_btn).setOnClickListener(this);
    findViewById(R.id.read_test_btn).setOnClickListener(this);
    mTextView = (TextView) findViewById(R.id.wast_time_txt);
  }

  private void onWrite() {

  }

  class TRunnable implements Runnable {
    private Handler mHandler;

    public TRunnable(Handler handler) {
      this.mHandler = handler;
    }

    @Override public void run() {
      for (int i = 0; i < 1000; i++) {
        ArgotRecord argotRecord = new ArgotRecord();
        argotRecord.setShortcut("shortcut" + i);
      }
      mHandler.sendEmptyMessage(0);
    }
  }
}
