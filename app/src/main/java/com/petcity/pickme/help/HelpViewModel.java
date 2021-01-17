package com.petcity.pickme.help;

import androidx.annotation.NonNull;

import com.petcity.pickme.base.BaseViewModel;
import com.petcity.pickme.base.PickMeApp;

import javax.inject.Inject;

/**
 * @ClassName HelpViewModel
 * @Description TODO
 * @Author sherily
 * @Date 13/01/21 4:10 PM
 * @Version 1.0
 */
public class HelpViewModel extends BaseViewModel {

    @Inject
    public HelpViewModel(@NonNull PickMeApp application) {
        super(application);
    }

    @Override
    public void onCreateViewModel() {

    }

    @Override
    public void loadData(boolean isRefresh) {

    }
}
