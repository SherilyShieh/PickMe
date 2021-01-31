package com.petcity.pickme.data.remote;

import com.google.gson.Gson;
import com.petcity.pickme.BuildConfig;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.petcity.pickme.common.Constants.host_url;


/**
 * @ClassName RestModule
 * @Description RestModule
 * @Author sherily
 * @Date 6/01/21 7:34 PM
 * @Version 1.0
 */
@Module
public class RestModule {

    public RestModule() {
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(BuildConfig.DEFAULT_NETWORK_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(BuildConfig.DEFAULT_NETWORK_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(BuildConfig.DEFAULT_NETWORK_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build();
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(host_url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

}
