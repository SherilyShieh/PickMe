package com.petcity.pickme.contacted;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.shape.ShapeAppearanceModel;
import com.petcity.pickme.R;
import com.petcity.pickme.ads.MyAdsAdapter;
import com.petcity.pickme.ads.MyAdsViewModel;
import com.petcity.pickme.data.response.AdvertiseResponse;
import com.petcity.pickme.data.response.ContactedResponse;
import com.petcity.pickme.databinding.MyAdsItemBinding;
import com.petcity.pickme.databinding.MyContactedItemBinding;

import java.util.List;

/**
 * @ClassName MyContactedAdapter
 * @Description TODO
 * @Author sherily
 * @Date 22/01/21 8:42 PM
 * @Version 1.0
 */
public class MyContactedAdapter extends RecyclerView.Adapter<MyContactedAdapter.ViewHolder>{

    private LayoutInflater inflater;
    private List<ContactedResponse> datas;
    private Object mEventObject;

    public void refresh(List<ContactedResponse> data){
        if (null != datas){
            datas.clear();
        }
        this.datas = data;
        notifyDataSetChanged();
    }

    public void loadMore(List<ContactedResponse> data){
        this.datas.addAll(data);
        notifyDataSetChanged();
    }

    public void setmEventObject(Object mEventObject) {
        this.mEventObject = mEventObject;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (null == inflater )
            inflater = LayoutInflater.from(parent.getContext());
        return new MyContactedAdapter.ViewHolder(DataBindingUtil.inflate(inflater, R.layout.my_contacted_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mEventObject, datas.get(position));
    }

    @Override
    public int getItemCount() {
        return null == datas ? 0 : datas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private MyContactedItemBinding binding;
        public ViewHolder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = (MyContactedItemBinding) binding;
        }

        public void bind(Object mEventHandler, ContactedResponse model){
            binding.setViewModel((MyContactedViewModel) mEventHandler);
            binding.setModel(model);
            ShapeAppearanceModel shape = ShapeAppearanceModel.builder().setAllCornerSizes(ShapeAppearanceModel.PILL).build();
            binding.avatar.setShapeAppearanceModel(shape);
        }
    }
}
