package com.petcity.pickme.data.response;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName AdvertiseResponse
 * @Description TODO
 * @Author sherily
 * @Date 15/01/21 4:08 PM
 * @Version 1.0
 */
public class AdvertiseResponse {

    @SerializedName("ad_id")
    private int ad_id;

    @SerializedName("user_id")
    private int user_id;

    @SerializedName("type")
    private String type;

    @SerializedName("dog_breed")
    private String dog_breed;

    @SerializedName("price")
    private String price;

    @SerializedName("date")
    private String date;

    @SerializedName("start_time")
    private String start_time;

    @SerializedName("duration")
    private String duration;

    @SerializedName("region")
    private String region;

    @SerializedName("district")
    private String district;

    @SerializedName("created_time")
    private String created_time;

    @SerializedName("updated_time")
    private String updated_time;

    @SerializedName("description")
    private String description;

    @SerializedName("user")
    private User user;

    public AdvertiseResponse(int ad_id, int user_id, String type, String dog_breed, String price, String date, String start_time, String duration, String region, String district, String created_time, String updated_time, String description, User user) {
        this.ad_id = ad_id;
        this.user_id = user_id;
        this.type = type;
        this.dog_breed = dog_breed;
        this.price = price;
        this.date = date;
        this.start_time = start_time;
        this.duration = duration;
        this.region = region;
        this.district = district;
        this.created_time = created_time;
        this.updated_time = updated_time;
        this.description = description;
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public String formatPrice() {
        return "$" + price + " for " + duration + " hours";
    }

    public String formatDate() {
        return date + " " + start_time;
    }

    public String formatLocation() {
        return district + ", " + region;
    }

    public int getAd_id() {
        return ad_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getType() {
        return type;
    }

    public String getDog_breed() {
        return dog_breed;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getDuration() {
        return duration;
    }

    public String getRegion() {
        return region;
    }

    public String getDistrict() {
        return district;
    }

    public String getCreated_time() {
        return created_time;
    }

    public String getUpdated_time() {
        return updated_time;
    }

    public User getUser() {
        return user;
    }
}
