package com.petcity.pickme.common;

import com.petcity.pickme.BuildConfig;

/**
 * @ClassName Constants
 * @Description Constants
 * @Author sherily
 * @Date 6/01/21 2:35 PM
 * @Version 1.0
 */
public interface Constants {

    boolean DEBUG = BuildConfig.DEBUG;
    String host_url = "http://192.168.31.88:7701/";
//    String host_url = "http://47.100.136.30:7701/";

    interface SP {
        String DEFAULT_SP_NAME = "PetCity_PickMe_SP";
    }

    interface API {
        String BASE_URL_LOCAL = "";
    }
}
