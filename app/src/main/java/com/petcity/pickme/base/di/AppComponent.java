package com.petcity.pickme.base.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.petcity.pickme.base.PickMeApp;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * @ClassName PickMeAppComponent
 * @Description TODO
 * @Author sherily
 * @Date 6/01/21 2:27 PM
 * @Version 1.0
 */
@Singleton
@Component(modules = {AndroidInjectionModule.class,
        AndroidSupportInjectionModule.class,
        ActivityBuilderModule.class,
        AppModule.class})
public interface AppComponent extends AndroidInjector<PickMeApp> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<PickMeApp> {
    }

}
