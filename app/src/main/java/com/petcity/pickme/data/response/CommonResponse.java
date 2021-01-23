package com.petcity.pickme.data.response;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName CommonResponse
 * @Description Common Response for Api
 * @Author sherily
 * @Date 21/01/21 10:56 PM
 * @Version 1.0
 */
public class CommonResponse {

    @SerializedName("msg")
    private String msg;

    @SerializedName("contacted_id")
    private String contactedId;

    @SerializedName("ad_id")
    private String adId;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("file")
    private String file;

    public String getFile() {
        return file;
    }

    public String getMsg() {
        return msg;
    }

    public String getContactedId() {
        return contactedId;
    }

    public String getAdId() {
        return adId;
    }

    public String getUser_id() {
        return user_id;
    }
}
