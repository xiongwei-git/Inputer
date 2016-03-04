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

package com.mrxiong.argot.util;

import com.mrxiong.argot.model.Argot;

/**
 * Created by Ted on 2015/12/15.
 * ArgotUtil
 */
public class ArgotUtil {

    /***
     * 转换含有暗语的字符串为明文
     *
     * @param srcStr      含有暗语的文字
     * @param selection   光标位置
     * @param argot 暗语对象
     * @return 明文
     */
    public String getPlanTxt(String srcStr, int selection, Argot argot) {
        String[] result = SplitText(srcStr, selection, argot);
        return result[0] + argot.getPhrase() + result[2];
    }

    /***
     * 根据selection和ArgotRecord 把目标字符串拆开为三段文字，分别为 前段，被替换段，后段
     *
     * @param resText     目标字符串
     * @param selection   selection
     * @param argot argotRecord
     * @return 数组
     */
    public static String[] SplitText(String resText, int selection, Argot argot) {
        String[] result = new String[3];
        if (null != argot && selection > 0) {
            String argotWord = argot.getShortcut();
            String selectStr = "";
            int selectNum = 0;
            for (int i = 1; i < selection + 1; i++) {
                selectNum++;
                selectStr = resText.substring(selection - i, selection);
                if (!argotWord.contains(selectStr)) {
                    selectStr = selectStr.substring(1, selectStr.length());
                    selectNum--;
                    break;
                }
            }
            if (selection > selectNum) {
                result[0] = resText.substring(0, selection - selectNum);
            } else result[0] = "";
            result[1] = selectStr;
            result[2] = resText.substring(selection);
        }
        return result;
    }

}
