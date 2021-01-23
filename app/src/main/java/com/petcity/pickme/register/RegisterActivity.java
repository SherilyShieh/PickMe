package com.petcity.pickme.register;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.petcity.pickme.R;
import com.petcity.pickme.base.BaseActivity;
import com.petcity.pickme.base.LiveDataWrapper;
import com.petcity.pickme.common.utils.RegexUtils;
import com.petcity.pickme.common.widget.LoadingDialog;
import com.petcity.pickme.data.response.SigninReponse;
import com.petcity.pickme.databinding.ActivityRegisterBinding;
import com.petcity.pickme.home.HomeActivity;
import com.petcity.pickme.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;


public class RegisterActivity extends BaseActivity<ActivityRegisterBinding, RegisterViewModel> implements View.OnClickListener {


    private static final String TAG = "RegisterActivity";
    private List<TextInputEditText> errorList;

    @Override
    protected boolean isHide() {
        return true;
    }

    @Override
    protected int getSysNavigationBarColor() {
        return R.color.darkBlue;
    }

    @Override
    protected Class<RegisterViewModel> getViewModel() {
        return RegisterViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_register;

    }

    private void register(final FirebaseUser currentUser, String channel) {
        String uid = currentUser.getUid();
        String firstName = mBinding.fnTxt.getText().toString().trim();
        String lastName = mBinding.lnTxt.getText().toString().trim();
        ;
        final String email = currentUser.getEmail();
        final String password = mBinding.pwdTxt.getText().toString().trim();
        LoadingDialog loadingDialog = new LoadingDialog();
        mViewModel.register(uid, channel, firstName, lastName, email, password)
                .observe(RegisterActivity.this, new Observer<LiveDataWrapper<SigninReponse>>() {

                    @Override
                    public void onChanged(LiveDataWrapper<SigninReponse> signinReponseLiveDataWrapper) {
                        switch (signinReponseLiveDataWrapper.status) {
                            case LOADING:
                                loadingDialog.show(getSupportFragmentManager(), null);
                                break;
                            case SUCCESS:
                                loadingDialog.dismiss();
                                sendEmailVerification();
                                goHome();
                                break;
                            case ERROR:
                                loadingDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Register failed cause by " + signinReponseLiveDataWrapper.error.getMessage(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    private void goHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        finish();
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        // [START create_user_with_email]
        //todo open loading dialog....
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            register(user, "Email");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Register failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void sendEmailVerification() {
        // Send verification email
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(RegisterActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mBinding.setViewModel(mViewModel);
        mBinding.register.setOnClickListener(this);
        mBinding.back.setOnClickListener(this);

        mBinding.fnTxt.addTextChangedListener(txtWatcher(mBinding.firstName));
        mBinding.emailTxt.addTextChangedListener(txtWatcher(mBinding.email));
        mBinding.pwdTxt.addTextChangedListener(txtWatcher(mBinding.password));
        mBinding.pwdConfirmTxt.addTextChangedListener(txtWatcher(mBinding.confirmPwd));

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

    private boolean validateFirstName() {
        String firstName = mBinding.fnTxt.getText().toString().trim();
        if (TextUtils.isEmpty(firstName)) {
            mBinding.firstName.setError("FirstName cannot be empty!");
            errorList.add(mBinding.fnTxt);
            return false;
        }
        return true;
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
        } else if (!RegexUtils.isValidPwd(pwd)) {
            mBinding.password.setError("Invalid password: password must be composed of " +
                    "6-20 characters and contain numbers and letters!");
            errorList.add(mBinding.pwdTxt);
            return false;
        }
        return true;
    }

    private boolean validatePwdConfirm() {
        String pwdConfirm = mBinding.pwdConfirmTxt.getText().toString().trim();
        String pwd = mBinding.pwdTxt.getText().toString().trim();
        if (TextUtils.isEmpty(pwdConfirm)) {
            mBinding.confirmPwd.setError("Confirm password cannot be empty!");
            errorList.add(mBinding.pwdConfirmTxt);
            return false;
        }
        if (!TextUtils.isEmpty(pwd) && !TextUtils.equals(pwd, pwdConfirm)) {
            mBinding.confirmPwd.setError("Two inconsistent passwords!");
            errorList.add(mBinding.pwdConfirmTxt);
            return false;
        }
        return true;
    }

    private void register() {
        errorList = new ArrayList();
        boolean validateFirstName = validateFirstName();
        boolean validateEmail = validateEmail();
        boolean validatePwd = validatePwd();
        boolean validatePwdConfirm = validatePwdConfirm();
        if (validateFirstName && validateEmail && validatePwd && validatePwdConfirm) {
            createAccount(mBinding.emailTxt.getText().toString().trim(), mBinding.pwdTxt.getText().toString().trim());
        } else if (!errorList.isEmpty() && !errorList.get(0).hasFocus()) {
            errorList.get(0).requestFocus();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                register();
                break;
            case R.id.back:
                finish();
                break;
        }
    }


}