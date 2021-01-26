package com.petcity.pickme.data.response;

import com.google.gson.annotations.SerializedName;
import com.petcity.pickme.data.request.TestInfo;

import java.util.List;

/**
 * @ClassName ListTestInfo
 * @Description TODO
 * @Author sherily
 * @Date 25/01/21 1:14 PM
 * @Version 1.0
 */
public class ListTestInfo {

    @SerializedName("list")
    private List<TestInfo> list;

    public List<TestInfo> getList() {
        return list;
    }
}
