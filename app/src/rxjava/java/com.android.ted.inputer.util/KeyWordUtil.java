package com.android.ted.inputer.util;

import android.text.TextUtils;

import com.android.ted.inputer.db.opt.KeyWordDataHelper;
import com.android.ted.inputer.main.MainApplication;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Description:
 * Creator: ZhangJinWei
 * Date:15/12/20 下午12:08
 */
public class KeyWordUtil {
    private KeyWordDataHelper mKeyWordDataHelper;
    private Subscription lastSubscription;

    public KeyWordUtil() {
    }

    public KeyWordUtil init() {
        if (mKeyWordDataHelper == null) {
            mKeyWordDataHelper = new KeyWordDataHelper(MainApplication.getIntance());
        }
        return this;
    }

    public KeyWordUtil readTextKeyWordFromDb(final String key, final KeyWordInterface callBack) {
        init();
        if (lastSubscription != null && lastSubscription.isUnsubscribed()) {
            lastSubscription.unsubscribe();
        }
        Subscription subscription = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                String word = mKeyWordDataHelper.queryWord(key);
                return Observable.just(word);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                if (callBack != null) {
                    callBack.getKeyWordSuccess(false, "error");
                }
            }

            @Override
            public void onNext           (String s) {
                if (callBack != null) {
                    boolean isSuccess = TextUtils.isEmpty(s) ? false : true;
                    callBack.getKeyWordSuccess(isSuccess, s);
                }
            }
        });
        lastSubscription = subscription;
        return this;
    }

    public void onDestroy() {
        if (lastSubscription != null && lastSubscription.isUnsubscribed()) {
            lastSubscription.unsubscribe();
        }
    }
}
