package com.android.ted.inputer.db;

import com.cocosw.favor.AllFavor;
import com.cocosw.favor.Default;

/**
 * Created by Ted on 2015/12/1.
 * SharePreData
 */
@AllFavor
public interface SharePreData {
    @Default("-1")
    int getFloatViewX();

    @Default("-1")
    int getFloatViewY();


    /*************************/
    void setFloatViewX(int floatViewX);
    void setFloatViewY(int floatViewY);
}
