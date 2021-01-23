package com.petcity.pickme.common.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @ClassName TimeUtils
 * @Description TODO
 * @Author sherily
 * @Date 22/01/21 12:08 PM
 * @Version 1.0
 */
public class TimeUtils {
    /**
     * 将时间戳转换为时间
     * s就是时间戳
     */
    public static String stampToDate(String s) {
        if (TextUtils.isEmpty(s))
            return "";
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //如果它本来就是long类型的,则不用写这一步
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 将时间戳转换为时间
     * s就是时间戳
     */
    public static String stampToDateS(String s, String fprmat) {
        if (TextUtils.isEmpty(s))
            return "";
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fprmat);
        //如果它本来就是long类型的,则不用写这一步
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 将时间转换为时间戳
     */
    @SuppressLint("SimpleDateFormat")
    public static String dateToStamp(String s) throws ParseException {
        if (TextUtils.isEmpty(s))
            return "";
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 将 2020-08-21T03:12:58.000+0000 格式化为日期
     */
    public static String dealDateFormat(String oldDate) {
        if (TextUtils.isEmpty(oldDate))
            return "";

        Date date1 = null;
        DateFormat df2 = null;
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = df.parse(oldDate);
            SimpleDateFormat df1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.UK);
            date1 = df1.parse(date.toString());
            df2 = new SimpleDateFormat("yyyy-MM-dd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df2.format(date1);
    }

}
