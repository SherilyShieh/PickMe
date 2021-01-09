package com.petcity.pickme.base.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.petcity.pickme.login.LoginViewModel;
import com.petcity.pickme.register.RegisterViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * @ClassName ViewModelModule
 * @Description TODO
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
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel bindsLoginViewModel(LoginViewModel loginViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel.class)
    abstract ViewModel bindsRegisterViewModel(RegisterViewModel registerViewModel);


}
