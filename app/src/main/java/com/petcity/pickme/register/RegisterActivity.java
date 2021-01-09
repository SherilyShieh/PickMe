package com.petcity.pickme.register;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.petcity.pickme.R;
import com.petcity.pickme.base.BaseActivity;
import com.petcity.pickme.databinding.ActivityRegisterBinding;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding, RegisterViewModel> {


    @Override
    protected Class<RegisterViewModel> getViewModel() {
        return RegisterViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }
}