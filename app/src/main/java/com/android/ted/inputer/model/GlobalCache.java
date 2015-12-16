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

import java.util.ArrayList;

/**
 * Created by Ted on 2015/12/2.
 */
public class GlobalCache {
    private static GlobalCache self = null;

    public static GlobalCache getInstance(){
        if(self == null){
            synchronized (GlobalCache.class) {
                if(self == null){
                    self = new GlobalCache();
                }
            }
        }
        return self;
    }
    //用户是否开启了无障碍功能
    private boolean isAccessibilitySupport = false;
    //是否绕行模式过滤App
    private boolean isAppFilterRound = false;
    private ArrayList<String> mAppPackageNameList = new ArrayList<>();

    public boolean isAppFilterRound() {
        return isAppFilterRound;
    }

    public void setAppFilterRound(boolean appFilterRound) {
        isAppFilterRound = appFilterRound;
    }

    public boolean isAccessibilitySupport() {
        return isAccessibilitySupport;
    }

    public void setAccessibilitySupport(boolean accessibilitySupport) {
        isAccessibilitySupport = accessibilitySupport;
    }

    public ArrayList<String> getAppPackageNameList() {
        if(mAppPackageNameList == null)mAppPackageNameList = new ArrayList<>();
        return mAppPackageNameList;
    }

    public void setAppPackageNameList(ArrayList<String> appPackageNameList) {
        mAppPackageNameList = appPackageNameList;
    }
}
