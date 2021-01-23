package com.petcity.pickme.common.widget;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * @ClassName Constants
 * @Description Constants
 * @Author sherily
 * @Date 6/01/21 2:35 PM
 * @Version 1.0
 * Used 自定义RecyclerView适配器
 */
public class WRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = WRecyclerViewAdapter.class.getSimpleName();

    private RecyclerView.Adapter adapter;
    /**
     * 是否启用上拉加载功能的标记：true-使用；false-禁用
     */
    private boolean mEnablePullLoad;
    /**
     * 上拉加载区域（foot区域）
     */
    private WRecyclerViewFooter mFooterView;

    //下面的ItemViewType是保留值(ReservedItemViewType),如果用户的adapter与它们重复将会强制抛出异常。不过为了简化,我们检测到重复时对用户的提示是ItemViewType必须小于10000
    private static final int TYPE_FOOTER = 100001;//设置一个很大的数字,尽可能避免和用户的adapter冲突
    private static final int TYPE_ITEM = 100002;

    public WRecyclerViewAdapter(RecyclerView.Adapter adapter, WRecyclerViewFooter mFooterView, boolean mEnablePullLoad) {
        this.adapter = adapter;
        this.mFooterView = mFooterView;
        this.mEnablePullLoad = mEnablePullLoad;
    }

    /**
     * 获取总的条目数
     */
    @Override
    public int getItemCount() {
        if (adapter == null) return 0;
        int itemCount = adapter.getItemCount();
        if (mEnablePullLoad) {//如果启用上拉加载区域，那么就需要在原来的列表总数基础上加1
            itemCount = itemCount + 1;
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooter(position)) {
            return TYPE_FOOTER;
        } else {
//        	return TYPE_ITEM;//不能return TYPE_ITEM，因为adapter中可能会设置不同的类型
            return adapter.getItemViewType(position);
        }
    }

    /**
     * 判断是否属于上拉加载区域-即最后一行
     */
    public boolean isFooter(int position) {
        if (mEnablePullLoad) {//如果启用上拉加载区域，那么最后一行，就是总数目- 1
            return position == getItemCount() - 1;
        } else {
            return false;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            return new SimpleViewHolder(mFooterView);
        }
        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int adapterCount;
        if (adapter != null) {
            adapterCount = adapter.getItemCount();
            if (position < adapterCount) {
                adapter.onBindViewHolder(holder, position);
                return;
            }
        }
    }

    /**
     * 简单的ViewHolder
     */
    private class SimpleViewHolder extends RecyclerView.ViewHolder {
        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (adapter == null) return;
        //对于九宫格样式，需要特殊处理
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    //如果是底部上拉加载区域，则独占一行
                    return isFooter(position) ? gridManager.getSpanCount() : 1;
                }
            });
        }
        adapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        if (adapter == null) return;
        adapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (adapter == null) return;
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams && isFooter(holder.getLayoutPosition())) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
        adapter.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (adapter == null) return;
        adapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if (adapter == null) return;
        adapter.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        return adapter.onFailedToRecycleView(holder);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        if (adapter == null) return;
        adapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        if (adapter == null) return;
        adapter.registerAdapterDataObserver(observer);
    }

    public boolean ismEnablePullLoad() {
        return mEnablePullLoad;
    }

    /**
     * 解决当第一页显示出来footview的时候刷新崩溃的问题
     */
    public void setmEnablePullLoad(boolean mEnablePullLoad) {
        this.mEnablePullLoad = mEnablePullLoad;
    }
}
