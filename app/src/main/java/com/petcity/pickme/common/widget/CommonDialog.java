package com.petcity.pickme.common.widget;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.petcity.pickme.R;
import com.petcity.pickme.databinding.CommonDialogLayoutBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName CommonDialog
 * @Description TODO
 * @Author sherily
 * @Date 13/01/21 7:03 PM
 * @Version 1.0
 */
public class CommonDialog extends DialogFragment {

    private static final String KEY_PARAM = "PARAM";


    View.OnClickListener mCancelListener;

    OnClickListener mConfirmListener;

    public interface OnClickListener {

        void onClick(View v, String str);

    }
    @SuppressLint("ValidFragment")
    private CommonDialog() {
    }

    public void setmCancelListener(View.OnClickListener mCancelListener) {
        this.mCancelListener = mCancelListener;
    }

    public void setmConfirmListener(OnClickListener mConfirmListener) {
        this.mConfirmListener = mConfirmListener;
    }

    private DialogParam mParam;
    private CommonDialogLayoutBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.dialog_no_bg);

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        binding.edit.setText("");
    }

    public SpannableStringBuilder matcherSearchText(int color, float textSize, String text, String keyword) {
        if ( textSize == 0.0f )
            textSize = 14f;
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, getResources().getDisplayMetrics());
        SpannableStringBuilder ss = new SpannableStringBuilder(text);
        Pattern pattern = Pattern.compile(keyword);
        Matcher matcher = pattern.matcher(ss);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new AbsoluteSizeSpan(size), start , end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.common_dialog_layout,container,false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (null != getArguments()){
            mParam = getArguments().getParcelable(KEY_PARAM);
            binding.title.setVisibility(mParam.showTitle ? View.VISIBLE : View.GONE);
            binding.title.setText(mParam.title);
            binding.content.setTextColor( ContextCompat.getColor(binding.content.getContext(), mParam.contentColor));
            if (!TextUtils.isEmpty(mParam.keyword)) {
                binding.content.setText( matcherSearchText(mParam.keyColor,mParam.keySize,mParam.content,mParam.keyword));

            } else {
                binding.content.setText(mParam.content);
            }
            binding.content.setGravity(mParam.contentGravity);
            binding.edit.setHint(mParam.editPlaceholder);
            binding.buttonCancel.setText(mParam.cancelStr);
            binding.buttonCancel.setTextColor( ContextCompat.getColor(binding.buttonCancel.getContext(), mParam.cancelColor));
            binding.buttonCancel.setVisibility(mParam.showCancel ? View.VISIBLE : View.GONE);
            binding.buttonConfirm.setText(mParam.confirmStr);
            binding.buttonConfirm.setTextColor(ContextCompat.getColor(binding.buttonConfirm.getContext(),mParam.confirmColor));
            binding.buttonConfirm.setVisibility(mParam.showConfirm ? View.VISIBLE : View.GONE);
            binding.editFl.setVisibility(mParam.showEditFl ? View.VISIBLE : View.GONE);
        }
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
                if (null != mConfirmListener)
                    mConfirmListener.onClick(v, binding.edit.getText().toString().trim());
            }
        });
        return binding.getRoot();
    }

    static CommonDialog newInstance(DialogParam param){
        Bundle args = new Bundle();
        args.putParcelable(KEY_PARAM, param);
        CommonDialog fragment = new CommonDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static class Builder {
        private DialogParam mParam;

        private View.OnClickListener mCancelListener;

        private OnClickListener mConfirmListener;

        public Builder() {
            mParam = new DialogParam();
        }

        public Builder setTitle(String title){
            mParam.title = title;
            return this;
        }

        public Builder showTitle(boolean show){
            mParam.showTitle = show;
            return this;
        }

        public Builder setContent(String content){
            mParam.content = content;
            return this;
        }

        public Builder setPlaceHolder(String content){
            mParam.editPlaceholder = content;
            return this;
        }

        public Builder setCancelBtn(String text, View.OnClickListener listener){
            mCancelListener = listener;
            mParam.cancelStr = text;
            return this;
        }

        public Builder setCancelColor(@ColorRes int res){
            mParam.cancelColor = res;
            return this;
        }

        public Builder showCancel(boolean show){
            mParam.showCancel = show;
            return this;
        }
        public Builder setConfirmBtn(String text, OnClickListener listener){
            mConfirmListener = listener;
            mParam.confirmStr = text;
            return this;
        }

        public Builder setConfirmColor(@ColorRes int res){
            mParam.confirmColor = res;
            return this;
        }

        public Builder showConfim(boolean show){
            mParam.showConfirm = show;
            return this;
        }

        public Builder showEdit(boolean show){
            mParam.showEditFl = show;
            return this;
        }

        public Builder setKey(String keyWord,int keyColor,float keySize){
            mParam.keyword = keyWord;
            mParam.keyColor = keyColor;
            mParam.keySize = keySize;
            return this;
        }

        public Builder setContentGravity(int gravity){
            mParam.contentGravity = gravity;
            return this;
        }

        public Builder setContentColor(int color){
            mParam.contentColor = color;
            return this;
        }
        public CommonDialog create() {
            CommonDialog fragment = newInstance(mParam);
            fragment.setmCancelListener(mCancelListener);
            fragment.setmConfirmListener(mConfirmListener);
            return fragment;
        }
    }

    static class DialogParam implements Parcelable {

        String title = "";
        boolean showTitle = true;

        String content = "";
        String editPlaceholder = "";

        String cancelStr = "";
        boolean showCancel = true;
        int cancelColor = R.color.blue;

        String confirmStr = "";
        boolean showConfirm = true;
        int confirmColor = R.color.blue;

        boolean showEditFl = true;

        String keyword = "";
        int keyColor = R.color.blue;
        float keySize = 14f;

        int contentGravity = Gravity.TOP;
        int contentColor = R.color.secondaryTxt;

        public DialogParam() {
        }

        protected DialogParam(Parcel in) {
            title = in.readString();
            showTitle = in.readByte() != 0;
            content = in.readString();
            editPlaceholder = in.readString();
            cancelStr = in.readString();
            showCancel = in.readByte() != 0;
            cancelColor = in.readInt();
            confirmStr = in.readString();
            showConfirm = in.readByte() != 0;
            confirmColor = in.readInt();
            showEditFl = in.readByte() != 0;
            keyword = in.readString();
            keyColor = in.readInt();
            keySize = in.readFloat();
            contentGravity = in.readInt();
            contentColor = in.readInt();
        }
        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeByte((byte) (showTitle ? 1 : 0));
            dest.writeString(content);
            dest.writeString(editPlaceholder);
            dest.writeString(cancelStr);
            dest.writeByte((byte)(showCancel ? 1 : 0));
            dest.writeInt(cancelColor);
            dest.writeString(confirmStr);
            dest.writeByte((byte)(showConfirm ? 1 : 0));
            dest.writeInt(confirmColor);
            dest.writeByte((byte)(showEditFl ? 1 : 0));
            dest.writeString(keyword);
            dest.writeInt(keyColor);
            dest.writeFloat(keySize);
            dest.writeInt(contentGravity);
            dest.writeInt(contentColor);

        }

        public static final Creator<DialogParam> CREATOR = new Creator<DialogParam>() {
            @Override
            public DialogParam createFromParcel(Parcel in) {
                return new DialogParam(in);
            }

            @Override
            public DialogParam[] newArray(int size) {
                return new DialogParam[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }


    }

}
