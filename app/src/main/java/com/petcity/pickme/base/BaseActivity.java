package com.petcity.pickme.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.petcity.pickme.R;
import com.petcity.pickme.common.utils.PreferenceManager;
import com.petcity.pickme.common.utils.StatusBarUtils;
import com.petcity.pickme.common.widget.CommonDialogSimple;
import com.petcity.pickme.home.HomeActivity;
import com.petcity.pickme.login.LoginActivity;


import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * @ClassName BaseActivity
 * @Description BaseActivity
 * @Author sherily
 * @Date 6/01/21 3:29 PM
 * @Version 1.0
 */
public abstract class BaseActivity<DB extends ViewDataBinding, VM extends BaseViewModel> extends DaggerAppCompatActivity {

    protected DB mBinding;
    protected VM mViewModel;
    protected FirebaseAuth mAuth;
    private CommonDialogSimple resendEmialDialog;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    protected PreferenceManager preferenceManager;

    protected void sendEmailVerification() {
        // Send verification email
        final FirebaseUser user = mAuth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(BaseActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            onSendEmailSuccess();

                        } else {
                            Log.e("sendEmailVerification", "sendEmailVerification", task.getException());
                            Toast.makeText(BaseActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                            onResendEmailCancel();

                        }
                    }
                });

    }

    protected void goHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
//        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        finish();
    }

    protected abstract void onLogoutSuccess();

    protected abstract void onSendEmailSuccess();

    protected void onResendEmailCancel() {

    }



    protected void logout(String channel) {
        mAuth.signOut();
        if (TextUtils.equals(channel, "Facebook")) {
            LoginManager.getInstance().logOut();
            onLogoutSuccess();
        } else if (TextUtils.equals(channel, "Google")) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(BaseActivity.this, gso);
            mGoogleSignInClient.signOut().addOnCompleteListener(BaseActivity.this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // todo
                            if (task.isSuccessful()) {
                                onLogoutSuccess();
                            }
                        }
                    });
        } else {
            onLogoutSuccess();
        }

    }

    protected void goToSignInOptions() {

        PreferenceManager.getInstance().setCurrentUserInfo(null);
//        if (logoutDialog != null) {
//            logoutDialog.dismiss();
//        }
        Intent intent = new Intent(BaseActivity.this, LoginActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    protected boolean verifyAccess() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            goToSignInOptions();
            return false;
        } else if (!user.isEmailVerified()) {
            resendEmialDialog = new CommonDialogSimple.Builder()
                    .setTitle("Your account has not been verified, please verify it before logging in. If you have lost your verification email, you can resend it to your email: " + user.getEmail())
                    .setKey(user.getEmail(), 0xff0290FA, 14)
                    .showAction2(false)
                    .setCancelBtn("Cancel", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onResendEmailCancel();
                            resendEmialDialog.dismiss();
                        }
                    })
                    .setAction1Btn("Resend", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendEmailVerification();
                            resendEmialDialog.dismiss();
                        }
                    })
                    .create();
            resendEmialDialog.show(getSupportFragmentManager(), null);
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        verifyAccess();
    }

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
