package com.petcity.pickme.options;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;

import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.petcity.pickme.R;

import com.petcity.pickme.base.BaseActivity;
import com.petcity.pickme.common.widget.DividerItemDecoration;
import com.petcity.pickme.databinding.ActivityOptionsBinding;

import java.util.Arrays;
import java.util.List;


public class OptionsActivity extends BaseActivity<ActivityOptionsBinding, OptionsViewModel> implements View.OnClickListener {


    private static final String TAG = "OptionsActivity";
    private LoginButton facebookBtn;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String EMAIL = "email";
    private static final String USER_PROFILE = "public_profile";
    private static final String AUTH_TYPE = "rerequest";
    private static final int AUTOCOMPLETE_REQUEST_CODE = 9000;
    private static final int RC_SIGN_IN = 9001;



    @Override
    protected boolean isHide() {
        return true;
    }

    @Override
    protected int getSysNavigationBarColor() {
        return R.color.white;
    }

    @Override
    protected Class<OptionsViewModel> getViewModel() {
        return OptionsViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_options;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mBinding.setViewModel(mViewModel);
        initClick();
        initRecycleView();
        initFacebook();
        initGoogle();
        initGooglePlace();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showUser(preferenceManager.getTestType());
        mViewModel.get();
    }

    private void showUser(String source) {
        String info;
        if (validUser()) {
            info = "Current User: " +  mAuth.getCurrentUser().getDisplayName() + " Signin with " + source;
        } else {
            info = "No User Login";
        }
        mBinding.user.setText(info);
    }

    private void initRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    private void initClick() {
        mBinding.fbLogin.setOnClickListener(this);
        mBinding.googleLogin.setOnClickListener(this);
        mBinding.register.setOnClickListener(this);
        mBinding.login.setOnClickListener(this);
        mBinding.send.setOnClickListener(this);
        mBinding.logout.setOnClickListener(this);
        mBinding.create.setOnClickListener(this);
        mBinding.addressBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fb_login:
                if (validUser()) {
                    toast("Already have users logged in, please log out first!");
                    return;
                }
                facebookBtn.performClick();
                break;
            case R.id.google_login:
                if (validUser()) {
                    toast("Already have users logged in, please log out first!");
                    return;
                }
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.register:
                if (validUser()) {
                    toast("Already have users logged in, please log out first!");
                    return;
                }
                register();
                break;
            case R.id.login:
                if (validUser()) {
                    toast("Already have users logged in, please log out first!");
                    return;
                }
                SigninWithEmailPwd();
                break;
            case R.id.send:
                sendEmail();
                break;
            case R.id.logout:
                logoutCurrentUser();
                break;
            case R.id.address_btn:
                // Set the fields to specify which types of place data to
                // return after the user has made a selection.
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS);
                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                        .setCountry("NZ")
                        .build(this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                break;
            case R.id.create:
                mViewModel.create(mBinding.info.getText().toString().trim());
                break;

        }

    }

    private void register() {
        String email = mBinding.email.getText().toString().trim();
        String password = mBinding.password.getText().toString().trim();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            createAccount(email, password);
        } else {
            toast("Email or Password could not be empty!");
        }
    }
    private void SigninWithEmailPwd() {
        String email = mBinding.email.getText().toString().trim();
        String password = mBinding.password.getText().toString().trim();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            toast("Email or Password could not be empty!");
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            preferenceManager.setTestType("Email");
                            showUser("Email");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            showUser(null);
                            toast("Authentication failed: Wrong password or email" + task.getException().getMessage());
                            preferenceManager.setTestType("");
                        }
                    }
                });
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
                            toast("Register Successful");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            toast("Register failed: " + task.getException().getMessage());

                        }
                    }
                });
    }
    private void initFacebook() {
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
                Toast.makeText(OptionsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }
    private void initGooglePlace() {
        Places.initialize(getApplicationContext(), "AIzaSyASJIP4JYSx0aYLKv3lw8Q-mllQE1JepA0");
        PlacesClient placesClient = Places.createClient(this);
    }
    private void logoutCurrentUser() {
        if (validUser()) {
            mAuth.signOut();
            String type = preferenceManager.getTestType();
            if (TextUtils.equals(type, "Facebook")) {
                LoginManager.getInstance().logOut();
                showUser(null);
                preferenceManager.setTestType("");
            } else if (TextUtils.equals(type, "Google")) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                // [END config_signin]

                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(OptionsActivity.this, gso);
                mGoogleSignInClient.signOut().addOnCompleteListener(OptionsActivity.this,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // todo
                                if (task.isSuccessful()) {
                                    showUser(null);
                                    preferenceManager.setTestType("");

                                } else {
                                    toast(task.getException().getMessage());
                                }
                            }
                        });
            } else {
               showUser(null);
                preferenceManager.setTestType("");
            }


            showUser(null);
        } else {
            toast("No Logined User, Please Login!");
        }

    }

    private void toast(String str) {
        Toast.makeText(OptionsActivity.this, str, Toast.LENGTH_SHORT).show();
    }
    private void sendEmail() {
        if (!validUser()) {
            toast("Please Login FIRST!");
            return;
        }
        final FirebaseUser user = mAuth.getCurrentUser();
        if (TextUtils.isEmpty(user.getEmail())) {
            toast("Current user hasn't bind email!");
            return;
        }

        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            toast("Verification email sent to " + user.getEmail() + ", please check your mailbox;");

                        } else {
                            Log.e("sendEmailVerification", "sendEmailVerification", task.getException());
                            toast("Failed to send verification email, caused by " + task.getException().getMessage());
                        }
                    }
                });
    }
    private boolean validUser() {
        return mAuth.getCurrentUser() != null;
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
                            preferenceManager.setTestType("Google");
                            showUser("Google");
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            toast("Authentication failed, caused by "+ task.getException().getMessage());
                            preferenceManager.setTestType("");
                            showUser(null);
                        }

                    }
                });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("MainActivity", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signInWithCredential", "signInWithCredential:success");
                            preferenceManager.setTestType("Facebook");
                            showUser("Facebook");


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signInWithCredential", "signInWithCredential:failure", task.getException());
                            toast("Authentication failed, caused by" +  task.getException());
                            preferenceManager.setTestType("");
                            showUser(null);

                        }

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("TAG", "Place: " + place.getAddress() + ", " + place.getId());
                mBinding.address.setText(place.getAddress());

            } else if (resultCode == 2) {

                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("TAG", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i("TAG", "Cancel");
            }
            return;
        } else if (requestCode == RC_SIGN_IN) {
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
                toast("Google sign in failed cause by:  " + e.getMessage());
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onLogoutSuccess() {

    }

    @Override
    protected void onSendEmailSuccess() {

    }
}
