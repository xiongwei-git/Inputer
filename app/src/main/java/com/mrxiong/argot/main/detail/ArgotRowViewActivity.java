/*
 * Copyright 2016 Ted xiong-wei@hotmail.com
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.mrxiong.argot.main.detail;

import android.graphics.drawable.Drawable;
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

import com.mrxiong.argot.R;
import com.mrxiong.argot.framework.BaseActivity;
import com.mrxiong.argot.model.Argot;
import com.mrxiong.argot.config.Constants;
import com.mrxiong.argot.model.DynamicTag;
import com.mrxiong.argot.util.ImageSpanUtil;
import com.mrxiong.argot.util.TagManager;
import com.mrxiong.argot.util.UiUtil;
import com.mrxiong.argot.view.ArgotEditText;
import com.mrxiong.argot.view.TClickableSpan;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ted on 2015/12/29.
 *
 * @ com.mrxiong.argot.main
 */
public class ArgotRowViewActivity extends BaseActivity
    implements View.OnClickListener, TClickableSpan.OnSpanClickListener,
    CompoundButton.OnCheckedChangeListener, ArgotRowViewMediator {

  public static final String KEY_ARGOT_FOR_UPDATE = "key_argot_for_update";

  private EditText mShortcutEditText;
  private Button mTagChooseButton;
  private TextView mTagChooseView;
  private ArgotEditText mPhraseEditText;
  private EditText mDesEditText;
  private Switch mImmediateSwitch;
  private Switch mInWordSwitch;
  private Drawable mErrorDrawable;

  private ArrayList<DynamicTag> mDynamicTagList;
  private Argot mArgot;

  private ArgotRowViewPresenter mPresenter;

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.addDynamicVal:
        showOrHideTagChooseView();
        break;
      default:
        break;
    }
  }

  @Override public void onClickSpan(View widget, DynamicTag dynamicTag) {
    mPhraseEditText.getEditableText()
        .insert(mPhraseEditText.getSelectionEnd(), dynamicTag.getTag());
  }

  @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    if (buttonView.getId() == R.id.expandImmediatelySwitch) {
      mArgot.setExpands_immediately(isChecked);
    } else if (buttonView.getId() == R.id.expandWithinWordSwitch) {
      mArgot.setExpands_within_word(isChecked);
    }
  }

  @Override public void onSaveArgotResult(int result) {
    switch (result) {
      case Constants.ARGOT_SAVE_RESULT_OK:
        setResult(RESULT_OK);
        finish();
        break;
      case Constants.ARGOT_SAVE_RESULT_ALL_NULL:
        finish();
        break;
      case Constants.ARGOT_SAVE_RESULT_PHRASE_NULL:
        mPhraseEditText.requestFocus();
        mPhraseEditText.setError(getText(R.string.no_phrase_error_message), mErrorDrawable);
        break;
      case Constants.ARGOT_SAVE_RESULT_SHORTCUT_NULL:
        mShortcutEditText.requestFocus();
        mShortcutEditText.setError(getText(R.string.no_phrase_error_message), mErrorDrawable);
        break;
      default:
        break;
    }
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.mErrorDrawable = getDrawable(R.drawable.ic_error_grey600_24dp);
    if (null != mErrorDrawable) {
      this.mErrorDrawable.setBounds(0, 0, this.mErrorDrawable.getIntrinsicWidth(),
          this.mErrorDrawable.getIntrinsicHeight());
    }

    setContentView(R.layout.activity_argot_row_view);
    final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    if (null != getSupportActionBar()) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
      getSupportActionBar().setTitle("");
    }
    mDynamicTagList = new TagManager(this).getAllTagList();
    mTagChooseView = (TextView) findViewById(R.id.anchorChooserTextView);
    mPhraseEditText = (ArgotEditText) findViewById(R.id.phrase);
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

  @Override protected void initPresenter() {
    mPresenter = new ArgotRowViewPresenter(this);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_add_argot, menu);
    menu.findItem(R.id.deletePhraseMenuItem)
        .setVisible(null != mArgot && !TextUtils.isEmpty(mArgot.getShortcut()));
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem menuItem) {
    if (menuItem.getItemId() == android.R.id.home) {
      finish();
    } else if (menuItem.getItemId() == R.id.savePhraseMenuItem) {
      onSaveArgot();
    }
    return super.onOptionsItemSelected(menuItem);
  }

  private void initBaseData() {
    if (getIntent().hasExtra(KEY_ARGOT_FOR_UPDATE)) {
      this.mArgot = (Argot) getIntent().getSerializableExtra(KEY_ARGOT_FOR_UPDATE);
      if (null != mArgot) {
        mShortcutEditText.setText(mArgot.getShortcut());
        mPhraseEditText.setText(mArgot.getPhrase());
        mDesEditText.setText(mArgot.getDescription());
        mImmediateSwitch.setChecked(mArgot.isExpands_immediately());
        mInWordSwitch.setChecked(mArgot.isExpands_within_word());
        return;
      }
    }
    this.mArgot = new Argot();
  }

  private void onSaveArgot() {
    this.mArgot.setShortcut(mShortcutEditText.getText().toString());
    this.mArgot.setPhrase(mPhraseEditText.getText().toString());
    this.mArgot.setDescription(mDesEditText.getText().toString());
    this.mArgot.setTimestamp(System.currentTimeMillis());
    this.mArgot.setUsage_count(0);
    this.mArgot.setExpands_immediately(mImmediateSwitch.isChecked());
    this.mArgot.setExpands_within_word(mInWordSwitch.isChecked());
    mPresenter.onSaveArgot(this.mArgot);
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
    for (DynamicTag dynamicTag : mDynamicTagList) {
      stringBuilder.append(getString(dynamicTag.getTagNameRes())).append(" ");
    }
    String tagName;
    int position = 0;
    for (DynamicTag dynamicTag : mDynamicTagList) {
      tagName = getString(dynamicTag.getTagNameRes());
      stringBuilder.setSpan(new ImageSpan(ImageSpanUtil.getTagView(this, dynamicTag)), position,
          position + tagName.length(), Spanned.SPAN_POINT_MARK);
      stringBuilder.setSpan(new TClickableSpan(dynamicTag, this), position,
          position + tagName.length(), Spanned.SPAN_POINT_MARK);
      position = position + tagName.length() + 1;
    }
    this.mTagChooseView.setTypeface(UiUtil.getTypeface(this));
    this.mTagChooseView.setText(stringBuilder);
    this.mTagChooseView.setMovementMethod(LinkMovementMethod.getInstance());
  }

  private class PhraseEditTextWatcher implements TextWatcher {
    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override public void afterTextChanged(Editable editable) {
      for (DynamicTag dynamicTag : mDynamicTagList) {
        Matcher matcher = Pattern.compile(
            dynamicTag.getTag().replace("[", "\\[").replace("]", "\\]").replace("/", "\\/"))
            .matcher(editable.toString());
        while (matcher.find()) {
          mPhraseEditText.getEditableText()
              .setSpan(
                  new ImageSpan(ImageSpanUtil.getTagView(ArgotRowViewActivity.this, dynamicTag)),
                  matcher.start(), matcher.end(), Spanned.SPAN_POINT_MARK);
        }
      }
    }
  }
}
