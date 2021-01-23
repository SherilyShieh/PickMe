package com.petcity.pickme.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * @ClassName IntentUtils
 * @Description IntentUtils
 * @Author sherily
 * @Date 7/01/21 8:03 PM
 * @Version 1.0
 */
public class JumpUtils {

    public static void withIntent(Context context, Class activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }

    //跳转到指定页面,并获取到返回的数据
    public static void withResult(Activity context, Class activity, int value) {
        Intent intent = new Intent(context, activity);
        context.startActivityForResult(intent, value);
    }

}
