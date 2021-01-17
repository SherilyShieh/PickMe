package com.petcity.pickme.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName LocationNZUtils
 * @Description TODO
 * @Author sherily
 * @Date 16/01/21 10:31 PM
 * @Version 1.0
 */
public class LocationNZUtils {


    public static List<String> getRegionNames() {
        List<String> locations = new ArrayList<>();
        locations.add("All");
        locations.add("Auckland");
        locations.add("Christchurch");
        locations.add("Dunedin");
        locations.add("Gisborne");
        locations.add("Hamilton");
        locations.add("Hastings");
        locations.add("Hibiscus Coast");
        locations.add("Invercargill");
        locations.add("Lower Hutt");
        locations.add("Napier");
        locations.add("Nelson");
        locations.add("New Plymouth");
        locations.add("Palmerston North");
        locations.add("Porirua");
        locations.add("Rotorua");
        locations.add("Tauranga");
        locations.add("Upper Hutt");
        locations.add("Wellington");
        locations.add("Whanganui");
        locations.add("Whangārei");
        return locations;
    }
}

