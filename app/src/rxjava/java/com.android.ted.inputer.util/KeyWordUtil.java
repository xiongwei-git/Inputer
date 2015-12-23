package com.android.ted.inputer.util;

import com.android.ted.inputer.db.LoaderSdk;
import com.android.ted.inputer.db.opt.KeyWordDataHelper;

import java.util.ArrayList;

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
            mKeyWordDataHelper = new KeyWordDataHelper(LoaderSdk.getInstance().getContext());
        }
        return this;
    }

    public KeyWordUtil readTextKeyWordFromDb(final String key, final KeyWordInterface callBack) {
        init();
        if (lastSubscription != null && lastSubscription.isUnsubscribed()) {
            lastSubscription.unsubscribe();
        }
        Subscription subscription = Observable.defer(new Func0<Observable<ArrayList<String>>>() {
            @Override
            public Observable<ArrayList<String>> call() {
                ArrayList<String> words = mKeyWordDataHelper.queryWords(key);
                return Observable.just(words);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ArrayList<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                if (callBack != null) {
                    ArrayList<String> words = new ArrayList<String>();
                    words.add("error");
                    callBack.getKeyWordSuccess(false, words);
                }
            }

            @Override
            public void onNext(ArrayList<String> resultWords) {
                if (callBack != null) {
                    boolean isSuccess = ( resultWords != null && resultWords.size() > 0 );
                    callBack.getKeyWordSuccess(isSuccess, resultWords);// TODO: 15/12/23 undo
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
