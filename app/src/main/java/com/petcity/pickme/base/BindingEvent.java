package com.petcity.pickme.base;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.petcity.pickme.R;
import com.petcity.pickme.common.widget.WRecyclerView;

/**
 * @ClassName BindingEvent
 * @Description BindingEvent
 * @Author sherily
 * @Date 15/01/21 4:57 PM
 * @Version 1.0
 */
public class BindingEvent {

    @BindingAdapter("glideUrl")
    public static void setGlideUrl(final ImageView iv, String url) {
        Glide.with(iv.getContext())
                .load(url)
                .centerCrop()
                .error(R.mipmap.avtar)
                .placeholder(R.mipmap.avtar)
                .into(iv);
    }

    @BindingAdapter("glideUrlInt")
    public static void setGlideUrlInt(final ImageView iv, int url) {
        Glide.with(iv.getContext())
                .load(url)
                .centerCrop()
                .circleCrop()
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(iv);
    }

    @BindingAdapter("adapter")
    public static void setAdapter(final WRecyclerView rv, RecyclerView.Adapter adapter) {
        rv.setAdapter(adapter);
    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(final WRecyclerView rv, RecyclerView.LayoutManager layoutManager) {
        if (null == layoutManager) layoutManager = new LinearLayoutManager(rv.getContext());
        rv.setLayoutManager(layoutManager);
    }

    @BindingAdapter("adapterNormal")
    public static void setAdapterForNormal(final RecyclerView rv, RecyclerView.Adapter adapter) {
        rv.setAdapter(adapter);
    }

    @BindingAdapter("layoutManagerNormal")
    public static void setLayoutManagerForNormal(final RecyclerView rv, RecyclerView.LayoutManager layoutManager) {
        if (null == layoutManager) layoutManager = new LinearLayoutManager(rv.getContext());
        rv.setLayoutManager(layoutManager);
    }

    @BindingAdapter("OnRefreshListener")
    public static void OnRefreshListener(final SwipeRefreshLayout swipeRefreshLayout, final BaseViewModel viewModel) {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                viewModel.loadData(true);
            }
        });
    }

    @BindingAdapter("OnLoadMoreListener")
    public static void OnLoadMoreListener(final WRecyclerView rv, final BaseViewModel viewModel) {

        rv.setWRecyclerListener(new WRecyclerView.WRecyclerViewListener() {
            @Override
            public void onLoadMore() {
                viewModel.loadData(false);
            }
        });

    }

    @BindingAdapter("enableLoadMore")
    public static void enableLoadMore(final WRecyclerView rv, boolean isEnabled) {
        rv.setPullLoadEnable(isEnabled);
    }

    @BindingAdapter("enableFresh")
    public static void enableFresh(final WRecyclerView rv, boolean isEnabled) {
        ;
        rv.setPullRefresh(isEnabled);
    }

    @BindingAdapter("onComplete")
    public static void onComplete(final WRecyclerView rv, boolean isComplete) {
        if (rv.ismPullLoading() && isComplete) {
            rv.stopLoadMore();
        }
        rv.setPullRefresh(false);
    }

    @BindingAdapter("onComplete")
    public static void onComplete(final SwipeRefreshLayout swipeRefreshLayout, boolean isComplete) {
        if (swipeRefreshLayout.isRefreshing() && isComplete) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
