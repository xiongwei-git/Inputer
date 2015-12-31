package com.android.ted.inputer.view;

import android.text.style.ClickableSpan;
import android.view.View;

import com.android.ted.inputer.model.Tag;

/**
 * Created by Ted on 2015/12/29.
 *
 * @ com.android.ted.inputer.view
 */
public class TClickableSpan extends ClickableSpan {
    private OnSpanClickListener mOnSpanClickListener;
    private Tag mTag;

    public TClickableSpan(Tag tag,OnSpanClickListener listener) {
        super();
        this.mTag = tag;
        this.mOnSpanClickListener = listener;
    }

    @Override
    public void onClick(View widget) {
        if(null != mOnSpanClickListener)
            mOnSpanClickListener.onClickSpan(widget,mTag);
    }

    public interface OnSpanClickListener {
        void onClickSpan(View widget, Tag tag);
    }
}
