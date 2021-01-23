package com.petcity.pickme.data.request;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName CreateAdRequest
 * @Description CreateAdRequest
 * @Author sherily
 * @Date 16/01/21 4:10 PM
 * @Version 1.0
 */
public class CreateAdRequest {

    @SerializedName("ad_id")
    private int adId;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("type")
    private String type;

    @SerializedName("dog_breed")
    private String dogBreed;

    @SerializedName("date")
    private String date;

    @SerializedName("start_time")
    private String startTime;

    @SerializedName("duration")
    private String duration;

    @SerializedName("price")
    private String price;

    @SerializedName("district")
    private String district;

    @SerializedName("region")
    private String region;

    @SerializedName("description")
    private String description;

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setDogBreed(String dogBreed) {
        this.dogBreed = dogBreed;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
