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
import android.view.accessibility.AccessibilityEvent;

import com.android.ted.inputer.window.FloatWindowView;
import com.android.ted.inputer.window.TWindowManager;

/**
 * Created by Ted on 2015/12/2.
 */
public class InputAccessibilityService extends AccessibilityService
        implements InputDataInterface,FloatWindowView.OnClickListener {
    private Context mContext;
    private InputDataOperator mDataOperator;

    @Override
    public void onClickBtn(FloatWindowView.FloatBtnType type) {
        mDataOperator.onExpand();
    }

    @Override
    public void onMatchPart() {
        if (TWindowManager.isWindowShowing()) return;
        TWindowManager.createFloatBtnWindow(mContext,this);
    }

    @Override
    public void onMatchAll() {

    }

    @Override
    public void onServiceConnected() {
        mContext = this;
        mDataOperator = new InputDataOperator(this);
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDataOperator = null;
    }

    /**
     * Processes an AccessibilityEvent, by traversing the View's tree and
     * putting together a message to speak to the user.
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        mDataOperator.onHandleAccessibilityEvent(event);

//        // Grab the parent of the view that fired the event.
//        AccessibilityNodeInfo rowNode = getListItemNodeInfo(source);
//        if (rowNode == null) {
//            return;
//        }
//
//        // Using this parent, get references to both child nodes, the label and the checkbox.
//        AccessibilityNodeInfo labelNode = rowNode.getChild(0);
//        if (labelNode == null) {
//            rowNode.recycle();
//            return;
//        }
//
//        AccessibilityNodeInfo completeNode = rowNode.getChild(1);
//        if (completeNode == null) {
//            rowNode.recycle();
//            return;
//        }
//
//        // Determine what the task is and whether or not it's complete, based on
//        // the text inside the label, and the state of the check-box.
//        if (rowNode.getChildCount() < 2 || !rowNode.getChild(1).isCheckable()) {
//            rowNode.recycle();
//            return;
//        }
//
//        CharSequence taskLabel = labelNode.getText();
//        final boolean isComplete = completeNode.isChecked();
//
//        String completeStr = null;
//        if (isComplete) {
//            completeStr = getString(R.string.task_complete);
//        } else {
//            completeStr = getString(R.string.task_not_complete);
//        }
//
//        String taskStr = getString(R.string.task_complete_template, taskLabel, completeStr);
//        StringBuilder utterance = new StringBuilder(taskStr);
//
//        // The custom ListView added extra context to the event by adding an
//        // AccessibilityRecord to it. Extract that from the event and read it.
//        final int records = event.getRecordCount();
//        for (int i = 0; i < records; i++) {
//            AccessibilityRecord record = event.getRecord(i);
//            CharSequence contentDescription = record.getContentDescription();
//            if (!TextUtils.isEmpty(contentDescription)) {
//                utterance.append(SEPARATOR);
//                utterance.append(contentDescription);
//            }
//        }
//
//        // Announce the utterance.
//        //mTts.speak(utterance.toString(), TextToSpeech.QUEUE_FLUSH, null);
//        Log.d(LOG_TAG, utterance.toString());
    }



//    private AccessibilityNodeInfo getListItemNodeInfo(AccessibilityNodeInfo source) {
//        AccessibilityNodeInfo current = source;
//        while (true) {
//            AccessibilityNodeInfo parent = current.getParent();
//            if (parent == null) {
//                return null;
//            }
//            if (TASK_LIST_VIEW_CLASS_NAME.equals(parent.getClassName())) {
//                return current;
//            }
//            // NOTE: Recycle the infos.
//            AccessibilityNodeInfo oldCurrent = current;
//            current = parent;
//            oldCurrent.recycle();
//        }
//    }



}
