package com.petcity.pickme.home;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.petcity.pickme.base.BaseViewModel;
import com.petcity.pickme.base.LiveDataWrapper;
import com.petcity.pickme.base.PickMeApp;
import com.petcity.pickme.common.utils.PreferenceManager;
import com.petcity.pickme.data.remote.ApiService;
import com.petcity.pickme.data.remote.ResultDataParse;
import com.petcity.pickme.data.remote.RxSchedulerTransformer;
import com.petcity.pickme.data.request.CommonRequest;
import com.petcity.pickme.data.request.CreateAdRequest;
import com.petcity.pickme.data.request.SigninRequest;
import com.petcity.pickme.data.response.AdvertiseResponse;
import com.petcity.pickme.data.response.CommonResponse;
import com.petcity.pickme.data.response.ListAdvertiseResponse;
import com.petcity.pickme.data.response.SigninReponse;
import com.petcity.pickme.data.response.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * @ClassName HomeViewModel
 * @Description TODO
 * @Author sherily
 * @Date 11/01/21 4:13 PM
 * @Version 1.0
 */
public class HomeViewModel extends BaseViewModel {

    @Inject
    ApiService apiService;
    @Inject
    PreferenceManager preferenceManager;


    @Inject
    public HomeViewModel(@NonNull PickMeApp application) {
        super(application);
        adapter.setmEventObject(this);
    }

    public MediatorLiveData<AdvertiseResponse> show = new MediatorLiveData<>();
    public AdvertiseAdapter adapter = new AdvertiseAdapter();
    public MediatorLiveData<LiveDataWrapper<ListAdvertiseResponse>> data = new MediatorLiveData<>();
    public MediatorLiveData<LiveDataWrapper<CommonResponse>> contacted = new MediatorLiveData<>();
    private int totalPage;
    private int currentPage = 1;
    private int pageSize = 10;
    private String breed;
    private String district;
    private String region;


    @Override
    public void onCreateViewModel() {

    }

    @Override
    public void loadData(boolean isRefresh) {
        if (isRefresh) {
            refresh();
        } else {
            loadMore();
        }

    }

    private void refresh() {
        enableRefresh.set(true);
        enableLoadMore.set(false);
        onComplete.set(false);
        currentPage = 1;
        data = new MediatorLiveData<>();
        getAds(currentPage, pageSize, breed, district, region, true);


    }

    private void loadMore() {
        currentPage = currentPage + 1;
        onComplete.set(false);
        enableRefresh.set(false);
        enableLoadMore.set(true);
        if (currentPage <= totalPage) {
            getAds(currentPage, pageSize, breed, district, region, false);

        } else {
            // toast no more
            onComplete.set(true);
        }

    }

    public LiveData<LiveDataWrapper<User>> getCurrentUser(String uid) {
        final MediatorLiveData<LiveDataWrapper<User>> currentUser = new MediatorLiveData<>();
        currentUser.setValue(LiveDataWrapper.<User>loading(null));
        Disposable disposable = apiService.getUser(uid)
                .flatMap(new ResultDataParse<User>())
                .compose(new RxSchedulerTransformer<User>())
                .subscribe(
                        new Consumer<User>() {
                            @Override
                            public void accept(User user) throws Throwable {
                                currentUser.setValue(LiveDataWrapper.success(user));
                                preferenceManager.setCurrentUserInfo(user);

                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                currentUser.setValue(LiveDataWrapper.<User>error(throwable));
                                preferenceManager.setCurrentUserInfo(null);
                            }
                        });
        addDisposable(disposable);
        return currentUser;
    }


    public void getAds(int index, int pageSize, String breed, String district, String region, boolean isRefresh) {

        data.setValue(LiveDataWrapper.<ListAdvertiseResponse>loading(null));
        Disposable disposable = apiService.getAllAds(index, pageSize, breed, region, district)
                .flatMap(new ResultDataParse<ListAdvertiseResponse>())
                .compose(new RxSchedulerTransformer<ListAdvertiseResponse>())
                .subscribe(
                        new Consumer<ListAdvertiseResponse>() {
                            @Override
                            public void accept(ListAdvertiseResponse listAdvertiseResponse) throws Throwable {
                                data.setValue(LiveDataWrapper.success(listAdvertiseResponse));
                                totalPage = listAdvertiseResponse.getTotalCount();
                                if (totalPage > 0) {
                                    isEmpty.set(false);
                                    if (currentPage == 1) {//如果首页数据为空或者小于每页展现的条数，则禁用上拉加载功能
                                        if (listAdvertiseResponse.getAdvertiseResponseList().size() < pageSize) {
                                            enableLoadMore.set(false);//禁用上拉加载功能
                                        } else {
                                            enableLoadMore.set(true);//启用上拉加载功能
                                        }
                                    }
                                    if (isRefresh) {
                                        adapter.refresh(listAdvertiseResponse.getAdvertiseResponseList());
                                    } else {
                                        adapter.loadMore(listAdvertiseResponse.getAdvertiseResponseList());
                                    }
                                    onComplete.set(true);
                                } else {
                                    isEmpty.set(true);
                                }
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                data.setValue(LiveDataWrapper.<ListAdvertiseResponse>error(throwable));
                                isEmpty.set(true);
                            }
                        });
        addDisposable(disposable);
    }

    public void clear() {
        this.currentPage = 1;
        this.totalPage = 0;
        this.breed = null;
        this.region = null;
        this.district = null;
        getAds(currentPage, 10, breed, district, region, true);
    }

    public void search(String breed, String region, String district) {
        this.currentPage = 1;
        this.totalPage = 0;
        this.breed = breed;
        this.region = region;
        this.district = district;
        getAds(currentPage, 10, breed, district, region, true);
    }

    public void showDialog(AdvertiseResponse response) {
        // show copy email
        Log.d("home", "showDialog");
        show.setValue(response);
    }

    public void addContacted(int adId) {
        contacted.setValue(LiveDataWrapper.<CommonResponse>loading(null));
        CommonRequest commonRequest = new CommonRequest();
        User user = preferenceManager.getCurrentUserInfo();
        commonRequest.setAdId(adId);
        commonRequest.setUserId(user.getUser_id());
        Disposable disposable = apiService.addContacted(commonRequest)
                .flatMap(new ResultDataParse<CommonResponse>())
                .compose(new RxSchedulerTransformer<CommonResponse>())
                .subscribe(
                        new Consumer<CommonResponse>() {
                            @Override
                            public void accept(CommonResponse commonResponse) throws Throwable {
                                contacted.setValue(LiveDataWrapper.success(commonResponse));
                                loadData(true);
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                contacted.setValue(LiveDataWrapper.<CommonResponse>error(throwable));
                            }
                        });
        addDisposable(disposable);
    }
}
