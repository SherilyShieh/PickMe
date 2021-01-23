package com.petcity.pickme.data.request;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName SigninRequest
 * @Description SigninRequest
 * @Author sherily
 * @Date 9/01/21 1:24 PM
 * @Version 1.0
 */
public class SigninRequest {

    @SerializedName("uid")
    private String uid;

    @SerializedName("channel")
    private String channel;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public SigninRequest(String uid, String channel, String firstName, String lastName, String email, String password) {
        this.uid = uid;
        this.channel = channel;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
