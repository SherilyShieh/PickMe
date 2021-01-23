package com.petcity.pickme.common.widget;

import android.annotation.SuppressLint;
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
import android.view.WindowManager;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.petcity.pickme.R;
import com.petcity.pickme.databinding.CommonDialogLayout2Binding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName CommonDialogSimple
 * @Description CommonDialogSimple
 * @Author sherily
 * @Date 17/01/21 4:32 PM
 * @Version 1.0
 */
public class CommonDialogSimple extends DialogFragment {
    private static final String KEY_PARAM = "PARAM";


    View.OnClickListener mCancelListener;
    View.OnClickListener mAction1Listener;
    View.OnClickListener mAction2Listener;

    @SuppressLint("ValidFragment")
    private CommonDialogSimple() {
    }

    public void setmCancelListener(View.OnClickListener mCancelListener) {
        this.mCancelListener = mCancelListener;
    }

    public void setmAction1Listener(View.OnClickListener mAction1Listener) {
        this.mAction1Listener = mAction1Listener;
    }

    public void setmAction2Listener(View.OnClickListener mAction2Listener) {
        this.mAction2Listener = mAction2Listener;
    }

    private CommonDialogSimple.DialogParam mParam;
    private CommonDialogLayout2Binding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.dialog_no_bg);
    }


    public SpannableStringBuilder matcherSearchText(int color, float textSize, String text, String keyword) {
        if (textSize == 0.0f)
            textSize = 14f;
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, getResources().getDisplayMetrics());
        SpannableStringBuilder ss = new SpannableStringBuilder(text);
        Pattern pattern = Pattern.compile(keyword);
        Matcher matcher = pattern.matcher(ss);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new AbsoluteSizeSpan(size), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.common_dialog_layout_2, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        if (null != getArguments()) {
            mParam = getArguments().getParcelable(KEY_PARAM);
            binding.title.setVisibility(mParam.showTitle ? View.VISIBLE : View.GONE);
            if (!TextUtils.isEmpty(mParam.keyword)) {
                binding.title.setText(matcherSearchText(mParam.keyColor, mParam.keySize, mParam.title, mParam.keyword));

            } else {
                binding.title.setText(mParam.title);
            }
            binding.title.setGravity(mParam.contentGravity);
            binding.cancel.setText(mParam.cancelStr);
            binding.cancel.setTextColor(ContextCompat.getColor(binding.cancel.getContext(), mParam.cancelColor));
            binding.cancel.setVisibility(mParam.showCancel ? View.VISIBLE : View.GONE);
            binding.action1.setText(mParam.action1Str);
            binding.action1.setTextColor(ContextCompat.getColor(binding.action1.getContext(), mParam.action1Color));
            binding.action1.setVisibility(mParam.showAction1 ? View.VISIBLE : View.GONE);
            binding.action2.setText(mParam.action2Str);
            binding.action2.setTextColor(ContextCompat.getColor(binding.action2.getContext(), mParam.action2Color));
            binding.action2.setVisibility(mParam.showAction2 ? View.VISIBLE : View.GONE);
        }
        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCancelListener)
                    mCancelListener.onClick(v);
            }
        });
        binding.action1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAction1Listener)
                    mAction1Listener.onClick(v);
            }
        });
        binding.action2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAction2Listener)
                    mAction2Listener.onClick(v);
            }
        });
        return binding.getRoot();
    }

    static CommonDialogSimple newInstance(DialogParam param) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_PARAM, param);
        CommonDialogSimple fragment = new CommonDialogSimple();
        fragment.setArguments(args);
        return fragment;
    }

    public static class Builder {
        private DialogParam mParam;

        private View.OnClickListener mCancelListener;

        private View.OnClickListener mAction1Listener;

        private View.OnClickListener mAction2Listener;

        public Builder() {
            mParam = new DialogParam();
        }

        public Builder setTitle(String title) {
            mParam.title = title;
            return this;
        }

        public Builder showTitle(boolean show) {
            mParam.showTitle = show;
            return this;
        }

        public Builder setCancelBtn(String text, View.OnClickListener listener) {
            mCancelListener = listener;
            mParam.cancelStr = text;
            return this;
        }

        public Builder setCancelColor(@ColorRes int res) {
            mParam.cancelColor = res;
            return this;
        }

        public Builder showCancel(boolean show) {
            mParam.showCancel = show;
            return this;
        }

        public Builder setAction1Btn(String text, View.OnClickListener listener) {
            mAction1Listener = listener;
            mParam.action1Str = text;
            return this;
        }

        public Builder setAction1Color(@ColorRes int res) {
            mParam.action1Color = res;
            return this;
        }

        public Builder showAction1(boolean show) {
            mParam.showAction1 = show;
            return this;
        }

        public Builder setAction2Btn(String text, View.OnClickListener listener) {
            mAction2Listener = listener;
            mParam.action2Str = text;
            return this;
        }

        public Builder setAction2Color(@ColorRes int res) {
            mParam.action2Color = res;
            return this;
        }

        public Builder showAction2(boolean show) {
            mParam.showAction2 = show;
            return this;
        }

        public Builder setKey(String keyWord, int keyColor, float keySize) {
            mParam.keyword = keyWord;
            mParam.keyColor = keyColor;
            mParam.keySize = keySize;
            return this;
        }

        public Builder setContentGravity(int gravity) {
            mParam.contentGravity = gravity;
            return this;
        }

        public CommonDialogSimple create() {
            CommonDialogSimple fragment = newInstance(mParam);
            fragment.setmCancelListener(mCancelListener);
            fragment.setmAction1Listener(mAction1Listener);
            fragment.setmAction2Listener(mAction2Listener);
            return fragment;
        }
    }

    static class DialogParam implements Parcelable {

        String title = "";
        boolean showTitle = true;

        String cancelStr = "";
        boolean showCancel = true;
        int cancelColor = R.color.blue;

        String action1Str = "";
        boolean showAction1 = true;
        int action1Color = R.color.blue;

        String action2Str = "";
        boolean showAction2 = true;
        int action2Color = R.color.blue;


        String keyword = "";
        int keyColor = R.color.blue;
        float keySize = 14f;

        int contentGravity = Gravity.TOP;

        public DialogParam() {
        }

        protected DialogParam(Parcel in) {
            title = in.readString();
            showTitle = in.readByte() != 0;
            cancelStr = in.readString();
            showCancel = in.readByte() != 0;
            cancelColor = in.readInt();
            action1Str = in.readString();
            showAction1 = in.readByte() != 0;
            action1Color = in.readInt();
            action2Str = in.readString();
            showAction2 = in.readByte() != 0;
            action2Color = in.readInt();
            keyword = in.readString();
            keyColor = in.readInt();
            keySize = in.readFloat();
            contentGravity = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeByte((byte) (showTitle ? 1 : 0));
            dest.writeString(cancelStr);
            dest.writeByte((byte) (showCancel ? 1 : 0));
            dest.writeInt(cancelColor);
            dest.writeString(action1Str);
            dest.writeByte((byte) (showAction1 ? 1 : 0));
            dest.writeInt(action1Color);
            dest.writeString(action2Str);
            dest.writeByte((byte) (showAction2 ? 1 : 0));
            dest.writeInt(action2Color);
            dest.writeString(keyword);
            dest.writeInt(keyColor);
            dest.writeFloat(keySize);
            dest.writeInt(contentGravity);

        }

        public static final Creator<CommonDialog.DialogParam> CREATOR = new Creator<CommonDialog.DialogParam>() {
            @Override
            public CommonDialog.DialogParam createFromParcel(Parcel in) {
                return new CommonDialog.DialogParam(in);
            }

            @Override
            public CommonDialog.DialogParam[] newArray(int size) {
                return new CommonDialog.DialogParam[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }


    }
}
