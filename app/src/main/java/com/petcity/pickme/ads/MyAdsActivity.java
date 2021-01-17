package com.petcity.pickme.ads;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.petcity.pickme.R;
import com.petcity.pickme.base.BaseActivity;
import com.petcity.pickme.databinding.ActivityMyAdsBinding;

public class MyAdsActivity extends BaseActivity<ActivityMyAdsBinding, MyAdsViewModel> {

    @Override
    protected boolean isHide() {
        return true;
    }

    @Override
    protected int getSysNavigationBarColor() {
        return R.color.darkBlue;
    }

    @Override
    protected Class<MyAdsViewModel> getViewModel() {
        return MyAdsViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_my_ads;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }
}