package com.petcity.pickme.data.request;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName TestInfo
 * @Description TODO
 * @Author sherily
 * @Date 22/12/20 1:13 PM
 * @Version 1.0
 */
public class TestInfo {

    @SerializedName("id")
    private int id;

    @SerializedName("info")
    private String info;

    public TestInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
