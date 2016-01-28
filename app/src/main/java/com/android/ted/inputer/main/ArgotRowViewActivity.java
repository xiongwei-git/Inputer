package com.android.ted.inputer.main;

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

import com.android.ted.inputer.R;
import com.android.ted.inputer.base.BaseActivity;
import com.android.ted.inputer.model.Argot;
import com.android.ted.inputer.model.Constants;
import com.android.ted.inputer.model.Tag;
import com.android.ted.inputer.util.CommonUtil;
import com.android.ted.inputer.util.ImageSpanUtil;
import com.android.ted.inputer.util.TagManager;
import com.android.ted.inputer.view.ArgotEditText;
import com.android.ted.inputer.view.TClickableSpan;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ted on 2015/12/29.
 *
 * @ com.android.ted.inputer.main
 */
public class ArgotRowViewActivity extends BaseActivity
    implements View.OnClickListener, TClickableSpan.OnSpanClickListener,
    CompoundButton.OnCheckedChangeListener, ArgotRowViewMediator {
  public static final String KEY_ARGOT_FOR_UPDATE = "PhraseRowViewActivity_KEY_ARGOT_FOR_UPDATE";

  private EditText mShortcutEditText;
  private Button mTagChooseButton;
  private TextView mTagChooseView;
  private ArgotEditText mPhraseEditText;
  private EditText mDesEditText;
  private Switch mImmediateSwitch;
  private Switch mInWordSwitch;
  private Drawable mErrorDrawable;

  private ArrayList<Tag> mTagList;
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

  @Override public void onClickSpan(View widget, Tag tag) {
    mPhraseEditText.getEditableText().insert(mPhraseEditText.getSelectionEnd(), tag.getTag());
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
    mTagList = new TagManager(this).getAllTagList();
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
    for (Tag tag : mTagList) {
      stringBuilder.append(getString(tag.getTagNameRes())).append(" ");
    }
    String tagName;
    int position = 0;
    for (Tag tag : mTagList) {
      tagName = getString(tag.getTagNameRes());
      stringBuilder.setSpan(new ImageSpan(ImageSpanUtil.getTagView(this, tag)), position,
          position + tagName.length(), Spanned.SPAN_POINT_MARK);
      stringBuilder.setSpan(new TClickableSpan(tag, this), position, position + tagName.length(),
          Spanned.SPAN_POINT_MARK);
      position = position + tagName.length() + 1;
    }
    this.mTagChooseView.setTypeface(CommonUtil.getTypeface(this));
    this.mTagChooseView.setText(stringBuilder);
    this.mTagChooseView.setMovementMethod(LinkMovementMethod.getInstance());
  }

  private class PhraseEditTextWatcher implements TextWatcher {
    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override public void afterTextChanged(Editable editable) {
      for (Tag tag : mTagList) {
        Matcher matcher = Pattern.compile(
            tag.getTag().replace("[", "\\[").replace("]", "\\]").replace("/", "\\/"))
            .matcher(editable.toString());
        while (matcher.find()) {
          mPhraseEditText.getEditableText()
              .setSpan(new ImageSpan(ImageSpanUtil.getTagView(ArgotRowViewActivity.this, tag)),
                  matcher.start(), matcher.end(), Spanned.SPAN_POINT_MARK);
        }
      }
    }
  }
}
