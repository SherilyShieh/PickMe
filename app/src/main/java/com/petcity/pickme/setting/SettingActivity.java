package com.petcity.pickme.setting;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.petcity.pickme.R;
import com.petcity.pickme.base.BaseActivity;
import com.petcity.pickme.base.LiveDataWrapper;
import com.petcity.pickme.common.utils.CameraTools;
import com.petcity.pickme.common.utils.PreferenceManager;
import com.petcity.pickme.common.widget.CommonDialogSimple;
import com.petcity.pickme.common.widget.LoadingDialog;
import com.petcity.pickme.common.widget.UpdateEmailDialog;
import com.petcity.pickme.common.widget.UpdateGenderDialog;
import com.petcity.pickme.common.widget.UpdateNameDialog;
import com.petcity.pickme.common.widget.UpdatePwdDialog;
import com.petcity.pickme.data.response.User;
import com.petcity.pickme.databinding.ActivitySettingBinding;
import com.petcity.pickme.login.LoginActivity;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class SettingActivity extends BaseActivity<ActivitySettingBinding, SettingViewModel> implements View.OnClickListener {

    private static final String TAG = "SettingActivity";
    private static int AUTOCOMPLETE_REQUEST_CODE = 1001;
    private CommonDialogSimple logoutDialog;
    private CommonDialogSimple updateAvatarDialog;
    private UpdatePwdDialog updatePwdDialog;
    private UpdateNameDialog updateNameDialog;
    private UpdateEmailDialog updateEmailDialog;
    private UpdateGenderDialog updateGenderDialog;

    CameraTools cameraTools;

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
        Places.initialize(getApplicationContext(), "AIzaSyASJIP4JYSx0aYLKv3lw8Q-mllQE1JepA0");
        PlacesClient placesClient = Places.createClient(this);
        mBinding.back.setOnClickListener(this);
        mBinding.avatarFl.setOnClickListener(this);
        mBinding.logoutRl.setOnClickListener(this);
        mBinding.nameRl.setOnClickListener(this);
        mBinding.emailRl.setOnClickListener(this);
        mBinding.genderRl.setOnClickListener(this);
        mBinding.locationRl.setOnClickListener(this);
        mBinding.passwordRl.setOnClickListener(this);


    }

    private void logout() {
        mAuth.signOut();
        String channel = PreferenceManager.getInstance().getCurrentUserInfo().getChannel();
        if (TextUtils.equals(channel, "Facebook")) {
            LoginManager.getInstance().logOut();
        } else if (TextUtils.equals(channel, "Google")) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            // [END config_signin]

            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(SettingActivity.this, gso);
            mGoogleSignInClient.signOut().addOnCompleteListener(SettingActivity.this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // todo
                        }
                    });
        }

        PreferenceManager.getInstance().setCurrentUserInfo(null);
        if (logoutDialog != null) {
            logoutDialog.dismiss();
        }
        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void updatePassword() {
        if (updatePwdDialog == null) {
            updatePwdDialog = new UpdatePwdDialog.Builder()
                    .setOnConfirmClickListener(new UpdatePwdDialog.OnClickListener() {
                        @Override
                        public void onClick(View v, String password) {
                            updatePassword(password);
                        }
                    })
                    .setOnCancelClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updatePwdDialog.dismiss();
                        }
                    })
                    .create();
        }
        updatePwdDialog.show(getSupportFragmentManager(), "updatePwdDialog");

    }
    private void updatePassword(String password) {
        mAuth.getCurrentUser().updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            updateProfile(mAuth.getCurrentUser().getUid(), null, null, null, null, null, null, password);
                            updatePwdDialog.dismiss();

                        } else {
                            Exception e = task.getException();

                            if (e instanceof FirebaseAuthInvalidCredentialsException || e instanceof FirebaseAuthInvalidUserException ) {
                                logout();
                            } else {
                                Toast.makeText(SettingActivity.this, "Please re-login, some errors occur: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
//
                        }
                    }
                });
    }

    private void updateProfile(String uid, String avatar, String firstName,
                               String lastName, String email, String gender, String location, String password) {
        LoadingDialog loadingDialog = new LoadingDialog();
        boolean isNeedReAuth = !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password);
        mViewModel.updateProfile(uid, avatar, firstName, lastName, email, gender, location, password)
                .observe(SettingActivity.this, new Observer<LiveDataWrapper<User>>() {
                    @Override
                    public void onChanged(LiveDataWrapper<User> userLiveDataWrapper) {
                        switch (userLiveDataWrapper.status) {
                            case LOADING:
                                loadingDialog.show(getSupportFragmentManager(), null);
                                break;
                            case SUCCESS:
                                mBinding.setModel(userLiveDataWrapper.data);
                                if (isNeedReAuth) {
                                    reauth();
                                }
                                loadingDialog.dismiss();
                                break;
                            case ERROR:
                                loadingDialog.dismiss();
                                Toast.makeText(SettingActivity.this, "Update failed cause by " + userLiveDataWrapper.error.getMessage(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });

    }

    private void updateLocation() {
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setCountry("NZ")
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }
    private void updateName(String firstName, String lastName) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(firstName + " " + lastName)
                .build();

        mAuth.getCurrentUser().updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            updateProfile(mAuth.getCurrentUser().getUid(), null, !TextUtils.isEmpty(firstName) ? firstName : null, !TextUtils.isEmpty(lastName) ? lastName : null, null, null, null, null);
                            updateNameDialog.dismiss();
                        } else {
                            Exception e = task.getException();

                            if (e instanceof FirebaseAuthInvalidCredentialsException || e instanceof FirebaseAuthInvalidUserException ) {
                                logout();
                            } else {
                                Toast.makeText(SettingActivity.this, "Please re-login, some errors occur: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    private void updateName() {
        if (updateNameDialog == null) {
            updateNameDialog = new UpdateNameDialog.Builder()
                    .setOnCancelClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateNameDialog.dismiss();
                        }
                    })
                    .setOnConfirmClickListener(new UpdateNameDialog.OnClickListener() {
                        @Override
                        public void onClick(View v, String firstName, String lastName) {
                            if (TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName)) {
                                Toast.makeText(SettingActivity.this, "Please enter at least one!", Toast.LENGTH_SHORT).show();
                            } else {
                                updateName(firstName, lastName);
                            }

                        }
                    })
                    .create();
        }
        updateNameDialog.show(getSupportFragmentManager(), "updateNameDialog");
    }

    private AuthCredential credential;
    private void reauth() {

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        User user = PreferenceManager.getInstance().getCurrentUserInfo();
        if (TextUtils.equals(user.getChannel(), "Email")) {
            credential = EmailAuthProvider
                    .getCredential(user.getEmail(), user.getPassword());
            reauthenticate(credential);
        } else {
            mAuth.getCurrentUser().getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>(){

                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            // todo
                            if (task.isSuccessful()) {
                                if (TextUtils.equals(user.getChannel(), "Google")) {
                                    credential = GoogleAuthProvider
                                            .getCredential(task.getResult().getToken(), null);
                                    reauthenticate(credential);
                                } else {
                                    credential = FacebookAuthProvider
                                            .getCredential(task.getResult().getToken());
                                    reauthenticate(credential);
                                }
                            } else {
                                Toast.makeText(SettingActivity.this, "Re-authenticate failed, please re-login! Caused by: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                logout();
                            }
                        }
                    });


        }

    }

    private void reauthenticate(AuthCredential credential) {
        mAuth.getCurrentUser().reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User re-authenticated.");

                        } else {
                            Exception e = task.getException();

                            if (e instanceof FirebaseAuthInvalidCredentialsException || e instanceof FirebaseAuthInvalidUserException ) {
                                logout();
                            } else {
                                Toast.makeText(SettingActivity.this, "Please re-login, some errors occur: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void takePhoto() {
        // todo
        if (cameraTools == null)
            cameraTools = new CameraTools(SettingActivity.this);
        cameraTools.takePhoto();
    }

    private void openAblum() {
        // todo
    }


    private void updateAvatar() {
        if (updateAvatarDialog == null) {
            updateAvatarDialog = new CommonDialogSimple.Builder()
                    .setTitle("Update Avatar")
                    .setContentGravity(Gravity.CENTER)
                    .setCancelBtn("Cancel", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateAvatarDialog.dismiss();
                        }
                    })
                    .setAction1Btn("Take Photo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            takePhoto();
                            updateAvatarDialog.dismiss();
                        }
                    })
                    .setAction2Btn("Album", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            openAblum();
                            updateAvatarDialog.dismiss();
                        }
                    })
                    .create();
        }
        updateAvatarDialog.show(getSupportFragmentManager(), null);
    }

    private void updateEmail(String email) {
        mAuth.getCurrentUser().updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            updateProfile(mAuth.getCurrentUser().getUid(), null, null, null, email, null, null, null);
                            updateEmailDialog.dismiss();
                        } else {
                            Exception e = task.getException();

                            if (e instanceof FirebaseAuthInvalidCredentialsException || e instanceof FirebaseAuthInvalidUserException || e instanceof FirebaseAuthRecentLoginRequiredException) {
                                logout();
                            } else {
                                Toast.makeText(SettingActivity.this, "Please re-login, some errors occur: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    private void updateEmail() {
        if (updateEmailDialog == null) {
            updateEmailDialog = new UpdateEmailDialog.Builder()
                    .setOnCancelClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateEmailDialog.dismiss();
                        }
                    })
                    .setOnConfirmClickListener(new UpdateEmailDialog.OnClickListener() {
                        @Override
                        public void onClick(View v, String email) {
                            updateEmail(email);
                        }
                    })
                    .create();
        }
        updateEmailDialog.show(getSupportFragmentManager(), "updateEmailDialog");

    }
    private void updateGender() {
        if (updateGenderDialog == null) {
            updateGenderDialog = new UpdateGenderDialog.Builder()
                    .setOnCancelClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateGenderDialog.dismiss();
                        }
                    })
                    .setOnConfirmClickListener(new UpdateGenderDialog.OnClickListener() {
                        @Override
                        public void onClick(View v, String gender) {
                            updateProfile(mAuth.getCurrentUser().getUid(), null, null, null, null, gender, null, null);
                            updateGenderDialog.dismiss();
                        }
                    })
                    .create();
        }
        updateGenderDialog.show(getSupportFragmentManager(), "updateGenderDialog");

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.avatar_fl:
                updateAvatar();
                break;
            case R.id.name_rl:
                updateName();
                break;
            case R.id.email_rl:
                updateEmail();
                break;
            case R.id.gender_rl:
                updateGender();
                break;
            case R.id.location_rl:
                updateLocation();
                break;
            case R.id.password_rl:
                updatePassword();
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
                                    logout();
                                }
                            })
                            .create();
                }
                logoutDialog.show(getSupportFragmentManager(), "");

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("TAG", "Place: " + place.getAddress() + ", " + place.getId());
                updateProfile(mAuth.getCurrentUser().getUid(), null, null, null,null, null, place.getAddress(),null);

            } else if (resultCode == 2) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("TAG", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}