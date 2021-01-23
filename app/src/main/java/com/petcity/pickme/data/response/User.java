package com.petcity.pickme.data.response;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName User
 * @Description User
 * @Author sherily
 * @Date 15/01/21 4:39 PM
 * @Version 1.0
 */
public class User {

    @SerializedName("user_id")
    private int user_id;

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

    @SerializedName("channel")
    private String channel;

    public User(int user_id, String uid, String firstName, String lastName, String email, String password, String region, String district, String detail_address, String gender, String avatar) {
        this.user_id = user_id;
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.region = region;
        this.district = district;
        this.detail_address = detail_address;
        this.gender = gender;
        this.avatar = avatar;
    }

    public String formatName() {
        return (TextUtils.isEmpty(firstName) ? "" : firstName)
                + " " + (TextUtils.isEmpty(lastName) ? "" : lastName);
    }

    public String formatAddress() {
        if (TextUtils.isEmpty(detail_address) && TextUtils.isEmpty(district) && TextUtils.isEmpty(region)) {
            return "Unknow";
        }
        return (TextUtils.isEmpty(detail_address) ? "" : detail_address) + ", "
                + (TextUtils.isEmpty(district) ? "" : district) + ", "
                + (TextUtils.isEmpty(region) ? "" : region);
    }

    public int getUserId() {
        return user_id;
    }

    public String getUid() {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return TextUtils.isEmpty(email) ? "Unbind" : email;
    }

    public String getPassword() {
        return password;
    }

    public String getRegion() {
        return region;
    }

    public String getDistrict() {
        return district;
    }

    public String getDetail_address() {
        return detail_address;
    }

    public String getGender() {
        return TextUtils.isEmpty(gender) ? "Unknow" : gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getChannel() {
        return channel;
    }
}
