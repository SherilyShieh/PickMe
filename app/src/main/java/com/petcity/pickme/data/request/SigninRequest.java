package com.petcity.pickme.data.request;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName SigninRequest
 * @Description TODO
 * @Author sherily
 * @Date 9/01/21 1:24 PM
 * @Version 1.0
 */
public class SigninRequest {

    @SerializedName("uid")
    private String uid;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("email")
    private String email;

    public SigninRequest(String uid, String firstName, String lastName, String email) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

}
