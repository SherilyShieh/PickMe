package com.petcity.pickme.base.di;

import com.petcity.pickme.login.LoginActivity;
import com.petcity.pickme.register.RegisterActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @ClassName ActivityBuilderModule
 * @Description TODO
 * @Author sherily
 * @Date 6/01/21 7:04 PM
 * @Version 1.0
 */
@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract LoginActivity loginActivity();

    @ContributesAndroidInjector
    abstract RegisterActivity registerActivity();

}
