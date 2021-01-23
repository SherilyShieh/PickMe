package com.petcity.pickme.data.remote;

import org.reactivestreams.Publisher;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableTransformer;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * @ClassName RxSchedulerTransformer
 * @Description RxSchedulerTransformer
 * @Author sherily
 * @Date 9/01/21 8:32 PM
 * @Version 1.0
 */
public class RxSchedulerTransformer<T> implements FlowableTransformer<T, T> {

    @Override
    public @NonNull Publisher<T> apply(@NonNull Flowable<T> upstream) {
        return upstream.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
