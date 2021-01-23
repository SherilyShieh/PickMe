package com.petcity.pickme.home;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.petcity.pickme.R;
import com.petcity.pickme.about.AboutActivity;
import com.petcity.pickme.ads.MyAdsActivity;
import com.petcity.pickme.base.BaseActivity;
import com.petcity.pickme.base.LiveDataWrapper;
import com.petcity.pickme.common.utils.ClipBoard;
import com.petcity.pickme.common.utils.PreferenceManager;
import com.petcity.pickme.common.utils.StatusBarUtils;
import com.petcity.pickme.common.widget.CommonDialogSimple;
import com.petcity.pickme.common.widget.LoadingDialog;
import com.petcity.pickme.common.widget.SearchDialog;
import com.petcity.pickme.common.widget.SpacesItemDecoration;
import com.petcity.pickme.contacted.MyContactedActivity;
import com.petcity.pickme.create.CreateAdsActivity;
import com.petcity.pickme.data.response.AdvertiseResponse;
import com.petcity.pickme.data.response.CommonResponse;
import com.petcity.pickme.data.response.ListAdvertiseResponse;
import com.petcity.pickme.data.response.User;
import com.petcity.pickme.databinding.ActivityHomeBinding;
import com.petcity.pickme.help.HelpActivity;
import com.petcity.pickme.login.LoginActivity;
import com.petcity.pickme.setting.SettingActivity;


public class HomeActivity extends BaseActivity<ActivityHomeBinding, HomeViewModel> implements View.OnClickListener {

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private LoadingDialog loadingDialog;
    private CommonDialogSimple copyEmailDialog;
    private SearchDialog searchDialog;


    @Override
    protected boolean isHide() {
        return false;
    }

    @Override
    protected int getSysNavigationBarColor() {
        return R.color.middleBlue;
    }

    @Override
    protected Class<HomeViewModel> getViewModel() {
        return HomeViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_home;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mBinding.setViewModel(mViewModel);
        StatusBarUtils.setColor(this, ContextCompat.getColor(this, R.color.transparent));
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mBinding.drawerLayout, mBinding.bottomAppBar, R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.syncState();
        mBinding.navigationView.setItemIconSize(48);
        mBinding.recyclerView.addItemDecoration(new SpacesItemDecoration(22));
        mBinding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.setting:
                        loadActivity(SettingActivity.class);
//                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                        break;
                    case R.id.ads:
                        loadActivity(MyAdsActivity.class);
                        break;
                    case R.id.contacted:
                        loadActivity(MyContactedActivity.class);
                        break;
                    case R.id.about:
                        loadActivity(AboutActivity.class);
                        break;
                    case R.id.help:
                        loadActivity(HelpActivity.class);
                        break;
                }
                item.setCheckable(false);
                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        mBinding.bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.search) {
                    if (searchDialog == null) {
                        searchDialog = new SearchDialog.Builder()
                                .setOnCancelListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        searchDialog.dismiss();
                                    }
                                })
                                .setOnClearListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mViewModel.clear();
                                        searchDialog.dismiss();
                                    }
                                })
                                .setOnSearchListener(new SearchDialog.OnSearchListener() {
                                    @Override
                                    public void onSearch(String region, String district, String breed) {
                                        region = TextUtils.equals(region, "All") ? null : region;
                                        district = TextUtils.equals(district, "All") ? null : district;
                                        breed = TextUtils.equals(breed, "All") ? null : breed;
                                        mViewModel.search(breed, region, district);
                                        searchDialog.dismiss();
                                    }
                                })
                                .create();
                    }
                    searchDialog.show(getSupportFragmentManager(), null);
                    return true;
                }
                return false;
            }
        });
        View drawview = mBinding.navigationView.inflateHeaderView(R.layout.nav_header_main);
        RelativeLayout header = (RelativeLayout) drawview.findViewById(R.id.user);
        ShapeableImageView imageView = (ShapeableImageView) drawview.findViewById(R.id.avatar);
        TextView name = (TextView) drawview.findViewById(R.id.name);

        ShapeAppearanceModel shape = ShapeAppearanceModel.builder().setAllCornerSizes(ShapeAppearanceModel.PILL).build();
        imageView.setShapeAppearanceModel(shape);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
                loadActivity(SettingActivity.class);
