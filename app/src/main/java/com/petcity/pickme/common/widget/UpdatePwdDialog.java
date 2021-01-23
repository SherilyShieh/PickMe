package com.petcity.pickme.common.widget;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.petcity.pickme.R;
import com.petcity.pickme.common.utils.RegexUtils;
import com.petcity.pickme.databinding.DialogUpdatePasswordBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UpdatePwdDialog
 * @Description UpdatePwdDialog
 * @Author sherily
 * @Date 17/01/21 6:05 PM
 * @Version 1.0
 */
public class UpdatePwdDialog extends DialogFragment {

    private static final String KEY_PARAM = "PARAM";
    private DialogUpdatePasswordBinding binding;
    private List<TextInputEditText> errorList;

    public interface OnClickListener {
        void onClick(View v, String password);
    }

    View.OnClickListener mCancelListener;

    OnClickListener mConfirmListener;


    @SuppressLint("ValidFragment")
    private UpdatePwdDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.dialog_no_bg);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_update_password, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding.pwdTxt.addTextChangedListener(txtWatcher(binding.password));
        binding.pwdConfirmTxt.addTextChangedListener(txtWatcher(binding.confirmPwd));
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
                errorList = new ArrayList();
                boolean validatePwd = validatePwd();
                boolean validatePwdConfirm = validatePwdConfirm();
                if (validatePwd && validatePwdConfirm) {
                    if (null != mConfirmListener)
                        mConfirmListener.onClick(v, binding.pwdTxt.getText().toString().trim());
                }
                if (!errorList.isEmpty() && !errorList.get(0).hasFocus()) {
                    errorList.get(0).requestFocus();
                }
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        binding.pwdTxt.setText("");
        binding.pwdConfirmTxt.setText("");
    }

    private TextWatcher txtWatcher(TextInputLayout textLayout) {
        return new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                textLayout.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                textLayout.setError(null);

            }
        };
    }

    private boolean validatePwd() {
        String pwd = binding.pwdTxt.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            binding.password.setError("Password cannot be empty!");
            errorList.add(binding.pwdTxt);
            return false;
        } else if (!RegexUtils.isValidPwd(pwd)) {
            binding.password.setError("Invalid password: password must be composed of " +
                    "6-20 characters and contain numbers and letters!");
            errorList.add(binding.pwdTxt);
            return false;
        }
        return true;
    }

    private boolean validatePwdConfirm() {
        String pwdConfirm = binding.pwdConfirmTxt.getText().toString().trim();
        String pwd = binding.pwdTxt.getText().toString().trim();
        if (TextUtils.isEmpty(pwdConfirm)) {
            binding.confirmPwd.setError("Confirm password cannot be empty!");
            errorList.add(binding.pwdConfirmTxt);
            return false;
        }
        if (!TextUtils.isEmpty(pwd) && !TextUtils.equals(pwd, pwdConfirm)) {
            binding.confirmPwd.setError("Two inconsistent passwords!");
            errorList.add(binding.pwdConfirmTxt);
            return false;
        }
        return true;
    }

    public void setmCancelListener(View.OnClickListener mCancelListener) {
        this.mCancelListener = mCancelListener;
    }

    public void setmConfirmListener(OnClickListener mConfirmListener) {
        this.mConfirmListener = mConfirmListener;
    }

    static UpdatePwdDialog newInstance() {
        UpdatePwdDialog fragment = new UpdatePwdDialog();
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

        public UpdatePwdDialog create() {
            UpdatePwdDialog fragment = newInstance();
            fragment.setmCancelListener(mCancelListener);
            fragment.setmConfirmListener(mConfirmListener);
            return fragment;
        }
    }
}
