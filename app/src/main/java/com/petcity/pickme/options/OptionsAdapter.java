package com.petcity.pickme.options;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.petcity.pickme.R;
import com.petcity.pickme.data.request.TestInfo;
import com.petcity.pickme.databinding.OptionItemBinding;



import java.util.List;

/**
 * @ClassName OptionsAdapter
 * @Description TODO
 * @Author sherily
 * @Date 22/01/20 2:05 PM
 * @Version 1.0
 */
public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ItemViewHolder>{

    private LayoutInflater inflater;
    private List<TestInfo> datas;
    private Object mEventObject;

    public void setmEventObject(Object mEventObject) {
        this.mEventObject = mEventObject;
    }

    public void setData(List<TestInfo> data) {
        if (null != datas) {
            datas.clear();
        }
        this.datas = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (null == inflater)
            inflater = LayoutInflater.from(parent.getContext());
        return new ItemViewHolder(DataBindingUtil.inflate(inflater, R.layout.option_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(mEventObject, datas.get(position));
    }

    @Override
    public int getItemCount() {
        return null == datas ? 0 : datas.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private OptionItemBinding binding;
        public ItemViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = (OptionItemBinding) binding;
        }
        public void bind(Object mEventHandler, TestInfo model) {
            binding.setViewModel((OptionsViewModel) mEventHandler);
            binding.setModel(model);
        }
    }
}
