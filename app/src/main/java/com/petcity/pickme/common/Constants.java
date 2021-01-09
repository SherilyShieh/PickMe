package com.petcity.pickme.common;

import com.petcity.pickme.BuildConfig;

/**
 * @ClassName Constants
 * @Description TODO
 * @Author sherily
 * @Date 6/01/21 2:35 PM
 * @Version 1.0
 */
public interface Constants {

    boolean DEBUG = BuildConfig.DEBUG;

    interface SP {
        String DEFAULT_SP_NAME = "PetCity_PickMe_SP";
    }

    interface API {
        String BASE_URL_LOCAL = "";
    }
}
