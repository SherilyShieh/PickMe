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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.petcity.pickme.R;
import com.petcity.pickme.common.utils.PreferenceManager;
import com.petcity.pickme.common.utils.RegexUtils;
import com.petcity.pickme.databinding.DialogUpdateEmailBinding;
import com.petcity.pickme.databinding.DialogUpdateNameBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UpdatePwdDialog
 * @Description TODO
 * @Author sherily
 * @Date 17/01/21 6:05 PM
 * @Version 1.0
 */
public class UpdateEmailDialog extends DialogFragment {

    private static final String KEY_PARAM = "PARAM";
    private DialogUpdateEmailBinding binding;
    private List<TextInputEditText> errorList;

    public interface OnClickListener {
        void onClick(View v, String email);
    }

    View.OnClickListener mCancelListener;

    OnClickListener mConfirmListener;


    @SuppressLint("ValidFragment")
    private UpdateEmailDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.dialog_no_bg);
    }

    @SuppressLint("ResourceAsColor")
    private void setWidth() {
        // 隐藏标题栏, 不加弹窗上方会一个透明的标题栏占着空间
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 必须设置这两个,才能设置宽度
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getDecorView().setBackgroundColor(R.color.black20);

        // 遮罩层透明度
        getDialog().getWindow().setDimAmount(0);

        // 设置宽度
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
//        params.windowAnimations = R.style.bottomSheet_animation;
        getDialog().getWindow().setAttributes(params);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_update_email,container,false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        setWidth();
//        binding.emailTxt.setHint(PreferenceManager.getInstance().getCurrentUserInfo().getEmail());
        binding.emailTxt.addTextChangedListener(txtWatcher(binding.email));
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
                if (validateEmail()) {
                    if (null != mConfirmListener)
                        mConfirmListener.onClick(v, binding.emailTxt.getText().toString().trim());
                }

            }
        });
        return binding.getRoot();
    }

    private boolean validateEmail() {
        errorList = new ArrayList<>();
        String email = binding.emailTxt.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            binding.email.setError("Email cannot be empty!");
            errorList.add(binding.emailTxt);
            return false;
        } else if (!RegexUtils.isValidEmail(email)) {
            binding.email.setError("Invalid email format!");
            errorList.add(binding.emailTxt);
            return false;
        } else if (TextUtils.equals(email, PreferenceManager.getInstance().getCurrentUserInfo().getEmail())) {
            binding.email.setError("Please bind a email different from the current one!");
            errorList.add(binding.emailTxt);
            return false;
        }
        return true;
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

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        binding.emailTxt.setText("");
    }


    public void setmCancelListener(View.OnClickListener mCancelListener) {
        this.mCancelListener = mCancelListener;
    }

    public void setmConfirmListener(OnClickListener mConfirmListener) {
        this.mConfirmListener = mConfirmListener;
    }

    static UpdateEmailDialog newInstance(){
        UpdateEmailDialog fragment = new UpdateEmailDialog();
        return fragment;
    }

    public static class Builder {

        private View.OnClickListener mCancelListener;

        private OnClickListener mConfirmListener;

        public Builder() {
        }
        public Builder setOnCancelClickListener(View.OnClickListener listener){
            mCancelListener = listener;
            return this;
        }

        public Builder setOnConfirmClickListener(OnClickListener listener){
            mConfirmListener = listener;
            return this;
        }

        public UpdateEmailDialog create() {
            UpdateEmailDialog fragment = newInstance();
            fragment.setmCancelListener(mCancelListener);
            fragment.setmConfirmListener(mConfirmListener);
            return fragment;
        }
    }
}
