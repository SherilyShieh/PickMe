package com.petcity.pickme.data.response;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName ContactedResponse
 * @Description ContactedResponse
 * @Author sherily
 * @Date 22/01/21 9:38 PM
 * @Version 1.0
 */
public class ContactedResponse {

    @SerializedName("contacted_id")
    private int contactedId;

    @SerializedName("ad_id")
    private int adid;

    @SerializedName("user_id")
    private int userID;

    @SerializedName("advertise")
    private AdvertiseResponse advertise;

    public int getContactedId() {
        return contactedId;
    }

    public int getAdid() {
        return adid;
    }

    public int getUserID() {
        return userID;
    }

    public AdvertiseResponse getAdvertise() {
        return advertise;
    }
}
