package com.petcity.pickme.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.petcity.pickme.R;
import com.petcity.pickme.base.BaseViewModel;
import com.petcity.pickme.base.di.ViewModelFactory;
import com.petcity.pickme.common.utils.PreferenceManager;
import com.petcity.pickme.common.utils.StatusBarUtils;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * @ClassName BaseActivity
 * @Description TODO
 * @Author sherily
 * @Date 6/01/21 3:29 PM
 * @Version 1.0
 */
public abstract class BaseActivity<DB extends ViewDataBinding, VM extends BaseViewModel> extends DaggerAppCompatActivity {

    protected DB mBinding;
    protected VM mViewModel;
    protected FirebaseAuth mAuth;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    public PreferenceManager preferenceManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        Log.d("BaseActovity", "OnCreate");
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModel());
        initDataBinding();
        StatusBarUtils.setColor(this, ContextCompat.getColor(this, R.color.darkBlue));
        hideBottomUIMenu(isHide());
        mAuth = FirebaseAuth.getInstance();
        setSysNavigationBarColor(getSysNavigationBarColor());
        this.init(savedInstanceState);


    }

    protected abstract boolean isHide();

    @ColorRes
    protected abstract int getSysNavigationBarColor();

    protected void setSysNavigationBarColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().setNavigationBarColor(ContextCompat.getColor(this, color));
        }
    }

    protected void hideBottomUIMenu(boolean isHide) {
        if (!isHide) return;
        //hide Bottom Menu
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);

        }
    }

    protected abstract Class<VM> getViewModel();

    protected void initDataBinding() {
//        setContentView(getLayoutRes());
        mBinding = DataBindingUtil.setContentView(this, getLayoutRes());
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    protected abstract void init(Bundle savedInstanceState);

    @Override
    protected void onDestroy() {
        if (mBinding != null)
            mBinding.unbind();
        super.onDestroy();
    }

    public void loadActivity(Class<?> clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }
}
