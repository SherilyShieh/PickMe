package com.petcity.pickme.base;

import com.petcity.pickme.base.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

/**
 * @ClassName PickMeApp
 * @Description PickMeApp
 * @Author sherily
 * @Date 6/01/21 2:25 PM
 * @Version 1.0
 */

public class PickMeApp extends DaggerApplication {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected AndroidInjector<PickMeApp> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}
