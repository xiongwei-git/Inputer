package com.mrxiong.argot.view;

import android.text.style.ClickableSpan;
import android.view.View;

import com.mrxiong.argot.model.DynamicTag;

/**
 * Created by Ted on 2015/12/29.
 *
 * @ com.mrxiong.argot.view
 */
public class TClickableSpan extends ClickableSpan {
    private OnSpanClickListener mOnSpanClickListener;
    private DynamicTag mDynamicTag;

    public TClickableSpan(DynamicTag dynamicTag,OnSpanClickListener listener) {
        super();
        this.mDynamicTag = dynamicTag;
        this.mOnSpanClickListener = listener;
    }

    @Override
    public void onClick(View widget) {
        if(null != mOnSpanClickListener)
            mOnSpanClickListener.onClickSpan(widget, mDynamicTag);
    }

    public interface OnSpanClickListener {
        void onClickSpan(View widget, DynamicTag dynamicTag);
    }
}
