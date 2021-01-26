package com.petcity.pickme.signin;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.petcity.pickme.R;
import com.petcity.pickme.base.BaseActivity;
import com.petcity.pickme.base.LiveDataWrapper;
import com.petcity.pickme.common.utils.PreferenceManager;
import com.petcity.pickme.common.utils.RegexUtils;
import com.petcity.pickme.common.widget.CommonDialog;
import com.petcity.pickme.common.widget.LoadingDialog;
import com.petcity.pickme.data.response.User;
import com.petcity.pickme.databinding.ActivitySigninWithAccountBinding;

import java.util.ArrayList;
import java.util.List;

public class SigninWithAccountActivity extends BaseActivity<ActivitySigninWithAccountBinding, SigninWithAccountViewModel> implements View.OnClickListener {

    private static final String TAG = "SigninWithAccountActivity";

    private List<TextInputEditText> errorList;

    private CommonDialog verifyEmialDialog;


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
    protected Class<SigninWithAccountViewModel> getViewModel() {
        return SigninWithAccountViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_signin_with_account;
    }

    private boolean validateEmail() {
        String email = mBinding.emailTxt.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            mBinding.email.setError("Email cannot be empty!");
            errorList.add(mBinding.emailTxt);
            return false;
        } else if (!RegexUtils.isValidEmail(email)) {
            mBinding.email.setError("Invalid email format!");
            errorList.add(mBinding.emailTxt);
            return false;
        }
        return true;
    }

    private boolean validatePwd() {
        String pwd = mBinding.pwdTxt.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            mBinding.password.setError("Password cannot be empty!");
            errorList.add(mBinding.pwdTxt);
            return false;
        }
        return true;
    }

    private void login() {
        errorList = new ArrayList<>();
        boolean validateEmail = validateEmail();
        boolean validatePwd = validatePwd();
        if (validateEmail && validatePwd) {
            signin(mBinding.emailTxt.getText().toString().trim(), mBinding.pwdTxt.getText().toString().trim());
        } else if (!errorList.isEmpty() && !errorList.get(0).hasFocus()) {
            errorList.get(0).requestFocus();
        }
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        mBinding.back.setOnClickListener(this);
        mBinding.login.setOnClickListener(this);
        mBinding.forget.setOnClickListener(this);
        mBinding.emailTxt.addTextChangedListener(txtWatcher(mBinding.email));
        mBinding.pwdTxt.addTextChangedListener(txtWatcher(mBinding.password));

    }

    private TextWatcher txtWatcher(TextInputLayout textLayout) {
        return new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                textLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                textLayout.setError(null);

            }
        };
    }

//    private void goHome() {
//        Intent intent = new Intent(this, HomeActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
////        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
//        finish();
//    }

    private void resetPasswordWithEmial(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(SigninWithAccountActivity.this, "Please check your email and reset your password.", Toast.LENGTH_SHORT).show();
                            verifyEmialDialog.dismiss();
                        }
                    }
                });
    }

    private void signin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            updatePassword(mAuth.getCurrentUser().getUid(), password);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            mBinding.email.setError("Please check your email!");
                            mBinding.password.setError("Please check your password!");
                            mBinding.emailTxt.requestFocus();
                            Toast.makeText(SigninWithAccountActivity.this, "Authentication failed: Wrong password or email",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updatePassword(String uid, String password) {
        LoadingDialog loadingDialog = new LoadingDialog();
        mViewModel.updatePassword(uid, password).observe(this, new Observer<LiveDataWrapper<User>>() {
            @Override
            public void onChanged(LiveDataWrapper<User> userLiveDataWrapper) {
                switch (userLiveDataWrapper.status) {
                    case LOADING:
                        loadingDialog.show(getSupportFragmentManager(), null);
                        break;
                    case SUCCESS:
                        loadingDialog.dismiss();
                        if (verifyAccess()) {
                            preferenceManager.setCurrentUserInfo(userLiveDataWrapper.data);
                            goHome();
                        } else {
                            logout(userLiveDataWrapper.data.getChannel());
                            preferenceManager.clearCurrentUserInfo();
                        }

                        break;
                    case ERROR:
                        loadingDialog.dismiss();
                        Toast.makeText(SigninWithAccountActivity.this, "Login failed cause by " + userLiveDataWrapper.error.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private boolean validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else if (!RegexUtils.isValidEmail(email)) {
            return false;
        }
        return true;
    }

    private void forgotEmail() {
        if (verifyEmialDialog == null) {
            verifyEmialDialog = new CommonDialog.Builder()
                    .setTitle("Verify Email")
                    .setContent("Please enter the email which you registered your account and a verification email will help you reset your password.")
                    .setPlaceHolder("Please enter your email")
                    .setConfirmBtn("Verify", new CommonDialog.OnClickListener() {
                        @Override
                        public void onClick(View v, String str) {
                            if (validateEmail(str)) {
                                resetPasswordWithEmial(str);
                            }
                        }
                    })
                    .setCancelBtn("Cancel", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            verifyEmialDialog.dismiss();
                        }
                    })
                    .create();
        }
        verifyEmialDialog.show(getSupportFragmentManager(), "verify");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.login:
                login();
                break;
            case R.id.forget:
                forgotEmail();
                break;
        }
    }
}