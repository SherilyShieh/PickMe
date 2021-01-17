package com.petcity.pickme.contacted;

import androidx.annotation.NonNull;

import com.petcity.pickme.base.BaseViewModel;
import com.petcity.pickme.base.PickMeApp;

import javax.inject.Inject;

/**
 * @ClassName MyContactedViewModel
 * @Description TODO
 * @Author sherily
 * @Date 13/01/21 4:03 PM
 * @Version 1.0
 */
public class MyContactedViewModel extends BaseViewModel {

    @Inject
    public MyContactedViewModel(@NonNull PickMeApp application) {
        super(application);
    }

    @Override
    public void onCreateViewModel() {

    }

    @Override
    public void loadData(boolean isRefresh) {

    }
}