//                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        });
        mBinding.create.setOnClickListener(this);

        mViewModel.getCurrentUser(mAuth.getCurrentUser().getUid()).observe(HomeActivity.this, new Observer<LiveDataWrapper<User>>() {
            @Override
            public void onChanged(LiveDataWrapper<User> userLiveDataWrapper) {
                switch (userLiveDataWrapper.status) {
                    case LOADING:
                        if(loadingDialog == null)
                            loadingDialog = new LoadingDialog();
                        loadingDialog.show(getSupportFragmentManager(), null);
                        break;
                    case SUCCESS:
                        User user = userLiveDataWrapper.data;
                        if (null == user) {
                            loadActivity(LoginActivity.class);
                            mAuth.signOut();
                            finish();
                        }
                        Glide.with(imageView.getContext())
                                .load((TextUtils.isEmpty(user.getAvatar()) ? R.mipmap.avtar : user.getAvatar()))
                                .centerCrop()
                                .error(R.mipmap.error)
                                .placeholder(R.mipmap.avtar)
                                .into(imageView);
                        name.setText(user.formatName());
                        if (null != loadingDialog)
                            loadingDialog.dismiss();
                        break;
                    case ERROR:
                        Toast.makeText(HomeActivity.this, "Error: " + userLiveDataWrapper.error.getMessage(), Toast.LENGTH_SHORT).show();
                        if (null != loadingDialog)
                            loadingDialog.dismiss();
                        break;
                }
            }
        });

        mViewModel.show.observe(this, new Observer<AdvertiseResponse>() {
            @Override
            public void onChanged(@Nullable AdvertiseResponse aResponse) {
                if (copyEmailDialog != null) {
                    copyEmailDialog.dismiss();
                    copyEmailDialog = null;
                }

                copyEmailDialog = new CommonDialogSimple.Builder()
                        .setTitle("Please copy the user's email and congtact the user:" + aResponse.getUser().getEmail())
                        .setKey(aResponse.getUser().getEmail(), 0xff0290FA, 14)
                        .showAction2(false)
                        .setCancelBtn("Cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                copyEmailDialog.dismiss();
                            }
                        })
                        .setAction1Btn("Copy", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mViewModel.addContacted(aResponse.getAd_id());
                                ClipBoard.copyClipboard(HomeActivity.this, aResponse.getUser().getEmail());
                                Toast.makeText(HomeActivity.this, "Copy email successfully", Toast.LENGTH_SHORT).show();
                                copyEmailDialog.dismiss();
                            }
                        })
                        .create();
                copyEmailDialog.show(getSupportFragmentManager(), null);
            }
        });
        mViewModel.contacted.observe(this, new Observer<LiveDataWrapper<CommonResponse>>() {
            @Override
            public void onChanged(LiveDataWrapper<CommonResponse> commonResponseLiveDataWrapper) {
                switch (commonResponseLiveDataWrapper.status) {
                    case LOADING:
                        if (loadingDialog == null)
                            loadingDialog = new LoadingDialog();
                        loadingDialog.show(getSupportFragmentManager(), null);
                        break;
                    case SUCCESS:
                        Toast.makeText(HomeActivity.this, "Add Contacted Successful!", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                        break;
                    case ERROR:
                        Toast.makeText(HomeActivity.this, "Add Contacted failed, casused by" + commonResponseLiveDataWrapper.error.getMessage(), Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                        break;
                }
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CreateAdsActivity.CREATE_OR_UPDATE_AD && resultCode == RESULT_OK) {
            mViewModel.loadData(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.loadData(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create:
                CreateAdsActivity.satrtCreateForResult(HomeActivity.this, false, null);
                break;
        }
    }
}