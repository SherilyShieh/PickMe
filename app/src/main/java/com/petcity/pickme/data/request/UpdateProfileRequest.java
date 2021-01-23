package com.petcity.pickme.data.request;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName UpdateProfileResponse
 * @Description UpdateProfileResponse
 * @Author sherily
 * @Date 11/01/21 4:06 PM
 * @Version 1.0
 */
public class UpdateProfileRequest {

    @SerializedName("uid")
    private String uid;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("region")
    private String region;

    @SerializedName("district")
    private String district;

    @SerializedName("detail_address")
    private String detail_address;

    @SerializedName("gender")
    private String gender;

    @SerializedName("avatar")
    private String avatar;

    public UpdateProfileRequest(String uid) {
        this.uid = uid;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setDetail_address(String detail_address) {
        this.detail_address = detail_address;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
