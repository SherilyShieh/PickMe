package com.petcity.pickme.setting;


import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.petcity.pickme.R;
import com.petcity.pickme.base.BaseActivity;
import com.petcity.pickme.common.utils.PreferenceManager;
import com.petcity.pickme.common.widget.CommonDialogSimple;
import com.petcity.pickme.data.response.User;
import com.petcity.pickme.databinding.ActivitySettingBinding;
import com.petcity.pickme.login.LoginActivity;

public class SettingActivity extends BaseActivity<ActivitySettingBinding, SettingViewModel> implements View.OnClickListener {

    private static final String TAG = "SettingActivity";
    private CommonDialogSimple logoutDialog;

    @Override
    protected boolean isHide() {
        return true;
    }

    @Override
    protected int getSysNavigationBarColor() {
        return R.color.darkBlue;
    }

    @Override
    protected Class<SettingViewModel> getViewModel() {
        return SettingViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_setting;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mBinding.setViewModel(mViewModel);
        User user = PreferenceManager.getInstance().getCurrentUserInfo();
        mBinding.setModel(user);

        mBinding.back.setOnClickListener(this);
        mBinding.logoutRl.setOnClickListener(this);
        mBinding.avatarFl.setOnClickListener(this);
        mBinding.nameRl.setOnClickListener(this);
        mBinding.emailRl.setOnClickListener(this);
        mBinding.genderRl.setOnClickListener(this);
        mBinding.locationRl.setOnClickListener(this);
        mBinding.passwordRl.setOnClickListener(this);

    }

    private void updatePassword(String password) {
        mAuth.getCurrentUser().updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // todo

                        }
                    }
                });
    }

    private void updateLocation() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.avatar_fl:
                //todo

                break;
            case R.id.name_rl:

                break;
            case R.id.email_rl:

                break;
            case R.id.gender_rl:

                break;
            case R.id.location_rl:

                break;
            case R.id.password_rl:

                break;
            case R.id.logout_rl:
                if (logoutDialog == null) {
                    logoutDialog = new CommonDialogSimple.Builder()
                            .setTitle("Are you sure to log out of the current account?")
                            .setContentGravity(Gravity.TOP)
                            .showAction2(false)
                            .setCancelBtn("Cancel", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    logoutDialog.dismiss();
                                }
                            })
                            .setAction1Btn("Sure", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mAuth.signOut();
                                    logoutDialog.dismiss();
                                    Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .create();
                }
                logoutDialog.show(getSupportFragmentManager(), "");

                break;
        }
    }
}