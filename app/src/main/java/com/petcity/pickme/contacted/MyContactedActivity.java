package com.petcity.pickme.contacted;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.petcity.pickme.R;
import com.petcity.pickme.base.BaseActivity;
import com.petcity.pickme.databinding.ActivityMyContactedBinding;

public class MyContactedActivity extends BaseActivity<ActivityMyContactedBinding, MyContactedViewModel> {


    @Override
    protected boolean isHide() {
        return true;
    }

    @Override
    protected int getSysNavigationBarColor() {
        return R.color.darkBlue;
    }

    @Override
    protected Class<MyContactedViewModel> getViewModel() {
        return MyContactedViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_my_contacted;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

}