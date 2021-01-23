package com.petcity.pickme.common.widget;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.petcity.pickme.R;
import com.petcity.pickme.common.utils.PreferenceManager;
import com.petcity.pickme.common.utils.RegexUtils;
import com.petcity.pickme.data.response.User;
import com.petcity.pickme.databinding.DialogUpdateEmailBinding;
import com.petcity.pickme.databinding.DialogUpdateGenderBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UpdatePwdDialog
 * @Description UpdatePwdDialog
 * @Author sherily
 * @Date 17/01/21 6:05 PM
 * @Version 1.0
 */
public class UpdateGenderDialog extends DialogFragment {

    private static final String KEY_PARAM = "PARAM";
    private DialogUpdateGenderBinding binding;
    private List<TextInputEditText> errorList;

    public interface OnClickListener {
        void onClick(View v, String gender);
    }

    View.OnClickListener mCancelListener;

    OnClickListener mConfirmListener;


    @SuppressLint("ValidFragment")
    private UpdateGenderDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.dialog_no_bg);
    }

    private String gender;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_update_gender, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup Group, int Checkid) {
                if (binding.female.isChecked()) {
                    binding.female.setChecked(true);
                    gender = "Female";
                }
                if (binding.male.isChecked()) {
                    binding.male.setChecked(true);
                    gender = "Male";
                }
                if (binding.x.isChecked()) {
                    binding.x.setChecked(true);
                    gender = "X";
                }
            }
        });
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCancelListener)
                    mCancelListener.onClick(v);
            }
        });
        binding.buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(gender)) {
                    Toast.makeText(getContext(), "Please select your gender!", Toast.LENGTH_SHORT).show();
                } else if (null != mConfirmListener) {
                    mConfirmListener.onClick(v, gender);
                }

            }
        });
        return binding.getRoot();
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        gender = null;
        binding.female.setChecked(false);
        binding.male.setChecked(false);
        binding.x.setChecked(false);

    }


    public void setmCancelListener(View.OnClickListener mCancelListener) {
        this.mCancelListener = mCancelListener;
    }

    public void setmConfirmListener(OnClickListener mConfirmListener) {
        this.mConfirmListener = mConfirmListener;
    }

    static UpdateGenderDialog newInstance() {
        UpdateGenderDialog fragment = new UpdateGenderDialog();
        return fragment;
    }

    public static class Builder {

        private View.OnClickListener mCancelListener;

        private OnClickListener mConfirmListener;

        public Builder() {
        }

        public Builder setOnCancelClickListener(View.OnClickListener listener) {
            mCancelListener = listener;
            return this;
        }

        public Builder setOnConfirmClickListener(OnClickListener listener) {
            mConfirmListener = listener;
            return this;
        }

        public UpdateGenderDialog create() {
            UpdateGenderDialog fragment = newInstance();
            fragment.setmCancelListener(mCancelListener);
            fragment.setmConfirmListener(mConfirmListener);
            return fragment;
        }
    }
}
