package com.android.ted.inputer.db;

import com.cocosw.favor.AllFavor;
import com.cocosw.favor.Default;

/**
 * Created by Ted on 2015/12/1.
 * SharePreData
 */
@AllFavor
public interface SharePreData {
    @Default("true")
    boolean getAppStatus();///主功能是否打开
    @Default("false")
    boolean isCheckDrawOverlays();//打开窗口显示的权限提示
    @Default("false")
    boolean isCheckAccessibility();//是否主动检查了无障碍权限设置
    @Default("-1")
    int getFloatViewX();//上次圆形浮窗的位置x
    @Default("-1")
    int getFloatViewY();//上次圆形浮窗的位置y




    /*************************/
    void setCheckDrawOverlays(boolean status);
    void setCheckAccessibility(boolean status);
    void setAppStatus(boolean status);
    void setFloatViewX(int floatViewX);
    void setFloatViewY(int floatViewY);
}
