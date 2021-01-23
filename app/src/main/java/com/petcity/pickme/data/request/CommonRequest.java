package com.petcity.pickme.data.request;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName ContacteRequest
 * @Description TODO
 * @Author sherily
 * @Date 21/01/21 10:38 PM
 * @Version 1.0
 */
public class CommonRequest {

    @SerializedName("contacted_id")
    private int contactedId;

    @SerializedName("ad_id")
    private int adId;

    @SerializedName("user_id")
    private int userId;

    public void setContactedId(int contactedId) {
        this.contactedId = contactedId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


}
