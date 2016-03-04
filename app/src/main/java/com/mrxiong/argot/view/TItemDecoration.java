package com.mrxiong.argot.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.mrxiong.argot.R;

/**
 * Created by Ted on 2016/1/28.
 *
 * @ com.mrxiong.argot.main
 */
public class TItemDecoration extends RecyclerView.ItemDecoration {
  //private static final int[] ATTRS = { android.R.attr.listDivider };
  private Drawable mDivider;
  private int mInsert;

  public TItemDecoration(Context context) {
    this.mDivider = context.getDrawable(R.drawable.lined_divider_bg);
    this.mInsert = context.getResources().getDimensionPixelSize(R.dimen.space_divider_inset);
  }

  @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
      RecyclerView.State state) {
    super.getItemOffsets(outRect, view, parent, state);
    outRect.set(this.mInsert, this.mInsert, this.mInsert, this.mInsert);
  }

  @Override public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    super.onDraw(c, parent, state);
    int left = parent.getPaddingLeft();
    int width = parent.getWidth();
    int right = parent.getPaddingRight();
    int count = parent.getChildCount();
    for (int i = 0; i < count; i++) {
      View view = parent.getChildAt(i);
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
      int bottom = view.getBottom();
      bottom = layoutParams.bottomMargin + bottom + this.mInsert;
      int i2 = this.mDivider.getIntrinsicHeight();
      this.mDivider.setBounds(left, bottom, width - right, i2 + bottom);
      this.mDivider.draw(c);
    }
  }
}
