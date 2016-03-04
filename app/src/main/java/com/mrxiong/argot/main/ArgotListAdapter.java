package com.mrxiong.argot.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mrxiong.argot.R;
import com.mrxiong.argot.model.Argot;
import com.mrxiong.argot.model.DynamicTag;
import com.mrxiong.argot.util.ImageSpanUtil;
import com.mrxiong.argot.util.TagManager;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ted on 2016/1/28.
 *
 * @ com.mrxiong.argot.main
 */
public class ArgotListAdapter extends RecyclerView.Adapter<ArgotListAdapter.ArgotViewHolder> {
  private Context mContext;
  private ArrayList<Argot> mArgotList;

  public ArgotListAdapter(Context context) {
    this.mContext = context;
    mArgotList = new ArrayList<>();
  }

  public void setArgotList(ArrayList<Argot> argotList) {
    this.mArgotList = argotList;
    notifyDataSetChanged();
  }

  @Override public int getItemCount() {
    return mArgotList.size();
  }

  @Override public ArgotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ArgotViewHolder(LayoutInflater.from(mContext).inflate( R.layout.argot_list_item_layout,parent,false));
  }

  @Override public void onBindViewHolder(ArgotViewHolder holder, int position) {
    Argot argot = mArgotList.get(position);
    holder.shortcutText.setText(argot.getShortcut());
    holder.argotText.setText(getPlainStr(argot.getPhrase()));
    holder.lastUsedText.setText(getTimestampStr(argot.getTimestamp()));
    holder.usageCountText.setText(String.valueOf(argot.getUsage_count()));
  }

  private String getTimestampStr(long timestamp) {
    return DateUtils.getRelativeTimeSpanString(timestamp, System.currentTimeMillis(), 60000L,
        DateUtils.FORMAT_ABBREV_ALL).toString();
  }

  private SpannableStringBuilder getPlainStr(String argotStr) {
    SpannableStringBuilder builder = new SpannableStringBuilder(argotStr);
    for (DynamicTag dynamicTag : new TagManager(mContext).getAllTagList()) {
      Matcher matcher =
          Pattern.compile(
              dynamicTag.getTag().replace("[", "\\[").replace("]", "\\]").replace("/", "\\/"))
              .matcher(builder);
      while (matcher.find()) {
        builder.setSpan(new ImageSpan(ImageSpanUtil.getTagView(mContext, dynamicTag)), matcher.start(),
            matcher.end(), Spanned.SPAN_POINT_MARK);
      }
    }
    return builder;
  }

  class ArgotViewHolder extends RecyclerView.ViewHolder {

    TextView shortcutText;
    TextView argotText;
    TextView lastUsedText;
    TextView usageCountText;

    public ArgotViewHolder(View view) {
      super(view);
      shortcutText = (TextView) view.findViewById(R.id.shortcutText);
      argotText = (TextView) view.findViewById(R.id.argotText);
      lastUsedText = (TextView) view.findViewById(R.id.lastUsedTextView);
      usageCountText = (TextView) view.findViewById(R.id.usageCountTextView);
    }
  }
}
