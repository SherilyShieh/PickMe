package com.petcity.pickme.setting;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.shape.ShapeAppearanceModel;
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
import com.petcity.pickme.common.utils.BitmapUtils;
import com.petcity.pickme.common.utils.CameraTools;
import com.petcity.pickme.common.utils.PreferenceManager;
import com.petcity.pickme.common.widget.CommonDialog;
import com.petcity.pickme.common.widget.CommonDialogSimple;
import com.petcity.pickme.common.widget.LoadingDialog;
import com.petcity.pickme.common.widget.UpdateEmailDialog;
import com.petcity.pickme.common.widget.UpdateGenderDialog;
import com.petcity.pickme.common.widget.UpdateNameDialog;
import com.petcity.pickme.common.widget.UpdatePwdDialog;
import com.petcity.pickme.create.CreateAdsActivity;
import com.petcity.pickme.data.response.CommonResponse;
import com.petcity.pickme.data.response.User;
import com.petcity.pickme.databinding.ActivitySettingBinding;

import java.io.File;
import java.util.Arrays;
import java.util.List;


public class SettingActivity extends BaseActivity<ActivitySettingBinding, SettingViewModel> implements View.OnClickListener {

    private static final String TAG = "SettingActivity";
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1001;
    private static final int REQUEST_CAMERA_CODE = 1002;
    private static final int OPEN_ABLUM = 1003;
    private CommonDialogSimple logoutDialog;
    private CommonDialogSimple updateAvatarDialog;
    private UpdatePwdDialog updatePwdDialog;
    private UpdateNameDialog updateNameDialog;
    private UpdateEmailDialog updateEmailDialog;
    private UpdateGenderDialog updateGenderDialog;
    private LoadingDialog loadingDialog;
    private boolean isNeedReAuth;

    CameraTools cameraTools;
    private CommonDialog addressDialog;


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
        ShapeAppearanceModel shape = ShapeAppearanceModel.builder().setAllCornerSizes(ShapeAppearanceModel.PILL).build();
        mBinding.avatar.setShapeAppearanceModel(shape);

