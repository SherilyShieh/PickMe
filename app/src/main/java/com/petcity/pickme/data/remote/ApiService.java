package com.petcity.pickme.data.remote;

import com.petcity.pickme.data.request.SigninRequest;
import com.petcity.pickme.data.response.SigninReponse;

import io.reactivex.rxjava3.core.Flowable;
import retrofit2.http.Body;
import retrofit2.http.PUT;

/**
 * @ClassName ApiService
 * @Description TODO
 * @Author sherily
 * @Date 6/01/21 11:13 PM
 * @Version 1.0
 */
public interface ApiService {

    @PUT("api/v1/signinWithThird")
    Flowable<ResultWrapper<SigninReponse>> signinWithThird(@Body SigninRequest request);
}
