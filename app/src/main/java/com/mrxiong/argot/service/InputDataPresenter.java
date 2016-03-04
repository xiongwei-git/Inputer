/*
 *    Copyright 2016 Ted xiong-wei@hotmail.com
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.mrxiong.argot.service;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.mrxiong.argot.store.cp.ArgotManager;
import com.mrxiong.argot.model.Argot;
import com.mrxiong.argot.store.GlobalCache;
import java.util.ArrayList;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Ted on 2015/12/2.
 * InputDataOperator
 */
public class InputDataPresenter {
  private InputDataMediator mDataInterface;

  private AccessibilityNodeInfo mFocusNodeInfo;
  private Argot mSelectArgot;
  private ArgotManager mManager;

  public InputDataPresenter(InputDataMediator mediator) {
    this.mDataInterface = mediator;
    mManager = new ArgotManager((Context) mediator);
  }

  /***
   * 处理无障碍消息事件
   *
   * @param event 消息事件
   */
  public void onHandleAccessibilityEvent(AccessibilityEvent event) {
    if (isFilterApp(event)) return;
    int eventType = event.getEventType();
    switch (eventType) {
      case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
        mDataInterface.onTextChanged();
        readTextChangeEvent(event);
        break;
      default:
        break;
    }
  }

  public void onExpand() {
    if (mFocusNodeInfo == null) return;
    if (TextUtils.isEmpty(mSelectArgot.getPhrase())) return;
    Bundle arguments = new Bundle();
    arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
        mSelectArgot.getPhrase());
    mFocusNodeInfo.performAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_TEXT.getId(),
        arguments);
  }

  public void onRepeal() {
    if (mFocusNodeInfo == null) return;
    if (TextUtils.isEmpty(mSelectArgot.getPhrase())) return;
  }

  /*************************************/
  private void readTextChangeEvent(AccessibilityEvent event) {
    AccessibilityNodeInfo source = event.getSource();
    if (source == null) return;
    mFocusNodeInfo = getEditTextNodeInfo(source);
    if (null != mFocusNodeInfo) {
      final String text = mFocusNodeInfo.getText() == null?"":mFocusNodeInfo.getText().toString();
      if (TextUtils.isEmpty(text)) return;
      Observable.create(new Observable.OnSubscribe<ArrayList<Argot>>() {
        @Override public void call(Subscriber<? super ArrayList<Argot>> subscriber) {
          ArrayList<Argot> list = mManager.query("shortcut", text);
          subscriber.onNext(list);
        }
      })
          .observeOn(AndroidSchedulers.mainThread())
          .subscribeOn(Schedulers.io())
          .subscribe(new Subscriber<ArrayList<Argot>>() {
            @Override public void onCompleted() {

            }

            @Override public void onError(Throwable e) {
              e.printStackTrace();
              mDataInterface.onMatchNothing();
            }

            @Override public void onNext(ArrayList<Argot> list) {
              if (null == list || list.size() == 0) {
                mDataInterface.onMatchNothing();
              } else {

              }
            }
          });
    }
  }

  private AccessibilityNodeInfo getEditTextNodeInfo(AccessibilityNodeInfo source) {
    AccessibilityNodeInfo current = source;
    while (true) {
      if (current == null) {
        return null;
      }
      if ("android.widget.EditText".equals(current.getClassName())) {
        return current;
      }
      AccessibilityNodeInfo oldCurrent = current;
      current = oldCurrent.getParent();
      oldCurrent.recycle();
    }
  }

  /***
   * 是否是过滤的应用
   *
   * @param event 消息事件
   * @return 是或者否
   */
  private boolean isFilterApp(AccessibilityEvent event) {
    String packageName = event.getPackageName().toString();
    return TextUtils.isEmpty(packageName) ||
        getPackageName().equals(packageName) ||
        GlobalCache.getInstance().getAppPackageNameList().contains(packageName);
  }

  /***
   * 获取应用程序的包名
   *
   * @return 报名
   */
  private String getPackageName() {
    String packageName = ((InputAccessibilityService) mDataInterface).getPackageName();
    if (TextUtils.isEmpty(packageName)) packageName = "com.mrxiong.argot";
    return packageName;
  }

  public void onDestroy() {
    mManager = null;
  }
}
