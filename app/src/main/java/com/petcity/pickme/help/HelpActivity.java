package com.petcity.pickme.help;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.petcity.pickme.R;
import com.petcity.pickme.base.BaseActivity;
import com.petcity.pickme.databinding.ActivityHelpBinding;

import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends BaseActivity<ActivityHelpBinding, HelpViewModel> implements View.OnClickListener {


    List<Boolean> openStatus;
    List<TextView> titles;
    List<ImageView> imageViews;

    @Override
    protected boolean isHide() {
        return true;
    }

    @Override
    protected int getSysNavigationBarColor() {
        return R.color.darkBlue;
    }

    @Override
    protected Class<HelpViewModel> getViewModel() {
        return HelpViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_help;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        openStatus = new ArrayList<>();
        openStatus.add(true);
        openStatus.add(true);
        openStatus.add(true);
        titles = new ArrayList<>();
        titles.add(mBinding.publishTitle);
        titles.add(mBinding.searchTitle);
        titles.add(mBinding.contactTitle);
        imageViews = new ArrayList<>();
        imageViews.add(mBinding.publishIv);
        imageViews.add(mBinding.searchIv);
        imageViews.add(mBinding.contactIv);
        mBinding.back.setOnClickListener(this);
        mBinding.publish.setOnClickListener(this);
        mBinding.search.setOnClickListener(this);
        mBinding.contact.setOnClickListener(this);
    }

    private void changeStatus(int index) {
        boolean isOpen = openStatus.get(index);
        openStatus.set(index, !isOpen);
        TextView tv = titles.get(index);
        int icon = openStatus.get(index) ? R.mipmap.down : R.mipmap.up;
        tv.setCompoundDrawablesRelativeWithIntrinsicBounds(icon, 0, 0, 0);
        imageViews.get(index).setVisibility(openStatus.get(index) ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.publish:
                changeStatus(0);
                break;
            case R.id.search:
                changeStatus(1);
                break;
            case R.id.contact:
                changeStatus(2);
                break;
        }
    }
}