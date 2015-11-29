/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.android.ted.inputer.service;


import android.accessibilityservice.AccessibilityService;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * This class demonstrates how an accessibility service can query
 * window content to improve the feedback given to the user.
 */
public class TaskBackService extends AccessibilityService {

    /**
     * Tag for logging.
     */
    private static final String LOG_TAG = "TaskBackService";

    /**
     * Comma separator.
     */
    private static final String SEPARATOR = ", ";

    /**
     * The class name of TaskListView - for simplicity we speak only its items.
     */
    private static final String TASK_LIST_VIEW_CLASS_NAME =
            "android.widget.ListView";


    /**
     * {@inheritDoc}
     */
    @Override
    public void onServiceConnected() {
        // Initializes the Text-To-Speech engine as soon as the service is connected.
        //mTts = new TextToSpeech(getApplicationContext(), this);
    }

    /**
     * Processes an AccessibilityEvent, by traversing the View's tree and
     * putting together a message to speak to the user.
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
//        Log.e(LOG_TAG, "Text-To-Speech engine not ready.  Bailing out.");
//        if (!mTextToSpeechInitialized) {
//            Log.e(LOG_TAG, "Text-To-Speech engine not ready.  Bailing out.");
//            return;
//        }

        // This AccessibilityNodeInfo represents the view that fired the
        // AccessibilityEvent. The following code will use it to traverse the
        // view hierarchy, using this node as a starting point.
        //
        // NOTE: Every method that returns an AccessibilityNodeInfo may return null,
        // because the explored window is in another process and the
        // corresponding View might be gone by the time your request reaches the
        // view hierarchy.

        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                readTextChangeEvent(event);
                break;
            default:
                break;
        }


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

    private void readTextChangeEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo source = event.getSource();
        if (source == null) {
            return;
        }

        AccessibilityNodeInfo editView = getGetEditTextNodeInfo(source);
        if (null != editView && null != editView.getText()) {
            String text = editView.getText().toString();
            Log.e(LOG_TAG, text);
            if (text.equalsIgnoreCase("123")) {

                Bundle arguments = new Bundle();
                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                        "android");
                editView.performAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_TEXT.getId(), arguments);
            }
        }
    }

    private AccessibilityNodeInfo getGetEditTextNodeInfo(AccessibilityNodeInfo source) {
        AccessibilityNodeInfo current = source;
        while (true) {

            if ("android.widget.EditText".equals(current.getClassName())) {
                return current;
            }
            return null;
        }
    }

    private AccessibilityNodeInfo getListItemNodeInfo(AccessibilityNodeInfo source) {
        AccessibilityNodeInfo current = source;
        while (true) {
            AccessibilityNodeInfo parent = current.getParent();
            if (parent == null) {
                return null;
            }
            if (TASK_LIST_VIEW_CLASS_NAME.equals(parent.getClassName())) {
                return current;
            }
            // NOTE: Recycle the infos.
            AccessibilityNodeInfo oldCurrent = current;
            current = parent;
            oldCurrent.recycle();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInterrupt() {
        /* do nothing */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
