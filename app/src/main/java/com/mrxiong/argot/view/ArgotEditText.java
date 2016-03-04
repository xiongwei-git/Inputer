package com.mrxiong.argot.view;

import android.content.Context;
import android.text.Editable;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

/**
 * Created by Ted on 2015/12/28.
 *
 * @ com.mrxiong.argot.view
 */
public class ArgotEditText extends EditText {
    public ArgotEditText(Context context) {
        super(context);
    }

    public ArgotEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ArgotEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ArgotEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private boolean deleteSurroundingText() {
        Editable editable = getText();
        ImageSpan[] arrayOfImageSpan = editable.getSpans(0, editable.length(), ImageSpan.class);
        for (int i = 0; i < arrayOfImageSpan.length; i++) {
            ImageSpan imageSpan = arrayOfImageSpan[i];
            if (getSelectionEnd() == editable.getSpanEnd(imageSpan)) {
                editable.delete(editable.getSpanStart(imageSpan), editable.getSpanEnd(imageSpan));
                return true;
            }
        }
        return false;
    }

    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        editorInfo.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI;
        return new TInputConnectionWrapper(this, super.onCreateInputConnection(editorInfo), true);
    }

    private class TInputConnectionWrapper extends InputConnectionWrapper {
        private ArgotEditText mEditText;

        public TInputConnectionWrapper(ArgotEditText phraseEditText, InputConnection inputConnection, boolean mutable) {
            super(inputConnection, mutable);
            mEditText = phraseEditText;
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            return (mEditText.deleteSurroundingText() || super.deleteSurroundingText(beforeLength, afterLength));
        }
    }
}
