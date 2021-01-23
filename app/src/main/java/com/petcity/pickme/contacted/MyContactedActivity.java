package com.petcity.pickme.contacted;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.petcity.pickme.R;
import com.petcity.pickme.ads.MyAdsActivity;
import com.petcity.pickme.base.BaseActivity;
import com.petcity.pickme.base.LiveDataWrapper;
import com.petcity.pickme.common.utils.ClipBoard;
import com.petcity.pickme.common.widget.CommonDialogSimple;
import com.petcity.pickme.common.widget.DividerItemDecoration;
import com.petcity.pickme.common.widget.LinearDividerItemDecoration;
import com.petcity.pickme.common.widget.LoadingDialog;
import com.petcity.pickme.data.response.AdvertiseResponse;
import com.petcity.pickme.data.response.CommonResponse;
import com.petcity.pickme.data.response.ContactedResponse;
import com.petcity.pickme.databinding.ActivityMyContactedBinding;
import com.petcity.pickme.home.HomeActivity;

public class MyContactedActivity extends BaseActivity<ActivityMyContactedBinding, MyContactedViewModel> {

    private CommonDialogSimple copyEmailDialog;
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
    protected Class<MyContactedViewModel> getViewModel() {
        return MyContactedViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_my_contacted;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mBinding.setViewModel(mViewModel);
        LinearDividerItemDecoration divider = new LinearDividerItemDecoration(MyContactedActivity.this, LinearLayoutManager.VERTICAL);
        divider.setShowLastDivider(true);
        divider.setDrawable(ContextCompat.getColor(MyContactedActivity.this, R.color.white12), 1);
        mBinding.recyclerView.addItemDecoration(divider);
        mBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mViewModel.resend.observe(this, new Observer<ContactedResponse>() {
            @Override
            public void onChanged(@Nullable ContactedResponse aResponse) {
                if (copyEmailDialog != null) {
                    copyEmailDialog.dismiss();
                    copyEmailDialog = null;
                }

                copyEmailDialog = new CommonDialogSimple.Builder()
                        .setTitle("Please copy the user's email and congtact the user:" + aResponse.getAdvertise().getUser().getEmail())
                        .setKey(aResponse.getAdvertise().getUser().getEmail(), 0xff0290FA, 14)
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
                                ClipBoard.copyClipboard(MyContactedActivity.this, aResponse.getAdvertise().getUser().getEmail());
                                Toast.makeText(MyContactedActivity.this, "Copy email successfully", Toast.LENGTH_SHORT).show();
                                copyEmailDialog.dismiss();
                            }
                        })
                        .create();
                copyEmailDialog.show(getSupportFragmentManager(), null);
            }
        });
        mViewModel.delete.observe(this, new Observer<ContactedResponse>() {
            @Override
            public void onChanged(@Nullable ContactedResponse aResponse) {
                if (deleteDialog == null) {
                    deleteDialog = new CommonDialogSimple.Builder()
                            .setTitle("Are you sure to remove the current record?")
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
                        Toast.makeText(MyContactedActivity.this, "Delete successful!", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                        break;
                    case ERROR:
                        Toast.makeText(MyContactedActivity.this, "Delete failed, casused by" + commonResponseLiveDataWrapper.error.getMessage(), Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                        break;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.loadData(true);
    }

}