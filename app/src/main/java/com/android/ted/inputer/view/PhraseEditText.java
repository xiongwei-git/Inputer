package com.android.ted.inputer.view;

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
 * @ com.android.ted.inputer.view
 */
public class PhraseEditText extends EditText {
    public PhraseEditText(Context context) {
        super(context);
    }

    public PhraseEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PhraseEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PhraseEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        private PhraseEditText mEditText;

        public TInputConnectionWrapper(PhraseEditText phraseEditText, InputConnection inputConnection, boolean mutable) {
            super(inputConnection, mutable);
            mEditText = phraseEditText;
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            return (mEditText.deleteSurroundingText() || super.deleteSurroundingText(beforeLength, afterLength));
        }
    }
}
