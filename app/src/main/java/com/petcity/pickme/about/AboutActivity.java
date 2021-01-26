package com.petcity.pickme.about;


import android.os.Bundle;
import android.view.View;

import com.petcity.pickme.R;
import com.petcity.pickme.base.BaseActivity;
import com.petcity.pickme.databinding.ActivityAboutBinding;

public class AboutActivity extends BaseActivity<ActivityAboutBinding, AboutViewModel> {


    @Override
    protected void onLogoutSuccess() {

    }

    @Override
    protected void onSendEmailSuccess() {

    }

    @Override
    protected boolean isHide() {
        return true;
    }

    @Override
    protected int getSysNavigationBarColor() {
        return R.color.darkBlue;
    }

    @Override
    protected Class<AboutViewModel> getViewModel() {
        return AboutViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_about;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}