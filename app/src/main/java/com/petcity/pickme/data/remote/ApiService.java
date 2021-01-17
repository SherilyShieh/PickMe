package com.petcity.pickme.data.remote;

import com.petcity.pickme.data.request.SigninRequest;
import com.petcity.pickme.data.request.UpdateProfileRequest;
import com.petcity.pickme.data.response.SigninReponse;
import com.petcity.pickme.data.response.User;

import io.reactivex.rxjava3.core.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * @ClassName ApiService
 * @Description TODO
 * @Author sherily
 * @Date 6/01/21 11:13 PM
 * @Version 1.0
 */
public interface ApiService {

    @PUT("api/v1/signUp")
    Flowable<ResultWrapper<SigninReponse>> signinWithThird(@Body SigninRequest request);

    @PUT("api/v1/register")
    Flowable<ResultWrapper<SigninReponse>> register(@Body SigninRequest request);

    @GET("api/v1/signin")
    Flowable<ResultWrapper<SigninReponse>> signin(@Query("email") String email, @Query("password") String pwd);

    @GET("api/v1/getUser")
    Flowable<ResultWrapper<User>> getUser(@Query("uid") String uid);

    @GET("api/v1/verifyEmail")
    Flowable<ResultWrapper<User>> verifyEmail(@Query("email") String email);


    @POST("api/v1/updateProfile")
    Flowable<ResultWrapper<User>> updateProfile(@Body UpdateProfileRequest request);


}
