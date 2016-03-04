package com.mrxiong.argot.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mrxiong.argot.R;
import com.mrxiong.argot.model.DynamicTag;

/**
 * Created by Ted on 2015/12/29.
 *
 * @ com.mrxiong.argot.util
 */
public class ImageSpanUtil {

  @SuppressWarnings("deprecation")
  public static BitmapDrawable getTagView(Context context, DynamicTag dynamicTag) {
    RelativeLayout relativeLayout =
        (RelativeLayout) View.inflate(context, R.layout.spanned_anchor_layout, null);
    TextView textView = (TextView) relativeLayout.findViewById(R.id.anchorNameTextView);
    textView.setText(context.getString(dynamicTag.getTagNameRes()));
    Drawable drawable = textView.getBackground();
    drawable.setColorFilter(context.getResources().getColor(dynamicTag.getTagBgRes()),
        PorterDuff.Mode.SRC);
    textView.setBackground(drawable);
    int i = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    relativeLayout.measure(i, i);
    relativeLayout.layout(0, 0, relativeLayout.getMeasuredWidth(),
        relativeLayout.getMeasuredHeight());
    Canvas canvas = new Canvas(
        Bitmap.createBitmap(textView.getWidth(), textView.getHeight(), Bitmap.Config.ARGB_8888));
    canvas.translate(relativeLayout.getX(), relativeLayout.getBottom());
    relativeLayout.draw(canvas);
    relativeLayout.setDrawingCacheEnabled(true);
    relativeLayout.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    Bitmap bitmap = relativeLayout.getDrawingCache().copy(Bitmap.Config.ARGB_8888, true);
    relativeLayout.destroyDrawingCache();
    BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), bitmap);
    bitmapDrawable.setBounds(0, 0, bitmapDrawable.getIntrinsicWidth(), bitmapDrawable.getIntrinsicHeight());
    return bitmapDrawable;
  }

}
