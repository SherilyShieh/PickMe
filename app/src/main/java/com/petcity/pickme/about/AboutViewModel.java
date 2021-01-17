package com.petcity.pickme.about;

import androidx.annotation.NonNull;

import com.petcity.pickme.base.BaseViewModel;
import com.petcity.pickme.base.PickMeApp;

import javax.inject.Inject;

/**
 * @ClassName AboutViewModel
 * @Description TODO
 * @Author sherily
 * @Date 13/01/21 4:06 PM
 * @Version 1.0
 */
public class AboutViewModel extends BaseViewModel {

    @Inject
    public AboutViewModel(@NonNull PickMeApp application) {
        super(application);
    }

    @Override
    public void onCreateViewModel() {

    }

    @Override
    public void loadData(boolean isRefresh) {

    }
}
