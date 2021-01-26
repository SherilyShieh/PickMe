package com.petcity.pickme.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.petcity.pickme.R;
import com.petcity.pickme.base.BaseActivity;
import com.petcity.pickme.base.LiveDataWrapper;
import com.petcity.pickme.common.widget.LoadingDialog;
import com.petcity.pickme.data.response.SigninReponse;
import com.petcity.pickme.databinding.ActivityLoginBinding;
import com.petcity.pickme.register.RegisterActivity;
import com.petcity.pickme.signin.SigninWithAccountActivity;

import java.util.Arrays;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private LoginButton facebookBtn;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String EMAIL = "email";
    private static final String USER_PROFILE = "public_profile";
    private static final String AUTH_TYPE = "rerequest";
    private static final int RC_SIGN_IN = 9001;


    @Override
    protected boolean isHide() {
        return true;
    }

    @Override
    protected int getSysNavigationBarColor() {
        return R.color.darkBlue;
    }

    @Override
    protected Class<LoginViewModel> getViewModel() {
        return LoginViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Log.d("LoginActivity", "login activity");
        mBinding.setViewModel(mViewModel);
        mBinding.fbLogin.setOnClickListener(this);
        mBinding.googleLogin.setOnClickListener(this);
        mBinding.petcityLogin.setOnClickListener(this);
        mBinding.petcityCreate.setOnClickListener(this);
        initFaceBookLogin();
        initGoogleLogin();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (null != currentUser) {
            goHome();
        }
    }

//    private void goHome() {
//        Intent intent = new Intent(this, HomeActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
////        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
//        finish();
//    }

    private void siginWithThird(final FirebaseUser currentUser, String channel) {
        String uid = currentUser.getUid();
        String name = currentUser.getDisplayName();
        String[] names = name.split("\\s+");
        String firstName = name;
        String lastName = "";
        if (names.length == 2) {
            firstName = names[0];
            lastName = names[1];
        }
        final String email = currentUser.getEmail();
        LoadingDialog loadingDialog = new LoadingDialog();
        mViewModel.signinWithThird(uid, channel, firstName, lastName, email)
                .observe(LoginActivity.this, new Observer<LiveDataWrapper<SigninReponse>>() {

                    @Override
                    public void onChanged(LiveDataWrapper<SigninReponse> signinReponseLiveDataWrapper) {
                        switch (signinReponseLiveDataWrapper.status) {
                            case LOADING:
                                loadingDialog.show(getSupportFragmentManager(), null);
                                break;
                            case SUCCESS:
                                loadingDialog.dismiss();
                                updateUI(currentUser);
                                break;
                            case ERROR:
                                loadingDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Sigin failed cause by " + signinReponseLiveDataWrapper.error.getMessage(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    private void initFaceBookLogin() {
        facebookBtn = new LoginButton(this);
        facebookBtn.setPermissions(Arrays.asList(EMAIL, USER_PROFILE));
        facebookBtn.setAuthType(AUTH_TYPE);
        callbackManager = CallbackManager.Factory.create();
        facebookBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                setResult(RESULT_CANCELED);
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initGoogleLogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    // [START on_activity_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                Toast.makeText(LoginActivity.this, "Sigin failed cause by:  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        } else {
            // Pass the activity result back to the Facebook SDK
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            siginWithThird(user, "Google");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            siginWithThird(user, "Facebook");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fb_login:
                facebookBtn.performClick();
                break;
            case R.id.google_login:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.petcity_login:
                loadActivity(SigninWithAccountActivity.class);
                break;
            case R.id.petcity_create:
                loadActivity(RegisterActivity.class);
                break;

        }
    }

    @Override
    protected void onLogoutSuccess() {

    }

    @Override
    protected void onSendEmailSuccess() {

    }
}