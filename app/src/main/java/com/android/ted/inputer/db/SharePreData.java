package com.android.ted.inputer.db;

import com.cocosw.favor.AllFavor;
import com.cocosw.favor.Default;

/**
 * Created by Ted on 2015/12/1.
 * SharePreData
 */
@AllFavor
public interface SharePreData {
    @Default("false")
    boolean getAppStatus();///主功能是否打开
    @Default("-1")
    int getFloatViewX();//上次浮窗的位置x
    @Default("-1")
    int getFloatViewY();//上次浮窗的位置y




    /*************************/
    void setAppStatus(boolean status);
    void setFloatViewX(int floatViewX);
    void setFloatViewY(int floatViewY);
}
