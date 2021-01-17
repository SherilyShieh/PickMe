package com.petcity.pickme.common.utils;

import java.util.List;

/**
 * @ClassName Location
 * @Description TODO
 * @Author sherily
 * @Date 16/01/21 10:32 PM
 * @Version 1.0
 */
public class Location {

    String Region;
    List<String> district;

    public Location() {
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public List<String> getDistrict() {
        return district;
    }

    public void setDistrict(List<String> district) {
        this.district = district;
    }
}
