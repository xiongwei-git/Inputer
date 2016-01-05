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

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.accessibility.AccessibilityEvent;

import com.android.ted.inputer.window.FloatWindowView;
import com.android.ted.inputer.window.TWindowManager;

/**
 * Created by Ted on 2015/12/2.
 * InputAccessibilityService
 */
public class InputAccessibilityService extends AccessibilityService
        implements InputDataMediator,FloatWindowView.OnClickListener {
    private final int MSG_HIDE_BTN = 0x001;

    private Context mContext;
    private InputDataPresenter mDataOperator;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == MSG_HIDE_BTN){
                TWindowManager.hideFloatBtn();
            }
            return false;
        }
    });

    @Override
    public void onClickBtn(FloatWindowView.FloatBtnType type) {
        mDataOperator.onExpand();
        mHandler.removeMessages(MSG_HIDE_BTN);
        TWindowManager.hideFloatBtn();
    }

    @Override
    public void onTextChanged() {
        TWindowManager.hideRevertBtn();
    }

    @Override
    public void onMatchNothing() {
        mHandler.removeMessages(MSG_HIDE_BTN);
    }

    @Override
    public void onMatchPart() {
        TWindowManager.showFloatBtnWindow(mContext,this, FloatWindowView.FloatBtnType.SUGGEST);
        mHandler.removeMessages(MSG_HIDE_BTN);
        mHandler.sendEmptyMessageDelayed(MSG_HIDE_BTN,5000L);
    }

    @Override
    public void onMatchAll() {
        TWindowManager.showFloatBtnWindow(mContext,this, FloatWindowView.FloatBtnType.SELECT);
        mHandler.removeMessages(MSG_HIDE_BTN);
        mHandler.sendEmptyMessageDelayed(MSG_HIDE_BTN,5000L);
    }



    @Override
    public void onServiceConnected() {
        mContext = this;
        mDataOperator = new InputDataPresenter(this);
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onDestroy() {
        mDataOperator.onDestroy();
        mDataOperator = null;
        mContext = null;
        super.onDestroy();
    }

    /**
     * Processes an AccessibilityEvent, by traversing the View's tree and
     * putting together a message to speak to the user.
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        mDataOperator.onHandleAccessibilityEvent(event);
    }


}
