package com.petcity.pickme.register;

import androidx.annotation.NonNull;

import com.petcity.pickme.base.BaseViewModel;
import com.petcity.pickme.base.PickMeApp;

import javax.inject.Inject;

/**
 * @ClassName RegisterViewModel
 * @Description TODO
 * @Author sherily
 * @Date 10/01/21 1:15 AM
 * @Version 1.0
 */
public class RegisterViewModel extends BaseViewModel {

    @Inject
    public RegisterViewModel(@NonNull PickMeApp application) {
        super(application);
    }

    @Override
    public void onCreateViewModel() {

    }
}
