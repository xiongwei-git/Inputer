package com.android.ted.inputer.window;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.android.ted.inputer.R;
import com.android.ted.inputer.db.SharePreData;
import com.android.ted.inputer.util.UiUtil;
import com.cocosw.favor.FavorAdapter;

/**
 * Created by Ted on 2015/12/1.
 * 圆形提示视图
 */
public class TWindowManager {
    private static FloatWindowView mFloatBtnView;
    private static LayoutParams mFloatBtnViewParams;
    private static WindowManager mWindowManager;

    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void createFloatBtnWindow(Context context, FloatWindowView.OnClickListener onClickListener) {
        if (mFloatBtnView == null) {
            mFloatBtnView = new FloatWindowView(context);
            mFloatBtnView.initFloatBtnView(FloatWindowView.FloatBtnType.SUGGEST,onClickListener);
            if (mFloatBtnViewParams == null)
                mFloatBtnViewParams = makeFloatBtnParams(context);
            mFloatBtnView.setParams(mFloatBtnViewParams);
            getWindowManager(context).addView(mFloatBtnView, mFloatBtnViewParams);
        }
    }

    private static LayoutParams makeFloatBtnParams(Context context) {
        SharePreData sharePreData = new FavorAdapter.Builder(context).build().create(SharePreData.class);
        int lastX = sharePreData.getFloatViewY();
        int lastY = sharePreData.getFloatViewY();
        if (lastX < 0 || lastY < 0) {
            int screenWidth = UiUtil.getScreenWidth(context);
            int screenHeight = UiUtil.getScreenHeight(context);
            lastX = screenWidth / 2 - context.getResources().getDimensionPixelSize(R.dimen.fab_size_normal) / 2;
            lastY = screenHeight / 2 - context.getResources().getDimensionPixelSize(R.dimen.fab_size_normal) / 2;
        }
        LayoutParams layoutParams = new LayoutParams();
        layoutParams.type = LayoutParams.TYPE_PHONE;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                | LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.width = LayoutParams.WRAP_CONTENT;
        layoutParams.height = LayoutParams.WRAP_CONTENT;
        layoutParams.alpha = 0.8f;
        layoutParams.x = lastX;
        layoutParams.y = lastY;
        layoutParams.windowAnimations=android.R.style.Animation_InputMethod;
        return layoutParams;
    }

    public static void removeFloatBtn(Context context) {
        if (mFloatBtnView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(mFloatBtnView);
            mFloatBtnView = null;
        }
    }

    /**
     * 是否有悬浮窗显示在屏幕上。
     *
     * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
     */
    public static boolean isWindowShowing() {
        return mFloatBtnView != null;
    }


    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }
}
