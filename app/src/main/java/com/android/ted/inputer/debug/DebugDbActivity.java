package com.android.ted.inputer.debug;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import com.android.ted.inputer.R;
import com.android.ted.inputer.base.BaseActivity;
import com.android.ted.inputer.model.Argot;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Ted on 2016/1/4.
 *
 * @ com.android.ted.inputer.debug
 */
public class DebugDbActivity extends BaseActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {
  private TextView mTextView;
  private com.android.ted.inputer.db.cp.ArgotManager manager;
  private int state = 0;

  @Override protected void initPresenter() {

  }

  @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    state = isChecked?1:0;
  }

  @Override public void onClick(View v) {
    if (v.getId() == R.id.write_test_btn) {
      onWrite();
    } else if (v.getId() == R.id.read_test_btn) {
      onRead();
    } else if (v.getId() == R.id.revert_btn) {
      onRevert();
    }
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    manager = new com.android.ted.inputer.db.cp.ArgotManager(this);
    setContentView(R.layout.activity_debug_db);
    findViewById(R.id.write_test_btn).setOnClickListener(this);
    findViewById(R.id.read_test_btn).setOnClickListener(this);
    findViewById(R.id.revert_btn).setOnClickListener(this);
    mTextView = (TextView) findViewById(R.id.wast_time_txt);
    Switch switchBtn = (Switch)findViewById(R.id.switch_btn);
    switchBtn.setChecked(state != 0);
    switchBtn.setOnCheckedChangeListener(this);
  }

  long timestamp = -1;

  private void onWrite() {
    Observable.create(new Observable.OnSubscribe<Argot>() {
      @Override public void call(Subscriber<? super Argot> subscriber) {
        //for (int i = 0; i < 1000; i++) {
          Argot argot = new Argot();
          argot.setShortcut(String.valueOf(6 + 1));
          argot.setPhrase("xiongwei == " + 6);
          argot.setTimestamp(System.currentTimeMillis());
          argot.setExpands_immediately(false);
          argot.setExpands_within_word(false);
          if(manager.has(argot.getShortcut())){
            manager.update(argot);
          }else manager.insert(argot);
          //if (ArgotManager.insert(argotRecord) < 0) ArgotManager.update(argotRecord);
        //}
        subscriber.onCompleted();
      }
    })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.newThread())
        .subscribe(new Subscriber<Argot>() {
          @Override public void onStart() {
            super.onStart();
            timestamp = System.currentTimeMillis();
          }

          @Override public void onCompleted() {
            mTextView.append("插入100条耗时" + (System.currentTimeMillis() - timestamp) + "毫秒\n");
          }

          @Override public void onError(Throwable e) {
          }

          @Override public void onNext(Argot argot) {
          }
        });
  }

  private void onRead() {
    Observable.create(new Observable.OnSubscribe<Argot>() {
      @Override public void call(Subscriber<? super Argot> subscriber) {
        //ArgotRecord argotRecord = ArgotManager.fuzzyQuery("78").get(0);
        Argot argot = manager.query("shortcut","78").get(0);
        subscriber.onNext(argot);
      }
    })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.newThread())
        .subscribe(new Subscriber<Argot>() {
          @Override public void onStart() {
            super.onStart();
            timestamp = System.currentTimeMillis();
          }

          @Override public void onCompleted() {
          }

          @Override public void onError(Throwable e) {
          }

          @Override public void onNext(Argot argot) {
            mTextView.append(
                "读取一条数据耗时" + (System.currentTimeMillis() - timestamp) + "毫秒" + "\n结果是" + argot
                    .getPhrase() + "\n");
          }
        });
  }

  private void onRevert() {
    Observable.create(new Observable.OnSubscribe<String>() {
      @Override public void call(Subscriber<? super String> subscriber) {
        //ArgotManager.deleteAll();
        manager.deleteAll();
        subscriber.onCompleted();
      }
    })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.newThread())
        .subscribe(new Subscriber<String>() {
          @Override public void onStart() {
            super.onStart();
            timestamp = System.currentTimeMillis();
          }

          @Override public void onCompleted() {
            mTextView.append("重置所有数据耗时" + (System.currentTimeMillis() - timestamp) + "毫秒\n");
          }

          @Override public void onError(Throwable e) {
          }

          @Override public void onNext(String argotRecord) {
          }
        });
  }
}
