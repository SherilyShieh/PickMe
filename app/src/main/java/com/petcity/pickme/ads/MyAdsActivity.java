package com.petcity.pickme.ads;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.petcity.pickme.R;
import com.petcity.pickme.base.BaseActivity;
import com.petcity.pickme.base.LiveDataWrapper;
import com.petcity.pickme.common.widget.CommonDialogSimple;
import com.petcity.pickme.common.widget.DividerItemDecoration;
import com.petcity.pickme.common.widget.LinearDividerItemDecoration;
import com.petcity.pickme.common.widget.LoadingDialog;
import com.petcity.pickme.create.CreateAdsActivity;
import com.petcity.pickme.data.response.AdvertiseResponse;
import com.petcity.pickme.data.response.CommonResponse;
import com.petcity.pickme.databinding.ActivityMyAdsBinding;


public class MyAdsActivity extends BaseActivity<ActivityMyAdsBinding, MyAdsViewModel> {


    private CommonDialogSimple deleteDialog;
    private LoadingDialog loadingDialog;
    @Override
    protected boolean isHide() {
        return true;
    }

    @Override
    protected int getSysNavigationBarColor() {
        return R.color.darkBlue;
    }

    @Override
    protected Class<MyAdsViewModel> getViewModel() {
        return MyAdsViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_my_ads;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.loadData(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CreateAdsActivity.CREATE_OR_UPDATE_AD && resultCode == RESULT_OK ) {
            mViewModel.loadData(true);
        }
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mBinding.setViewModel(mViewModel);
        LinearDividerItemDecoration divider = new LinearDividerItemDecoration(MyAdsActivity.this,  LinearLayoutManager.VERTICAL);
        divider.setShowLastDivider(true);
        divider.setDrawable(ContextCompat.getColor(MyAdsActivity.this, R.color.white12), 1);
        mBinding.recyclerView.addItemDecoration(divider);
        mBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mViewModel.edit.observe(this, new Observer<AdvertiseResponse>() {
            @Override
            public void onChanged(@Nullable AdvertiseResponse aResponse) {
                CreateAdsActivity.satrtCreateForResult(MyAdsActivity.this, true, aResponse);
            }
        });
        mViewModel.delete.observe(this, new Observer<AdvertiseResponse>() {
            @Override
            public void onChanged(@Nullable AdvertiseResponse aResponse) {
                if (deleteDialog == null){
                    deleteDialog = new CommonDialogSimple.Builder()
                            .setTitle("Are you sure to remove the current ad?")
                            .showAction2(false)
                            .setAction1Btn("Delete", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mViewModel.deleteInDB(aResponse);
                                    deleteDialog.dismiss();
                                }
                            })
                            .setCancelBtn("Cancel", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteDialog.dismiss();
                                }
                            })
                            .create();
                }
                deleteDialog.show(getSupportFragmentManager(), null);
            }
        });
        mViewModel.deleteInDB.observe(this, new Observer<LiveDataWrapper<CommonResponse>>() {
            @Override
            public void onChanged(LiveDataWrapper<CommonResponse> commonResponseLiveDataWrapper) {
                switch (commonResponseLiveDataWrapper.status) {
                    case LOADING:
                        if (loadingDialog == null)
                            loadingDialog = new LoadingDialog();
                        loadingDialog.show(getSupportFragmentManager(), null);
                        break;
                    case SUCCESS:
                        Toast.makeText(MyAdsActivity.this, "Delete successful!", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                        break;
                    case ERROR:
                        Toast.makeText(MyAdsActivity.this, "Delete failed, casused by" + commonResponseLiveDataWrapper.error.getMessage(), Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                        break;
                }
            }
        });

    }
}