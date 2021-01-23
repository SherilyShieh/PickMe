package com.petcity.pickme.ads;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.petcity.pickme.R;
import com.petcity.pickme.data.response.AdvertiseResponse;
import com.petcity.pickme.databinding.MyAdsItemBinding;

import java.util.List;

/**
 * @ClassName MyAdsAdapter
 * @Description TODO
 * @Author sherily
 * @Date 22/01/21 6:19 PM
 * @Version 1.0
 */
public class MyAdsAdapter extends RecyclerView.Adapter<MyAdsAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<AdvertiseResponse> datas;
    private Object mEventObject;

    public void refresh(List<AdvertiseResponse> data){
        if (null != datas){
            datas.clear();
        }
        this.datas = data;
        notifyDataSetChanged();
    }

    public void loadMore(List<AdvertiseResponse> data){
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
        return new MyAdsAdapter.ViewHolder(DataBindingUtil.inflate(inflater, R.layout.my_ads_item, parent, false));
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
        private MyAdsItemBinding binding;

        public ViewHolder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = (MyAdsItemBinding) binding;
        }

        public void bind(Object mEventHandler, AdvertiseResponse model){
            binding.setViewModel((MyAdsViewModel) mEventHandler);
            binding.setModel(model);
        }
    }
}
