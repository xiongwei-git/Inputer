package com.android.ted.inputer.main;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.android.ted.inputer.R;
import com.android.ted.inputer.base.BaseActivity;
import com.android.ted.inputer.model.ArgotRecord;
import com.android.ted.inputer.model.Tag;
import com.android.ted.inputer.util.CommonUtil;
import com.android.ted.inputer.util.ImageSpanUtil;
import com.android.ted.inputer.util.TagManager;
import com.android.ted.inputer.view.PhraseEditText;
import com.android.ted.inputer.view.TClickableSpan;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ted on 2015/12/29.
 *
 * @ com.android.ted.inputer.main
 */
public class PhraseRowViewActivity extends BaseActivity implements TClickableSpan.OnSpanClickListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    public static final String KEY_ARGOT_FOR_UPDATE = "PhraseRowViewActivity_KEY_ARGOT_FOR_UPDATE";

    private EditText mShortcutEditText;
    private Button mTagChooseButton;
    private TextView mTagChooseView;
    private PhraseEditText mPhraseEditText;
    private EditText mDesEditText;
    private Switch mImmediateSwitch;
    private Switch mInWordSwitch;

    private TagManager mTagManager;
    private ArrayList<Tag> mTagList;
    private ArgotRecord mArgotRecord;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addDynamicVal:
                showOrHideTagChooseView();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId() == R.id.expandImmediatelySwitch){
            mArgotRecord.setExpands_immediately(isChecked);
        }else if(buttonView.getId() == R.id.expandWithinWordSwitch){
            mArgotRecord.setExpands_within_word(isChecked);
        }
    }

    @Override
    public void onClickSpan(View widget, Tag tag) {
        mPhraseEditText.getEditableText().insert(mPhraseEditText.getSelectionEnd(), tag.getTag());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrase_row_view);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }
        this.mTagManager = new TagManager(this);
        mTagList = mTagManager.getAllTagList();
        mTagChooseView = (TextView) findViewById(R.id.anchorChooserTextView);
        mPhraseEditText = (PhraseEditText) findViewById(R.id.phrase);
        mTagChooseButton = (Button) findViewById(R.id.addDynamicVal);
        mShortcutEditText = (EditText) findViewById(R.id.shortcut);
        mDesEditText = (EditText) findViewById(R.id.description);
        mImmediateSwitch = (Switch) findViewById(R.id.expandImmediatelySwitch);
        mInWordSwitch = (Switch) findViewById(R.id.expandWithinWordSwitch);

        mPhraseEditText.addTextChangedListener(new PhraseEditTextWatcher());
        mTagChooseButton.setOnClickListener(this);
        mInWordSwitch.setOnCheckedChangeListener(this);
        mImmediateSwitch.setOnCheckedChangeListener(this);

        initBaseData();
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_phrase, menu);
        menu.findItem(R.id.deletePhraseMenuItem).setVisible(
                null != mArgotRecord && !TextUtils.isEmpty(mArgotRecord.getShortcut()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void initBaseData(){
        if(getIntent().hasExtra(KEY_ARGOT_FOR_UPDATE)){
            this.mArgotRecord = (ArgotRecord)getIntent().getSerializableExtra(KEY_ARGOT_FOR_UPDATE);
            if(null != mArgotRecord){
                mShortcutEditText.setText(mArgotRecord.getShortcut());
                mPhraseEditText.setText(mArgotRecord.getPhrase());
                mDesEditText.setText(mArgotRecord.getDescription());
                mImmediateSwitch.setChecked(mArgotRecord.isExpands_immediately());
                mInWordSwitch.setChecked(mArgotRecord.isExpands_within_word());
                return;
            }
        }
        this.mArgotRecord = new ArgotRecord();
    }

    private void showOrHideTagChooseView() {
        if (mTagChooseView.getVisibility() == View.VISIBLE) {
            mTagChooseView.setVisibility(View.GONE);
            mTagChooseButton.setText(getString(R.string.show_dynamic_values));
            return;
        }
        mTagChooseButton.setText(getString(R.string.hide_dynamic_values));
        mTagChooseView.setVisibility(View.VISIBLE);
        if (mTagChooseView.getText().length() > 0) return;
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        for (Tag tag : mTagList) {
            stringBuilder.append(getString(tag.getTagNameRes())).append(" ");
        }
        String tagName;

        int position = 0;
        for (Tag tag : mTagList) {
            tagName = getString(tag.getTagNameRes());
            stringBuilder.setSpan(new ImageSpan(ImageSpanUtil.getTagView(this, tag)), position, position + tagName.length(), Spanned.SPAN_POINT_MARK);
            stringBuilder.setSpan(new TClickableSpan(tag, this), position, position + tagName.length(), Spanned.SPAN_POINT_MARK);
            position = position + tagName.length() + 1;
        }
        this.mTagChooseView.setTypeface(CommonUtil.getTypeface(this));
        this.mTagChooseView.setText(stringBuilder);
        this.mTagChooseView.setMovementMethod(LinkMovementMethod.getInstance());
    }


    private class PhraseEditTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            for (Tag tag : mTagList) {
                Matcher matcher = Pattern.compile(tag.getTag().replace("[", "\\[").replace("]", "\\]").replace("/", "\\/")).matcher(editable.toString());
                while (matcher.find()) {
                    mPhraseEditText.getEditableText().setSpan(new ImageSpan(ImageSpanUtil.getTagView(PhraseRowViewActivity.this, tag)), matcher.start(), matcher.end(), Spanned.SPAN_POINT_MARK);
                }
            }
        }
    }
}
