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

package com.android.ted.inputer.service;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.android.ted.inputer.db.cp.ArgotManager;
import com.android.ted.inputer.model.Argot;
import com.android.ted.inputer.model.GlobalCache;
import com.android.ted.inputer.util.AppContext;
import com.orhanobut.logger.Logger;
import java.util.ArrayList;

/**
 * Created by Ted on 2015/12/2.
 * InputDataOperator
 */
public class InputDataPresenter   {
    private InputDataMediator mDataInterface;

    private AccessibilityNodeInfo mFocusNodeInfo;
    private Argot mSelectArgot;
    private ArgotManager mManager;

    public InputDataPresenter(InputDataMediator mediator) {
        this.mDataInterface = mediator;
        mManager = new ArgotManager((Context)mediator);
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
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, mSelectArgot.getPhrase());
        mFocusNodeInfo.performAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_TEXT.getId(), arguments);
    }

    public void onRepeal() {
        if (mFocusNodeInfo == null) return;
        if (TextUtils.isEmpty(mSelectArgot.getPhrase())) return;
    }


    /*************************************/
    private void readTextChangeEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo source = event.getSource();
        if (source == null) {
            return;
        }
        final AccessibilityNodeInfo editView = getEditTextNodeInfo(source);
        if (null != editView && null != editView.getText()) {
            String text = editView.getText().toString();
            if(TextUtils.isEmpty(text))return;
            ArrayList<Argot> list = mManager.query("shortcut",text);
            Logger.d("size = "+list.size());
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
        if (TextUtils.isEmpty(packageName))
            packageName = "com.android.ted.inputer";
        return packageName;
    }

    public void onDestroy() {
        mManager = null;
    }
}
