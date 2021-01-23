package com.petcity.pickme.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.shape.ShapeAppearanceModel;
import com.petcity.pickme.R;
import com.petcity.pickme.data.response.AdvertiseResponse;
import com.petcity.pickme.databinding.HomeAdItemBinding;

import java.util.List;

/**
 * @ClassName AdvertiseAdapter
 * @Description AdvertiseAdapter
 * @Author sherily
 * @Date 15/01/21 4:04 PM
 * @Version 1.0
 */
public class AdvertiseAdapter extends RecyclerView.Adapter<AdvertiseAdapter.ItemViewHolder> {

    private LayoutInflater inflater;
    private List<AdvertiseResponse> datas;
    private Object mEventObject;

    public void refresh(List<AdvertiseResponse> data) {
        if (null != datas) {
            datas.clear();
        }
        this.datas = data;
        notifyDataSetChanged();
    }

    public void loadMore(List<AdvertiseResponse> data) {
        this.datas.addAll(data);
        notifyDataSetChanged();
    }


    public void setmEventObject(Object mEventObject) {
        this.mEventObject = mEventObject;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (null == inflater)
            inflater = LayoutInflater.from(parent.getContext());
        return new ItemViewHolder(DataBindingUtil.inflate(inflater, R.layout.home_ad_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(mEventObject, datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private HomeAdItemBinding binding;

        public ItemViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = (HomeAdItemBinding) binding;
        }

        public void bind(Object mEventHandler, AdvertiseResponse model) {
            binding.setViewModel((HomeViewModel) mEventHandler);
            binding.setModel(model);
            ShapeAppearanceModel shape = ShapeAppearanceModel.builder().setAllCornerSizes(ShapeAppearanceModel.PILL).build();
            binding.avatar.setShapeAppearanceModel(shape);
        }


    }
}
