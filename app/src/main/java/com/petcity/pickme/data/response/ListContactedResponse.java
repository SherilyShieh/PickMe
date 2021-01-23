package com.petcity.pickme.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @ClassName ListContactedResponse
 * @Description ListContactedResponse
 * @Author sherily
 * @Date 22/01/21 9:41 PM
 * @Version 1.0
 */
public class ListContactedResponse {

    @SerializedName("total_count")
    private int totalCount;

    @SerializedName("page_index")
    private int pageIndex;

    @SerializedName("page_size")
    private int pageSize;

    @SerializedName("list")
    private List<ContactedResponse> advertiseResponseList;

    public int getTotalCount() {

        return totalCount;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<ContactedResponse> getAdvertiseResponseList() {
        return advertiseResponseList;
    }
}
