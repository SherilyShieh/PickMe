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
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.petcity.pickme.R;
import com.petcity.pickme.common.utils.DogBreedUtil;
import com.petcity.pickme.common.utils.LocationNZUtils;
import com.petcity.pickme.create.CreateAdsActivity;
import com.petcity.pickme.databinding.CommonDialogLayout2Binding;
import com.petcity.pickme.databinding.SearchDialogLayoutBinding;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName CommonDialogSimple
 * @Description CommonDialogSimple
 * @Author sherily
 * @Date 17/01/21 4:32 PM
 * @Version 1.0
 */
public class SearchDialog extends DialogFragment {
    private static final String KEY_PARAM = "PARAM";


    View.OnClickListener mCancelListener;
    OnSearchListener mOnSearchListener;
    View.OnClickListener mAction2Listener;

    public interface OnSearchListener {
        void onSearch(String region, String district, String breed);
    }

    @SuppressLint("ValidFragment")
    private SearchDialog() {
    }

    public void setmCancelListener(View.OnClickListener mCancelListener) {
        this.mCancelListener = mCancelListener;
    }

    public void setmOnSearchListener(OnSearchListener mAction1Listener) {
        this.mOnSearchListener = mAction1Listener;
    }

    public void setmAction2Listener(View.OnClickListener mAction2Listener) {
        this.mAction2Listener = mAction2Listener;
    }

    private SearchDialogLayoutBinding binding;

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

    @LayoutRes
    public int getAdapterItemLayout() {
        return R.layout.cat_exposed_dropdown_popup_item;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.search_dialog_layout, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        binding.breed.setDropDownBackgroundResource(R.drawable.white_bg);
        List<String> breeds = DogBreedUtil.CreateDogBreed();
        breeds.add(0, "All");
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        getAdapterItemLayout(),
                        breeds);
        binding.breed.setAdapter(adapter);
        binding.breed.setThreshold(0);
        binding.breed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int icon = hasFocus ? R.mipmap.down2 : R.mipmap.up2;
                binding.breed.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, icon, 0);
                if (hasFocus) {
                    binding.breed.showDropDown();
                } else {
                    binding.breed.dismissDropDown();
                }
            }
        });
        binding.region.setDropDownBackgroundResource(R.drawable.white_bg);
        ArrayAdapter<String> adapter2 =
                new ArrayAdapter<>(
                        getContext(),
                        getAdapterItemLayout(),
                        LocationNZUtils.getRegionNames());
        binding.region.setAdapter(adapter2);
        binding.region.setThreshold(0);
        binding.region.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int icon = hasFocus ? R.mipmap.down2 : R.mipmap.up2;
                binding.region.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, icon, 0);
                if (hasFocus) {
                    binding.region.showDropDown();
                } else {
                    binding.region.dismissDropDown();
                }
            }
        });
        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCancelListener)
                    mCancelListener.onClick(v);
            }
        });
        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String region = binding.region.getText().toString().trim();
                String breed = binding.breed.getText().toString().trim();
                if (TextUtils.isEmpty(region) && TextUtils.isEmpty(breed)) {
                    Toast.makeText(getContext(), "Please enter at least one search keyword!", Toast.LENGTH_SHORT).show();
                }
                if (null != mOnSearchListener)
                    mOnSearchListener.onSearch(region, null, breed);
            }
        });
        binding.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.breed.setText("All");
                binding.region.setText("All");
                binding.district.setText("All");
                if (null != mAction2Listener)
                    mAction2Listener.onClick(v);
            }
        });
        return binding.getRoot();
    }

    static SearchDialog newInstance() {
        SearchDialog fragment = new SearchDialog();
        return fragment;
    }

    public static class Builder {

        private View.OnClickListener mCancelListener;

        private OnSearchListener mOnSearchListener;

        private View.OnClickListener mAction2Listener;

        public Builder() {

        }


        public Builder setOnCancelListener(View.OnClickListener listener) {
            mCancelListener = listener;
            return this;
        }

        public Builder setOnSearchListener(OnSearchListener listener) {
            mOnSearchListener = listener;
            return this;
        }

        public Builder setOnClearListener(View.OnClickListener listener) {
            mAction2Listener = listener;
            return this;
        }


        public SearchDialog create() {
            SearchDialog fragment = newInstance();
            fragment.setmCancelListener(mCancelListener);
            fragment.setmOnSearchListener(mOnSearchListener);
            fragment.setmAction2Listener(mAction2Listener);
            return fragment;
        }
    }

}
