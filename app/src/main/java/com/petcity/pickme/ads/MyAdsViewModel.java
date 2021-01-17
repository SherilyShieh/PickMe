package com.petcity.pickme.ads;

import androidx.annotation.NonNull;

import com.petcity.pickme.base.BaseViewModel;
import com.petcity.pickme.base.PickMeApp;

import javax.inject.Inject;

/**
 * @ClassName MyAdsViewModel
 * @Description TODO
 * @Author sherily
 * @Date 13/01/21 4:00 PM
 * @Version 1.0
 */
public class MyAdsViewModel extends BaseViewModel {

    @Inject
    public MyAdsViewModel(@NonNull PickMeApp application) {
        super(application);
    }

    @Override
    public void onCreateViewModel() {

    }

    @Override
    public void loadData(boolean isRefresh) {

    }
}
