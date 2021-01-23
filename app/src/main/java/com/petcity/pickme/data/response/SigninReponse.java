package com.petcity.pickme.data.response;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName SigninReponse
 * @Description SigninReponse
 * @Author sherily
 * @Date 9/01/21 1:27 PM
 * @Version 1.0
 */
public class SigninReponse {

    @SerializedName("uid")
    private String uid;

    @SerializedName("msg")
    private String msg;

    public String getUid() {
        return uid;
    }

    public String getMsg() {
        return msg;
    }
}
