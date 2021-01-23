package com.petcity.pickme.data.remote;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName ResultWrapper
 * @Description ResultWrapper
 * @Author sherily
 * @Date 9/01/21 1:06 PM
 * @Version 1.0
 */
public class ResultWrapper<T> {

    @SerializedName("success")
    private boolean success;

    @SerializedName("code")
    private int code;

    @SerializedName("errorMsg")
    private String errorMsg;

    @SerializedName("data")
    @Nullable
    private T data;


    public boolean isSuccess() {
        return success;
    }

    public int getCode() {
        return code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    @Nullable
    public T getData() {
        return data;
    }
}