        mViewModel.updates
                .observe(SettingActivity.this, new Observer<LiveDataWrapper<User>>() {
                    @Override
                    public void onChanged(LiveDataWrapper<User> userLiveDataWrapper) {
                        switch (userLiveDataWrapper.status) {
                            case LOADING:
                                if (null == loadingDialog)
                                    loadingDialog = new LoadingDialog();
                                loadingDialog.show(getSupportFragmentManager(), null);
                                break;
                            case SUCCESS:
                                mBinding.setModel(userLiveDataWrapper.data);
                                if (isNeedReAuth) {
                                    reauth();
                                }
                                Toast.makeText(SettingActivity.this, "Update Successful!", Toast.LENGTH_SHORT).show();
                                loadingDialog.dismiss();
                                break;
                            case ERROR:
                                loadingDialog.dismiss();
                                Toast.makeText(SettingActivity.this, "Update failed cause by " + userLiveDataWrapper.error.getMessage(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
        mViewModel.upload
                .observe(SettingActivity.this, new Observer<LiveDataWrapper<CommonResponse>>() {
                    @Override
                    public void onChanged(LiveDataWrapper<CommonResponse> userLiveDataWrapper) {
                        switch (userLiveDataWrapper.status) {
                            case LOADING:
                                if (null == loadingDialog)
                                    loadingDialog = new LoadingDialog();
                                loadingDialog.show(getSupportFragmentManager(), null);
                                break;
                            case SUCCESS:
                                Toast.makeText(SettingActivity.this, "Upload avatar successful!", Toast.LENGTH_SHORT).show();
                                loadingDialog.dismiss();
                                break;
                            case ERROR:
                                Toast.makeText(SettingActivity.this, "Upload avatar failed cause by " + userLiveDataWrapper.error.getMessage(), Toast.LENGTH_SHORT).show();
                                loadingDialog.dismiss();
                                break;
                        }
                    }
                });


    }

//    private void logout() {
//        mAuth.signOut();
//        String channel = preferenceManager.getCurrentUserInfo().getChannel();
//        if (TextUtils.equals(channel, "Facebook")) {
//            LoginManager.getInstance().logOut();
//        } else if (TextUtils.equals(channel, "Google")) {
//            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                    .requestIdToken(getString(R.string.default_web_client_id))
//                    .requestEmail()
//                    .build();
//            // [END config_signin]
//
//            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(SettingActivity.this, gso);
//            mGoogleSignInClient.signOut().addOnCompleteListener(SettingActivity.this,
//                    new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            // todo
//                        }
//                    });
//        }
//
//        PreferenceManager.getInstance().setCurrentUserInfo(null);
////        if (logoutDialog != null) {
////            logoutDialog.dismiss();
////        }
//        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();
//    }

    private void showAddressDialog() {
        if (addressDialog == null) {
            addressDialog = new CommonDialog.Builder()
                    .setTitle("Enter Location")
                    .setContent("The beta version only allows beta testers to access the Google Places API, you are not a beta tester, " +
                            "please enter your address in the following format: Street, District, Region, Country.")
                    .setPlaceHolder("Street, District, Region, Country")
                    .setConfirmBtn("OK", new CommonDialog.OnClickListener() {
                        @Override
                        public void onClick(View v, String str) {
                            if (!TextUtils.isEmpty(str)) {
                                updateProfile(mAuth.getCurrentUser().getUid(), null, null, null, null, null, str, null);
                                addressDialog.dismiss();
                            } else {
                                Toast.makeText(SettingActivity.this, "Please input your address!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setCancelBtn("Cancel", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addressDialog.dismiss();
                        }
                    })
                    .create();
        }
        addressDialog.show(getSupportFragmentManager(), "verify");
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

                            if (e instanceof FirebaseAuthInvalidCredentialsException || e instanceof FirebaseAuthInvalidUserException) {
                                logout(preferenceManager.getCurrentUserInfo().getChannel());
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
        isNeedReAuth = !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password);
        mViewModel.updateProfile(uid, avatar, firstName, lastName, email, gender, location, password);


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

                            if (e instanceof FirebaseAuthInvalidCredentialsException || e instanceof FirebaseAuthInvalidUserException) {
                                logout(preferenceManager.getCurrentUserInfo().getChannel());
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
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {

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
                                logout(preferenceManager.getCurrentUserInfo().getChannel());
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

                            if (e instanceof FirebaseAuthInvalidCredentialsException || e instanceof FirebaseAuthInvalidUserException) {
                                logout(preferenceManager.getCurrentUserInfo().getChannel());
                            } else {
                                Toast.makeText(SettingActivity.this, "Please re-login, some errors occur: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void openCamera() {
        if (cameraTools != null)
            cameraTools = null;
        cameraTools = new CameraTools(SettingActivity.this);
        cameraTools.takePhoto();
    }

    private boolean isAblum;

    private void requestPermissionForPhoto(boolean isAblum) {
        this.isAblum = isAblum;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CAMERA_CODE);
                return;
            } else {
                if (isAblum) {
                    openAblum();
                } else {
                    openCamera();
                }
            }
        } else {
            if (isAblum) {
                openAblum();
            } else {
                openCamera();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);//grantResults用来存放被拒绝的权限
        switch (requestCode) {
            case REQUEST_CAMERA_CODE:
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "No Camera Permission", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                if (isAblum) {
                    openAblum();
                } else {
                    openCamera();
                }
                break;
            default:
                break;
        }
    }

    private void openAblum() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, OPEN_ABLUM);
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

                            requestPermissionForPhoto(false);
                            updateAvatarDialog.dismiss();
                        }
                    })
                    .setAction2Btn("Album", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestPermissionForPhoto(true);
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
                            Toast.makeText(SettingActivity.this, "Please re-login, some errors occur: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            if (e instanceof FirebaseAuthInvalidCredentialsException || e instanceof FirebaseAuthInvalidUserException || e instanceof FirebaseAuthRecentLoginRequiredException) {

                                logout(preferenceManager.getCurrentUserInfo().getChannel());
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
                                    logout(preferenceManager.getCurrentUserInfo().getChannel());
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

        switch (requestCode) {
            case AUTOCOMPLETE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    Log.i("TAG", "Place: " + place.getAddress() + ", " + place.getId());
                    updateProfile(mAuth.getCurrentUser().getUid(), null, null, null, null, null, place.getAddress(), null);
//                    showAddressDialog();
                } else if (resultCode == 2) {
                    // TODO: Handle the error.
                    Status status = Autocomplete.getStatusFromIntent(data);
                    Log.i("TAG", status.getStatusMessage());
                    showAddressDialog();
                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                return;
            case CameraTools.CODE_TAKE_PHOTO:

                try {
                    File path = cameraTools.getTempFile();
                    if (null != path && resultCode == RESULT_OK) {
                        File path2 = BitmapUtils.comprass(path.getAbsolutePath());
                        mViewModel.saveAvatar(mAuth.getCurrentUser().getUid(), path2 == null ? null : path2.getAbsolutePath());
                        cameraTools.setTempFile(null);
                    }
                } catch (Exception e) {
                    Log.d("TakePhoto", e.getMessage());
                }
                break;
            case OPEN_ABLUM:
                if (data != null && resultCode == RESULT_OK) {
                    try {
                        Uri selectedImage = data.getData();
                        String[] filePathColumns = {MediaStore.Images.Media.DATA};
                        Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                        c.moveToFirst();
                        int columnIndex = c.getColumnIndex(filePathColumns[0]);
                        String imagePath = c.getString(columnIndex);
                        File path = BitmapUtils.comprass(imagePath);
                        mViewModel.saveAvatar(mAuth.getCurrentUser().getUid(), path == null ? null : path.getAbsolutePath());
                        c.close();
                    } catch (Exception e) {
                        Log.d("OpenAblum", e.getMessage());
                    }
                }
                break;
        }
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onLogoutSuccess() {
        goToSignInOptions();

    }

    @Override
    protected void onSendEmailSuccess() {

    }
}