package com.petcity.pickme.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @ClassName ListAdvertiseResponse
 * @Description ListAdvertiseResponse
 * @Author sherily
 * @Date 15/01/21 6:54 PM
 * @Version 1.0
 */
public class ListAdvertiseResponse {

    @SerializedName("total_count")
    private int totalCount;

    @SerializedName("page_index")
    private int pageIndex;

    @SerializedName("page_size")
    private int pageSize;

    @SerializedName("list")
    private List<AdvertiseResponse> advertiseResponseList;

    public int getTotalCount() {
        return totalCount;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<AdvertiseResponse> getAdvertiseResponseList() {
        return advertiseResponseList;
    }
}
