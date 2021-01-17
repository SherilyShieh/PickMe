package com.petcity.pickme.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.petcity.pickme.base.Status.ERROR;
import static com.petcity.pickme.base.Status.LOADING;
import static com.petcity.pickme.base.Status.SUCCESS;

/**
 * @ClassName LiveDataWrapper
 * @Description TODO
 * @Author sherily
 * @Date 9/01/21 1:36 PM
 * @Version 1.0
 */
public class LiveDataWrapper<T> {
    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable
    public final Throwable error;

    private LiveDataWrapper(@NonNull Status status, @Nullable T data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> LiveDataWrapper<T> success(@NonNull T data) {
        return new LiveDataWrapper<>(SUCCESS, data, null);
    }

    public static <T> LiveDataWrapper<T> error(Throwable error) {
        return new LiveDataWrapper<>(ERROR, null, error);
    }

    public static <T> LiveDataWrapper<T> loading(@Nullable T data) {
        return new LiveDataWrapper<>(LOADING, data, null);
    }
}
