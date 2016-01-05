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

package com.android.ted.inputer.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.ted.inputer.R;
import com.android.ted.inputer.base.BaseActivity;
import com.android.ted.inputer.db.SharePreData;
import com.android.ted.inputer.debug.DebugDbActivity;
import com.android.ted.inputer.model.Constants;
import com.cocosw.favor.FavorAdapter;

public class MainActivity extends BaseActivity implements MainMediator, View.OnClickListener {

    private SharePreData mSharePreData;
    private RelativeLayout mSwitchBar;
    private MainPresenter mMainPresenter;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_bar:
                boolean isChecked = !mSharePreData.getAppStatus();
                updateSwitchBarData(isChecked);
                break;
            default:
                break;
        }
    }

    private DialogInterface.OnClickListener mDrawOverlaysDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mSharePreData.setCheckDrawOverlays(true);
            if (which == -1)
                mMainPresenter.setDrawOverlays();
            else
                updateAccessibilitySwitchBar();
        }
    };

    private DialogInterface.OnClickListener mAccessibilityDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mSharePreData.setCheckAccessibility(true);
            if (which == -1)
                mMainPresenter.setAccessibilityPermission();
            else
                updateAccessibilitySwitchBar();
        }
    };

    private CompoundButton.OnCheckedChangeListener mSwitchOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            updateSwitchBarData(isChecked);
        }
    };

    @Override
    public void showDrawOverlaysDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.system_alter_permission_rationale_dialog_title)
                .setMessage(R.string.system_alert_permission_rationale_dialog_text)
                .setPositiveButton(R.string.open_settings, mDrawOverlaysDialogListener)
                .setNegativeButton(R.string.cancel, mDrawOverlaysDialogListener)
                .setCancelable(false)
                .show();
    }

    @Override
    public void showAccessibilityDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.enable_accessibility_dialog_title)
                .setMessage(R.string.enable_accessibility_service_message)
                .setPositiveButton(R.string.open_settings, mAccessibilityDialogListener)
                .setNegativeButton(R.string.cancel, mAccessibilityDialogListener)
                .setCancelable(false)
                .show();
    }

    @Override
    protected void initPresenter() {
        mMainPresenter = new MainPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharePreData = new FavorAdapter.Builder(this).build().create(SharePreData.class);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PhraseRowViewActivity.class));
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });

        mSwitchBar = (RelativeLayout) findViewById(R.id.switch_bar);
        mSwitchBar.setOnClickListener(this);

        final Snackbar snackbar = Snackbar.make(fab, "click the red button,and enjoy it", Snackbar
                .LENGTH_LONG);
        snackbar.show();
        snackbar.setAction("I know it", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.debug_enter){
            startActivity(new Intent(MainActivity.this, DebugDbActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mSharePreData.isCheckDrawOverlays() && !mMainPresenter.canDrawOverlays()) {
            mSharePreData.setCheckDrawOverlays(true);
            return;
        }
        if (!mSharePreData.isCheckAccessibility() && !mMainPresenter.isSupportAccessibility()) {
            showAccessibilityDialog();
            return;
        }
        updateAccessibilitySwitchBar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.OVERLAY_PERMISSION_REQ_CODE) {
            if (!mSharePreData.isCheckDrawOverlays()) {
                mMainPresenter.canDrawOverlays();
            }
        } else if (requestCode == Constants.ACCESSIBILITY_PERMISSION_REQ_CODE) {
            updateAccessibilitySwitchBar();
        }
    }

    /***
     * 刷新无障碍权限的切换开关状态
     */
    private void updateAccessibilitySwitchBar() {
        boolean appStatus = mSharePreData.getAppStatus();
        boolean hasPermission = mMainPresenter.isSupportAccessibility();
        ((TextView) mSwitchBar.findViewById(R.id.status_text)).setText(appStatus && hasPermission ? "开" : "关");
        mSwitchBar.findViewById(R.id.accessibility_service_disabled_warning).setVisibility(hasPermission ? View.GONE : View.VISIBLE);
        ((Switch) mSwitchBar.findViewById(R.id.status_switch)).setOnCheckedChangeListener(mSwitchOnCheckedChangeListener);
        ((Switch) mSwitchBar.findViewById(R.id.status_switch)).setChecked(appStatus && hasPermission);
    }

    /***
     * 更新switch bar 对应的数据
     *
     * @param isChecked 当前switch bar 是否被选中
     */
    private void updateSwitchBarData(boolean isChecked) {
        if (isChecked) {
            mSharePreData.setAppStatus(true);
            if (mMainPresenter.isSupportAccessibility()) {
                updateAccessibilitySwitchBar();
            } else {
                showAccessibilityDialog();
            }
        } else {
            mSharePreData.setAppStatus(false);
            updateAccessibilitySwitchBar();
        }
    }
}
