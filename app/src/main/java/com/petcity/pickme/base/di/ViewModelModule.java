package com.petcity.pickme.base.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.petcity.pickme.about.AboutViewModel;
import com.petcity.pickme.ads.MyAdsViewModel;
import com.petcity.pickme.contacted.MyContactedViewModel;
import com.petcity.pickme.create.CreateAdsViewModel;
import com.petcity.pickme.help.HelpViewModel;
import com.petcity.pickme.home.HomeViewModel;
import com.petcity.pickme.login.LoginViewModel;
import com.petcity.pickme.options.OptionsViewModel;
import com.petcity.pickme.register.RegisterViewModel;
import com.petcity.pickme.setting.SettingViewModel;
import com.petcity.pickme.signin.SigninWithAccountViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * @ClassName ViewModelModule
 * @Description ViewModelModule
 * @Author sherily
 * @Date 6/01/21 8:32 PM
 * @Version 1.0
 */
@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindsViewModelFactory(ViewModelFactory viewModelFactory);

    @Binds
    @IntoMap
    @ViewModelKey(OptionsViewModel.class)
    abstract ViewModel bindsOptionsViewModel(OptionsViewModel optionsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel bindsLoginViewModel(LoginViewModel loginViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel.class)
    abstract ViewModel bindsRegisterViewModel(RegisterViewModel registerViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    abstract ViewModel bindsHomeViewModel(HomeViewModel homeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AboutViewModel.class)
    abstract ViewModel bindsAboutViewModel(AboutViewModel aboutViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MyAdsViewModel.class)
    abstract ViewModel bindsMyAdsViewModel(MyAdsViewModel myAdsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MyContactedViewModel.class)
    abstract ViewModel bindsMyContactedViewModel(MyContactedViewModel myContactedViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HelpViewModel.class)
    abstract ViewModel bindsHelpViewModel(HelpViewModel helpViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SettingViewModel.class)
    abstract ViewModel bindsSettingViewModel(SettingViewModel settingViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SigninWithAccountViewModel.class)
    abstract ViewModel bindsSigninWithAccountViewModel(SigninWithAccountViewModel signinWithAccountViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CreateAdsViewModel.class)
    abstract ViewModel bindsCreateAdsViewModel(CreateAdsViewModel createAdsViewModel);

}
