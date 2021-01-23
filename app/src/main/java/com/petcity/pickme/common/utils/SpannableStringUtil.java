package com.petcity.pickme.common.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.TypedValue;
import android.widget.TextView;

import com.petcity.pickme.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName SpannableStringUtil
 * @Description SpannableStringUtil
 * @Author sherily
 * @Date 17/01/21 3:24 PM
 * @Version 1.0
 */
public class SpannableStringUtil {

    public static SpannableStringBuilder matcherSearchText(int color, float textSize, String text, String keyword, TextView view) {
        if (textSize == 0.0f)
            textSize = 14f;
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, view.getResources().getDisplayMetrics());
        SpannableStringBuilder ss = new SpannableStringBuilder(text);
        Pattern pattern = Pattern.compile(keyword);
        Matcher matcher = pattern.matcher(ss);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new AbsoluteSizeSpan(size), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }

}
