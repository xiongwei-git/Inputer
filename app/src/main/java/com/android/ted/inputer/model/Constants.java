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

package com.android.ted.inputer.model;

/**
 * Created by Ted on 2015/12/16.
 * Constants
 */
public class Constants {
    public static final int ARGOT_SAVE_RESULT_OK = 0;//保存成功
    public static final int ARGOT_SAVE_RESULT_ALL_NULL = -1;//全部数据为空
    public static final int ARGOT_SAVE_RESULT_SHORTCUT_NULL = -2;//暗语文本为空
    public static final int ARGOT_SAVE_RESULT_PHRASE_NULL = -3;//明语文本为空
    public static final int ARGOT_SAVE_RESULT_ILLEGAL_CHA = -4;//不合法字符
    public static final int ARGOT_SAVE_RESULT_LINE_FEED = -5;//换行

    public static int OVERLAY_PERMISSION_REQ_CODE = 0x0001;
    public static int ACCESSIBILITY_PERMISSION_REQ_CODE = 0x0002;

    public static String ACCESSIBILITY_SERVICE_NAME = "/.service.InputAccessibilityService";
}
