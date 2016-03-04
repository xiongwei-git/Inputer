/*
 * Copyright 2016 Ted xiong-wei@hotmail.com
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

package com.mrxiong.argot.main.detail;

import android.content.Context;
import android.text.TextUtils;
import com.mrxiong.argot.store.cp.ArgotManager;
import com.mrxiong.argot.model.Argot;
import com.mrxiong.argot.config.Constants;
import java.util.ArrayList;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Ted on 2015/12/30.
 *
 * @ com.mrxiong.argot.main
 */
public class ArgotRowViewPresenter {
  public ArgotRowViewMediator mMediator;
  private ArgotManager mManager;

  public ArgotRowViewPresenter(ArgotRowViewMediator mediator) {
    this.mMediator = mediator;
    this.mManager = new ArgotManager((Context) mediator);
  }

  public void onSaveArgot(final Argot argot) {
    if (null == argot) return;
    if (TextUtils.isEmpty(argot.getShortcut() + argot.getPhrase())) {
      mMediator.onSaveArgotResult(Constants.ARGOT_SAVE_RESULT_ALL_NULL);
    } else if (TextUtils.isEmpty(argot.getShortcut())) {
      mMediator.onSaveArgotResult(Constants.ARGOT_SAVE_RESULT_SHORTCUT_NULL);
    } else if (TextUtils.isEmpty(argot.getPhrase())) {
      mMediator.onSaveArgotResult(Constants.ARGOT_SAVE_RESULT_PHRASE_NULL);
    } else {
      Observable.create(new Observable.OnSubscribe<Integer>() {
        @Override public void call(Subscriber<? super Integer> subscriber) {
          int result;
          if (mManager.has(argot.getShortcut())) {
            ArrayList<Argot> argotList = mManager.queryAll("shortcut = ?",new String[] { argot.getShortcut() },null);
            if(null == argotList || argotList.size() > 1){
              throw new IllegalArgumentException("Error data in database");
            }else {
              argot.set_id(argotList.get(0).get_id());
              result = mManager.update(argot);
            }
          } else {
            result = mManager.insert(argot);
          }
          subscriber.onNext(result);
        }
      })
          .observeOn(AndroidSchedulers.mainThread())
          .subscribeOn(Schedulers.newThread())
          .subscribe(new Action1<Integer>() {
            @Override public void call(Integer s) {
              mMediator.onSaveArgotResult(Constants.ARGOT_SAVE_RESULT_OK);
            }
          });
    }
  }
}
