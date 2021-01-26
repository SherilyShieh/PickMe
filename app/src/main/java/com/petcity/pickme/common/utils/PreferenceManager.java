package com.petcity.pickme.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;


import com.google.gson.Gson;
import com.petcity.pickme.data.response.User;

import javax.inject.Singleton;

/**
 * @ClassName PreferenceManager
 * @Description PreferenceManager
 * @Author sherily
 * @Date 6/01/21 8:12 PM
 * @Version 1.0
 */
@Singleton
public class PreferenceManager {

    public static final String SP_NAME = "PetCity_PickMe_SP";
    private static String CURRENT_USERINFO = "CURRENT_USERINFO";
    private static String CURRENT_TEST_TYPE = "CURRENT_TEST_TYPE";
    private static SharedPreferences mSharedPreferences;
    private static PreferenceManager mPreferenceManager;
    private static SharedPreferences.Editor mEditor;

    private PreferenceManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public synchronized static void init(Context context) {
        if (mPreferenceManager == null) {
            mPreferenceManager = new PreferenceManager(context);
        }
    }

    public synchronized static PreferenceManager getInstance() {
        if (mPreferenceManager == null) {
            throw new RuntimeException("Please init first!");
        }
        return mPreferenceManager;
    }

    public void setCurrentUserInfo(User user) {
        if (user == null) return;
        String userjson = new Gson().toJson(user);
        mEditor.putString(CURRENT_USERINFO, userjson);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mEditor.apply();
        } else {
            mEditor.commit();
        }
    }

    public void clearCurrentUserInfo() {
        mEditor.putString(CURRENT_USERINFO, "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mEditor.apply();
        } else {
            mEditor.commit();
        }
    }

    public User getCurrentUserInfo() {
        String userjson = mSharedPreferences.getString(CURRENT_USERINFO, null);
        if (TextUtils.isEmpty(userjson))
            return null;
        return new Gson().fromJson(userjson, User.class);
    }

    public void setTestType(String type) {
        mEditor.putString(CURRENT_TEST_TYPE, type);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mEditor.apply();
        } else {
            mEditor.commit();
        }
    }


    public String getTestType() {
        return mSharedPreferences.getString(CURRENT_TEST_TYPE, "");
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
