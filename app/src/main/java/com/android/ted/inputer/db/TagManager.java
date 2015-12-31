package com.android.ted.inputer.db;

import android.content.ClipboardManager;
import android.content.Context;

import com.android.ted.inputer.R;
import com.android.ted.inputer.model.Tag;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Ted on 2015/12/28.
 *
 * @ com.android.ted.inputer.util
 */
public class TagManager {

    public static final String[] tagArray = {"[day/n]", "[day/sn]", "[day/fn]", "[month/n]", "[month/sn]", "[month/fn]", "[year/ns]", "[year/nf]", "[hour/12]", "[hour/24]", "[am/pm]", "[minute]", "[second]", "[clipboard]"};

    public static final int[] tagNameRes = {
            R.string.day_numeric_anchor,
            R.string.day_name_short_anchor,
            R.string.day_name_full_anchor,
            R.string.month_numeric_anchor,
            R.string.month_name_short_anchor,
            R.string.month_name_full_anchor,
            R.string.year_short_anchor,
            R.string.year_full_anchor,
            R.string.hour_12_anchor,
            R.string.hour_24_anchor,
            R.string.am_pm_anchor,
            R.string.minute_anchor,
            R.string.second_anchor,
            R.string.clipboard_anchor
    };
    public static final int[] tagBgRes = {
            R.color.day_anchor_color, R.color.day_anchor_color, R.color.day_anchor_color,
            R.color.month_anchor_color, R.color.month_anchor_color, R.color.month_anchor_color,
            R.color.year_anchor_color, R.color.year_anchor_color,
            R.color.hour_anchor_color, R.color.hour_anchor_color, R.color.hour_anchor_color,
            R.color.minute_anchor_color,
            R.color.second_anchor_color,
            R.color.clipboard_anchor_color
    };

    private ClipboardManager mClipboardManager;

    public TagManager(Context context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }
        this.mClipboardManager = ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE));
    }

    /***
     * 识别出字符串中间的标签
     *
     * @param desStr 目标字符串
     * @return 转换之后的字符串
     */
    public String identifyTag(String desStr) {
        ArrayList<Tag> tagList = getAllTagList();
        for (Tag tag : tagList) {
            desStr = desStr.replace(tag.getTag(), tag.getTagValue());
        }
        return desStr;
    }

    /***
     * 获取所有的tag列表
     *
     * @return 列表
     */
    public ArrayList<Tag> getAllTagList() {
        ArrayList<Tag> tagList = new ArrayList<>();
        String[] allTagValue = initAllTagValue();
        for (int i = 0; i < allTagValue.length; i++) {
            tagList.add(new Tag(tagArray[i], tagNameRes[i], tagBgRes[i], allTagValue[i]));
        }
        return tagList;
    }


    private String[] initAllTagValue() {
        Calendar calendar = Calendar.getInstance();
        Locale locale = Locale.getDefault();

        String dayOfMonth = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if (dayOfMonth.length() == 1) {
            dayOfMonth = "0" + dayOfMonth;
        }
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        if (month.length() == 1) {
            month = "0" + month;
        }
        String hour = String.valueOf(calendar.get(Calendar.HOUR));
        if ((hour).length() == 1) {
            hour = "0" + hour;
            if ((hour).equals("00")) {
                hour = "12";
            }
        }
        String hourOfDay = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        if ((hourOfDay).length() == 1) {
            hourOfDay = "0" + hourOfDay;
        }
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        if ((minute).length() == 1) {
            minute = "0" + minute;
        }
        String second = String.valueOf(calendar.get(Calendar.SECOND));
        if ((second).length() == 1) {
            second = "0" + second;
        }


        return new String[]{
                dayOfMonth,
                calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, locale),
                calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale),
                month,
                calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, locale),
                calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, locale),
                String.valueOf(calendar.get(Calendar.YEAR)).substring(2, 4),
                String.valueOf(calendar.get(Calendar.YEAR)),
                hour,
                hourOfDay,
                calendar.getDisplayName(Calendar.AM_PM, Calendar.LONG, locale),
                minute,
                second,
                getClipboardStr()
        };
    }

    private String getClipboardStr() {
        if (!this.mClipboardManager.hasPrimaryClip()) {
            return "";
        }
        CharSequence localCharSequence = this.mClipboardManager.getPrimaryClip().getItemAt(0).getText();
        if (localCharSequence == null) {
            return "";
        }
        return localCharSequence.toString();
    }


}

