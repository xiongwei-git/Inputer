package com.android.ted.inputer.window;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.android.ted.inputer.R;
import com.android.ted.inputer.db.SharePreData;
import com.android.ted.inputer.util.UiUtil;
import com.android.ted.inputer.view.FloatWindowBigView;
import com.cocosw.favor.FavorAdapter;

/**
 * Created by Ted on 2015/12/1.
 * 圆形提示视图
 */
public class TWindowManager {
    private static FloatWindowView mFloatBtnView;
    private static FloatWindowBigView bigWindow;
    private static LayoutParams mFloatBtnViewParams;
    private static LayoutParams bigWindowParams;
    private static WindowManager mWindowManager;

    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void createFloatBtnWindow(Context context) {
        if (mFloatBtnView == null) {
            mFloatBtnView = new FloatWindowView(context);
            mFloatBtnView.initFloatBtnView(FloatWindowView.FloatBtnType.SUGGEST);
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
     * 创建一个大悬浮窗。位置为屏幕正中间。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void createBigWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (bigWindow == null) {
            bigWindow = new FloatWindowBigView(context);
            if (bigWindowParams == null) {
                bigWindowParams = new LayoutParams();
                bigWindowParams.x = screenWidth / 2 - FloatWindowBigView.viewWidth / 2;
                bigWindowParams.y = screenHeight / 2 - FloatWindowBigView.viewHeight / 2;
                bigWindowParams.type = LayoutParams.TYPE_PHONE;
                bigWindowParams.format = PixelFormat.RGBA_8888;
                bigWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                bigWindowParams.width = FloatWindowBigView.viewWidth;
                bigWindowParams.height = FloatWindowBigView.viewHeight;
            }
            windowManager.addView(bigWindow, bigWindowParams);
        }
    }

    /**
     * 将大悬浮窗从屏幕上移除。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void removeBigWindow(Context context) {
        if (bigWindow != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(bigWindow);
            bigWindow = null;
        }
    }

    /**
     * 更新小悬浮窗的TextView上的数据，显示内存使用的百分比。
     *
     * @param context 可传入应用程序上下文。
     */
    public static void updateUsedPercent(Context context) {
        if (mFloatBtnView != null) {
//			TextView percentView = (TextView) mFloatBtnView.findViewById(R.id.percent);
//			percentView.setText("xiongwei");
        }
    }

    /**
     * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。
     *
     * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
     */
    public static boolean isWindowShowing() {
        return mFloatBtnView != null || bigWindow != null;
    }


    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }
}
