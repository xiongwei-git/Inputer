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

package com.mrxiong.argot.main;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;

import com.mrxiong.argot.config.Constants;
import com.mrxiong.argot.util.VersionUtil;

import java.util.List;

/**
 * Created by Ted on 2015/12/17.
 *
 * @ com.mrxiong.argot.main
 */
public class MainPresenter {
    private MainMediator mMainMediator;

    public MainPresenter(MainMediator mainInterface) {
        this.mMainMediator = mainInterface;
    }

    /***
     * 检测是否支持windows显示
     */
    public boolean canDrawOverlays() {
        if (VersionUtil.isM()) {
            MainActivity mainActivity = (MainActivity) mMainMediator;
            if (!Settings.canDrawOverlays(mainActivity)) {
                mMainMediator.showDrawOverlaysDialog();
                return false;
            }
        }
        return true;
    }

    /***
     * 设置浮窗权限
     */
    public void setDrawOverlays() {
        if (VersionUtil.isM()) {
            MainActivity mainActivity = (MainActivity) mMainMediator;
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + mainActivity.getPackageName()));
            mainActivity.startActivityForResult(intent, Constants.REQ_CODE_OVERLAY_PERMISSION);
        }
    }

    public void setAccessibilityPermission() {
        MainActivity mainActivity = (MainActivity) mMainMediator;
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        mainActivity.startActivityForResult(intent, Constants.REQ_CODE_ACCESSIBILITY_PERMISSION);
    }

    public boolean isSupportAccessibility() {
        MainActivity mainActivity = (MainActivity) mMainMediator;
        boolean result = false;
        AccessibilityManager accessibilityManager =
                (AccessibilityManager) mainActivity.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> list =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        for (AccessibilityServiceInfo info : list) {
            if (info.getId().equals(mainActivity.getPackageName() + Constants.ACCESSIBILITY_SERVICE_NAME))
                result = true;
        }
        return result;
    }


}
