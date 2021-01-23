package com.petcity.pickme.data.remote;

import com.petcity.pickme.data.request.CommonRequest;
import com.petcity.pickme.data.request.CreateAdRequest;
import com.petcity.pickme.data.request.SigninRequest;
import com.petcity.pickme.data.request.UpdateProfileRequest;
import com.petcity.pickme.data.response.AdvertiseResponse;
import com.petcity.pickme.data.response.CommonResponse;
import com.petcity.pickme.data.response.ListAdvertiseResponse;
import com.petcity.pickme.data.response.ListContactedResponse;
import com.petcity.pickme.data.response.SigninReponse;
import com.petcity.pickme.data.response.User;

import java.util.Map;

import io.reactivex.rxjava3.core.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * @ClassName ApiService
 * @Description ApiService
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

    @PUT("api/v1/postAd")
    Flowable<ResultWrapper<AdvertiseResponse>> postAd(@Body CreateAdRequest request);

    @POST("api/v1/updateAd")
    Flowable<ResultWrapper<CommonResponse>> updateAd(@Body CreateAdRequest request);

    @GET("api/v1/getAllAds")
    Flowable<ResultWrapper<ListAdvertiseResponse>> getAllAds(@Query("index") int index,
                                                             @Query("page_size") int pageSize,
                                                             @Query("dog_breed") String breed,
                                                             @Query("region") String region,
                                                             @Query("district") String district);

    //    @DELETE("api/v1/deleteMyAds")
    @HTTP(method = "DELETE", path = "api/v1/deleteMyAds", hasBody = true)
    Flowable<ResultWrapper<CommonResponse>> deleteMyAds(@Body CommonRequest request);

    @GET("api/v1/getMyAds")
    Flowable<ResultWrapper<ListAdvertiseResponse>> getMyAds(@Query("index") int index,
                                                            @Query("page_size") int pageSize,
                                                            @Query("user_id") int user_id);

    @PUT("api/v1/addContacted")
    Flowable<ResultWrapper<CommonResponse>> addContacted(@Body CommonRequest request);

    @GET("api/v1/getAllContacted")
    Flowable<ResultWrapper<ListContactedResponse>> getAllContacted(@Query("index") int index,
                                                                   @Query("page_size") int pageSize,
                                                                   @Query("user_id") int user_id);

    //    @DELETE("api/v1/deleteContacted")
    @HTTP(method = "DELETE", path = "api/v1/deleteContacted", hasBody = true)
    Flowable<ResultWrapper<CommonResponse>> deleteContacted(@Body CommonRequest request);

    @Multipart
    @POST("api/v1/saveAvatar")
    Flowable<ResultWrapper<CommonResponse>> saveAvatar(@PartMap Map<String, RequestBody> multipartParams);
}
