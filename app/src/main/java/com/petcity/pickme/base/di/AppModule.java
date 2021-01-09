package com.petcity.pickme.base.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.petcity.pickme.base.PickMeApp;
import com.petcity.pickme.common.Constants;
import com.petcity.pickme.common.utils.PreferenceManager;
import com.petcity.pickme.data.remote.RestModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @ClassName PickMeAppModule
 * @Description TODO
 * @Author sherily
 * @Date 6/01/21 2:29 PM
 * @Version 1.0
 */
@Module(includes = {ViewModelModule.class, RestModule.class})
public class AppModule {

    @Provides
    @Singleton
    PreferenceManager providePreferenceManager(PickMeApp app) {
        PreferenceManager.init(app);
        return PreferenceManager.getInstance();
    }


}
