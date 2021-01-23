package com.petcity.pickme.create;

import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;

import com.petcity.pickme.base.BaseViewModel;
import com.petcity.pickme.base.LiveDataWrapper;
import com.petcity.pickme.base.PickMeApp;
import com.petcity.pickme.data.remote.ApiService;
import com.petcity.pickme.data.remote.ResultDataParse;
import com.petcity.pickme.data.remote.RxSchedulerTransformer;
import com.petcity.pickme.data.request.CreateAdRequest;
import com.petcity.pickme.data.response.AdvertiseResponse;
import com.petcity.pickme.data.response.CommonResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * @ClassName CreateAdsViewModel
 * @Description CreateAdsViewModel
 * @Author sherily
 * @Date 16/01/21 12:11 AM
 * @Version 1.0
 */
public class CreateAdsViewModel extends BaseViewModel {

    @Inject
    ApiService apiService;


    @Inject
    public CreateAdsViewModel(@NonNull PickMeApp application) {
        super(application);
    }

    @Override
    public void onCreateViewModel() {

    }

    @Override
    public void loadData(boolean isRefresh) {

    }

    public MediatorLiveData<LiveDataWrapper<AdvertiseResponse>> create = new MediatorLiveData<>();
    public MediatorLiveData<LiveDataWrapper<CommonResponse>> update = new MediatorLiveData<>();

    public void postAd(CreateAdRequest request) {
        create.setValue(LiveDataWrapper.loading(null));
        Disposable disposable = apiService.postAd(request)
                .flatMap(new ResultDataParse<AdvertiseResponse>())
                .compose(new RxSchedulerTransformer<AdvertiseResponse>())
                .subscribe(
                        new Consumer<AdvertiseResponse>() {
                            @Override
                            public void accept(AdvertiseResponse advertiseResponse) throws Throwable {
                                create.setValue(LiveDataWrapper.success(advertiseResponse));
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                create.setValue(LiveDataWrapper.error(throwable));
                            }
                        });
        addDisposable(disposable);
    }

    public void updateAd(CreateAdRequest request) {
        update.setValue(LiveDataWrapper.loading(null));
        Disposable disposable = apiService.updateAd(request)
                .flatMap(new ResultDataParse<CommonResponse>())
                .compose(new RxSchedulerTransformer<CommonResponse>())
                .subscribe(
                        new Consumer<CommonResponse>() {
                            @Override
                            public void accept(CommonResponse commonResponse) throws Throwable {
                                update.setValue(LiveDataWrapper.success(commonResponse));
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                update.setValue(LiveDataWrapper.error(throwable));
                            }
                        });
        addDisposable(disposable);
    }
}

