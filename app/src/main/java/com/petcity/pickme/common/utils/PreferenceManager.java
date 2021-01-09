package com.petcity.pickme.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;


import javax.inject.Singleton;

/**
 * @ClassName PreferenceManager
 * @Description TODO
 * @Author sherily
 * @Date 6/01/21 8:12 PM
 * @Version 1.0
 */
@Singleton
public class PreferenceManager {

    public static final String SP_NAME = "PetCity_PickMe_SP";
    private static SharedPreferences mSharedPreferences;
    private static PreferenceManager mPreferenceManager;
    private static SharedPreferences.Editor mEditor;

    private PreferenceManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public synchronized static void init(Context context) {
        if (mPreferenceManager == null ) {
            mPreferenceManager = new PreferenceManager(context);
        }
    }
    public synchronized static PreferenceManager getInstance() {
       if (mPreferenceManager == null) {
           throw new RuntimeException("Please init first!");
       }
       return mPreferenceManager;
    }

    public void clearAll() {
        mEditor.clear();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mEditor.apply();
        } else {
            mEditor.commit();
        }
    }
}
