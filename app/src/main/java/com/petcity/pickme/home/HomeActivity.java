package com.petcity.pickme.home;


import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
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
import com.petcity.pickme.common.utils.PreferenceManager;
import com.petcity.pickme.common.utils.StatusBarUtils;
import com.petcity.pickme.common.widget.SpacesItemDecoration;
import com.petcity.pickme.contacted.MyContactedActivity;
import com.petcity.pickme.data.response.User;
import com.petcity.pickme.databinding.ActivityHomeBinding;
import com.petcity.pickme.help.HelpActivity;
import com.petcity.pickme.login.LoginActivity;
import com.petcity.pickme.setting.SettingActivity;


public class HomeActivity extends BaseActivity<ActivityHomeBinding, HomeViewModel> {

    private ActionBarDrawerToggle actionBarDrawerToggle;

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
        mViewModel.getCurrentUser(mAuth.getCurrentUser().getUid()).observe(HomeActivity.this, new Observer<LiveDataWrapper<User>>() {
            @Override
            public void onChanged(LiveDataWrapper<User> userLiveDataWrapper) {
                switch (userLiveDataWrapper.status) {
                    case LOADING:
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
                        break;
                    case ERROR:
                        break;
                }
            }
        });
        mBinding.recyclerView.addItemDecoration(new SpacesItemDecoration(22));

    }
}