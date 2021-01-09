package com.petcity.pickme.data.remote;



import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.functions.Function;

/**
 * @ClassName ResultDataParse
 * @Description TODO
 * @Author sherily
 * @Date 9/01/21 1:10 PM
 * @Version 1.0
 */
public class ResultDataParse<T> implements Function<ResultWrapper<T>, Flowable<T>> {

    @Override
    public Flowable<T> apply(final ResultWrapper<T> tResultWrapper) {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<T> emitter) throws Throwable {
                if (tResultWrapper.isSuccess()) {
                    if (null == tResultWrapper.getData()) {
                        emitter.onNext((T)ResultEmpty.EMPTY);
                    } else {
                        emitter.onNext(tResultWrapper.getData());
                    }
                    emitter.onComplete();
                } else {
                    emitter.onError(new ResultException(tResultWrapper.getErrorMsg(), tResultWrapper.getCode()));
                }
            }
        }, BackpressureStrategy.BUFFER);
    }
}
