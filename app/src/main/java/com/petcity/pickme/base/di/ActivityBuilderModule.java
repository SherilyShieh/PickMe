package com.petcity.pickme.base.di;

import com.petcity.pickme.about.AboutActivity;
import com.petcity.pickme.ads.MyAdsActivity;
import com.petcity.pickme.contacted.MyContactedActivity;
import com.petcity.pickme.create.CreateAdsActivity;
import com.petcity.pickme.help.HelpActivity;
import com.petcity.pickme.home.HomeActivity;
import com.petcity.pickme.login.LoginActivity;
import com.petcity.pickme.register.RegisterActivity;
import com.petcity.pickme.setting.SettingActivity;
import com.petcity.pickme.signin.SigninWithAccountActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @ClassName ActivityBuilderModule
 * @Description ActivityBuilderModule
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

    @ContributesAndroidInjector
    abstract HomeActivity homeActivity();

    @ContributesAndroidInjector
    abstract AboutActivity aboutActivity();

    @ContributesAndroidInjector
    abstract MyAdsActivity myAdsActivity();

    @ContributesAndroidInjector
    abstract MyContactedActivity myContactedActivity();

    @ContributesAndroidInjector
    abstract HelpActivity helpActivity();

    @ContributesAndroidInjector
    abstract SettingActivity settingActivity();

    @ContributesAndroidInjector
    abstract SigninWithAccountActivity signinWithAccountActivity();

    @ContributesAndroidInjector
    abstract CreateAdsActivity createAdsActivity();
}
