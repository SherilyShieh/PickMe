package com.petcity.pickme.create;

import androidx.annotation.NonNull;

import com.petcity.pickme.base.BaseViewModel;
import com.petcity.pickme.base.PickMeApp;

import javax.inject.Inject;

/**
 * @ClassName CreateAdsViewModel
 * @Description TODO
 * @Author sherily
 * @Date 16/01/21 12:11 AM
 * @Version 1.0
 */
public class CreateAdsViewModel extends BaseViewModel {

    @Inject
    public CreateAdsViewModel(@NonNull PickMeApp application) {
        super(application);
    }

    @Override
    public void onCreateViewModel() {

    }

    @Override
    public void loadData(boolean isRefresh) {

    }
}
